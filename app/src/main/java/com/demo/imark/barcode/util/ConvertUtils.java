package com.demo.imark.barcode.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PC on 13-Apr-2018.
 */

public class ConvertUtils {
    public static int ConvertStringMoneyToInt(String s) {
        if (s.toString().trim().equals("")) {
            return 0;
        } else {
            String result = "";
            String[] listSp = s.split("\\.");
            for (String item : listSp) {
                result = result + item;
            }
            return Integer.parseInt(result);
        }
    }

    public static Date ConvertStringToShortDate(String s){
        Date date = null;
        String expectedPattern = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try
        {
            date = formatter.parse(s);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;

    }

}
