package com.evolutivelabs.app.counter.database.mysql;

import com.evolutivelabs.app.counter.database.mysql.repository.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RepositoryFactory {
    /**
     * 品項資訊
     */
    private final ItemInfoRepository itemInfoRepository;

    /**
     * 箱子資訊
     */
    private final BoxInfoRepository boxInfoRepository;
    /**
     * 裝箱欄位mapping
     */
    private final PackingConfigRepository packingConfigRepository;
    /**
     * 排程設定表
     */
    private final BatchScheduleRepository batchScheduleRepository;

    /**
     * 客戶與箱子資料mapping
     */
    private final BoxOfBusinessRepository boxOfBusinessRepository;

    /**
     * excel處理結果
     */
    private final ExcelFileRepository excelFileRepository;

    /**
     * excel分箱
     */
    private final BoxOfExcelRepository boxOfExcelRepository;

    /**
     * 箱內的商品
     */
    private final ItemsOfBoxRepository itemsOfBoxRepository;

    /**
     * 使用者資訊
     */
    private final UserInfoRepository userInfoRepository;

    private final ApiGroupRepository apiGroupRepository;

    private final ApiServerRepository apiServerRepository;

    private final ApiRouteRepository apiRouteRepository;
}
