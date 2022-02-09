package com.evolutivelabs.app.counter.common.model.ordercounter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel("產品紀錄")
public class Log {
    @ApiModelProperty("時間戳記")
    Long timestamp;
    @ApiModelProperty("數量")
    Long count = 0L;

    public Log() {}

    public Log(Long timestamp, Long count) {
        this.timestamp = timestamp;
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(timestamp, log.timestamp) && Objects.equals(count, log.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, count);
    }

    @Override
    public String toString() {
        return "Log{" +
                "timestamp=" + timestamp +
                ", count=" + count +
                '}';
    }
}
