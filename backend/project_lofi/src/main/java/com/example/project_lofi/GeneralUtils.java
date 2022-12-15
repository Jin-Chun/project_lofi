package com.example.project_lofi;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeneralUtils {
    
    // Generate unique random numbers as many as the given number for a given range
    public static Set<Integer> generateUniqueRandomNumbers(int numOfRandoms, int fromNum, int toNum){

        int totalNumOfGivenRange = toNum - fromNum + 1;
        Set<Integer> result = new HashSet<>();

        if (numOfRandoms > totalNumOfGivenRange){
            String message = String.format("The given num(numOfRandoms: %d) cannot exceed the total number of the given range (from %d to %d)",
             numOfRandoms, fromNum, toNum);
            throw new IllegalArgumentException(message);

        } else if (numOfRandoms == totalNumOfGivenRange){
            for (int i = fromNum; i < toNum+1; i++){
                result.add(i);
            }

        } else {
            while(result.size() < numOfRandoms){

                Random random = new Random();
                int n = random.nextInt(toNum - fromNum + 1)+fromNum;
                result.add(n);
            }
        }
        return result;
    }
}
