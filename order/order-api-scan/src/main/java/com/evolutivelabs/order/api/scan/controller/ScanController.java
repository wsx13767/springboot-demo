package com.evolutivelabs.order.api.scan.controller;

import com.evolutivelabs.order.api.scan.service.ScanService;
import com.evolutivelabs.order.database.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/scan")
@RestController
public class ScanController {
    @Autowired
    private ScanService scanService;

    @Autowired
    private DiscoveryClient client;

    @GetMapping("/menu/{sno}")
    public Menu find(@PathVariable("sno") String sno) {
        return scanService.getMenuBySno(sno);
    }

    @GetMapping("/discovery")
    public Object discovery() {
        return client;
    }
}
