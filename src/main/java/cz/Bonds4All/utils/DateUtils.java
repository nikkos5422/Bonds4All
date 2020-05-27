package cz.Bonds4All.utils;

import lombok.var;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final String HOUR_FORMAT = "HH:mm";
    private static final String SIMPLE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_FROM = "22:00";
    private static final String TIME_TO = "6:00";

    public static boolean isHourInAfterMidnightInterval() {
        var target = DateUtils.getCurrentHour();

        return ((target.compareTo(TIME_FROM) >= 0)
                && (target.compareTo(TIME_TO) <= 0));
    }

    private static String getCurrentHour() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdfHour = new SimpleDateFormat(HOUR_FORMAT);
        return sdfHour.format(cal.getTime());
    }

    public static String getSimpleDate(Date date) {
        SimpleDateFormat sdfHour = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT);
        return sdfHour.format(date);

    }

    public static String getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);

        return getSimpleDate(calendar.getTime());

    }

    public static String getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);

        return getSimpleDate(calendar.getTime());
    }
}