package com.swufestu.canlendar;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.DialogInterface.OnClickListener;
import com.swufestu.canlendar.Helper.Schedelue;
import com.swufestu.canlendar.IO.SceVO;
import com.swufestu.canlendar.Text.BorderText;
import android.view.View.OnLongClickListener;
import java.util.ArrayList;

public class ScheduleInfoView extends Activity {

    private LinearLayout layout = null;
    private BorderText textTop = null;
    private BorderText info = null;
    private BorderText date = null;
    private BorderText type = null;
    private EditText editInfo = null;
    private Schedelue dao = null;
    private SceVO scheduleVO = null;
    private Button save=null;
    private String scheduleInfo = "";    //日程信息被修改前的内容
    private String scheduleChangeInfo = "";  //日程信息被修改之后的内容
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private ArrayList<String> scheduleDate;
    private String[] scheduleIDs;
    private SceVO scheduleVO1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_schedule_info_view);
        dao = new Schedelue(this);

        //final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 0);
        layout = new LinearLayout(this); // 实例化布局对象
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.drawable.schedule_bk);
        layout.setLayoutParams(params);

        textTop = new BorderText(this, null);
        textTop.setTextColor(Color.BLACK);
        textTop.setBackgroundResource(R.drawable.top_day);
        textTop.setText("日程详情");
        textTop.setHeight(78);
        textTop.setGravity(Gravity.CENTER);

        layout.addView(textTop);


        Intent intent = getIntent();
        //scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));
        //一个日期可能对应多个标记日程(scheduleID)
        scheduleDate=intent.getStringArrayListExtra("scheduleDate");
        scheduleVO1=(SceVO) intent.getExtras().getSerializable("scheduleVO");
        //显示日程详细信息
        handlerInfo(scheduleVO1);
        setContentView(layout);
    }

    public void handlerInfo(SceVO scheduleVO2){
        BorderText date = new BorderText(this, null);
        date.setTextColor(Color.BLACK);
        date.setBackgroundColor(Color.WHITE);
        date.setLayoutParams(params);
        date.setGravity(Gravity.CENTER_VERTICAL);
        date.setHeight(40);
        date.setPadding(10, 0, 10, 0);

        BorderText type = new BorderText(this, null);
        type.setTextColor(Color.BLACK);
        type.setBackgroundColor(Color.WHITE);
        type.setLayoutParams(params);
        type.setGravity(Gravity.CENTER);
        type.setHeight(67);
        type.setPadding(10, 0, 10, 0);
        type.setTag(scheduleVO2);

        final BorderText info = new BorderText(this, null);
        info.setTextColor(Color.BLACK);
        info.setBackgroundColor(Color.WHITE);
        info.setGravity(Gravity.CENTER_VERTICAL);
        info.setLayoutParams(params);
        info.setPadding(10, 5, 10, 5);


        layout.addView(type);
        layout.addView(date);
        layout.addView(info);
		/*Intent intent = getIntent();
		int scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));*/
        date.setText(scheduleVO2.getScheduleDate());
        type.setText(CalendarConstant.sch_type[scheduleVO2.getScheduleTypeID()]);
        info.setText(scheduleVO2.getScheduleContent());



        //长时间按住日程类型textview就提示是否删除日程信息
        type.setOnLongClickListener(new OnLongClickListener() {


            public boolean onLongClick(View v) {

                scheduleVO = (SceVO) v.getTag();

                new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除").setPositiveButton("确认", new OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        dao.delete(scheduleVO.getScheduleID());
                        Toast.makeText(ScheduleInfoView.this, "日程已删除", '0').show();
                        ScheduleView.setAlart(ScheduleInfoView.this);
                        finish();
                    }
                }).setNegativeButton("取消", null).show();

                return true;
            }
        });
    }
}