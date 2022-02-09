package com.evolutivelabs.app.counter.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@ApiModel("目錄")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Directory {
    @ApiModelProperty("名稱")
    private String name;
    @ApiModelProperty("路徑")
    private String path;
    @ApiModelProperty("類別")
    private String type;
    @ApiModelProperty("是否為資料夾")
    private Boolean isDir;
    @ApiModelProperty("檔案異動日")
    private LocalDateTime lastModified;
    @ApiModelProperty("大小")
    private Long size;
    @ApiModelProperty("子目錄")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<? extends Directory> children;

    public Directory() {}

    public <T> List<T> getChildren() {
        return (List<T>) children;
    }
}
