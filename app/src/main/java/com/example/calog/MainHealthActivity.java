package com.example.calog;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class MainHealthActivity extends AppCompatActivity {

    RelativeLayout btnDiet, btnFitness, btnSleep, btnDrink;
    Button btnWordCloud, btnDrinkCheck, btnSleepStart, btnShare;
    ImageView btnBack;
    TextView monthName;
    ImageButton btnUser;
    TextView txtDiet, txtFitness, txtSleep, txtDrink;
    ImageView imgDied, imgFitness, imgSleep, imgDrink;
    TextView txtEatCalorie, txtSuggestedEatCalorie;
    TextView txtUsedCalorie, txtSuggestedUsedCalorie;
    TextView txtSleepHours, txtSuggestedSleepHours;
    TextView txtAlcoholContent, txtAlert;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainHealthActivity.this, "이전 페이지 Activity로 이동",
                        Toast.LENGTH_SHORT).show();
            }
        });

        monthName = findViewById(R.id.monthName);

        intent = getIntent();
        monthName.setText(intent.getStringExtra("date"));

        monthName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainActivity.this, "달력 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                intent = new Intent(MainHealthActivity.this, CalendarActivity.class);
                intent.putExtra("date", monthName.getText().toString());
                startActivity(intent);
            }
        });

        btnUser = findViewById(R.id.btnUser);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainHealthActivity.this, "로그인 Activity로 이동",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainHealthActivity.this, MainJoinActivity.class);
                startActivity(intent);
            }
        });

        btnDiet = findViewById(R.id.btnDiet);
        btnDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainHealthActivity.this, "식사 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                intent = new Intent(MainHealthActivity.this, DietActivity.class);
                startActivity(intent);
            }
        });

        btnFitness = findViewById(R.id.btnFitness);
        btnFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainHealthActivity.this, "운동 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(MainHealthActivity.this, FitnessActivity.class);
                startActivity(intent);
            }
        });

        btnSleep = findViewById(R.id.btnSleep);
        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                intent = new Intent(MainHealthActivity.this, SleepingActivity.class);
                startActivity(intent);
            }
        });

        btnDrink = findViewById(R.id.btnDrink);
        btnDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(MainHealthActivity.this,DrinkingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                  intent = new Intent(MainHealthActivity.this, TestActivity.class);
//                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                  startActivity(intent);

            }
        });

        btnWordCloud = findViewById(R.id.btnWordCloud);
        btnWordCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainHealthActivity.this, "인기검색어 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(MainHealthActivity.this, WordCloudActivity.class);
                startActivity(intent);
            }
        });

        btnDrinkCheck = findViewById(R.id.btnDrinkCheck);
        btnDrinkCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //DrinkingActivity 이동
                intent = new Intent(MainHealthActivity.this,DrinkingCheckActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btnSleepStart = findViewById(R.id.btnSleepStart);
        btnSleepStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainHealthActivity.this, SleepCheckActivity.class);
                startActivity(intent);
            }
        });

        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainHealthActivity.this, "공유 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/

                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());

                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
                File dir = new File(dirPath);
                if (!dir.exists())
                    dir.mkdirs();
                File file = new File(dirPath, "screenshot");
                try {
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Uri uri = FileProvider.getUriForFile(rootView.getContext(),
                        "com.bignerdranch.android.test.fileprovider", file);

                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");

                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                try {
                    startActivity(Intent.createChooser(intent, "Share Screenshot"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainHealthActivity.this, "No App Available",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(10f, 20f, 10f)
                .showDayName(true)
                .showMonthName(true)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Toast.makeText(MainHealthActivity.this,
                        DateFormat.getDateInstance().format(date) + " is selected!",
                        Toast.LENGTH_SHORT).show();
                monthName.setText(DateFormat.getDateInstance().format(date));
            }
        });
    }
}
