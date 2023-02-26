package com.example.project_lofi;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Defines utilizable methods for all serverside classes.
 * 
 * @author Gwanjin
 */
public class GeneralUtils {
    
    private GeneralUtils(){}

    /**
     * Generate unique random numbers as many as the given number for a given range
     * 
     * @param numOfRandoms a number of random numbers to get
     * @param fromNum a begin number of the random number range
     * @param toNum a end number of the random number range
     * @return a set of numbers that are picked up randomly in a given range
     */
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
