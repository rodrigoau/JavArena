package com.rodrigo.datastructures;

public class Arrays {
    public static void main(String[] args) {

        int[] ages = new int[3];
        ages[0] = 5;
        ages[1] = 15;
        ages[2] = 25;

        double[] prices = { 1.1, 2.2, 3.3, 4.4, 5.5 };

        for(int i=0; i<prices.length; i++){
            System.out.println("The prices is: $" + prices[i]);
        }

    }


}
