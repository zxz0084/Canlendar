package com.swufestu.canlendar.calendar;

import java.util.Calendar;

public class Special {
    private int dayMonth=0;
    private int dayWeek=0;

    public boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }
    public int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    dayMonth = 29;
                } else {
                    dayMonth = 28;
                }

        }
        return dayMonth;
    }
    public int getWeekdayOfMonth(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        dayWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
        return dayWeek;
    }
}
