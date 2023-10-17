package com.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtil {

    /**
     * Format String of date from oldFormat to newFormet
     *
     * @param stringDate 11/11/2011
     * @param oldFormat  dd/MM/yyyy
     * @param newFormat  yyyy-MM-dd
     * @return
     */
    public static String convertDate(String oldFormat, String newFormat, String stringDate) {
        if (TextUtils.isEmpty(stringDate))
            return "";
        SimpleDateFormat dt = new SimpleDateFormat(oldFormat, Locale.getDefault());
        try {
            Date date = dt.parse(stringDate);
            SimpleDateFormat format = new SimpleDateFormat(newFormat, Locale.getDefault());
            return format.format(date);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return "";
    }

    public static String convertDate(String newFormat, Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat(newFormat, Locale.getDefault());
        try {
            return format.format(calendar.getTime());
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return "";
    }

    public static String convertDate(String newFormat, long time) {
        try {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat format = new SimpleDateFormat(newFormat, Locale.getDefault());
            return format.format(date);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return "";
    }

    public static Calendar setTimeCalendar(String format, String stringTime) {
        if (stringTime != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format != null ? format : "yyyy-MM-dd", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(simpleDateFormat.parse(stringTime));
                return calendar;
            } catch (ParseException e) {
                LogUtil.e(e.getMessage());
            }
        }
        return null;
    }

    public static String getToday(String format) {
        SimpleDateFormat f = new SimpleDateFormat(format, Locale.getDefault());
        return f.format(Calendar.getInstance().getTime());
    }
}
