
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
        Double[] d = data.clone();
        for (int i = d.length - 1; i >= 0; i--) {
            Double total = 0.0;
            for (int j = 0; j < windowSize; j++) {
                if (i - j < 0) {
                    break;
                } else {
                    total += d[i - j];
                }
            }
            d[i] = total / windowSize;
        }
        return d;
    }

    public static Double[] getMovingVariance(Double[] data, int windowSize) {
        Double[] d = data.clone();
        for (int i = (d.length - 1); i >= 0; i--) {
            Double[] varData = null;
            if ((i + 1) >= windowSize) {
                varData = new Double[windowSize];
            } else {
                varData = new Double[i + 1];
            }

            for (int j = 0; j < varData.length; j++) {

                varData[j] = d[i - j];

            }
            d[i] = onlineVariance(varData);

        }
        return d;
    }

    public static Double onlineVariance(Double[] data) {
        /*
         def online_variance(data):
         n = 0
         mean = 0
         M2 = 0 
         for x in data:
         {n = n + 1
         delta = x - mean
         mean = mean + delta/n
         M2 = M2 + delta*(x - mean) 
         }
         variance = M2/(n - 1)
         return variance
         */
        //System.out.println("data\n[");
        //for (Double x : data) {
        //    System.out.print(x + ",");
        //  }
        // System.out.println("]");
        int n = 0;
        Double mean = 0d;
        Double M2 = 0d;
        Double variance = Double.NaN;
        for (Double d : data) {
            n = n + 1;
            //System.out.println("DEBUG:mean="+mean+" d="+d+" n="+n);            
            Double delta = d - mean;
            //System.out.println("DEBUG:delta="+delta);
            mean = mean + (delta / n);
            M2 = M2 + delta * (d - mean);
        }
        variance = M2 / (n - 1);

        return variance;
    }
    //Test case for the onlineVariance method
    //public static void main(String[] args) {
    //   Double[] varData = {679.0, 579.0, 749.0, 728.0, 810.0};
    // Double[] val = getMovingVariance(varData, 5);
    // System.out.println(DistributionStat.onlineVariance(varData));
    //System.out.println(DistributionStat.onlineVariance(new Double[]{1d}));
    // System.out.println(DistributionStat.onlineVariance(new Double[]{0d}));
    // }
    /* public static void main(String[] args) {
     Double [] x = {1.0, 2.0, 3.0, 4.0, 5.0};
     Double [] y=DistributionStat.getMvoingAverage(x, 5);
     int i=0;
     for(Double d:y){            
     System.out.println(x[i]+":"+d);
     i++;
     }
     }
     */
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
