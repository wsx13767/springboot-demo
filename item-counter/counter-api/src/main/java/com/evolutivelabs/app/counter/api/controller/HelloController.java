package com.evolutivelabs.app.counter.api.controller;

import com.evolutivelabs.app.counter.common.model.ordercounter.InBoxItems;
import com.evolutivelabs.app.counter.common.model.ordercounter.BoxDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@ApiIgnore
@RequestMapping("/api/hello")
@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);


    @GetMapping("/error")
    public String error() throws Exception {
        try {
            throw new Exception("test");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "error";
    }
    @GetMapping
    public String all() {
        return "all";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable String id, @RequestParam Map map) {
        return "hello";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id, @RequestParam Map map) {
        return "delete";
    }
    @PutMapping("/{id}")
    public String put(@PathVariable String id, @RequestParam Map att, @RequestBody Map map) {
        return "put";
    }
    @PostMapping("/{id}")
    public String post(@PathVariable String id, @RequestParam Map att, @RequestBody Map map) {
        return "post";
    }

    public BoxDetail test(InBoxItems items) {
        return BoxDetail.newBuilder(items.getBoxId())
                .addItem(items)
                .build();
    }
}
