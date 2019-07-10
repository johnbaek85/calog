package com.example.calog;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.soundcloud.android.crop.Crop;

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

    File screenShot;
    Uri uriFile;

    Intent intent;

    HorizontalCalendar horizontalCalendar;

    long currentSelectedTime=0; //선택시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);

        permissionCheck();

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
                /*Toast.makeText(MainHealthActivity.this, "로그인 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
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
                intent = new Intent(MainHealthActivity.this, DrinkingCheckActivity.class);
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

                View rootView = getWindow().getDecorView();
                screenShot = ScreenShot(rootView);
                uriFile = Uri.fromFile(screenShot);
                if(screenShot!=null){
                    Crop.of(uriFile, uriFile).asSquare().start(MainHealthActivity.this, 100);
                }
            }
        });

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File cropFile = screenShot;

        if(requestCode ==100){
            if (resultCode == RESULT_OK) {
                cropFile = new File(Crop.getOutput(data).getPath());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API 24 이상 일경우..
                uriFile = FileProvider.getUriForFile(getApplicationContext(),
                        getApplicationContext().getPackageName() + ".provider", cropFile);
            } else { // API 24 미만 일경우..
                uriFile = Uri.fromFile(cropFile);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriFile);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "선택"));
        }
    }

    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);

        System.out.println("..........." + filename);
        //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
            os.close();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }

    public void permissionCheck(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

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
}

