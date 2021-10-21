package com.swufestu.canlendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.swufestu.canlendar.Helper.Schedelue;
import com.swufestu.canlendar.IO.SceVO;
import com.swufestu.canlendar.Text.BorderText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, DialogInterface.OnClickListener, View.OnLongClickListener {

    private ViewFlipper flipper = null;
    private GestureDetector gestureDetector = null;
    private CalendaView calV = null;
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
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView,0);
        topText = (BorderText) findViewById(R.id.toptext);
        addTextToTopTextView(topText);
    }

    private void addTextToTopTextView(BorderText topText) {
        StringBuffer textDate = new StringBuffer();
        draw = getResources().getDrawable(R.drawable.top_day);
        topText.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(
                calV.getShowMonth()).append("月").append("\t");
        if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
            textDate.append("闰").append(calV.getLeapMonth()).append("月")
                    .append("\t");
        }
        textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
                calV.getCyclical()).append("年)");
        topText.setText(textDate);
        topText.setTextColor(Color.BLACK);
        topText.setTypeface(Typeface.DEFAULT_BOLD);
    }
    protected void onRestart() {
        int xMonth = jumpMonth;
        int xYear = jumpYear;
        int gvFlag =0;
        jumpMonth = 0;
        jumpYear = 0;
        addGridView();   //添加一个gridView
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        calV = new CalendaView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(topText);
        gvFlag++;
        flipper.addView(gridView,gvFlag);
        flipper.removeViewAt(0);
        super.onRestart();
    }
    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        //取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(46);
        //	gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if(Width == 480 && Height == 800){
            gridView.setColumnWidth(69);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return MainActivity.this.gestureDetector
                        .onTouchEvent(event);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //gridView中的每一个item的点击事件

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if(startPosition <= position  && position <= endPosition){
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    String week = "";

                    //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
                    scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
                    //得到这一天是星期几
                    switch(position%7){
                        case 0:
                            week = "星期日";
                            break;
                        case 1:
                            week = "星期一";
                            break;
                        case 2:
                            week = "星期二";
                            break;
                        case 3:
                            week = "星期三";
                            break;
                        case 4:
                            week = "星期四";
                            break;
                        case 5:
                            week = "星期五";
                            break;
                        case 6:
                            week = "星期六";
                            break;
                    }

                    scheduleDate = new ArrayList<String>();
                    scheduleDate.add(scheduleYear);
                    scheduleDate.add(scheduleMonth);
                    scheduleDate.add(scheduleDay);
                    scheduleDate.add(week);
                    if(scheduleIDs != null && scheduleIDs.length > 0){
                        LayoutInflater inflater=getLayoutInflater();
                        View linearlayout= inflater.inflate(R.layout.dialog_can, null);
                        ImageButton img_creat=(ImageButton)linearlayout.findViewById(R.id.img_creat);
                        ImageButton img_del=(ImageButton) linearlayout.findViewById(R.id.img_close);
                        TextView day_tv=(TextView) linearlayout.findViewById(R.id.day_tv);
                        day_tv.setText(scheduleDay+"日");
                        TableLayout dialog_tab=(TableLayout) linearlayout.findViewById(R.id.dialog_tab);
                        img_creat.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                if(builder!=null&&builder.isShowing()){
                                    builder.dismiss();
                                    Intent intent = new Intent();
                                    intent.putStringArrayListExtra("scheduleDate", scheduleDate);
                                    intent.setClass(MainActivity.this, ScheduleView.class);
                                    startActivity(intent);
                                }
                            }
                        });img_del.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                if(builder!=null&&builder.isShowing()){
                                    builder.dismiss();
                                }
                            }
                        });
                        Schedelue dao=new Schedelue(MainActivity.this);
                        for(int i=0;i<scheduleIDs.length;i++){
                            scheduleVO=dao.getSheduByID(MainActivity.this,Integer.parseInt(scheduleIDs[i]));
                            String info="";
                            info=scheduleVO.getTime()+"   "+scheduleVO.getScheduleContent();
                            TableRow localTableRow = new TableRow(MainActivity.this);
                            TextView tv=new TextView(MainActivity.this);
                            tv.setPadding(10, 5, 5, 5);
                            tv.setTextSize(18.0F);
                            //tv.setTextColor(R.color.black);
                            tv.setSingleLine(true);
                            tv.setText(info);
                            localTableRow.addView(tv);
                            localTableRow.setTag(scheduleVO);
                            localTableRow.setOnLongClickListener(MainActivity.this);
                            localTableRow.setOnClickListener((View.OnClickListener) MainActivity.this);
                            dialog_tab.addView(localTableRow);

                        }
                        //自定义dialog设置到底部，设置宽度
                        builder =	new Dialog(MainActivity.this,R.style.FullScreenDialog);
                        builder.setContentView(linearlayout);
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
                        lp.width = (int)(display.getWidth()); //设置宽度
                        lp.y=display.getHeight()-10;
                        builder.getWindow().setAttributes(lp);
                        builder.setCanceledOnTouchOutside(true);
                        builder.show();
                    }else{
                        //直接跳转到需要添加日程的界面
                        //scheduleDate.add(scheduleLunarDay);
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("scheduleDate", scheduleDate);
                        intent.setClass(MainActivity.this,ScheduleView.class);
                        startActivity(intent);
                    }
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    public void onClick(View view) {
        if(builder!=null&&builder.isShowing()){
            builder.dismiss();
        }
        SceVO scheduleVO=  (SceVO) view.getTag();
        Intent intent = new Intent();
        if(scheduleDate!=null){
            intent.putStringArrayListExtra("scheduleDate", scheduleDate);
        }
        intent.setClass(MainActivity.this,ScheduleInfoView.class);
        intent.putExtra("scheduleVO", scheduleVO);
        startActivity(intent);
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
    public boolean onTouchEvent(MotionEvent event) {

        return this.gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        if (motionEvent.getX() - motionEvent1.getX() > 50) {
            //像左滑动
            addGridView();   //添加一个gridView
            jumpMonth++;     //下一个月

            calV = new CalendaView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
            gridView.setAdapter(calV);
            //flipper.addView(gridView);
            addTextToTopTextView(topText);
            gvFlag++;
            flipper.addView(gridView, gvFlag);
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
            this.flipper.showNext();
            flipper.removeViewAt(0);
            return true;
        } else if (motionEvent.getX() - motionEvent1.getX() < -50) {
            //向右滑动
            addGridView();   //添加一个gridView
            jumpMonth--;     //上一个月

            calV = new CalendaView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
            gridView.setAdapter(calV);
            gvFlag++;
            addTextToTopTextView(topText);
            //flipper.addView(gridView);
            flipper.addView(gridView,gvFlag);

            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
            this.flipper.showPrevious();
            flipper.removeViewAt(0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        scheduleVO_del=  (SceVO) view.getTag();
        Dialog alertDialog = new AlertDialog.Builder(MainActivity.this).
                setMessage("删除日程信息？").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dao.delete(scheduleVO_del.getScheduleID());
                        ScheduleView.setAlart(MainActivity.this);
                        if(builder!=null&&builder.isShowing()){
                            builder.dismiss();
                        }
                    }

                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                create();
        alertDialog.show();

        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, menu.FIRST, menu.FIRST, "今天");
        menu.add(0, menu.FIRST+1, menu.FIRST+1, "跳转");
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case Menu.FIRST:
                //跳转到今天
                int xMonth = jumpMonth;
                int xYear = jumpYear;
                int gvFlag =0;
                jumpMonth = 0;
                jumpYear = 0;
                addGridView();   //添加一个gridView
                year_c = Integer.parseInt(currentDate.split("-")[0]);
                month_c = Integer.parseInt(currentDate.split("-")[1]);
                day_c = Integer.parseInt(currentDate.split("-")[2]);
                calV = new CalendaView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
                gridView.setAdapter(calV);
                addTextToTopTextView(topText);
                gvFlag++;
                flipper.addView(gridView,gvFlag);
                if(xMonth == 0 && xYear == 0){
                    //nothing to do
                }else if((xYear == 0 && xMonth >0) || xYear >0){
                    this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
                    this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
                    this.flipper.showNext();
                }else{
                    this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
                    this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
                    this.flipper.showPrevious();
                }
                flipper.removeViewAt(0);
                break;
            case Menu.FIRST+1:

                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        //1901-1-1 ----> 2049-12-31
                        if(year < 1901 || year > 2049){
                            //不在查询范围内
                            new AlertDialog.Builder(MainActivity.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
                        }else{
                            int gvFlag = 0;
                            addGridView();   //添加一个gridView
                            calV = new CalendaView (MainActivity.this, MainActivity.this.getResources(),year,monthOfYear+1,dayOfMonth);
                            gridView.setAdapter(calV);
                            addTextToTopTextView(topText);
                            gvFlag++;
                            flipper.addView(gridView,gvFlag);
                            if(year == year_c && monthOfYear+1 == month_c){
                                //nothing to do
                            }
                            if((year == year_c && monthOfYear+1 > month_c) || year > year_c ){
                                MainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_left_in));
                                MainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_left_out));
                                MainActivity.this.flipper.showNext();
                            }else{
                                MainActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_right_in));
                                MainActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_right_out));
                                MainActivity.this.flipper.showPrevious();
                            }
                            flipper.removeViewAt(0);
                            //跳转之后将跳转之后的日期设置为当期日期
                            year_c = year;
                            month_c = monthOfYear+1;
                            day_c = dayOfMonth;
                            jumpMonth = 0;
                            jumpYear = 0;
                        }
                    }
                },year_c, month_c-1, day_c).show();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
}