package com.siang.security.server;

import java.util.*;
import java.util.stream.Collectors;

public class ListTest {
    public static void main(String[] args) {





        String[] array = {"a", "b"};
        String[] array2 = new String[2];
        List<String> list = new ArrayList<>(Arrays.asList(array));
        Random random = new Random();

        List<String> list2 = Collections.synchronizedList(list);
        list2.add("");



        int index = 0;
        do {
            for (int i = 0; i < array.length; i++) {
                list.remove(array[i]);
                String santa = list.get(random.nextInt(list.size()));
                list.remove(santa);
                list.add(array[i]);
                array2[i] = santa;
            }
            System.out.println(Arrays.toString(array));
            System.out.println(Arrays.toString(array2));
            System.out.println("----");
            index++;
        } while (index < 20);



    }
}
