package com.demo.architect.utils.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final String APP_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static String getShortDateCurrent() {
        String sDate = "";
        Date currentTime = Calendar.getInstance().getTime();
        sDate = DateFormat.getDateInstance(DateFormat.SHORT).format(currentTime);
        return sDate;
    }


    public static String getDateTimeCurrent() {
        SimpleDateFormat formatter = new SimpleDateFormat(APP_DATETIME_FORMAT);
        String newFormat = formatter.format(new Date());
        return newFormat;
    }
}
