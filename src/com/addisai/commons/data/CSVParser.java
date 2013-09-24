/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.data;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class CSVParser {

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
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowSt;
    }
}
