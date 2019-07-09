package com.example.calog;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Diet.DietActivity;
import com.example.calog.Drinking.DrinkingActivity;
import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Fitness.FitnessActivity;
import com.example.calog.Sleeping.SleepCheckActivity;
import com.example.calog.Sleeping.SleepingActivity;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class MainHealthActivity extends AppCompatActivity {

    RelativeLayout btnDiet, btnFitness, btnSleep, btnDrink;
    ImageView btnWordCloud, btnDrinkCheck, btnSleepStart, btnShare;
    ImageView btnBack;
    TextView monthName;
    ImageButton btnUser;
    TextView txtDiet, txtFitness, txtSleep, txtDrink;
    ImageView imgDiet, imgFitness, imgSleep, imgDrink;
    TextView txtEatCalorie, txtSuggestedEatCalorie;
    TextView txtUsedCalorie, txtSuggestedUsedCalorie;
    TextView txtSleepHours, txtSuggestedSleepHours;
    TextView txtAlcoholContent, txtAlert;

    Intent intent;

    HorizontalCalendar horizontalCalendar;

    long currentSelectedTime=0; //선택시간

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);

        monthName = findViewById(R.id.monthName);

        intent = getIntent();
        monthName.setText(intent.getStringExtra("date"));

        monthName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainActivity.this, "달력 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                intent = new Intent(MainHealthActivity.this, CalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //변경된 현재 시간값을 가져가서 달력을 재구성한다.
                intent.putExtra("currentSelectedTime",currentSelectedTime);
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//엑티비티 생성안함
                startActivity(intent);
            }
        });

        btnDrink = findViewById(R.id.btnDrink);
        btnDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(MainHealthActivity.this, DrinkingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                  intent = new Intent(MainHealthActivity.this, TestActivity.class);
//                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                  startActivity(intent);

            }
        });

        //TODO 하단 메뉴설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
         {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item)
             {

                 switch (item.getItemId())
                 {
                     case R.id.rankingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "랭킹 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                         Intent intent = new Intent(MainHealthActivity.this, WordCloudActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                         startActivity(intent);
                         break;
                     }
                     case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                         intent = new Intent(MainHealthActivity.this, DrinkingCheckActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                         startActivity(intent);
                         break;
                     }
                     case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                         intent = new Intent(MainHealthActivity.this, SleepCheckActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                         startActivity(intent);
                         break;
                     }
                     case R.id.shareMenu: {
//                         Toast.makeText(MainHealthActivity.this, "공유 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

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

                         break;
                     }
                 }
                 return true;
             }
         });

        //바텀메뉴 초기화
        //BottomMenuClearSelection(bottomNavigationView,false);


        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -1);


        //java.sql.Date date = java.sql.Date.valueOf("2019-7-17");

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
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



        //캘린더 데이터 변경
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener()
        {
            @Override
            public void onDateSelected(Date date, int position) {

                Toast.makeText(MainHealthActivity.this,
                        DateFormat.getDateInstance().format(date) + " is selected!",
                        Toast.LENGTH_SHORT).show();

                //horizontalCalendar.date
                monthName.setText(DateFormat.getDateInstance().format(date));
            }
        });


        bottomNavigationView.setSelected(false);
    }

//    public static void BottomMenuClearSelection(BottomNavigationView view,boolean checkable) {
//        final Menu menu = view.getMenu();
//        for(int i = 0; i < menu.size(); i++) {
//            menu.getItem(i).setCheckable(checkable);
//
//        }
//    }

    //기존액티비티가 재실행될때
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        System.out.println("onNewIntent Call");

        //현재 선택된 시간 가져오기
        currentSelectedTime=intent.getLongExtra("currentSelectedTime",0);

        java.sql.Date date = new java.sql.Date(currentSelectedTime);

        //date.setTime(mill);
        //시간 재설정
        monthName.setText(DateFormat.getDateInstance().format(date));
        horizontalCalendar.selectDate(date,true); //false는 이벤트를 주고 true는 이벤트를 주지않고 즉시 변경
    }


    @Override
    protected void onResume() {
        super.onResume();

        //선택 초기화
        bottomNavigationView.setSelectedItemId(R.id.HomeMenu);
    }
}

