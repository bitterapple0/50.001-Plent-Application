package com.example.plent.models;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class Event {
    private String id;
    private String creatorId;
    private String title;
    int[] date;         // size 3
    int[] startTime;    // size 2
    int[] endTime;      // size 2
    private String location;
    private String description;
    private String imageUrl;
    private String telegram;
    private String clashString;
    private ArrayList attendees;
    private ActivityType type;

    public Event(String title, int[] date, int[] startTime, int[] endTime, String location,
                 String description, String telegram, ActivityType type, String imageUrl) {
    this.title = title;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.location = location;
    this.description = description;
    this.telegram = telegram;
    this.attendees = new ArrayList();
    this.imageUrl = imageUrl;
    this.clashString = "";
    this.type = type;
    }

    public String getId() {return id;}
    public String getTitle() { return title; }
    public int[] getDate() { return date; }
    public int[] getStartTime() { return startTime; }
    public int[] getEndTime() { return endTime; }
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
    public ActivityType getType() { return type; }

//    public String toString() {
//        return "{}";
//
//    }
}
