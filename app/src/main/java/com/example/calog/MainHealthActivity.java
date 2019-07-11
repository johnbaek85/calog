package com.example.calog;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.example.calog.Diet.DietActivity;
import com.example.calog.Drinking.DrinkingActivity;
import com.example.calog.Drinking.DrinkingCheckActivity;

import com.example.calog.Fitness.FitnessActivity;
import com.example.calog.Sleeping.SleepCheckActivity;
import com.example.calog.Sleeping.SleepingActivity;
import com.example.calog.VO.MainHealthVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

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

    BottomNavigationView bottomNavigationView;

    Retrofit retrofit;
    RemoteService rs;


    private FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);

        permissionCheck();

        txtEatCalorie = findViewById(R.id.txtEatCalorie);
        txtEatCalorie.setText("섭취칼로리 : " + 0 + "kcal");

        txtUsedCalorie = findViewById(R.id.txtUsedCalorie);
        txtUsedCalorie.setText("소모 칼로리 : " + 0 + "kcal");

        txtSleepHours = findViewById(R.id.txtSleepHours);
        txtSleepHours.setText("수면시간 : " + 0 + "시간");

        txtAlcoholContent = findViewById(R.id.txtAlcoholContent);
        txtAlcoholContent.setText("알코올 수치 : " + 0 + "%");

        txtAlert = findViewById(R.id.txtAlert);
        txtAlert.setText("");

        imgDiet = findViewById(R.id.imgDiet);
        imgDiet.setBackgroundResource(R.drawable.ic_neutral);
        imgFitness = findViewById(R.id.imgFitness);
        imgFitness.setBackgroundResource(R.drawable.ic_neutral);
        imgSleep = findViewById(R.id.imgSleep);
        imgSleep.setBackgroundResource(R.drawable.ic_neutral);
        imgDrink = findViewById(R.id.imgDrink);
        imgDrink.setBackgroundResource(R.drawable.ic_neutral);

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

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

                         View rootView = getWindow().getDecorView();
                         screenShot = ScreenShot(rootView);
                         uriFile = Uri.fromFile(screenShot);
                         if(screenShot != null) {
                             Crop.of(uriFile, uriFile).asSquare().start(MainHealthActivity.this, 100);
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
        endDate.add(Calendar.YEAR, 5);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -5);


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

                java.sql.Date datesql = new java.sql.Date(date.getTime());
                currentSelectedTime = datesql.getTime();

                Call<MainHealthVO> call =
                        rs.userMainHealth("spider", String.valueOf(datesql));
                call.enqueue(new Callback<MainHealthVO>() {
                    @Override
                    public void onResponse(Call<MainHealthVO> call, Response<MainHealthVO> response) {
                        System.out.println("섭취칼로리 ..............................." + currentSelectedTime);
                        MainHealthVO userVO = response.body();

                        if(userVO == null){
                            txtEatCalorie.setText("섭취칼로리 : " + 0 + "kcal");
                            txtUsedCalorie.setText("소모 칼로리 : " + 0 + "kcal");
                            txtSleepHours.setText("수면시간 : " + 0 + "시간");
                            txtAlcoholContent.setText("알코올 수치 : " + 0 + "%");
                        }else{
                            int hour = userVO.getSleeping_seconds() / 3600;
                            int minute = userVO.getSleeping_seconds() % 3600 / 60;

                            int dietSum = (int)(userVO.getSum_calorie());
                            int fitnessSum = (int)(userVO.getSum_cardio_used_calorie() + userVO.getSum_weight_used_calorie());

                            txtEatCalorie.setText("섭취칼로리 : " + dietSum + "kcal");
                            txtUsedCalorie.setText("소모 칼로리 : " + fitnessSum + "kcal");
                            txtSleepHours.setText("수면시간 : " + hour + "시간 " + minute + "분 ");
                            txtAlcoholContent.setText("알코올 수치 : " + userVO.getAlcohol_content() + "%");

                            if(userVO.getSum_calorie() < 1500 || userVO.getSum_calorie() > 3000){
                                imgDiet.setBackgroundResource(R.drawable.ic_bad);
                            }else if(userVO.getSum_calorie() >= 2000 && userVO.getSum_calorie() <= 2500){
                                imgDiet.setBackgroundResource(R.drawable.ic_good);
                            }else{
                                imgDiet.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if((userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) < 300
                                    || (userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) > 1500){
                                imgFitness.setBackgroundResource(R.drawable.ic_bad);
                            }else if((userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) >= 500
                                    && (userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) <= 1000){
                                imgFitness.setBackgroundResource(R.drawable.ic_good);
                            }else{
                                imgFitness.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if(userVO.getSleeping_seconds() < 18000 || userVO.getSleeping_seconds() > 36000){
                                imgSleep.setBackgroundResource(R.drawable.ic_bad);
                            }else if(userVO.getSleeping_seconds() >= 25200 && userVO.getSleeping_seconds() <= 28800){
                                imgSleep.setBackgroundResource(R.drawable.ic_good);
                            }else{
                                imgSleep.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if(userVO.getAlcohol_content() > 0.07){
                                imgDrink.setBackgroundResource(R.drawable.ic_bad);
                                txtAlert.setText("만취 상태입니다. 음주에 유의하세요.");
                            }else if(userVO.getSleeping_seconds() >= 0.001 && userVO.getSleeping_seconds() <= 0.02){
                                imgDrink.setBackgroundResource(R.drawable.ic_good);
                                txtAlert.setText("음주가 시작되었군요. 적당한 음주를 권합니다.");
                            }else{
                                imgDrink.setBackgroundResource(R.drawable.ic_neutral);
                                txtAlert.setText("적당히 마셨습니다. 그만 마시는 것을 권합니다.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainHealthVO> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : "+ t.toString());
                    }
                });

                System.out.println("horizontalCalendar.setCalendarListener : " + datesql);
                //horizontalCalendar.date
                monthName.setText(DateFormat.getDateInstance().format(date));

                //java.sql.Date sqldate=new java.sql.Date(date.getTime());

                currentSelectedTime=date.getTime();
                //System.out.println("horizontalCalendar.setCalendarListener time:"+currentSelectedTime);

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

        System.out.println("<<<<<<<<<<<<<<<현재 선택된 시간 : " + currentSelectedTime);

        //date.setTime(mill);
        //시간 재설정
        monthName.setText(DateFormat.getDateInstance().format(date));
        horizontalCalendar.selectDate(date,true); //false는 이벤트를 주고 true는 이벤트를 주지않고 즉시 변경
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //선택 초기화
        bottomNavigationView.setSelectedItemId(R.id.HomeMenu);
    }
}

