package com.example.plent.models;

import android.net.Uri;

import java.util.ArrayList;

public class Event {
    private String id;
    private String title;
    String type;
    String date;
    String start_time;
    String end_time;
    private String location;
    private String description;
    private Uri poster;
    private String telegram;
    private String clashString;
    private ArrayList attendees;
    // private ActivityType type;

    public Event(String title, String date, String start_time, String end_time, String location,
                 String description, String telegram, String type, Uri poster) {
        this.title = title;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.description = description;
        this.telegram = telegram;
        this.attendees = new ArrayList();
        this.poster = poster;
        this.clashString = "";
        this.type = type;
    }

    public String getId() {return id;}
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStart_time() { return start_time; }
    public String getEnd_time() { return end_time; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public Uri getPoster() { return poster; }
    public String getTelegram() { return telegram; }
    public String getClashString() { return clashString; }
    public ArrayList getAttendees() { return attendees; }

    public void addAttendee(String userId) {
        this.attendees.add(userId);
    }

    public void removeAttendee(String userId) {
        this.attendees.remove(userId);
    }
    public String getType() { return type; }

    public boolean compareDate(int date, int month, int year){
        String date_string = Integer.toString(date) + Integer.toString(month) + Integer.toString(year);
        return date_string.equals(this.date);
    }
}
