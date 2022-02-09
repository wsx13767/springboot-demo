package com.evolutivelabs.app.counter.batch.config;

import com.evolutivelabs.app.counter.common.config.redis.RedisKey;
import com.evolutivelabs.app.counter.common.service.RedisClientService;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.BoxInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * cache產生放置處
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CacheInit {
    private final RedisClientService redisClientService;
    private final RepositoryFactory repositoryFactory;

    @PostConstruct
    private void init() {
        initBoxInfo();
        initItemInfo();
        initPackingConfig();
        businessDetail();
    }

    /**
     * 裝箱資訊放入 cache
     */
    private void initBoxInfo() {
        redisClientService.set(RedisKey.BOX_INFO, repositoryFactory.getBoxInfoRepository().findAllOrderByQty()
                .stream().collect(Collectors.toMap(BoxInfo::getCarton, Function.identity())));
    }

    /**
     * 每樣商品資訊
     */
    private void initItemInfo() {
        redisClientService.set(RedisKey.ITEM_INFO, StreamSupport.stream(repositoryFactory.getItemInfoRepository().findAll().spliterator(), false)
                .sorted((v1, v2) -> {
                    int l1 = v1.getItemInfoId().getSkuPrefix().compareTo(v2.getItemInfoId().getSkuPrefix());
                    return l1 == 0 ? v2.getItemInfoId().getSkuPostfix().compareTo(v1.getItemInfoId().getSkuPostfix()) : l1;
                })
                .collect(Collectors.toList()));
    }

    /**
     * 客製化必要欄位設定檔
     */
    private void initPackingConfig() {
        redisClientService.set(RedisKey.PACKING_CONFIG, StreamSupport.stream(repositoryFactory.getPackingConfigRepository().findAll().spliterator(), false)
                .sorted((v1, v2) -> v2.getId().length() - v1.getId().length())
                .collect(Collectors.toList()));
    }

    /**
     * 每個客戶支援哪些size的箱子
     */
    private void businessDetail() {
        Map<String, BoxInfo> boxInfoMap = repositoryFactory.getBoxInfoRepository().findAllOrderByQty()
                .stream().collect(Collectors.toMap(BoxInfo::getCarton, Function.identity()));

        Map<String, List<BoxInfo>> businessDetail = StreamSupport
                .stream(repositoryFactory.getBoxOfBusinessRepository().findAll().spliterator(), false)
                .collect(Collectors.groupingBy(entry -> entry.getBusinessId()))
                .entrySet().stream().map(entry -> {
                    List<BoxInfo> boxes = new ArrayList<>();
                    entry.getValue().forEach(info -> boxes.add(boxInfoMap.get(info.getBoxCarton())));
                    Collections.sort(boxes, (v1, v2) -> v2.getQty().compareTo(v1.getQty()));

                    return new AbstractMap.SimpleEntry<String, List<BoxInfo>>(entry.getKey(), boxes);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        redisClientService.set(RedisKey.BUSINESS_BOX_DETAIL, businessDetail);
    }
}
