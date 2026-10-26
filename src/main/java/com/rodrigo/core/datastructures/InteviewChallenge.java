package com.rodrigo.core.datastructures;
import java.util.Arrays;

public class InteviewChallenge {
    static void main() {
        int[][] input = { {5,3}, {5,9,7}, {0,8}, {1,2} };
        System.out.println(Arrays.toString(minOfEach(input)));

    }

    public static int[] minOfEach(int[][] input) {
        int[] output = new int[input.length];
        for(int i=0; i < input.length; i++){
            int min = input[i][0];
            for(int n: input[i]){
                if(n < min) min = n;
            }
            output[i] = min;
        }
        return output;
    }
}
