package com.swufestu.canlendar.IO;

import java.io.Serializable;

public class SceVO implements Serializable {
    private int scheduleID;
    private int scheduleTypeID;
    private int remindID;
    private String scheduleContent;
    private String scheduleDate;
    private String time;
    private long alartime;
    public SceVO(int ID,int TypeId,int remindID,String scheduleContent,String scheduleDate,String time,Long alartime){
        this.scheduleID=ID;
        this.scheduleTypeID=TypeId;
        this.remindID=remindID;
        this.scheduleContent=scheduleContent;
        this.scheduleDate=scheduleDate;
        this.time=time;
        this.alartime=alartime;
    }

    public long getAlartime() {
        return alartime;
    }

    public void setAlartime(long alartime) {
        this.alartime = alartime;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setScheduleTypeID(int scheduleTypeID) {
        this.scheduleTypeID = scheduleTypeID;
    }

    public int getScheduleTypeID() {
        return scheduleTypeID;
    }

    public int getRemindID() {
        return remindID;
    }

    public void setRemindID(int remindID) {
        this.remindID = remindID;
    }

    public String getScheduleContent() {
        return scheduleContent;
    }

    public void setScheduleContent(String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
