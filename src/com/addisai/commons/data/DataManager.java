/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 *The purpose of this class is saving and fetching data from a file
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class DataManager {
    String fileName;
    CSVParser csvParser;
    FileReader fileReader;
    public DataManager(String fileName) throws FileNotFoundException {
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
}
