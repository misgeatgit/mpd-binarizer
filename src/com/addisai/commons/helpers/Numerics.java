/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Numerics {

    public static double[] doubleObjToDoublePrim(Double[] values) {
        final int length = values.length;
        double[] primitives = new double[length];
        for (int i = 0; i < length; i++) {
            primitives[i] = values[i];
        }
        return primitives;
    }

    public static Double[] doublePrimToDoubleObj(double[] values) {
        final int length = values.length;
        Double[] doubleObjects = new Double[length];
        for (int i = 0; i < length; i++) {
            doubleObjects[i] = values[i];
        }
        return doubleObjects;
    }

    public static void sort(Double[] data) {
        Arrays.sort(data);
    }

    public static Integer[] binarize(List<Double[]> buckets, Double val) {
        int length = buckets.size();
        //set the bits to zero intially
        Integer[] bits = new Integer[length];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = 0;
        }
        //binarize val
        for (int i = 0; i < bits.length; i++) {
            Double[] d = buckets.get(i);
            if (i != bits.length - 1) {
                if (d[d.length - 1] >= val) {
                    bits[i] = 1;
                    break;
                }
            } else {
                if (d[0] <= val) {
                    bits[i] = 1;
                    break;
                }
            }
        }
        return bits;
    }

    public static Double[] getLookBacks(Double[] data, int refIndex, int lookBackSize) throws Exception {
        Double[] lookBacks = new Double[lookBackSize];
        if ((refIndex + 1) < lookBackSize) {
            throw new Exception("lookBack size exceeds the data size");
        } else {

            for (int i = 0; i < lookBacks.length; i++) {
                lookBacks[i] = data[refIndex - i];
            }

        }
        return lookBacks;
    }

    //Test case for binarize
   
    /*public static void main(String[] args) {
        List<Double[]> x = new ArrayList<>();
        x.add(new Double[]{1d, 2d, 3d, 4d, 5d});
        x.add(new Double[]{6d, 7d, 8d, 9d, 10d});
        x.add(new Double[]{11d, 12d, 13d, 14d, 15d});
        x.add(new Double[]{16d, 17d, 18d, 19d, 20d});
        x.add(new Double[]{21d, 22d, 23d, 24d, 25d});
        List<Double[]> y = new ArrayList<>();
        y.add(new Double[]{1d});
        y.add(new Double[]{6d});
        y.add(new Double[]{11d});
        y.add(new Double[]{16d});
        y.add(new Double[]{21d});
        Integer [] bits=Numerics.binarize(y,25d);
        if(bits[0]==1)
            System.out.println("PASSED");
        for(Integer z: bits){
            System.out.print(z); 
        }
    }*/
    
    /*
     //Test case for getLookBacks method
     public static void main(String[] args) {
     Double[] d = new Double[]{2d, 3d, 56d, 4d, 5d,14d,12d,45d,65d,76d};
     try {
     Double [] l=Numerics.getLookBacks(d, 3, 6);
     for(Double x:l){
     System.out.print(x+",");
     }
     } catch (Exception ex) {
     ex.getMessage();
     Logger.getLogger(Numerics.class.getName()).log(Level.SEVERE, null, ex);
     }
     }  
     */
    /*
     //Test method for doublePrimToDoubleObj and doubleObjToDoublePrim
     public static void main(String[] args) {
     Double[] d = new Double[]{2d, 3d, 56d, 4d, 5d};
     double[] dPrim = Numerics.doubleObjToDoublePrim(d);
     double[] dprm = new double[]{3d, 56d, 4d, 45d, 4d};
     Double[] dobject = Numerics.doublePrimToDoubleObj(dprm);
     boolean isEqualLen = (d.length == dPrim.length);
     boolean conversionErr = false;
     if (isEqualLen) {
     System.out.println("PASSED:len matching");
     for (int i = 0; i < dprm.length; i++) {
     if (dobject[i] != dprm[i]) {
     conversionErr = true;
     System.out.println("FAILED:conversion err@index" + i);
     } else {
     continue;
     }
     }
     if (!conversionErr) {
     System.out.println("PASSED:conversion cross check");
     }
     } else {
     System.out.println("FAILED:unmatching length err");
     }
     }*/
}
