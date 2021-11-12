package com.siang.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.SubmissionPublisher;


class ReactorApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ReactorApplicationTests.class);

    @Test
    void contextLoads() throws InterruptedException {
        Member<String> member = new Member<>();
        SubmissionPublisher<String> channel = new SubmissionPublisher<>();
        channel.subscribe(member);
        List<String> episodes = List.of("1", "2", "3", "4");
        episodes.forEach(channel::submit);
        logger.debug("episodes:{}", member.getEpisodes());
        Thread.sleep(1000);
        channel.close();
        logger.debug("episodes:{}", member.getEpisodes());
    }

    @Test
    void test2() throws InterruptedException {
        Member<String> member = new Member<>();
        SubmissionPublisher<String> channel = new SubmissionPublisher<>();
        ApplePodcastProcessor<String, String> processor = new ApplePodcastProcessor<>(item -> "apple : " + item);
        channel.subscribe(processor);
        processor.subscribe(member);
        List<String> episodes = List.of("1", "2", "3", "4");
        episodes.forEach(channel::submit);
        logger.debug("episodes:{}", member.getEpisodes());
        Thread.sleep(1000);
        channel.close();
        logger.debug("episodes:{}", member.getEpisodes());
    }

    @Test
    void test3() {
        Flux<String> seq1 = Flux.just("Robert", "Jean", "Jerry");
        seq1.flatMap(i -> Flux.just(i.split(""))).subscribe(System.out::println);

        Flux<String> seq2 = Flux.fromIterable(Arrays.asList("Robert", "Jean", "Jerry"));

        Flux<Integer> numbersFromFiveToSix = Flux.range(5, 2);
        numbersFromFiveToSix.subscribe(i -> logger.debug(i.toString()), error -> logger.debug("Eooro {}", error),
                () -> logger.debug("Done"));
        Mono<String> mono = Mono.just("Robert");
        Mono<String> mono1 = Mono.empty();
        Mono<String> mono2 = Mono.justOrEmpty("Robert");
    }

    @Test
    void test4() {
        Flux<String> name = Flux.just("Robert", "Jean", "Jerry");
        Flux<Integer> age = Flux.just(30, 39, 36);
        Flux<String> sex = Flux.just("M", "F");
        Flux<Tuple3<String, Integer, String>> zip = Flux.zip(name, age, sex);
        zip.map(data -> new Customer(data.getT1(), data.getT2(), data.getT3()))
                .subscribe(o -> logger.debug("{}", o));
    }

    @Test
    void test5() {
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana");
        fruitFlux.subscribe(f -> logger.debug("Here's son=me fruit: {}", f));
        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .verifyComplete();
    }

    @Test
    void test6() throws InterruptedException {
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));
        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));
        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);
        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete();
    }

    @Test
    void test7() {
        Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
                .buffer(2)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(y -> y.toUpperCase())
                                .subscribeOn(Schedulers.parallel())
                                .log()).subscribe();

    }

    class Customer {
        private String name;
        private Integer age;
        private String sex;

        public Customer(String name, Integer age, String sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }
}
