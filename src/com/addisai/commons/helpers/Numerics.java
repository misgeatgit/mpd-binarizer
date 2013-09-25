/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.helpers;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Numerics {
    public static double[] doublePrimitiveOf(Double [] values){
        final int length=values.length;
        double [] primitives=new double[length];
        for(int i=0;i<length;i++){
          primitives[i]=values[i];
        }
        return primitives;
    }
    public static void main(String[] args) {
        Double [] d=new Double[]{2d,3d,56d,4d,5d};
        double [] dPrim=Numerics.doublePrimitiveOf(d);
        boolean isEqualLen= (d.length==dPrim.length);
        boolean conversionErr=false;
        if(isEqualLen){
            System.out.println("PASSED:len matching");
            for(int i=0;i<d.length;i++){
                if(d[i]!=dPrim[i]){
                    conversionErr=true;
                    System.out.println("FAILED:conversion err@inde"+i);
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
