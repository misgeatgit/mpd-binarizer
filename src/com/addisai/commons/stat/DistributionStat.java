
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
    public static Double[] RateOfChange(Double[] lookBackData, int N) {
        int curPos = lookBackData.length - 1;
        ArrayList<Double> ratios = new ArrayList<>();
        while (curPos - (N + 1) >= 0) {
            ratios.add((lookBackData[curPos] - lookBackData[curPos - (N + 1)]) / N);
            curPos -= 1;
        }
        Double[] ret = new Double[ratios.size()];
        for (int i = ret.length - 1, j = 0; i >= 0; i--, j++) {
            ret[i] = ratios.get(j);
        }
        return ret;
    }

    public static Double[] ratio(Double[] lookBackData, int N) {
        int curPos = lookBackData.length - 1;
        ArrayList<Double> ratios = new ArrayList<>();
        while (curPos - (N + 1) >= 0) {
            ratios.add(lookBackData[curPos] / lookBackData[curPos - (N + 1)]);
            curPos -= 1;
        }
        Double[] ret = new Double[ratios.size()];
        for (int i = ret.length - 1, j = 0; i >= 0; i--, j++) {
            ret[i] = ratios.get(j);
        }
        return ret;
    }

    public static Double[] acceleration(Double[] data1, Double[] data2, int d1, int d2) {
        Double[] acc = null;

        if (data1.length != data2.length) {
            int diff = data2.length - data1.length;
            if (data1.length < data2.length) {
                data2=trimLast(data2, Math.abs(diff));
            } else {
                data1=trimLast(data1, Math.abs(diff));
            }
        } 
            Double[] temp = new Double[data1.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = changeRate(data1[i], data2[i], d1, d2);
            }
            acc = temp;
        
        return acc;
    }

    private static Double changeRate(Double x1, Double x2, int y1, int y2) {
        return (x1 - x2) / (y1 - y2);
    }

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
        int n = 0;
        Double mean = 0d;
        Double M2 = 0d;
        Double variance = Double.NaN;
        for (Double d : data) {
            n = n + 1;
            Double delta = d - mean;
            mean = mean + (delta / n);
            M2 = M2 + delta * (d - mean);
        }
        variance = M2 / (n - 1);

        return variance;
    }

    private static Double[]  trimLast(Double[] data2, int diff) {
        Double[] trimmedData=new Double[data2.length-diff];
       for(int i=data2.length-1,j=trimmedData.length-1;j>=0;j--,i--){
           trimmedData[j]=data2[i];
       }
       return trimmedData;
    }
    //Test case for trim
   /* public static void main(String[] args) {
        Double [] data=new Double[]{1.4,3.5,3.2,4.2};
        data=trim(data,2);
        if(data.length!=2){
            System.out.println("TEST FAILED");
        }
        else{
            System.out.println("TEST PASSED");
        }
    }*/
    //Test case for acceleration
   /* public static void main(String[] args) {
         Double [] t1=new Double[]{2.0,3.0,4.0,10.0};
         Double [] t2=new Double[]{5.0,2.0,3.0,4.0,10.0};
         Double [] acc=acceleration(t1, t2,7,14);
         
         
    }
    */
    //Test case for RateOfChang
    /*public static void main(String[] args) {
     Double [] t1=new Double[]{2.0,3.0,4.0,10.0,12.4,5.0,2.0,3.5,6.0,9.0};
     Double [] res=DistributionStat.RateOfChange(t1,7);
     if(res.length!=1 || res[0]!=(6.0-2.0)/7){
     System.out.println("TEST FAILED");
     }
     else{
     System.out.println("TEST PASSED");
     }
     }*/
    //Test case for ratio
    /*public static void main(String[] args) {
     Double [] t1=new Double[]{2.0,3.0,4.0,10.0,12.4,5.0,2.0,3.5,6.0};
     Double [] res=DistributionStat.ratio(t1,7);
     if(res.length!=1 || res[0]!=3.0){
     System.out.println("TEST FAILED");
     }
     else{
     System.out.println("TEST PASSED");
     }
     }*/
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
