/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.data;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *The purpose of this class is saving and fetching data from a file
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class CSVDataManager {
    String fileName;
    CSVParser csvParser;
    FileReader fileReader;
    CSVWriter csvWriter;
    public CSVDataManager(String fileName) throws FileNotFoundException {
        this.fileName=fileName;
        fileReader=new FileReader(fileName);
        csvParser=new CSVParser(fileReader);
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * 
     * @return Rows of Data from the file
     */
    public List<String []> getData(){
       return csvParser.parse();
    }
    public int getDataWidth(){
        return getData().get(0).length-1; //one is subtracted for the empty cross point denoted by #
    }
    private class CSVParser {

    FileReader csvFileReader;
    CSVReader csvReader;

    public CSVParser(FileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
        csvReader = new CSVReader(csvFileReader);
    }

    public List<String []> parse() {

        String[] row = null;
        List<String[]> rowSt = new ArrayList<>();
        try {
            while ((row = csvReader.readNext()) != null) {

                rowSt.add(row); // add each row of feature values            
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());            
        }
        return rowSt;
    }
}
    public void saveCSV(List<String []> binData,String fileName){
        try {
            csvWriter=new CSVWriter(new FileWriter(fileName));
            csvWriter.writeAll(binData);
        } catch (IOException ex) {
            ex.getMessage();
            Logger.getLogger(CSVDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
