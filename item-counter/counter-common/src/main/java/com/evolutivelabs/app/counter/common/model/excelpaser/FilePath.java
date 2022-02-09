package com.evolutivelabs.app.counter.common.model.excelpaser;

import com.evolutivelabs.app.counter.common.model.Directory;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@ApiModel("目錄")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilePath extends Directory {
    @ApiModelProperty("檔案序號")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @ApiModelProperty("是否已經執行")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean done;
}
