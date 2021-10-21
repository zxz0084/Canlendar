package com.swufestu.canlendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ViewFlipper;

import com.swufestu.canlendar.Helper.Schedelue;
import com.swufestu.canlendar.IO.SceVO;
import com.swufestu.canlendar.Text.BorderText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, DialogInterface.OnClickListener, View.OnLongClickListener {

    private ViewFlipper flipper = null;
    private GestureDetector gestureDetector = null;
    private CalendarView calV = null;
    private GridView gridView = null;
    private BorderText topText = null;
    private Drawable draw = null;
    private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private Schedelue dao = null;
    private SceVO scheduleVO;
    private String[] scheduleIDs;
    private ArrayList<String> scheduleDate;
    private Dialog builder;
    private SceVO scheduleVO_del;

    public MainActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        dao = new Schedelue(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(this);
        flipper=findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV=new CalendaView(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}