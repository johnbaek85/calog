package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity
{

    ImageView btnBack, btnHome;

    CalendarView calendarView;
    Calendar calendar;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        System.out.println("온create실행");
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //CalendarView 인스턴스 만들기
        calendarView = (CalendarView)findViewById(R.id.calendar);
        //리스너 등록

        ////////////현재 선택된 시간값을 가져옴 값유지.
        intent=getIntent();
        long currentSelectedTime=intent.getLongExtra("currentSelectedTime",0);

        if(currentSelectedTime!=0)
        {
            calendarView.setDate(currentSelectedTime);
        }
        ////////////////

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
//                Toast.makeText(CalendarActivity.this, ""+year+"/"+(month+1)+"/"
//                        +dayOfMonth, Toast.LENGTH_SHORT).show();
                System.out.println("dayOfMonth:"+dayOfMonth);

                //String date=String.valueOf(year+"-"+(month+1)+"-"+(dayOfMonth));

                intent = new Intent(CalendarActivity.this, MainHealthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티를 생성하지않고 위로 올리겠다. onNewIntent를 실행하기위한 작업

                //달력을 선택하면 값을 유지하고 있어야한다.
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                intent.putExtra("currentSelectedTime",calendar.getTimeInMillis());
                /////////////////////////////////

                finish();
                startActivity(intent);
            }
        });
    }
}
