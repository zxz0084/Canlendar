package com.swufestu.canlendar.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swufestu.canlendar.IO.DateTag;
import com.swufestu.canlendar.IO.SceVO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Schedelue {
    private DBHelper dbHelper=null;
    public Schedelue(Context context){
            DBHelper helper = dbHelper = new DBHelper(context, "schedules.db");
        }

        public int save(SceVO sceVO){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("scheduleTypeID", sceVO.getScheduleTypeID());
            values.put("remindID", sceVO.getRemindID());
            values.put("scheduleContent", sceVO.getScheduleContent());
            values.put("scheduleDate", sceVO.getScheduleDate());
            values.put("time", sceVO.getTime());
            values.put("alartime", sceVO.getAlartime());
            db.beginTransaction();
            int scheduleID=-1;
            try {
                db.insert("schedule", null, values);
                Cursor cursor = db.rawQuery("select max(scheduleID) from schedule", null);
                if(cursor.moveToFirst()){
                    scheduleID = (int) cursor.getLong(0);
                }
                cursor.close();
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
                db.close();
            }
            return scheduleID;
        }

        public SceVO getSheduByID(Context context,int schedule){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("schedule",
                    new String[]{"scheduleID","scheduleTypeID","remindID","scheduleContent","scheduleDate","time","alartime"},
                    "scheduleID=?", new String[]{String.valueOf(schedule)},
                    null, null, null,null);
            if(cursor.moveToFirst()){
                int schID = cursor.getInt(0);
                int scheduleTypeID = cursor.getInt(1);
                int remindID = cursor.getInt(2);
                String scheduleContent = cursor.getString(3);
                String scheduleDate = cursor.getString(4);
                String time=cursor.getString(5);
                long alartime=cursor.getLong(6);
                cursor.close();
                db.close();
                return new SceVO(schID,scheduleTypeID,remindID,scheduleContent,scheduleDate,time,alartime);
            }
            cursor.close();
            db.close();
            return null;
        }
    public ArrayList<SceVO> getAllSchedule(){
        ArrayList<SceVO> list = new ArrayList<SceVO>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("schedule", new String[]{"scheduleID","scheduleTypeID","remindID","scheduleContent","scheduleDate","time","alartime"}, null, null, null, null, "scheduleID desc",null);
        while(cursor.moveToNext()){
            int scheduleID = cursor.getInt(0);
            int scheduleTypeID = cursor.getInt(1);
            int remindID = cursor.getInt(2);
            String scheduleContent = cursor.getString(3);
            String scheduleDate = cursor.getString(4);
            String time=cursor.getString(5);
            long alartime=cursor.getLong(6);
            SceVO vo = new SceVO(scheduleID,scheduleTypeID,remindID,scheduleContent,scheduleDate,time,alartime);
            list.add(vo);
        }
        cursor.close();
        db.close();
        if(list != null && list.size() > 0){
            return list;
        }
        return null;
    }

    public void delete(int scheduleID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete("schedule", "scheduleID=?", new String[]{String.valueOf(scheduleID)});
            db.delete("scheduletagdate", "scheduleID=?", new String[]{String.valueOf(scheduleID)});
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
            db.close();
        }
    }

    public void update(SceVO vo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scheduleTypeID", vo.getScheduleTypeID());
        values.put("remindID", vo.getRemindID());
        values.put("scheduleContent", vo.getScheduleContent());
        values.put("scheduleDate", vo.getScheduleDate());
        values.put("time", vo.getTime());
        db.update("schedule", values, "scheduleID=?", new String[]{String.valueOf(vo.getScheduleID())});
        db.close();
    }
    public void saveTagDate(ArrayList<DateTag> dateTagList){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DateTag dateTag = new DateTag();
        for(int i = 0; i < dateTagList.size(); i++){
            dateTag = dateTagList.get(i);
            ContentValues values = new ContentValues();
            values.put("year", dateTag.getYear());
            values.put("month", dateTag.getMonth());
            values.put("day", dateTag.getDay());
            values.put("scheduleID", dateTag.getScheduleID());
            db.insert("scheduletagdate", null, values);
        }
        db.close();
    }
    public ArrayList<DateTag> getTagDate(int currentYear, int currentMonth){
        ArrayList<DateTag> dateTagList = new ArrayList<DateTag>();
        //dbOpenHelper = new DBOpenHelper(context, "schedules.db");
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor cursor = db.query("scheduletagdate", new String[]{"tagID","year","month","day","scheduleID"}, "year=? and month=?", new String[]{String.valueOf(currentYear),String.valueOf(currentMonth)}, null, null, null);
        while(cursor.moveToNext()){
            int tagID = cursor.getInt(0);
            int year = cursor.getInt(1);
            int month = cursor.getInt(2);
            int day = cursor.getInt(3);
            int scheduleID = cursor.getInt(4);
            DateTag dateTag = new DateTag(tagID,year,month,day,scheduleID);
            dateTagList.add(dateTag);
        }
        cursor.close();
        db.close();
        if(dateTagList != null && dateTagList.size() > 0){
            return dateTagList;
        }
        return null;
    }

    public String[] getScheduleByTagDate(int year, int month, int day){
        ArrayList<SceVO> scheduleList = new ArrayList<SceVO>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //根据时间查询出日程ID（scheduleID），一个日期可能对应多个日程ID
        Cursor cursor = db.query("scheduletagdate", new String[]{"scheduleID"}, "year=? and month=? and day=?", new String[]{String.valueOf(year),String.valueOf(month),String.valueOf(day)}, null, null, null);
        String scheduleIDs[] = null;
        scheduleIDs = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            String scheduleID = cursor.getString(0);
            scheduleIDs[i] = scheduleID;
            i++;
        }
        cursor.close();
        db.close();
        return scheduleIDs;
    }

    public void destoryDB(){
        if(dbHelper != null){
            dbHelper.close();
        }
    }

}

