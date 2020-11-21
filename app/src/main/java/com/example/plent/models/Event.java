package com.example.plent.models;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Event {
    private String id;
    private String title;
    private String date;
    private String start_time;
    private String end_time;
    private String location;
    private String description;
//     private Bitmap poster
    private String imageUrl;
    private String telegram;
    private String clashString;
    private ArrayList attendees;

    public Event(String title, String date, String start_time, String end_time, String location, String description, String telegram, String imageUrl) {
        this.title = title;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
        this.telegram = telegram;
        this.attendees = new ArrayList();
        this.imageUrl = imageUrl;
        this.clashString = "";
    }

    public String getId() {return id;}
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStart_time() { return start_time; }
    public String getEnd_time() { return end_time; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getTelegram() { return telegram; }
    public String getClashString() { return clashString; }
    public ArrayList getAttendees() { return attendees; }

    public void addAttendee(String userId) {
        this.attendees.add(userId);
    }

    public void removeAttendee(String userId) {
        this.attendees.remove(userId);
    }
}
