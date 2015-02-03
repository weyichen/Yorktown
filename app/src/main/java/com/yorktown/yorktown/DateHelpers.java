package com.yorktown.yorktown;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daniel on 2/2/2015.
 */
public class DateHelpers {

    // format a date for display, according to the user's format setting
    public static String formatDate(Date date) {
        java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(App.getContext());
        return dateFormat.format(date);
    }

    // format a time for display, according to the user's format setting
    public static String formatTime(Date date) {
        java.text.DateFormat timeFormat = DateFormat.getTimeFormat(App.getContext());
        return timeFormat.format(date);
    }

    // format a date and time string for JSON storage
    public static String formatJSONTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssZ");
        return dateFormat.format(date);
    }
}
