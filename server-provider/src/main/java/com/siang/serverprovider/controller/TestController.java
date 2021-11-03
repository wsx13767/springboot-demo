package com.siang.serverprovider.controller;

import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private static final String SERVICE_INSTANCE = "SERVER-PROVIDER";
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/info")
    public String info() {
        ServiceInstance instance = discoveryClient.getInstances(SERVICE_INSTANCE).get(0);
        String info = "host: " + instance.getHost() + ", service_id: " + instance.getServiceId();
        logger.info(info);
        return info;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
