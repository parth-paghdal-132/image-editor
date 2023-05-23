package com.editor.image.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date getDate(String dateStr) {
        String datePattern = "MMM dd, yyyy hh:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

        try {
            Date date = dateFormat.parse(dateStr);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getFormattedDate(Date date) {
        String datePattern = "MMM dd, yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        return dateFormat.format(date);
    }
}
