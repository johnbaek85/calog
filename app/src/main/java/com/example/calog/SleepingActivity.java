package com.example.calog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.zip.Inflater;

public class SleepingActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    TimePicker alarmPicker;
    Context context;
    PendingIntent pendingIntent;
    Button btnSleepfinish,btnSleepstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);

        this.context = getApplicationContext();
        //알람매니저 설정
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //타임피커 설정
        alarmPicker = findViewById(R.id.timepicker);
        //Calendar 객체 설정
        final Calendar calendar = Calendar.getInstance();
        //알림 리시버 설정
        final Intent intent = new Intent(this.context,Alarm_Reciver.class);

        //알람 시작 버튼
        Button alarm_on = findViewById(R.id.btnSleepStart);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //calendar에 시간 셋팅
                calendar.set(Calendar.HOUR_OF_DAY, alarmPicker.getHour());
                calendar.set(Calendar.MINUTE, alarmPicker.getMinute());

                // 시간 가져옴
                int hour = alarmPicker.getHour();
                int minute = alarmPicker.getMinute();
                Toast.makeText(SleepingActivity.this,"기상시간 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                // reveiver에 string 값 넘겨주기
                intent.putExtra("state","alarm on");

                pendingIntent = PendingIntent.getBroadcast(SleepingActivity.this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // 알람셋팅
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);
                //오류 가능
                Intent gointent = new Intent(SleepingActivity.this,SleepCheckActivity.class);
                startActivity(gointent);
            }
        });
        // 알람 정지 버튼
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_sleep_check,null);

        Button alarm_off = view.findViewById(R.id.btnSleepFinish);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SleepingActivity.this,"Alarm 종료",Toast.LENGTH_SHORT).show();
                // 알람매니저 취소
                alarmManager.cancel(pendingIntent);

                intent.putExtra("state","alarm off");

                // 알람취소
                sendBroadcast(intent);
            }
        });
    }
}

