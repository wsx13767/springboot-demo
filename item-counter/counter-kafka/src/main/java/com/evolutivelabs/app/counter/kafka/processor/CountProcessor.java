package com.evolutivelabs.app.counter.kafka.processor;

import com.evolutivelabs.app.counter.common.model.ordercounter.BoxInfoCount;
import com.evolutivelabs.app.counter.common.model.ordercounter.InBoxItems;
import com.evolutivelabs.app.counter.common.utils.StreamsSerdesUtils;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemKafkaLog;
import com.evolutivelabs.app.counter.database.mysql.repository.ItemKafkaLogRepository;
import com.evolutivelabs.app.counter.kafka.config.KafkaStreamProperties;
import com.evolutivelabs.app.counter.kafka.schedule.BatchSaveDataSechedule;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CountProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CountProcessor.class);
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;
    @Autowired
    private BatchSaveDataSechedule batchSaveDataSechedule;

    @Autowired
    void process(StreamsBuilder streamsBuilder) {
        KStream<String, InBoxItems> messageStream = streamsBuilder
                .stream(kafkaStreamProperties.getConsumer_topic(),
                        Consumed.with(STRING_SERDE, StreamsSerdesUtils.InboxItemsSerde()));
        KTable<String, BoxInfoCount> table = messageStream
                .peek((k, value) -> {
                    if (logger.isDebugEnabled())
                        logger.debug("key: {}, value: {}", k, value);
                })
                .filter((key, value) -> filter(value))
                .peek((key, value) -> saveToDB(value))
                .selectKey((key, value) -> value.getBoxId())
                .mapValues(value -> {
                    BoxInfoCount boxInfoCount = new BoxInfoCount();
                    boxInfoCount.setBoxId(value.getBoxId());
                    boxInfoCount.setCount(value.getMultiple());
                    return boxInfoCount;
                })
                .peek((key, value) -> {
                    if (logger.isDebugEnabled())
                        logger.debug("key: {}, value: {}", key, value);
                })
                .groupByKey()
                .reduce((total, value) -> {
                            total.setCount(total.getCount() + value.getCount());
                            return total;
                        });//, Materialized.as(kafkaStreamProperties.getStoreName()));
        table.toStream().print(Printed.<String, BoxInfoCount>toSysOut().withLabel("test"));
    }

    private boolean filter(InBoxItems item) {
        return item != null
                && !item.getError()
                && item.getBoxId() != null
                && item.getBarcode() != null;
    }

    private void saveToDB(InBoxItems value) {
        ItemKafkaLog itemKafkaLog = new ItemKafkaLog();
        itemKafkaLog.setBoxId(value.getBoxId());
        itemKafkaLog.setSku(value.getBarcode());
        itemKafkaLog.setError(value.getError());
        itemKafkaLog.setNum(value.getMultiple());
        itemKafkaLog.setLogtime(value.getTimestamp());
        batchSaveDataSechedule.add(itemKafkaLog);
    }
}
