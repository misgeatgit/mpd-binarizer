/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.stat;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class DataSmooth {
    /**
     * 
     * @param data set of row data
     * @param index the index of the value to be smoothed in the data
     * @param windowSize the size of the window
     * @return The moving average of the las n days
     */
  public static double getMA(double[] data,int index,int windowSize){
        int count=0;
        double total=0;
        for(int i=index;i<index+windowSize;i++){
            if(i<data.length){
                count+=1;
                total+=data[i];
                            }
            getMA(data, index, windowSize);
        }
        return total/count;
    }  
  //    public static void main(String[] args) {
//        double [] testData={1,2,3,4,5,6,7,8,9,10};
//        double MAof=getMA(testData, 8, 3);
//        System.out.println("MA of "+testData[8]+" in {1,2,3,4,5,6,7,8,9,10} with window size of 3 "+" is "+MAof);
//    }
}
