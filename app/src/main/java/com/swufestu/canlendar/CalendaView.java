package com.swufestu.canlendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swufestu.canlendar.IO.SceVO;
import com.swufestu.canlendar.calendar.Lunnar;
import com.swufestu.canlendar.calendar.Special;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendaView extends BaseAdapter {
    private SceVO dao = null;
    private boolean isLeapyear = false;  //是否为闰年
    private int daysOfMonth = 0;      //某月的天数
    private int dayOfWeek = 0;        //具体某一天是星期几
    private int lastDaysOfMonth = 0;  //上一个月的总天数
    private Context context;
    private String[] dayNumber = new String[49];  //一个gridview中的日期存入此数组中
    private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
    private Special sc = null;
    private Lunnar lc = null;
    private Resources res = null;
    private Drawable drawable = null;

    private String currentYear = "";
    private String currentMonth = "";
    private String currentDay = "";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private int currentFlag = -1;     //用于标记当天
    private int[] schDateTagFlag = null;  //存储当月所有的日程日期

    private String showYear = "";   //用于在头部显示的年份
    private String showMonth = "";  //用于在头部显示的月份
    private String animalsYear = "";
    private String leapMonth = "";   //闰哪一个月
    private String cyclical = "";   //天干地支
    //系统当前时间
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";

    //日程时间(需要标记的日程日期)
    private String sch_year = "";
    private String sch_month = "";
    private String sch_day = "";
    
    public CalendaView(){
        Date date = new Date();
        sysDate = sdf.format(date);  //当期日期
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];

    }

    public CalendaView(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c){
        this();
        this.context= context;
        sc = new Special();
        lc = new Lunnar();
        this.res = rs;

        int stepYear = year_c+jumpYear;
        int stepMonth = month_c+jumpMonth ;
        if(stepMonth > 0){
            //往下一个月滑动
            if(stepMonth%12 == 0){
                stepYear = year_c + stepMonth/12 -1;
                stepMonth = 12;
            }else{
                stepYear = year_c + stepMonth/12;
                stepMonth = stepMonth%12;
            }
        }else{
            //往上一个月滑动
            stepYear = year_c - 1 + stepMonth/12;
            stepMonth = stepMonth%12 + 12;
            if(stepMonth%12 == 0){

            }
        }

        currentYear = String.valueOf(stepYear);;  //得到当前的年份
        currentMonth = String.valueOf(stepMonth);  //得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
        currentDay = String.valueOf(day_c);  //得到当前日期是哪天

        getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));

    }



    private void getCalendar(int parseInt, int parseInt1) {
    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view== null){
            view = LayoutInflater.from(context).inflate(R.layout.canlendar_layout, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.tvtext);
        String d = dayNumber[i].split("\\.")[0];
        String dv = dayNumber[i].split("\\.")[1];
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica.ttf");
        //textView.setTypeface(typeface);
        SpannableString sp = new SpannableString(d+"\n"+dv);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new RelativeSizeSpan(1.2f) , 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(dv != null || dv != ""){
            sp.setSpan(new RelativeSizeSpan(0.75f), d.length()+1, dayNumber[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //sp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(sp);
        textView.setTextColor(Color.GRAY);
        if(i<7){
            //设置周
            textView.setTextColor(Color.BLACK);
            drawable = res.getDrawable(R.drawable.week_top);
            textView.setBackgroundDrawable(drawable);
        }

        if (i < daysOfMonth + dayOfWeek+7 &&i >= dayOfWeek+7) {
            // 当前月信息显示
            textView.setTextColor(Color.BLACK);// 当月字体设黑
            drawable = res.getDrawable(R.drawable.item);
            //textView.setBackgroundDrawable(drawable);
            //textView.setBackgroundColor(Color.WHITE);

        }
        if(schDateTagFlag != null && schDateTagFlag.length >0){
            for(int i = 0; i < schDateTagFlag.length; i++){
                if(schDateTagFlag[i] == i){
                    //设置日程标记背景
                    textView.setBackgroundResource(R.drawable.mark);
                }
            }
        }
        if(currentFlag == i){
            //设置当天的背景
            drawable = res.getDrawable(R.drawable.current_day_bgc);
            textView.setBackgroundDrawable(drawable);
            textView.setTextColor(Color.WHITE);
        }
        return view;
    }
}
