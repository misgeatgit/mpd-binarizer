/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.binarizer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class DayPopularity {

    private static Date date;
    private static List<String[]> dataSet;
    static ArrayList<SongPopularityOnDay> allSongPopularitOnDay = new ArrayList<>();

    public DayPopularity(Date d, List<String[]> dataSet) {
        this.date = d;
        this.dataSet = dataSet;
    }

    public ArrayList<SongPopularityOnDay> getAllSongPopularitOnDay() {
        try {
            // DataManager dm = new DataManager();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //List<String[]> dataSet = dm.getData();
            String[] dates = dataSet.get(0);
            int dateIndex = getDateIndex();
            for (int rowIndex = 0; rowIndex < dataSet.size(); rowIndex++) { // fetch all (SongId,popularity) tuple of the particular date of interest                
                String songId = dataSet.get(rowIndex)[0];
                Double popularity = Double.valueOf(dataSet.get(rowIndex)[dateIndex]);
                SongPopularityOnDay sod = new SongPopularityOnDay(songId, popularity);
                allSongPopularitOnDay.add(sod);
            }
        } catch (Exception ex) {
            Logger.getLogger(DayPopularity.class.getName()).log(Level.SEVERE, null, ex);
        }

        return allSongPopularitOnDay;
    }

    public int getDateIndex() {
        // DataManager dm = new DataManager();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //List<String[]> dataSet = dm.getData();
        String[] dates = dataSet.get(0);
        int dateIndex = 0;
        for (int colIndex = 1; colIndex < dates.length; colIndex++) {  //find the index of particular date of interest
            try {
                Date dt = df.parse(dates[colIndex]);  //CAUTION this section of date matching is left without a test.It should be tested before practical application

                if (df.format(date).equals(df.format(dt))) {
                    dateIndex = colIndex;
                    break;
                }
            } catch (ParseException ex) {
                Logger.getLogger(DayPopularity.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return dateIndex;
    }

    /**
     *
     * @param lIndex
     * @param uIndex
     * @return The popularity set between two index in the dataset
     */
    public Double[] getInBetweenPopularity(String songId, int lIndex, int uIndex) {
        Double[] list = new Double[uIndex - lIndex + 1];
        ArrayList<String[]> headerRemovedSet = (ArrayList<String[]>) dataSet;
        headerRemovedSet.remove(0);
        for (String[] stAr : dataSet) {
            if (stAr[0].equals(songId)) {
                for (int index = lIndex, i = 0; index <= uIndex; index++, i++) {
                    list[i] = Double.valueOf(stAr[index]);
                }

            } else {
                continue;
            }

        }
        return list;
    }

    public Date getDate() {
        return date;
    }

    public List<String[]> getDataSet() {
        return dataSet;
    }
    public static void main(String[] args) {
        List<String[]> s=new ArrayList<>();
        String [] dts=new String[]{"2012-01-04","2012-01-05","2012-01-06","2012-01-07","2012-01-08","2012-01-09","2012-01-10"};
        s.add(dts);
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = df.parse("2012-01-07");
            DayPopularity dp=new DayPopularity(d, s);
        boolean asert=dp.getDateIndex()==3;
            System.out.println(asert?"PASSED":"FAILED");
        } catch (ParseException ex) {
            ex.getMessage();
            Logger.getLogger(DayPopularity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
