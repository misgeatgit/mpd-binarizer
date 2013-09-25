/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.addisai.commons.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Just a test class
 * @author Misgana Bayetta <misgana.bayetta@gmail.com>
 */
public class DateFormatTest {

    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//DateFormat.getDateInstance();

        Calendar c = new GregorianCalendar();
        String dateText = "2012-01-02";
        String date;
        try {
            Date d = Calendar.getInstance().getTime();
            d = df.parse(dateText);
            date=df.format(d);
            System.out.println(date);
        } catch (ParseException ex) {
            ex.getMessage();
            Logger.getLogger(DateFormatTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
