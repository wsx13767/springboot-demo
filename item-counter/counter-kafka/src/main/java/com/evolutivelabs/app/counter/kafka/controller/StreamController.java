package com.evolutivelabs.app.counter.kafka.controller;

import com.evolutivelabs.app.counter.common.model.ordercounter.*;
import com.evolutivelabs.app.counter.kafka.config.KafkaStreamProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "kafka stream處理結果")
@RequestMapping("/api/kafka/stream/box/counter")
@RestController
public class StreamController {
    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;
    @Autowired
    private StreamsBuilderFactoryBean factoryBean;

    @ApiOperation("查詢boxId箱內商品數量")
    @GetMapping("/{boxId}")
    public BoxInfoCount getRealCount(@PathVariable String boxId) {
        ReadOnlyKeyValueStore<String, BoxInfoCount> keyValueStore = getkeyValueStore();
        KeyValueIterator<String, BoxInfoCount> all = keyValueStore.all();
        return getkeyValueStore().get(boxId);
    }

    private ReadOnlyKeyValueStore<String, BoxInfoCount> getkeyValueStore() {
        return factoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(kafkaStreamProperties.getStoreName(),
                        QueryableStoreTypes.keyValueStore()));
    }
}
