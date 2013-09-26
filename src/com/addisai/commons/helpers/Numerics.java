/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.helpers;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Numerics {
    public static double[] doubleObjToDoublePrim(Double [] values){
        final int length=values.length;
        double [] primitives=new double[length];
        for(int i=0;i<length;i++){
          primitives[i]=values[i];
        }
        return primitives;
    }
    public static Double[] doublePrimToDoubleObj(double [] values){
       final int length=values.length;
        Double [] doubleObjects=new Double[length];
        for(int i=0;i<length;i++){
          doubleObjects[i]=values[i];
        }
        return doubleObjects;  
    }
    public static void sort(Double [] data){
        Arrays.sort(data);
    }
    public static Integer [] binarize(List<Double[]> buckets){
        return null;
    }
    public static Double[] getLookBacks(Double [] data,int lookBackSize){
        return null;
    }
    public static void main(String[] args) {
        Double [] d=new Double[]{2d,3d,56d,4d,5d};
        double [] dPrim=Numerics.doubleObjToDoublePrim(d);
        double [] dprm=new double[]{3d,56d,4d,45d,4d};
        Double [] dobject=Numerics.doublePrimToDoubleObj(dprm);
        boolean isEqualLen= (d.length==dPrim.length);
        boolean conversionErr=false;
        if(isEqualLen){
            System.out.println("PASSED:len matching");
            for(int i=0;i<dprm.length;i++){
                if(dobject[i]!=dprm[i]){
                    conversionErr=true;
                    System.out.println("FAILED:conversion err@index"+i);
                }
                else{
                    continue;
                }
            }
            if(!conversionErr)
                System.out.println("PASSED:conversion cross check");
        }
        else{
            System.out.println("FAILED:unmatching length err");
        }
    }
}
