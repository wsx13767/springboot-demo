package com.evolutivelabs.app.counter.api.service;

import com.evolutivelabs.app.counter.common.model.ordercounter.BoxDetail;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCountService {

    /**
     * 整理
     * @param boxId
     * @param itemEntityList
     */
    public BoxDetail getBoxDetail(String boxId, List<ItemLog> itemEntityList) {

        BoxDetail.Builder builder = BoxDetail.newBuilder(boxId);
        for (ItemLog item : itemEntityList) {
            builder.addItem(item);
        }

        return builder.buildDistinct();
    }
}
