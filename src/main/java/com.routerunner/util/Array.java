package main.java.com.routerunner.util;

import java.util.ArrayList;

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
}
