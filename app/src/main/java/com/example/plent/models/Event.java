package com.example.plent.models;

import java.sql.Time;
import java.util.Date;

public class Event {
    private String title;
    private String date;
    private String start_time;
    private String end_time;
    private String location;
    private String description;
    // private Bitmap poster
    private String telegram;

    public Event(String title, String date, String start_time, String end_time, String location, String description, String telegram) {
        this.title = title;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
        this.telegram = telegram;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStart_time() { return start_time; }
    public String getEnd_time() { return end_time; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getTelegram() { return telegram; }


}
