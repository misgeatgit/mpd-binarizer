/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.binarizer;

import com.addisai.commons.data.CSVDataManager;
import com.addisai.commons.helpers.Numerics;
import com.addisai.commons.stat.DistributionStat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */public class Binarizer {

    CSVDataManager dm;
    ArrayList<String[]> rawData;
    final int[] M_WINSIZEs = {5, 10, 20, 30, 50}; //different set of moving average window sizes
    int lookAheadDay; //K
    int lookBackDay; //R   
    final int Q_binSize = 5;  //the binsize for the R days lookback moving averages
    boolean catagorizationIs_UP;
    final String CSV_Dir="song_bin_of_";
    File f;
    private String[] featureNames;
   
    enum Category {

        UP, NEUTRAL, DOWN //set values for each
    };

    public Binarizer(String rawDataFilePath, int lookBackDays, int lookAheadDays, boolean catType) {
        try {
            dm = new CSVDataManager(rawDataFilePath);
            this.lookBackDay = lookBackDays;
            this.lookAheadDay = lookAheadDays;
            this.catagorizationIs_UP = catType; 
           
            //create a saving dir
            StringTokenizer stk=new StringTokenizer(CSV_Dir,"/");
            String fileName=null;
            while(stk.hasMoreElements()){
                fileName=stk.nextToken();
            }
            f=new File(CSV_Dir+fileName);
            f.mkdir();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Binarizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Category getCat(Double[] lookBackValues, Double val) {
        List<Double[]> bins = DistributionStat.getBuckets(lookBackValues, 3);
        Double[] midBucket = bins.get(1);
        Category cat = null;
        if (midBucket[0] > val) {
            cat = Category.DOWN;
        } else if (midBucket[0] <= val && midBucket[midBucket.length - 1] >= val) {
            cat = Category.NEUTRAL;
        } else if (midBucket[midBucket.length - 1] < val) {
            cat = Category.UP;
        }
        return cat;
    }

    private void setCatValue(String[] rowBinVlaues, Category cat) {
        if (catagorizationIs_UP) {
            if (cat == Category.UP) {
                rowBinVlaues[rowBinVlaues.length - 1] = "1";
            } else {
                rowBinVlaues[rowBinVlaues.length - 1] = "0";
            }
        }
        if (!catagorizationIs_UP) {
            if (cat == Category.DOWN) {
                rowBinVlaues[rowBinVlaues.length - 1] = "1";
            } else {
                rowBinVlaues[rowBinVlaues.length - 1] = "0";
            }
        }
    }

    private void setFeatureNames() {
        featureNames = new String[2 * Q_binSize * M_WINSIZEs.length + 1]; // The last one is for the target class
        for (int l = 0; l < M_WINSIZEs.length; l++) {
            for (int i = 0; i < Q_binSize * M_WINSIZEs.length; i++) {
                featureNames[i] = "MA" + M_WINSIZEs[l] + "_bin" + i;
            }
        }
        for (int l = 0; l < M_WINSIZEs.length; l++) {
            for (int i = Q_binSize * M_WINSIZEs.length; i < featureNames.length - 1; i++) {
                featureNames[i] = "MV" + M_WINSIZEs[l] + "_bin" + i;
            }
        }
        featureNames[featureNames.length - 1] = "OUT";
    }

    private HashMap<String, Double[]> getSongPopularityhash(List<String[]> headerRemovedData) {
        HashMap<String, Double[]> hashedList = new HashMap<>();
        for (String[] st : headerRemovedData) {
            String musicId = st[0];
            Double[] popularity = new Double[st.length - 1];
            for (int i = 1; i < st.length; i++) {
                popularity[i - 1] = Double.valueOf(st[i]);
            }
            hashedList.put(musicId, popularity);
        }
        return hashedList;
    }

    public void binarize() {
        ArrayList<String[]> headerRemovedData = (ArrayList<String[]>) rawData.clone();
        headerRemovedData.remove(0);
        setFeatureNames();
        HashMap<String, Double[]> songPopularityhash = getSongPopularityhash(headerRemovedData);
        String fileName;  //the file name for the binary
        List<String[]> binData = new ArrayList<>(); //the final output to be saved
        binData.add(featureNames);
        System.out.println("INFO:Started Binarization of");
        for (String key : songPopularityhash.keySet()) { //loop on each song
            System.out.print("music #"+key+"...");
            fileName = key + ".moses";           
            Double[] songPoplarity = songPopularityhash.get(key);
            for (int i = lookBackDay; i < songPoplarity.length; i++) {
                Double[] lookBackData = Numerics.getLookBacks(songPoplarity, i, lookBackDay);
                Category prCat = getCat(lookBackData, songPoplarity[i]); //Modify it with the look ahead value
                String[] rowBinVlaues = new String[featureNames.length]; //the bin values as a string 
                setCatValue(rowBinVlaues, prCat);
                int ref = 0;
                for (int j = 0; j < M_WINSIZEs.length; j++) { //binarize using d/t window sizes with Moving average and Moving variance
                    Double[] lookBackMA = DistributionStat.getMvoingAverage(lookBackData, M_WINSIZEs[j]);
                    Double[] lookBackMV = DistributionStat.getMovingVariance(lookBackData, M_WINSIZEs[j]);
                    List<Double[]> lookBackMABucket = DistributionStat.getBuckets(lookBackMA, Q_binSize);
                    List<Double[]> lookBackMVBucket = DistributionStat.getBuckets(lookBackMV, Q_binSize);


                    //Moving averages binarization
                    Integer[] MABinValue = Numerics.binarize(lookBackMABucket);
                    for (int index = 0; index < MABinValue.length; index++) {
                        rowBinVlaues[ref] = String.valueOf(MABinValue[index]);
                        ref++;
                    }
                    //Moving variance binarization
                    Integer[] MVBinValue = Numerics.binarize(lookBackMVBucket);
                    for (int index = 0; index < MVBinValue.length; index++) {
                        rowBinVlaues[ref] = String.valueOf(MVBinValue[index]);
                        ref++;
                    }
                }
               binData.add(rowBinVlaues);
            }
           //save the CSV generated
            System.out.print("saving("+fileName+")...");
           dm.saveCSV(binData,f.getAbsolutePath()+"/"+fileName);
            System.out.println("finished");
        }
    }
   
}