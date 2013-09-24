package com.addisai.commons.data;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com> function merge_sort(list m) // if list size is 1, consider it
 * sorted and return it if length(m) <= 1 return m // else list size is > 1, so
 * split the list into two sublists var list left, right var integer middle =
 * length(m) / 2 for each x in m before middle add x to left for each x in m
 * after or equal middle add x to right // recursively call merge_sort() to
 * further split each sublist // until sublist size is 1 left = merge_sort(left)
 * right = merge_sort(right) // merge the sublists returned from prior calls to
 * merge_sort() // and return the resulting merged sublist return merge(left,
 * right)
 *
 * function merge(left, right) var list result while length(left) > 0 or
 * length(right) > 0 if length(left) > 0 and length(right) > 0 if first(left) <=
 * first(right) append first(left) to result left = rest(left) else append
 * first(right) to result right = rest(right) else if length(left) > 0 append
 * first(left) to result left = rest(left) else if length(right) > 0 append
 * first(right) to result right = rest(right) end while return result
 */
public class MergeSort {

    public static ArrayList merge(ArrayList<Double> left, ArrayList<Double> right) {
        ArrayList<Double> result = new ArrayList<Double>();
        while (left.size() > 0 || right.size() > 0) {
            int i = 0;
            if (left.size() > 0 && right.size() > 0) {
                if (left.get(0) <= right.get(0)) {
                    result.add(left.get(0));
                    left.remove(0);
                } else {
                    result.add(right.get(0));
                    right.remove(0);
                }
            } else if (left.size() > 0) {
                result.add(left.get(0));
                left.remove(0);
            } else if (right.size() > 0) {
                result.add(right.get(0));
                right.remove(0);
            }
        }
        return result;
    }

    public static ArrayList mergeSort(ArrayList<Double> list) {
        if (list.size() <= 1) {
            return list;
        }
        ArrayList<Double> left = new ArrayList<Double>();
        ArrayList<Double> right = new ArrayList<Double>();
        int middle = list.size() / 2;
        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }
        for (int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }
        left = mergeSort(left);
        right = mergeSort(right);
        return merge(left, right);
    }

//    public static void main(String[] args) {
//        final int SIZE = 10;
//        ArrayList<Double> inpArray = new ArrayList<Double>();
//        for (int i = 0; i < SIZE; i++) {
//            Random rand = new Random();
//            float x = Math.abs(rand.nextInt() % SIZE);
//            inpArray.add(x);
//        }
//        System.out.println("Before merge sort");
//        for (int index = 0; index < inpArray.size(); index++) {
//            System.out.print(inpArray.get(index) + " ");
//        }
//        System.out.println("");
//        long initTime = System.nanoTime();
//        inpArray = MergeSort.mergeSort(inpArray);
//        long finalTime = System.nanoTime();
//        System.out.println("After merge sort");
//        for (int index = 0; index < inpArray.size(); index++) {
//            System.out.print(inpArray.get(index) + " ");
//        }
//        System.out.println("***Time taken:" + (finalTime - initTime) / (Math.pow(10, 6)) + " milli seconds");
//    }
}
