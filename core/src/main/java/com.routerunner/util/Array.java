package com.routerunner.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Array includes static helper methods for operations on array
 * that might be used in other places of the code.
 */
public class Array {

    public static ArrayList<Integer> cumSum(ArrayList<Integer> elements) {
        ArrayList<Integer> sum = new ArrayList<>(elements.size()) ;
        int total = 0 ;
        for (Integer element : elements) {
            total += element;
            sum.add(total);
        }
        return sum ;
    }
    public static ArrayList<Integer> createMask(ArrayList<Integer> numbers, int len) {
       ArrayList<Integer> mask = new ArrayList<>(Collections.nCopies(len, 0)) ;
       for (int num: numbers) {
           mask.set(num, 1) ;
       }
       return mask ;
    }
}
