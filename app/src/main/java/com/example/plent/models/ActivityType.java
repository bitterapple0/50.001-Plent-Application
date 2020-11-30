package com.example.plent.models;

public enum ActivityType {
    FIFTH_ROW,
    INDUSTRY_TALK,
    STUDENT_LIFE;

    public static String convertEnumToString(ActivityType activityType) {
        String out = "";
        if (activityType == FIFTH_ROW) {
            out = "Fifth Row Activities";
        } else if (activityType == STUDENT_LIFE) {
            out = "Student Life";
        } else if (activityType == INDUSTRY_TALK) {
            out = "Industry Talks";
        }
        return out;
    }
}


