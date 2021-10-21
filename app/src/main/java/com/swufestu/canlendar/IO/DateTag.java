package com.swufestu.canlendar.IO;

public class DateTag {
    private int tagID;
    private int scheduleTypeID;
    private int remindID;
    private int month;
    private int year;
    private int day;
    private int scheduleID;

    public DateTag(){
    }

    public DateTag(int tagID, int year, int month, int day, int scheduleID) {

        this.tagID = tagID;
        this.month = month;
        this.year = year;
        this.day = day;
        this.scheduleID = scheduleID;
    }

    public void setRemindID(int remindID) {
        this.remindID = remindID;
    }

    public void setScheduleTypeID(int scheduleTypeID) {
        this.scheduleTypeID = scheduleTypeID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRemindID() {
        return remindID;
    }

    public int getScheduleTypeID() {
        return scheduleTypeID;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getTagID() {
        return tagID;
    }

    public int getYear() {
        return year;
    }
}
