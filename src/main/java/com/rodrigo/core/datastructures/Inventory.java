package com.rodrigo.core.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    static void main(String[] args) {
        List<String> products = new ArrayList<>();
        products.add("Laptop");
        products.add("Mouse");

        Map<String, Integer> stock = new HashMap<>();
        stock.put("Laptop", 100);
        stock.put("Mouse", 50);

        String searchProduct = "Laptop";
        if(stock.containsKey(searchProduct)){
            int quantity = stock.get(searchProduct);
            System.out.println("Hay " + quantity + " unidades de " + searchProduct);
        }
    }
}
