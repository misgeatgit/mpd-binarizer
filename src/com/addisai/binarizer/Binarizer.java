/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.binarizer;

import com.addisai.commons.data.DataManager;
import com.addisai.commons.stat.DataSmooth;
import com.addisai.commons.stat.Quantile;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class Binarizer {

    /**
     * @param args the command line arguments
     */
    DataManager dm;
    DayPopularity dp;
    String[] featureNames;
    ArrayList<String[]> rawData;
    ArrayList<String[]> binarizedData;
    int [] MA_WINSIZEs={5,10,20,30,50}; //different set of moving average window sizes
    int lookAheadDay; //K
    int lookBackDay; //R   
    int Q_binSize;  //the binsize for the R days lookback moving averages
    boolean catagorizationIs_UP;
    Date D;

    /**
     *Initializes the binarization params
     * @param fileName
     * @param lookAhead
     * @param getlookBackList
     * @param date
     */
    public Binarizer(String fileName, int lookAhead, int lookBack, Date date,int Q_binSize) {
        try {
            dm = new DataManager(fileName);
            rawData = (ArrayList<String[]>) dm.getData();
            this.lookAheadDay = lookAhead;
            lookBackDay = lookBack;
            D = date;
            this.Q_binSize=Q_binSize;            
            dp = new DayPopularity(date, rawData);
            featureNames=new String[(Q_binSize*MA_WINSIZEs.length)+1]; //the last one is for the target class
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Binarizer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Sets the T_UP and T_DOWN values
     *
     * @param lookBacks
     */
    private double upThreshold(double[] lookBacks) { //this method currently throws exception for set size less than three
        double[] temp = lookBacks.clone();
        /*Arrays.sort(temp);
         int bottomBinSize=(temp.length/3); 
         T_DOWN=temp[bottomBinSize];
         T_UP=temp[bottomBinSize+(temp.length-bottomBinSize*2)-1];*/
        List< Double[]> thirdQuantile = Quantile.getQuantile(lookBacks, 3);
        return thirdQuantile.get(1)[thirdQuantile.get(1).length - 1];
    }

    private double downThreshold(double[] lookBacks) { //this method currently throws exception for set size less than three
        double[] temp = lookBacks.clone();
        /*Arrays.sort(temp);
         int bottomBinSize=(temp.length/3); 
         T_DOWN=temp[bottomBinSize];
         T_UP=temp[bottomBinSize+(temp.length-bottomBinSize*2)-1];*/
        List< Double[]> thirdQuantile = Quantile.getQuantile(lookBacks, 3);
        return thirdQuantile.get(1)[0];

    }

    public List<String[]> binarize(String datasetFileName) {
        rawData = (ArrayList<String[]>) dm.getData();        
        ArrayList<SongPopularityOnDay> sodList = dp.getAllSongPopularitOnDay();//THIS LINE HAS A DEFFECT
        HashMap<String, Double[]> songLookBacks = new HashMap<>();   // musicId key and popularity set values     
        int [] binValues=new int[Q_binSize*featureNames.length+1]; //last one for UP/DOWN
        int UP;
        int DOWN;        
        List<String []> binerizedDataSet=new ArrayList<>();
        //set feature names
            for (int i =0; i <Q_binSize; i++) {
                for(int j=0;j<MA_WINSIZEs.length;j++){
                featureNames[i]="MA_"+j+"_bin"+i;
                }
            }
            featureNames[featureNames.length-1]="OUT";
        //get all popularity values related to a songId and add it to a list
        for (SongPopularityOnDay sod : sodList) {
            try {
                songLookBacks.put(sod.songId, getlookBackList(sod.songId, lookBackDay));//get R lookback popularity of each song

            } catch (Exception ex) {
                ex.getMessage();
                Logger.getLogger(Binarizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //apply moving average on each popualrity values of each song in the list
        for (String key : songLookBacks.keySet()) {
            Double[] lookBacks = songLookBacks.get(key);
            HashMap<String, Double[]> R_MovingAveragesOfSong = new HashMap<>();
            //set the UP,NEUTRAL and down 
            //CAUTION adjust it with D+K days popularity
            if (lookBacks[lookBacks.length - 1] <= downThreshold(lookBacks)) {
                if(catagorizationIs_UP){
                  UP=DOWN=0;                  
                }
                else{
                 DOWN=1; 
                 UP=0;
                }
            } else if (lookBacks[lookBacks.length - 1] >= upThreshold(lookBacks)) {
                if(catagorizationIs_UP){
                  UP=1; 
                  DOWN=0;
                }
                else{
                 UP=DOWN=0;   
                }
            } else {  //neutral case
                UP=DOWN=0;
            }
            // calculate the R days moving average with d/t windows of D of each song i.e  (D-R+1,D) 
            HashMap<Integer,Double[]> Q_MAs=new HashMap<>(); //key is the window size and values are the moving average of the song
            List<HashMap<Integer,Double[]>> Q_MAList= new ArrayList<>(); //for containing d/t window size moving averages
            
            for (int index = songLookBacks.get(key).length - 1; index >= 0; index--) {
                double [] songMAs=new double[songLookBacks.get(key).length];
                for(int maIndex=0;maIndex<MA_WINSIZEs.length;maIndex++){
                songMAs[maIndex] = DataSmooth.getMA(songLookBacks.get(key),index,MA_WINSIZEs[maIndex]);
                }
                MAs.add(songMAs);
            }            
            //binerize
            int [] MA_bins=new int[MA_WINSIZEs.length];
            for(Double[]d:MAs){
            List<Double[]> bins = Quantile.getQuantile(d, Q_binSize);
            //set the bits            
            }
            
        }
        return null;
    }

    /**
     *
     * @param songId
     * @param R
     * @return The R days look backs and throws exception if the dataset size is
     * less than or equal to R
     * @throws Exception
     */
    public Double[] getlookBackList(String songId, int R) throws Exception {
        Double[] lookBacks;
        if (dm.getDataWidth() < R) {
            throw new Exception("Look back size should be smaller than dataset size");
        } else {
            lookBacks = dp.getInBetweenPopularity(songId, dp.getDateIndex() - (R - 1), dp.getDateIndex());

        }
        return lookBacks;
    }

    public int getLookAheadDay() {
        return lookAheadDay;
    }

    public int getLookBackDay() {
        return lookBackDay;
    }

    public Date getD() {
        return D;
    }
//    public static void main(String[] args) {
//        /*PARAMS
//         -R (The look back)
//         -K (The look ahead)
//         -D (The day)
//         *PROCEDURES
 //        -Set UP and DOWN
//         -Get all songs of the day
//         -calculate the thresholds with R days look bakc
//         -calculate the moving averages of 5,10,20,30 and 50 days
//         -quantize the moving averages
//         ******/
//        Binarizer b = new Binarizer(null, 0, 0, null);
//        double[] thTest = {4820, 5041, 5580, 5568, 5973};
//        b.upThreshold(thTest);
//        System.out.print("Input:{");
//        for (double d : thTest) {
//            System.out.print(d + ",");
//        }
//        System.out.println("}");
//        System.out.println("T_DOWN:" + b.getT_DOWN());
//        System.out.println("T_UP:" + b.getT_UP());
//    }
}
