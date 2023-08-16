package com.routerunner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    /**
     * this can be made more efficient easily, but when numPoints
     * are limited, this is good enough
     * @param numPoints number of random numbers to be generated
     * @return returns numPoints distinct random numbers
     */
    public static ArrayList<Integer> generateRandom(int numPoints, int lowerBound, int upperBound) {
        ArrayList<Integer> randomNum = new ArrayList<>(numPoints);
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < numPoints; i++) {
            int num;
            do {
                num = rand.nextInt((upperBound - lowerBound) + 1) + lowerBound;
            } while (randomNum.contains(num));
            randomNum.add(num);
        }
        return randomNum ;
    }

    public static int getPositiveRandom(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }

}
