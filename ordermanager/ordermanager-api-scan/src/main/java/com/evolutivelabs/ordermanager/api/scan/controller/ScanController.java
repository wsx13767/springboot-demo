package com.evolutivelabs.ordermanager.api.scan.controller;

import com.evolutivelabs.ordermanager.api.scan.service.ScanService;
import com.evolutivelabs.ordermanager.common.controller.CommonController;
import com.evolutivelabs.ordermanager.database.entity.Menu;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/scan")
@RestController
public class ScanController extends CommonController {
    private ScanService scanService;

    @Autowired
    public ScanController(HttpServletRequest httpServletRequest, ScanService scanService) {
        super(httpServletRequest);
        this.scanService = scanService;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/menu/{sno}")
    public Menu getMenu(@PathVariable("sno") String sno) {
        return scanService.searchBySno(sno);
    }
}
