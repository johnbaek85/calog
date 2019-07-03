package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //CalendarView 인스턴스 만들기
        CalendarView calendar = (CalendarView)findViewById(R.id.calendar);
        //리스너 등록
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Toast.makeText(CalendarActivity.this, ""+year+"/"+(month+1)+"/"
                        +dayOfMonth, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CalendarActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });
    }
}
