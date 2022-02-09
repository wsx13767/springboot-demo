package com.evolutivelabs.app.counter.batch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BatchServiceTest {
    @Autowired
    private BatchService batchService;

    static class Test2 {
        String prefix;
        String suffix;
        public Test2(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public String toString() {
            return "Test2{" +
                    "prefix='" + prefix + '\'' +
                    ", suffix='" + suffix + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        Long qty = 155L;
        Double volume = 0.9;


        System.out.println(Double.valueOf(4.99999).longValue());
//        System.out.println(Paths.get("/asd/dasd/da").getName(0));
//
//        List<Test2> list = new ArrayList<>();
//        list.add(new Test2("a1", "a1"));
//        list.add(new Test2("a1", "a2"));
//        list.add(new Test2("a2", "a1"));
//        list.add(new Test2("a1", "a3"));
//        list.add(new Test2("a2", "a2"));
//        list.add(new Test2("a1", "a5"));
//        list.add(new Test2("a1", "a4"));
//
//        Collections.sort(list, (v1, v2) -> v1.prefix.compareTo(v2.prefix) == 0 ? v2.suffix.compareTo(v1.suffix) : v1.prefix.compareTo(v2.prefix));
//        System.out.println(list);
//        System.out.println("23".endsWith(""));
//        System.out.println(Double.valueOf("12.3").longValue());
//
//
//        System.out.println(new TypeReference<List<String>>() {}.getType());
    }
}