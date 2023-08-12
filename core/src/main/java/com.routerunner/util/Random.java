package com.routerunner.util;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static ArrayList<Integer> generateRandom(int numPoints, int lowerBound, int upperBound) {
        ArrayList<Integer> randomNum = new ArrayList<>(numPoints) ;
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < numPoints; i++) {
            int num = rand.nextInt((upperBound - lowerBound) + 1) + lowerBound;
            randomNum.add(num) ;
        }
        return randomNum ;
    }

    public static int getPositiveRandom(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }
}
