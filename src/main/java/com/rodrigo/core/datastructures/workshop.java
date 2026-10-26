package com.rodrigo.core.datastructures;

import java.util.*;
import java.util.Arrays;

public class workshop {
    static void main(String[] args) {
        String strChallenge = "java java is is is really really really really awesome";

        String[] strArray = strChallenge.split(" ");

        List<String> listStr = Arrays.asList(strArray);

        /*
        for (String letter : strArray) {
            System.out.println(letter);
        }
        */

        // Elimina repetidos
        listStr.stream().distinct().forEach(System.out::println);

        // Cuanta los repetidos
        Map<String, Integer> mapWords = new HashMap<>();
        for(String word : listStr){
            mapWords.put(word, mapWords.getOrDefault(word, 0) + 1);
        }
        System.out.println(mapWords);





    }
}
