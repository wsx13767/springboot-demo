package com.evolutivelabs.app.counter.batch.job.impl;

import com.evolutivelabs.app.counter.batch.annotation.BeanInject;
import com.evolutivelabs.app.counter.batch.config.BatchProperties;
import com.evolutivelabs.app.counter.batch.job.CommonJob;
import com.evolutivelabs.app.counter.batch.service.ExcelContentService;
import com.evolutivelabs.app.counter.common.config.redis.RedisKey;
import com.evolutivelabs.app.counter.common.model.Directory;
import com.evolutivelabs.app.counter.common.model.excelpaser.ItemBundle;
import com.evolutivelabs.app.counter.common.model.excelpaser.ShippingBox;
import com.evolutivelabs.app.counter.common.service.RedisClientService;
import com.evolutivelabs.app.counter.common.utils.PathDirUtils;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.BoxInfo;
import com.evolutivelabs.app.counter.database.mysql.entity.ExcelFile;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemInfo;
import com.evolutivelabs.app.counter.database.mysql.entity.PackingConfig;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PackingJob extends CommonJob {
    private static final Logger logger = LoggerFactory.getLogger(PackingJob.class);

    @BeanInject
    private RedisClientService redisClientService;
    @BeanInject
    private RepositoryFactory repositoryFactory;
    @BeanInject
    private BatchProperties batchProperties;
    @BeanInject
    private ExcelContentService excelContentService;

    @Override
    protected void execute() throws Exception {
        PathDirUtils.dirProcess(Paths.get(batchProperties.getEXCEL_ROOT_PATH()), Directory.class,
                PathDirUtils.<Directory>condition()
                        .suffix("xlsx")
                        .consumer(chidren -> {
                            if (!chidren.getIsDir()) {
                                if (chidren.getName().startsWith("_") || chidren.getName().startsWith("~")) return;
                                repositoryFactory.getExcelFileRepository().findByFilePath(chidren.getPath())
                                        .orElseGet(() -> {
                                            String source = Path.of(chidren.getPath().replace(batchProperties.getEXCEL_ROOT_PATH(), "")).getName(0).toString();
                                            ExcelFile excelFile = new ExcelFile();
                                            excelFile.setFileName(chidren.getName());
                                            excelFile.setFilePath(chidren.getPath());
                                            excelFile.setModifyTime(chidren.getLastModified());
                                            excelFile.setSize(chidren.getSize());
                                            excelFile.setSource(source);

                                            try {
                                                Map<String, List<BoxInfo>> businesses = redisClientService.get(RedisKey.BUSINESS_BOX_DETAIL);
                                                List<BoxInfo> boxInfos = businesses.get(source);
                                                if (boxInfos == null) {
                                                    excelFile.setError(true);
                                                    excelFile.setMsg("無參數設定檔");
                                                    throw new Exception("查無客戶參數設定檔");
                                                }
                                                repositoryFactory.getExcelFileRepository().save(excelFile);
                                                if (boxInfos != null) {
                                                    excelContentService.saveExcelContent(excelFile, process(Path.of(chidren.getPath()), boxInfos));
                                                }
                                            } catch (Exception e) {
                                                logger.error(e.getMessage(), e);
                                                excelFile.setError(true);
                                                excelFile.setMsg(e.getMessage());
                                                repositoryFactory.getExcelFileRepository().save(excelFile);
                                            }

                                            return excelFile;
                                        });
                            }
                        }));
    }


    private Optional<List<ShippingBox>> process(Path path, List<BoxInfo> boxInfos) throws Exception {
        if (boxInfos == null) return Optional.empty();

        BoxInfo maxBox = boxInfos.get(0);

        List<ItemBundle> rows = parser(path);

        // 配箱順序分組
        Map<Integer, List<ItemBundle>> groupOrder = rows.stream()
                .collect(Collectors.groupingBy(a -> a.getOrder()));

        // 配箱順序總和
        Map<Integer, BigDecimal> groupTotal = groupOrder.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<Integer, BigDecimal>(entry.getKey(),
                        entry.getValue()
                                .stream()
                                .map(itemBundle -> itemBundle.getNum().multiply(itemBundle.getItemInfo().getVolume()))
                                .reduce((v1, v2) -> v1.add(v2)).get()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 將單品項超過上像拆出
        List<List<ItemBundle>> itemBundlesGroup = packing(groupOrder, groupTotal, maxBox);

        // 裝箱
        List<ShippingBox> shippingBoxes = boxing(itemBundlesGroup, maxBox);

        // 重新裝箱
        shippingBoxes = reboxing(shippingBoxes, maxBox);

        // 變更箱子
        changeBox(shippingBoxes, boxInfos);

        Collections.sort(shippingBoxes, (o1, o2) -> o1.getNowQty().compareTo(o2.getNowQty()));

        if (logger.isDebugEnabled()) {
            logger.debug("箱子數量: {}", shippingBoxes.size());
            shippingBoxes.stream()
                    //.filter(box -> !box.isOK())
                    .forEach(box -> {
                        logger.debug("箱號: {}, 最大數量: {}, 目前數量: {}, 捆數: {}",
                                box.getBoxCarton(),
                                box.getBoxQty(),
                                box.getNowQty(),
                                box.getItemBundles().size());
                        box.getItemBundles().forEach(itemBundle -> logger.debug("      {}, {}, {}, {}",
                                itemBundle.getSku(), itemBundle.getOrder(), itemBundle.getNum(), itemBundle.getItemInfo().getVolume()));

                    });
        }
        return Optional.of(shippingBoxes);
    }

    /**
     * 變更箱子
     * @param shippingBoxes
     * @param boxInfos
     */
    private void changeBox(List<ShippingBox> shippingBoxes, List<BoxInfo> boxInfos) {
        for (ShippingBox shippingBox : shippingBoxes) {
            for (BoxInfo boxInfo : boxInfos) {
                if (boxInfo.getQty().compareTo(shippingBox.getNowQty()) == 1) {
                    shippingBox.setBoxCarton(boxInfo.getCarton());
                    shippingBox.setBoxQty(boxInfo.getQty());
                }
            }
        }
    }

    /**
     * 重新裝箱
     * @param shippingBoxes
     * @param maxBox
     * @return
     */
    private List<ShippingBox> reboxing(List<ShippingBox> shippingBoxes, BoxInfo maxBox) {
        Collections.sort(shippingBoxes, (v1, v2) -> v2.getNowQty().compareTo(v1.getNowQty()));
        List<ShippingBox> newBoxes = new ArrayList<>();

        while (shippingBoxes.size() > 0) {
            List<ShippingBox> copyBoxes = new ArrayList<>(shippingBoxes);
            List<ShippingBox> indexList = reboxingIndex(shippingBoxes);
            ShippingBox shippingBox = new ShippingBox();
            shippingBox.setBoxQty(maxBox.getQty());
            shippingBox.setBoxCarton(maxBox.getCarton());
            indexList.forEach(index -> {
                shippingBox.add(index.getItemBundles().toArray(ItemBundle[]::new));
                copyBoxes.remove(index);
            });
            newBoxes.add(shippingBox);
            shippingBoxes = copyBoxes;
        }

        return newBoxes;
//        shippingBoxes.get(0).get
//        Map<Integer, List<ShippingBox>> groupOrderBox = shippingBoxes.stream().collect(Collectors.groupingBy(box -> box.getItemBundles().get(0).getOrder()));
//        for (Map.Entry<Integer, List<ShippingBox>> orderBox : groupOrderBox.entrySet()) {
//            List<ShippingBox> boxes = orderBox.getValue();
//            int maxIndex = 0;
//            int max = 0;
//            for (int index = 0; index < boxes.size();index++) {
//                int count = boxes.get(index).getItemBundles().size();
//                if (count > max) {
//                    max = count;
//                    maxIndex = index;
//                }
//            }
//            boxes.get(maxIndex).getItemBundles()
//        }
    }

    /**
     * 重新裝箱的index
     * @param shippingBoxes
     * @return
     */
    private List<ShippingBox> reboxingIndex(List<ShippingBox> shippingBoxes) {
        List<ShippingBox> index = new ArrayList<>();
        ShippingBox box = shippingBoxes.get(0);
        index.add(box);

        BigDecimal total = box.getNowQty();
        for (int i = 1; i < shippingBoxes.size(); i++) {
            ShippingBox innerBox = shippingBoxes.get(i);
            if (total.add(innerBox.getNowQty()).compareTo(box.getBoxQty()) == 1) continue;

            total = total.add(innerBox.getNowQty());
            index.add(innerBox);
        }

        return index;
    }

    /**
     * 裝箱
     * @param itemBundlesGroup
     * @param maxBox
     * @return
     */
    private List<ShippingBox> boxing(List<List<ItemBundle>> itemBundlesGroup, BoxInfo maxBox) {
        // 開始配箱
        List<ShippingBox> shippingBoxes = new ArrayList<>();
        for (List<ItemBundle> bundles : itemBundlesGroup) {
            // 此配箱順序的總和
            BigDecimal total = bundles
                    .stream()
                    .map(row -> row.getNum().multiply(row.getItemInfo().getVolume()))
                    .reduce((v1, v2) -> v1.add(v2)).get();

            if (total.compareTo(maxBox.getQty()) == 1) {
                // 過濾是否達上限，已達上限裝成一箱
                bundles.stream()
                        .filter(ItemBundle::isMax)
                        .forEach(itemBundle -> {
                            ShippingBox shippingBox = new ShippingBox();
                            shippingBox.setBoxQty(maxBox.getQty());
                            shippingBox.setBoxCarton(maxBox.getCarton());
                            shippingBox.add(itemBundle);
                            shippingBox.setOK(true);
                            shippingBoxes.add(shippingBox);
                        });

                // 開始其它作業
                List<ItemBundle> newBundles = bundles
                        .stream()
                        .filter(itemBundle -> !itemBundle.isMax())
                        .sorted((o1, o2) -> o2.getTotalVolume().compareTo(o1.getTotalVolume()))
                        .collect(Collectors.toList());

                while (newBundles.size() > 0) {
                    List<ItemBundle> newBundles2 = new ArrayList<>(newBundles);
                    List<ItemBundle> indexList = getToBundleIndex(newBundles2, maxBox);
                    ShippingBox shippingBox = new ShippingBox();
                    shippingBox.setBoxQty(maxBox.getQty());
                    shippingBox.setBoxCarton(maxBox.getCarton());

                    indexList.forEach(index -> {
                        shippingBox.add(index);
                        newBundles2.remove(index);
                    });

                    shippingBoxes.add(shippingBox);
                    newBundles = newBundles2;
                }
            } else {
                ShippingBox shippingBox = new ShippingBox();
                shippingBox.setBoxQty(maxBox.getQty());
                shippingBox.setBoxCarton(maxBox.getCarton());
                shippingBox.add(bundles.stream().toArray(ItemBundle[]::new));
                shippingBoxes.add(shippingBox);
            }
        }

        return shippingBoxes;
    }

    /**
     * 取得一捆一捆的
     * @param bundles
     * @param maxBox
     * @return
     */
    private List<ItemBundle> getToBundleIndex(List<ItemBundle> bundles, BoxInfo maxBox) {
        BigDecimal maxNum = maxBox.getQty();
        List<ItemBundle> index = new ArrayList<>();
        ItemBundle bundle = bundles.get(0);
        index.add(bundle);

        BigDecimal total = bundle.getTotalVolume();

        for (int i = 1; i < bundles.size(); i++) {
            ItemBundle innerBundle = bundles.get(i);
            if (total.add(innerBundle.getTotalVolume()).compareTo(maxNum) == 1) continue;

            total = total.add(innerBundle.getTotalVolume());
            index.add(innerBundle);
        }
        return index;
    }

    /**
     * 商品裝成捆數
     * @param groupOrder
     * @param groupTotal
     * @param maxBox
     * @return
     */
    private List<List<ItemBundle>> packing(Map<Integer, List<ItemBundle>> groupOrder,
                                           Map<Integer, BigDecimal> groupTotal, BoxInfo maxBox) {
        List<List<ItemBundle>> boxes = new ArrayList<>();

        for (Map.Entry<Integer, BigDecimal> entry : groupTotal.entrySet()) {
            List<ItemBundle> itemRows = groupOrder.get(entry.getKey());

            // 單品項數量 > 箱子最大數量
            if (entry.getValue().compareTo(maxBox.getQty()) == 1) {
                itemRows = repacking(itemRows, maxBox);
            }

            boxes.add(itemRows);
        }
        return boxes;
    }

    /**
     * 拆除超過上限商品
     * @param itemRows
     * @param maxBox
     * @return
     */
    private List<ItemBundle> repacking(List<ItemBundle> itemRows, BoxInfo maxBox) {
        List<ItemBundle> boxes = new ArrayList<>();
        for (ItemBundle row : itemRows) {

            BigDecimal maxNum = maxBox.getQty().divide(row.getItemInfo().getVolume(), RoundingMode.DOWN);
            if (row.getNum().compareTo(maxNum) == 1) {
                ItemBundle splitRow = new ItemBundle(row);
                splitRow.setNum(maxNum);
                splitRow.setMax(true);
                boxes.add(splitRow);
                row.setNum(row.getNum().subtract(maxNum));
                boxes.addAll(repacking(Arrays.asList(row), maxBox));
            } else {
                boxes.add(row);
            }
        }

        return boxes;
    }

    /**
     * 解析excel
     * @param path
     * @return
     * @throws Exception
     */
    private List<ItemBundle> parser(Path path) throws Exception {
        List<ItemBundle> result = new ArrayList<>();

        String filename = path.getFileName().toString();

        PackingConfig config = null;

        List<PackingConfig> packingConfigs = redisClientService.get(RedisKey.PACKING_CONFIG);
        List<ItemInfo> itemInfos = redisClientService.get(RedisKey.ITEM_INFO);

        for (PackingConfig packingConfig : packingConfigs) {
            if (filename.startsWith(packingConfig.getId())) {
                config = packingConfig;
                break;
            }
        }

        if (config == null) throw new Exception("無欄位設定檔");

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(path));
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            PackingConfig finalConfig = config;
            Map<String, Integer> column = null;
            for (Row row : sheet) {
                if (column == null || column.size() != 4) {
                    column = StreamSupport.stream(row.spliterator(), false)
                            .filter(cell -> {
                                String name = String.valueOf(cell).trim();
                                return name.equalsIgnoreCase(finalConfig.getSku())
                                        || name.equalsIgnoreCase(finalConfig.getFnsku())
                                        || name.equalsIgnoreCase(finalConfig.getNum())
                                        || name.equalsIgnoreCase(finalConfig.getTitle());
                            })
                            .collect(Collectors.toMap(cell -> String.valueOf(cell).toUpperCase(),
                                    cell -> cell.getColumnIndex()));
                    continue;
                }

                String sku = String.valueOf(row.getCell(column.get(config.getSku()))).trim();
                String fnsku = String.valueOf(row.getCell(column.get(config.getFnsku()))).trim();
                String num = String.valueOf(row.getCell(column.get(config.getNum()))).trim();
                String title = String.valueOf(row.getCell(column.get(config.getTitle()))).trim();

                if (sku.indexOf("-") > -1) {
                    sku = sku.substring(sku.indexOf("-") + 1);
                }

                ItemBundle excelItemRow = new ItemBundle();
                excelItemRow.setSku(sku);
                excelItemRow.setFnsku(fnsku);
                excelItemRow.setTitle(title);
                excelItemRow.setNum(new BigDecimal(num));


                for (ItemInfo itemInfo : itemInfos) {
                    if (sku.startsWith(itemInfo.getItemInfoId().getSkuPrefix())
                            && sku.endsWith(itemInfo.getItemInfoId().getSkuPostfix())
                    ) {
                        excelItemRow.setItemInfo(itemInfo);
                        excelItemRow.setOrder(itemInfo.getSequence());
                        result.add(excelItemRow);
                        break;
                    }
                }
            }
        }

        Collections.sort(result, (v1, v2) -> v1.getOrder().compareTo(v2.getOrder()));

        return result;
    }
}
