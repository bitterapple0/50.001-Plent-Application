package com.example.plent.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateTimeUtils {

    public static final Map<Integer, String> MONTH_MAP;
    public static final Map<Integer, String> DAY_MAP;
    static {
        MONTH_MAP = new HashMap<>();
        MONTH_MAP.put(1, "Jan");
        MONTH_MAP.put(2, "Feb");
        MONTH_MAP.put(3, "Mar");
        MONTH_MAP.put(4, "Apr");
        MONTH_MAP.put(5, "May");
        MONTH_MAP.put(6, "Jun");
        MONTH_MAP.put(7, "Jul");
        MONTH_MAP.put(8, "Aug");
        MONTH_MAP.put(9, "Sep");
        MONTH_MAP.put(10, "Oct");
        MONTH_MAP.put(11, "Nov");
        MONTH_MAP.put(12, "Dec");

        DAY_MAP = new HashMap<>();
        DAY_MAP.put(2, "Monday");
        DAY_MAP.put(3, "Tuesday");
        DAY_MAP.put(4, "Wednesday");
        DAY_MAP.put(5, "Thursday");
        DAY_MAP.put(6, "Friday");
        DAY_MAP.put(7, "Saturday");
        DAY_MAP.put(1, "Sunday");
    }

    public static boolean compareDate(int[] calendarDate, int[] eventDate){
        for (int i=0; i<eventDate.length; i++){
            if (eventDate[i] != calendarDate[i]) return false;
        }
        return true;
    }

    public static String formatEventDateTime(int[] date, int[] startTime, int[] endTime) {
        String returnString = "";
        Calendar c = Calendar.getInstance();
        c.set(date[2], date[1] - 1, date[0], 0, 0);
        returnString += DAY_MAP.get(c.get(Calendar.DAY_OF_WEEK)) + ", " + MONTH_MAP.get(date[1]) + " " + date[0];

        if (date[0] == 1) {
            returnString += "st, ";
        } else if (date[0] == 2) {
            returnString += "nd, ";
        } else if (date[0] == 3) {
            returnString += "rd, ";
        } else {
            returnString += "th, ";
        }

        returnString += (startTime[0] % 12 == 0 ? 12 : startTime[0] % 12) + ".";
        returnString += (Integer.valueOf(startTime[1]).toString().length() == 1 ? "0" + startTime[1] : startTime[1]);
        returnString += (startTime[0] / 12 == 0 ? "am" : "pm") + " - ";

        returnString += (endTime[0] % 12 == 0 ? 12 : endTime[0] % 12) + ".";
        returnString += (Integer.valueOf(endTime[1]).toString().length() == 1 ? "0" + endTime[1] : endTime[1]);
        returnString += (endTime[0] / 12 == 0 ? "am" : "pm");

        return returnString;
    }

}
