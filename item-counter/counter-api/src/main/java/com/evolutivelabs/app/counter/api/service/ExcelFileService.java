package com.evolutivelabs.app.counter.api.service;

import com.evolutivelabs.app.counter.common.config.redis.RedisKey;
import com.evolutivelabs.app.counter.common.exception.NotFoundException;
import com.evolutivelabs.app.counter.common.model.excelpaser.ExcelResult;
import com.evolutivelabs.app.counter.common.model.excelpaser.ExcelRow;
import com.evolutivelabs.app.counter.common.model.excelpaser.ExcelSheet;
import com.evolutivelabs.app.counter.common.model.excelpaser.FilePath;
import com.evolutivelabs.app.counter.common.service.RedisClientService;
import com.evolutivelabs.app.counter.common.utils.PathDirUtils;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.BoxOfExcel;
import com.evolutivelabs.app.counter.database.mysql.entity.ExcelFile;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemsOfBox;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExcelFileService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFileService.class);

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLook = lock.readLock();

    private final RedisClientService redisClientService;
    private final RepositoryFactory repositoryFactory;

    /**
     * 取得cache的目錄
     *
     * @return
     */
    public FilePath getFilePath() {
        return redisClientService.get(RedisKey.FILE_DIR);
    }

    /**
     * 確定變更檔案狀態
     *
     * @param id
     * @return
     * @throws IOException
     */
    public ExcelFile chooseExcelFile(String id) {
        try {
            writeLock.lock();
            ExcelFile excelFile = repositoryFactory.getExcelFileRepository().findByIdAndDone(id, false)
                    .orElseThrow(() -> new NotFoundException(String.format("id: %s not Found", id)));
            excelFile.setDone(true);
            repositoryFactory.getExcelFileRepository().save(excelFile);
            return excelFile;
        } finally {
            writeLock.unlock();
        }
    }

    public ExcelResult getExcelContent(ExcelFile excelFile) {
        String excelId = excelFile.getId();


        ExcelResult result = new ExcelResult();
        result.setId(excelId);
        result.setPath(excelFile.getFilePath());
        result.setLastModified(excelFile.getModifyTime());
        List<BoxOfExcel> boxOfExcels = repositoryFactory.getBoxOfExcelRepository().findByExcelId(excelId);
        List<ItemsOfBox> itemsOfBoxes = repositoryFactory.getItemsOfBoxRepository().findByExcelId(excelId);

        for (BoxOfExcel boxOfExcel : boxOfExcels) {
            ExcelSheet sheet = new ExcelSheet();
            List<ItemsOfBox> filterItemsOfBoxes = itemsOfBoxes
                    .stream()
                    .filter(itemsOfBox -> boxOfExcel.getBoxSeries().equals(itemsOfBox.getBoxSeries()))
                    .collect(Collectors.toList());
            sheet.setSheetName(String.format("BOX_%03d", boxOfExcel.getBoxSeries()));
            sheet.setFulfilled(false);
            sheet.setBox(boxOfExcel.getBoxCarton());
            for (ItemsOfBox itemsOfBox : filterItemsOfBoxes) {
                ExcelRow excelRow = new ExcelRow();
                excelRow.setPosition(itemsOfBox.getPosition());
                excelRow.setSku(itemsOfBox.getSku());
                excelRow.setFnsku(itemsOfBox.getFnSku());
                excelRow.setEan(itemsOfBox.getEan());
                excelRow.setQty(itemsOfBox.getNum());
                excelRow.setWeight(itemsOfBox.getWeight());
                excelRow.setTitle(itemsOfBox.getTitle());
                excelRow.setVolumn(itemsOfBox.getVolume());
//                excelRow.setUpc(itemsOfBox.getEan());
//                excelRow.setSkuId(map.get(excelRow.getFnsku()).get("SKUID"));
                sheet.add(excelRow);
            }
            result.add(sheet);
        }
        return result;
    }

    /**
     * 儲存excelFile下所有檔案目錄至redis
     */
    public void saveExcelAllPathToRedis() {
        try {
            writeLock.lock();
            redisClientService.set(RedisKey.FILE_DIR, getShippingExcelAllPath().get());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 取得excelFile下所有檔案目錄
     *
     * @return
     */
    public Optional<FilePath> getShippingExcelAllPath() {
        Optional<ArrayList<FilePath>> filePathsOption = repositoryFactory.getExcelFileRepository().findAllByDoneAndError(false, false)
                .stream()
                .map(excelFile -> {
                    Path path = Path.of(excelFile.getFilePath());
                    FilePath filePath = new FilePath();
                    filePath.setIsDir(false);
                    filePath.setPath(path.toString());
                    filePath.setName(path.getFileName().toString());
                    filePath.setId(excelFile.getId());
                    Optional<String> fileType = PathDirUtils.getExtension(filePath.getName());
                    if (fileType.isPresent()) filePath.setType(fileType.get());
                    filePath.setDone(excelFile.getDone());
                    filePath.setLastModified(excelFile.getModifyTime());
                    filePath.setSize(excelFile.getSize());

                    path = path.getParent();
                    while (path != null) {
                        FilePath parent = new FilePath();
                        parent.setIsDir(true);
                        parent.setPath(path.toString());
                        Path fileName = path.getFileName();
                        if (fileName != null) parent.setName(fileName.toString());
                        parent.setChildren(new ArrayList<>(Arrays.asList(filePath)));
                        filePath = parent;
                        path = path.getParent();
                    }
                    return new ArrayList<>(Arrays.asList(filePath));
                })
                .reduce((f1, f2) -> {
                    List<FilePath> copy = new ArrayList<>(f1);
                    List<FilePath> result = copy;
                    FilePath children = f2.get(0);
                    loop:
                    while (true) {
                        for (FilePath filePath : copy) {
                            if (filePath.getPath().equals(children.getPath())) {
                                copy = filePath.getChildren();
                                List<FilePath> childrenes = children.getChildren();
                                if (childrenes == null) break loop;
                                children = childrenes.get(0);
                                continue loop;
                            }
                        }
                        copy.add(children);
                        break;
                    }

                    return (ArrayList<FilePath>) result;
                });

        if (filePathsOption.isPresent()) {
            List<FilePath> filePaths = filePathsOption.get();
            if (filePaths != null && filePaths.size() == 1) {
                return Optional.of(filePaths.get(0));
            }
        }

        return Optional.empty();
    }
}
