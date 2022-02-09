package com.evolutivelabs.app.counter.api.controller;

import com.evolutivelabs.app.counter.api.service.OrderCountService;
import com.evolutivelabs.app.counter.common.exception.NotFoundException;
import com.evolutivelabs.app.counter.common.model.ordercounter.BoxDetail;
import com.evolutivelabs.app.counter.common.model.ordercounter.InBoxItems;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemLog;
import com.evolutivelabs.app.counter.database.mysql.repository.ItemLogRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "裝箱數貨")
@RequestMapping("/api/orderCounter")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderCountController {
    private static final Logger logger = LoggerFactory.getLogger(OrderCountController.class);
    private final ItemLogRepository itemLogRepository;
    private final OrderCountService orderCountService;
    private final HttpServletRequest request;

    @ApiOperation("箱內商品數量累計")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "新增成功"),
            @ApiResponse(responseCode = "400", description = "檢核有誤")
    })
    @PostMapping(value = "/inBoxItems")
    public String inBoxItems(@Valid @RequestBody InBoxItems inBoxItems) throws MethodArgumentNotValidException {
        ItemLog item = new ItemLog();
        item.setShippmentId(inBoxItems.getShippment_id());
        item.setBoxId(inBoxItems.getBoxId());
        item.setLogtime(inBoxItems.getTimestamp());
        item.setBarcode(inBoxItems.getBarcode());
        item.setSource(inBoxItems.getFrom());
        item.setError(inBoxItems.getError());
        item.setStatus(inBoxItems.getStatus());
        item.setMultiple(inBoxItems.getMultiple());
        item.setIp(request.getRemoteAddr());

        itemLogRepository.save(item);

        return "新增紀錄成功";
    }

    @ApiOperation("刪除紀錄")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "刪除成功"),
            @ApiResponse(responseCode = "400", description = "檢核有誤")
    })
    @DeleteMapping("/boxItems/{boxId}")
    public String resetItemCounts(@Validated @PathVariable @NotBlank(message = "裝箱序號不可為空") String boxId) {
        itemLogRepository.deleteByBoxId(boxId);
        return "刪除成功";
    }

    @ApiOperation("箱內全部數量")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "檢核有誤"),
            @ApiResponse(responseCode = "404", description = "查無資料")
    })
    @GetMapping("/boxItems/{boxId}")
    public BoxDetail showDetails(@Validated @PathVariable @NotBlank(message = "裝箱序號不可為空") String boxId) {
        List<ItemLog> itemEntityList = itemLogRepository.findByBoxId(boxId);
        if (CollectionUtils.isEmpty(itemEntityList)) {
            throw new NotFoundException("查無相關資料");
        }
        BoxDetail box = orderCountService.getBoxDetail(boxId, itemEntityList);

        return box;
    }
}
