package com.siang.reactor.controller;

import com.siang.reactor.database.entity.Taco;
import com.siang.reactor.database.repository.TacoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RequestMapping("/design")
@RestController
public class DesignTacoController {
    private static final Logger logger = LoggerFactory.getLogger(DesignTacoController.class);

    @Autowired
    private TacoRepository tacoRepository;


    @GetMapping("/recent")
    public Flux<Taco> recentTacos() {
        return tacoRepository.findAll().take(12);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable Long id) {
        return tacoRepository.findById(id);
    }

    @PostMapping
    public Mono<Taco> postTaco(@RequestBody Mono<Taco> taco) {
        return tacoRepository.saveAll(taco).next();
    }

    @GetMapping("/test")
    public Flux<String> testFlux() {
        long timeMillis = System.currentTimeMillis();
        logger.info("webflux() start");
        Flux<String> result = Flux.fromStream(IntStream.range(1, 20)
                .mapToObj(i -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e) {
                        logger.error("", e);
                    }
                    return "flux data-" + i;
                }));
        logger.info("webflux() end use time {}/ms", System.currentTimeMillis() - timeMillis);
        return result;
    }
}
