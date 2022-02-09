package com.evolutivelabs.app.counter.common.model.batch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("排程資訊")
@Data
public class BatchModel {
    @ApiModelProperty("代號")
    private String taskId;
    @ApiModelProperty("名稱")
    private String name;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("資料庫狀態")
    private Boolean status;
    @ApiModelProperty("系統別")
    private String system;
    @ApiModelProperty("執行時間")
    private String scheduledTime;
    @ApiModelProperty("是否啟動")
    private Boolean isOn;
}
