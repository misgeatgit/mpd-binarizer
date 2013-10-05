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
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Binarizer {

    CSVDataManager dm;
    ArrayList<String[]> rawData;
    final int[] M_WINSIZEs = {5, 10, 20, 30, 50}; //different set of moving average window sizes
    int lookAheadDay; //K
    int lookBackDay; //R   
    final int Q_binSize = 5;  //the binsize for the R days lookback moving averages
    boolean catagorizationIs_UP;
    final String CSV_Dir = "song_bin_of_";
    File f;
    private String[] featureNames;

    enum Category {

        UP, NEUTRAL, DOWN //set values for each
    };

    public Binarizer(String rawDataFilePath, int lookBackDays, int lookAheadDays, boolean catType) {
        try {
            dm = new CSVDataManager(rawDataFilePath);
            this.rawData = (ArrayList<String[]>) dm.getData();
            // System.out.println("SIZE="+rawData.size());
            this.lookBackDay = lookBackDays;
            this.lookAheadDay = lookAheadDays;
            this.catagorizationIs_UP = catType;

            //create a saving dir
            StringTokenizer stk = new StringTokenizer(rawDataFilePath, "/");
            String fileName = null;
            while (stk.hasMoreElements()) {
                fileName = stk.nextToken();
            }
            f = new File(CSV_Dir + fileName+"-K="+lookAheadDays+"&-R="+lookBackDays+"&-cat="+(catagorizationIs_UP?"UP":"DOWN"));
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
        int ref = 0;
        for (int l = 0; l < M_WINSIZEs.length; l++) {
            for (int i = 0; i < Q_binSize; i++) {
                featureNames[ref + i] = "MA" + M_WINSIZEs[l] + "_bin" + (i + 1);
            }
            ref += (Q_binSize);
        }
        //int ref=Q_binSize*M_WINSIZEs.length;//the next location for setting other feature names
        for (int l = 0; l < M_WINSIZEs.length; l++) {
            for (int i = 0; i < Q_binSize; i++) {
                featureNames[ref + i] = "MV" + M_WINSIZEs[l] + "_bin" + (i + 1);
            }
            ref += (Q_binSize);
        }
        featureNames[featureNames.length - 1] = "OUT";
    }

    private HashMap<String, Double[]> getSongPopularityhash(List<String[]> headerRemovedData) {
        HashMap<String, Double[]> hashedList = new HashMap<>();
        for (String[] st : headerRemovedData) {
            String musicId = st[0];
            Double[] popularity = new Double[st.length - 1];
            for (int i = 1; i < st.length; i++) { // popularity starts at the second index in the file
                popularity[i - 1] = Double.valueOf(st[i]);
            }
            hashedList.put(musicId, popularity);
        }
        return hashedList;
    }

    public void binarize() {
        ArrayList<String[]> headerRemovedData = (ArrayList<String[]>) rawData.clone();
        int expectedNoOfRows=headerRemovedData.get(0).length-1-lookBackDay; //for assertion
        headerRemovedData.remove(0); //remove the date row
        setFeatureNames();
        HashMap<String, Double[]> songPopularityhash = getSongPopularityhash(headerRemovedData);
        System.out.println("NO of songs="+songPopularityhash.size());
        String fileName;  //the file name for the binary
        
        System.out.println("INFO:Started Binarization of");
        for (String key : songPopularityhash.keySet()) { //loop on each song
            List<String[]> binData = new ArrayList<>(); //the final output to be saved
            binData.add(featureNames);
            System.out.print("music #" + key + "...");
            fileName = key + ".moses";
            Double[] songPoplarity = songPopularityhash.get(key);
            for (int i = lookBackDay; i < songPoplarity.length; i++) {
                Double[] lookBackData = null;
                try {
                    lookBackData = Numerics.getLookBacks(songPoplarity, i, lookBackDay);                   
                } catch (Exception ex) {
                    ex.getMessage();
                }
                Category prCat = null;
                if (i + lookAheadDay > (songPoplarity.length - 1)) { // get the cat of D+K
                    prCat = getCat(lookBackData, songPoplarity[songPoplarity.length - 1]);
                } else {
                    prCat = getCat(lookBackData, songPoplarity[i + lookAheadDay]);
                }
                String[] rowBinVlaues = new String[featureNames.length]; //the bin values as a string 
                setCatValue(rowBinVlaues, prCat);
                int ref = 0;
                for (int j = 0; j < M_WINSIZEs.length; j++) { //binarize using d/t window sizes with Moving average and Moving variance
                    Double[] lookBackMA = DistributionStat.getMvoingAverage(lookBackData, M_WINSIZEs[j]);
                    Double[] lookBackMV = DistributionStat.getMovingVariance(lookBackData, M_WINSIZEs[j]);
                    List<Double[]> lookBackMABucket = DistributionStat.getBuckets(lookBackMA, Q_binSize);
                    List<Double[]> lookBackMVBucket = DistributionStat.getBuckets(lookBackMV, Q_binSize);


                    //Moving averages binarization
                    Integer[] MABinValue = Numerics.binarize(lookBackMABucket, lookBackMA[lookBackMA.length - 1]);
                    for (int index = 0; index < MABinValue.length; index++) {
                        rowBinVlaues[ref] = String.valueOf(MABinValue[index]);
                        ref++;
                    }
                    //Moving variance binarization
                    Integer[] MVBinValue = Numerics.binarize(lookBackMVBucket, lookBackMV[lookBackMV.length - 1]);
                    for (int index = 0; index < MVBinValue.length; index++) {
                        rowBinVlaues[ref] = String.valueOf(MVBinValue[index]);
                        ref++;
                    }
                }
                if (rowBinVlaues.length != featureNames.length) {
                    System.out.println("FATAL ERROR:feature names and row values doesnt match in length");
                    System.exit(0);
                }
                binData.add(rowBinVlaues);
            }
            //save the CSV generated
            System.out.print("saving(" + fileName + ")...");
            int actualRows=binData.size()-1;
            System.out.println("\nexpected rows:"+expectedNoOfRows+" actual rows:"+actualRows);
            //showOut(binData);
            dm.saveCSV(binData, f.getAbsolutePath() + "/" + fileName);
            System.out.println("finished");
        }
        System.out.println("Fnished binarization of " + songPopularityhash.size() + " songs");
    }
    //test method
    private void showOut(List<String[]> data){
        int r=1;
        for(String [] st:data){
            System.out.print("r"+r+" ");
            for(int i=0;i<st.length;i++){
                System.out.print(st[i]+" ");
            }
            System.out.println("");r++;
        }
    }
    //For debugging
    /*public static void main(String[] args) {
         Binarizer bz=new Binarizer("ArtistDatePlaysMatrixGT60sTop1K.csv", 50, 5, true);
         bz.binarize();
    }
    */
    public static void main(String[] args) {
        try {
            int k, r;
            String filePath;
            boolean catType;
            // Binarizer bz=new Binarizer("home/misgana/Desktop/music-popularity-prediction/Rhapsody Time Series Data/ArtistDatePlaysMatrixGT60sTop1K.csv", 50, 5, true);
            Options opt = new Options();
            opt.addOption("k", true, "The number of Look ahead days to use");
            opt.addOption("r", true, "The number of Look back days to use");
            opt.addOption("cat", true, "The categorization i.e either up or down");
            opt.addOption("i", true, "Input file name to be binarized.");
            CommandLineParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (!cl.hasOption("k") || !cl.hasOption("r") || !cl.hasOption("cat") || !cl.hasOption("i")) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp("argparse", opt);
                System.err.print("insufficient argument was entered");
                System.exit(0);
            } else {
                k = Integer.valueOf(cl.getOptionValue("k"));
                r = Integer.valueOf(cl.getOptionValue("r"));
                filePath = cl.getOptionValue("i");
                catType = cl.getOptionValue("cat").toLowerCase().equals("up") ? true : false;
                Binarizer bz = new Binarizer(filePath, r, k, catType);
                bz.binarize();
            }
        } catch (ParseException ex) {
            Logger.getLogger(Binarizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}