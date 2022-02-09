package com.evolutivelabs.app.counter.common.model.ordercounter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Objects;

@Data
@ApiModel("掃描內容")
public class InBoxItems {
    @NotBlank(message = "Excel序號不可為空")
    @ApiModelProperty("excel序號")
    private String shippment_id;

    @NotBlank(message = "裝箱序號不可空白")
    @ApiModelProperty(value = "裝箱序號", example = "AJCijcm39Jd93NCj93kjHNC", required = true)
    private String boxId;

    @Min(0)
    @NotNull(message = "時間戳記不能為空")
    @ApiModelProperty(value = "時間戳記", example = "1639544687760", required = true)
    private Long timestamp;

    @ApiModelProperty(value = "產品序號", example = "4710227231632")
    private String barcode;

    @ApiModelProperty(value = "來源", example = "掃碼槍")
    private String from;

    @NotNull(message = "是否有錯誤不可為空")
    @ApiModelProperty(value = "是否錯誤", required = true)
    private Boolean error;

    @ApiModelProperty(value = "狀態描述", example = "成功")
    private String status;

    @NotNull(message = "數量不可為空")
    @ApiModelProperty(value = "數量", required = true)
    private Long multiple;

}
