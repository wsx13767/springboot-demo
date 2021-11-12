package com.siang.reactor.controller;

import com.siang.reactor.database.entity.Ingredient;
import com.siang.reactor.database.entity.Taco;
import com.siang.reactor.database.repository.TacoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebFluxTest(controllers = DesignTacoController.class)
class DesignTacoControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(DesignTacoControllerTest.class);

    @Autowired
    WebTestClient testClient;

    @MockBean
    TacoRepository tacoRepository;

    @Test
    void shouldReturnRecentTacos() {
        Taco[] tacos = {
                testTaco(0L), testTaco(1L), testTaco(2L), testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L), testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L), testTaco(11L), testTaco(12L), testTaco(13L),
                testTaco(14L), testTaco(15L), testTaco(16L), testTaco(17L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);
        Mockito.when(tacoRepository.findAll()).thenReturn(tacoFlux);

        byte[] result = testClient.get().uri("/design/recent")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty().returnResult().getResponseBody();
        logger.info(new String(result));

    }

    Taco testTaco(Long id) {
        Taco taco = new Taco();
        taco.setId(id);
        taco.setName(UUID.randomUUID().toString());
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
        taco.setIngredientList(ingredients);
        return taco;
    }
}