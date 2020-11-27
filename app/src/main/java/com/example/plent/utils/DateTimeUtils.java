package com.example.plent.utils;

public class DateTimeUtils {
    public static boolean compareDate(int[] calendarDate, int[] eventDate){
        for (int i=0; i<eventDate.length; i++){
            if (eventDate[i] != calendarDate[i]) return false;
        }
        return true;
//        String date_string = Integer.toString(day) + Integer.toString(month) + Integer.toString(year);
//        return date_string.equals(date);
    }


}
