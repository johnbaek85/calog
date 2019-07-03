package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class SleepingActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    TimePicker alarmPicker;
    Context context;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);

        this.context = context;
        //알람매니저 설정
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //타임피커 설정
        alarmPicker = findViewById(R.id.timepicker);
        //Calendar 객체 설정
        final Calendar calendar = Calendar.getInstance();
        //알림 리시버 설정
        final Intent intent = new Intent(this.context, Alarm_Reciver.class);

        //알람 시작 버튼
        Button alarm_on = findViewById(R.id.btnSleep);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}

