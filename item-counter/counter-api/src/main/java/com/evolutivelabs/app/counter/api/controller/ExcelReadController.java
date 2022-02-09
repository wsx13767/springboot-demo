package com.evolutivelabs.app.counter.api.controller;

import com.evolutivelabs.app.counter.api.schedule.FileFindSchedule;
import com.evolutivelabs.app.counter.api.service.ExcelFileService;
import com.evolutivelabs.app.counter.common.model.excelpaser.ExcelResult;
import com.evolutivelabs.app.counter.common.model.excelpaser.ExcelRow;
import com.evolutivelabs.app.counter.common.model.excelpaser.ExcelSheet;
import com.evolutivelabs.app.counter.common.model.excelpaser.FilePath;
import com.evolutivelabs.app.counter.database.mysql.entity.ExcelFile;
import com.evolutivelabs.app.counter.database.mysql.repository.ExcelFileRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = "檔案查詢")
@RequestMapping("/api/excelRead")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExcelReadController {
    private final ExcelFileService excelFileService;
    private final ExcelFileRepository excelFileRepository;

    @ApiOperation("讀取所有目錄")
    @GetMapping("/allPath")
    public FilePath allPath() {
        return excelFileService.getFilePath();
    }

    @ApiOperation("取得檔案內容")
    @GetMapping(value = "/file/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "404", description = "查無excel檔案")
    public ExcelResult chooseExcelFile(@PathVariable @ApiParam("檔案序號") @NotBlank(message = "檔案id不可為空") String id) throws IOException {
        ExcelRow excelPaserContent = new ExcelRow();
        ExcelFile excelFile = excelFileService.chooseExcelFile(id);
        return excelFileService.getExcelContent(excelFile);
    }
}
