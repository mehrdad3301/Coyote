package com.routerunner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static ArrayList<Integer> generateRandom(int numPoints, int lowerBound, int upperBound) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = lowerBound ; i <= upperBound; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        ArrayList<Integer> randomNum = new ArrayList<>(numbers.subList(0, numPoints));
        return randomNum ;
    }

    public static int getPositiveRandom(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }
}
