/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.stat;

import com.addisai.commons.data.MergeSort;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class DistributionStat {

    /**
     * here the technique is somewhat diffferent than the conventional. i.e the
     * data is sorted and divided into n bins in our case
     *
     * @param data
     * @param binSize
     * @return buckets of binsize
     */
    public static List<Double[]> getBuckets(Double[] data, int binSize) {
        List<Double[]> quantileList = new ArrayList<>();
        List<Double> dataList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            dataList.add(data[i]);
        }
        List<Double> sortedData = MergeSort.mergeSort((ArrayList) dataList);
        if (binSize >= sortedData.size()) {
            quantileList.add((Double[]) sortedData.toArray());
            return quantileList;
        } else {
            int qGroupSize = (sortedData.size() - (sortedData.size() % binSize)) / binSize;
            int remainingSize = sortedData.size() % binSize;
            int index = 0;
            for (int i = 0; i < binSize; i++) {
                Double[] qGroup;
                if (i == binSize - 1) {
                    qGroup = new Double[qGroupSize + remainingSize];
                    for (int groupSize = 0; groupSize < qGroupSize + remainingSize; groupSize++) {
                        qGroup[groupSize] = sortedData.get(index);
                        index++;
                    }
                } else {
                    qGroup = new Double[qGroupSize];
                    for (int groupSize = 0; groupSize < qGroupSize; groupSize++) {
                        qGroup[groupSize] = sortedData.get(index);
                        index++;
                    }
                }
                quantileList.add(qGroup);
            }
        }
        return quantileList;
    }

    public static Double[] getMvoingAverage(Double[] data, int windowSize) {
        return null;
    }

    public static Double[] getMovingVariance(Double[] data,int windowSize) {
        return null;
    }
    /**
     * For testing purpose
     *
     * @param args
     */
    /* public static void main(String[] args) {
     double[] testData = {470, 541, 745, 765, 834, 705, 511, 699, 531, 580, 591, 663, 563, 535};
     System.out.println("Initial Data:{470,541,745,765,705,511,699,531,580,591,663,563,535,616}");
     List<Double[]> testq = getBuckets(testData, 3);
     for (int i = 0; i < testq.size(); i++) {
     System.out.print("[");
     for (int j = 0; j < testq.get(i).length; j++) {
     System.out.print(testq.get(i)[j] + ",");
     }
     System.out.println("]");
     }  */
}
