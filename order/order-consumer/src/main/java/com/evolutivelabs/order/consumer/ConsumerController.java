package com.evolutivelabs.order.consumer;

import com.evolutivelabs.order.database.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RequestMapping("/consumer")
@RestController
public class ConsumerController {
    private static final String REST_URL = "http://INGREDIENT-SERVICE";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/scan")
    public Menu get() {
        return restTemplate.getForObject(REST_URL + "/api/scan/menu/test", Menu.class);
    }
}
