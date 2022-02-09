package com.evolutivelabs.app.counter.batch.controller;

import com.evolutivelabs.app.counter.batch.service.BatchService;
import com.evolutivelabs.app.counter.common.model.batch.BatchModel;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "排程相關")
@RequestMapping("/api/batch")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BatchController {
    private final BatchService batchService;

    @ApiOperation("取得所有排程")
    @GetMapping
    public List<BatchModel> getAllBatches() {
        return batchService.getAllBatchJobDetail();
    }

    @ApiOperation("取得排程")
    @GetMapping("/{taskId}")
    public BatchModel getBatch(@PathVariable String taskId) {
        return batchService.getBatchModel(taskId);
    }

    @ApiOperation("停止排程")
    @DeleteMapping("/{taskId}")
    public BatchModel stopBatch(@PathVariable String taskId) {
        return batchService.stopTask(taskId);
    }

    @ApiOperation("啟動排程")
    @PostMapping("/{taskId}")
    public BatchModel startBatch(@PathVariable String taskId) {
        return batchService.startTask(taskId);
    }
}
