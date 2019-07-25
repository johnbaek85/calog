package com.example.calog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.calog.Diet.DietActivity;
import com.example.calog.Drinking.DrinkingActivity;
import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Fitness.FitnessActivity;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.Sleeping.SleepingActivity;
import com.example.calog.VO.MainHealthVO;
import com.example.calog.VO.UserVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.example.calog.signUp.UpdateUserInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    TextView txtDate;
    TextView txtDiet, txtFitness, txtSleep, txtDrink;
    ImageView imgDiet, imgFitness, imgSleep, imgDrink;
    TextView txtEatCalorie, txtSuggestedEatCalorie;
    TextView txtUsedCalorie, txtSuggestedUsedCalorie;
    TextView txtSleepHours, txtSuggestedSleepHours;
    TextView txtAlcoholContent, txtAlert;

    TextView user_id;
    String strUser_id;
    Toolbar toolbar;

    MainHealthVO userVO;

    UserVO user;

    Intent intent;

    HorizontalCalendar horizontalCalendar;

    long currentSelectedTime = 0; //선택시간

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

    Retrofit retrofit;
    RemoteService rs;

    SharedPreferences pref;
    boolean logInStatus = false;

    //=============TODO 로그인 관련
    //옵션 메뉴 user 로그인 여부
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.loginmenu, menu);

        return true;
    }

    //로그인 상태
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (logInStatus) { // 로그인 한 상태: 로그인은 안보이게, 로그아웃은 보이게
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        } else { // 로그 아웃 한 상태 : 로그인 보이게, 로그아웃은 안보이게
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }

        //logInStatus = !logInStatus;   // 값을 반대로 바꿈

        return super.onPrepareOptionsMenu(menu);
    }

    //로그인, 회원정보 수정, 회원 탈퇴
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.login:
                intent = new Intent(MainHealthActivity.this, MainJoinActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Toast.makeText(this, "로그아웃이 완료되었습니다", Toast.LENGTH_SHORT).show();
                //로그인 정보 프레퍼런스에 로그인정보 삭제
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_id", "");
                editor.commit();
                user_id.setText("");
                logInStatus = false;
                intent = new Intent(MainHealthActivity.this, MainJoinActivity.class);
                startActivity(intent);
                break;

            case R.id.adjust:
                Call<UserVO> call = rs.readUser(strUser_id);
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        user = response.body();

                        intent = new Intent(MainHealthActivity.this, UpdateUserInfoActivity.class);

                        intent.putExtra("user_id", user.getUser_id());
                        intent.putExtra("password", user.getPassword());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("name", user.getName());
                        intent.putExtra("phone", user.getPhone());
                        intent.putExtra("birthday", user.getBirthday());
                        intent.putExtra("gender", user.getGender());
                        intent.putExtra("height", user.getHeight());
                        intent.putExtra("weight", user.getWeight());
                        intent.putExtra("bmi", user.getBmi());
                        intent.putExtra("address", user.getAddress());

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                    }
                });
                break;

            case R.id.withdraw:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("회원탈퇴");
                builder.setMessage("탈퇴하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = rs.deleteUser(strUser_id);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(MainHealthActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(MainHealthActivity.this, MainJoinActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                            }
                        });
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainHealthActivity.this,
                                "회원탈퇴가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        //TODO sharedpreference에서 userid 값 받아옴
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);
        intent = getIntent();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);

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
        imgFitness = findViewById(R.id.imgFitness);
        imgSleep = findViewById(R.id.imgSleep);
        imgDrink = findViewById(R.id.imgDrink);

        user_id = findViewById(R.id.user_id);

        permissionCheck();
        //logInStatus = intent.getBooleanExtra("loginStatus", false);

        //TODO User Login
        //로그인 정보 프레퍼런스에서 불러오기
        strUser_id = pref.getString("user_id", "");
        user_id.setText(strUser_id);

        if (strUser_id.equals("")) {
            user_id.setText("");
            logInStatus = false;
        } else {
            user_id.setText(strUser_id + "님 환영합니다!");
            logInStatus = true;
        }

        if (userVO == null) {
            imgDiet.setBackgroundResource(R.drawable.ic_neutral);
            imgFitness.setBackgroundResource(R.drawable.ic_neutral);
            imgSleep.setBackgroundResource(R.drawable.ic_neutral);
            imgDrink.setBackgroundResource(R.drawable.ic_neutral);
        }

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        txtDate = findViewById(R.id.txtDate);

        intent = getIntent();
        txtDate.setText(intent.getStringExtra("date"));

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainActivity.this, "달력 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                intent = new Intent(MainHealthActivity.this, CalendarActivity.class);
                //변경된 현재 시간값을 가져가서 달력을 재구성한다.
                intent.putExtra("currentSelectedTime", currentSelectedTime);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("select_date", selectedDate);

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

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUser_id);
                intent.putExtra("select_date", selectedDate);



                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


                startActivity(intent);
            }
        });

        btnFitness = findViewById(R.id.btnFitness);
        btnFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainHealthActivity.this, "운동 Activity로 이동",
                        Toast.LENGTH_SHORT).show();*/
                intent = new Intent(MainHealthActivity.this, FitnessActivity.class);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUser_id);
                intent.putExtra("select_date", selectedDate);

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

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUser_id);
                intent.putExtra("select_date", selectedDate);

                startActivity(intent);
            }
        });

        btnDrink = findViewById(R.id.btnDrink);
        btnDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(MainHealthActivity.this, DrinkingActivity.class);

                Date longDate = new Date(currentSelectedTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = dateFormat.format(longDate);

                intent.putExtra("user_id", strUser_id);
                intent.putExtra("select_date", selectedDate);

                startActivity(intent);

//                  intent = new Intent(MainHealthActivity.this, TestActivity.class);
//                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                  startActivity(intent);

            }
        });

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
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {

                /*Toast.makeText(MainHealthActivity.this,
                        DateFormat.getDateInstance().format(date) + " is selected!",
                        Toast.LENGTH_SHORT).show();*/


                java.sql.Date datesql = new java.sql.Date(date.getTime());
                currentSelectedTime = datesql.getTime();

                Call<MainHealthVO> call =
                        rs.userMainHealth(strUser_id, String.valueOf(datesql));
                call.enqueue(new Callback<MainHealthVO>() {
                    @Override
                    public void onResponse(Call<MainHealthVO> call, Response<MainHealthVO> response) {
                        System.out.println("섭취칼로리 ..............................." + currentSelectedTime);
                        userVO = response.body();

                        if (userVO != null) {
                            int hour = userVO.getSleeping_seconds() / 3600;
                            int minute = userVO.getSleeping_seconds() % 3600 / 60;
                            int second = userVO.getSleeping_seconds() % 3600 % 60;

                            int dietSum = (int) (userVO.getSum_calorie());
                            int fitnessSum = (int) (userVO.getSum_cardio_used_calorie() + userVO.getSum_weight_used_calorie());

                            txtEatCalorie.setText("섭취칼로리 : " + dietSum + "kcal");
                            txtUsedCalorie.setText("소모 칼로리 : " + fitnessSum + "kcal");
                            txtSleepHours.setText("수면시간 : " + hour + "시간 " + minute + "분 " + second + "초");
                            txtAlcoholContent.setText("알코올 수치 : " + userVO.getAlcohol_content() + "%");

                            if (userVO.getSum_calorie() < 1500 || userVO.getSum_calorie() > 3000) {
                                imgDiet.setBackgroundResource(R.drawable.ic_bad);
                            } else if (userVO.getSum_calorie() == 0.0 || userVO == null) {
                                imgDiet.setBackgroundResource(R.drawable.ic_neutral);
                            } else if (userVO.getSum_calorie() >= 2000 && userVO.getSum_calorie() <= 2500) {
                                imgDiet.setBackgroundResource(R.drawable.ic_good);
                            } else {
                                imgDiet.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if ((userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) < 300
                                    || (userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) > 1500) {
                                imgFitness.setBackgroundResource(R.drawable.ic_bad);
                            } else if ((userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) == 0.0 || userVO == null) {
                                imgFitness.setBackgroundResource(R.drawable.ic_neutral);
                            } else if ((userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) >= 500
                                    && (userVO.getSum_weight_used_calorie() + userVO.getSum_cardio_used_calorie()) <= 1000) {
                                imgFitness.setBackgroundResource(R.drawable.ic_good);
                            } else {
                                imgFitness.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if (userVO.getSleeping_seconds() < 18000 || userVO.getSleeping_seconds() > 36000) {
                                imgSleep.setBackgroundResource(R.drawable.ic_bad);
                            } else if (userVO.getSleeping_seconds() == 0 || userVO == null) {
                                imgSleep.setBackgroundResource(R.drawable.ic_neutral);
                            } else if (userVO.getSleeping_seconds() >= 25200 && userVO.getSleeping_seconds() <= 28800) {
                                imgSleep.setBackgroundResource(R.drawable.ic_good);
                            } else {
                                imgSleep.setBackgroundResource(R.drawable.ic_neutral);
                            }

                            if (userVO.getAlcohol_content() > 0.07) {
                                imgDrink.setBackgroundResource(R.drawable.ic_bad);
                                txtAlert.setText("만취 상태입니다. 음주에 유의하세요.");
                            } else if (userVO.getAlcohol_content() == 0.0 || userVO.getAlcohol_content() < 0.0) {
                                imgDrink.setBackgroundResource(R.drawable.ic_neutral);
                                txtAlert.setText("");
                            } else if (userVO.getAlcohol_content() >= 0.001 && userVO.getAlcohol_content() <= 0.02) {
                                imgDrink.setBackgroundResource(R.drawable.ic_good);
                                txtAlert.setText("음주가 시작되었군요. 적당한 음주를 권합니다.");
                            } else {
                                imgDrink.setBackgroundResource(R.drawable.ic_neutral);
                                txtAlert.setText("적당히 마셨습니다. 그만 마시는 것을 권합니다.");
                            }
                        } else {
                            txtEatCalorie.setText("섭취칼로리 : " + 0 + "kcal");
                            txtUsedCalorie.setText("소모 칼로리 : " + 0 + "kcal");
                            txtSleepHours.setText("수면시간 : " + 0 + "시간");
                            txtAlcoholContent.setText("알코올 수치 : " + 0 + "%");
                        }
                    }

                    @Override
                    public void onFailure(Call<MainHealthVO> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                    }
                });

                System.out.println("horizontalCalendar.setCalendarListener : " + datesql);
                //horizontalCalendar.date
                txtDate.setText(DateFormat.getDateInstance().format(date));

                //java.sql.Date sqldate=new java.sql.Date(date.getTime());

                currentSelectedTime = date.getTime();
                //System.out.println("horizontalCalendar.setCalendarListener time:"+currentSelectedTime);

                //TODO 하단 메뉴설정
                bottomNavigationView = findViewById(R.id.bottom_navigation_view);

                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.rankingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "랭킹 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainHealthActivity.this, WordCloudActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                                intent = new Intent(MainHealthActivity.this, DrinkingCheckActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                                intent = new Intent(MainHealthActivity.this, SleepCheckActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.shareMenu: {
//                         Toast.makeText(MainHealthActivity.this, "공유 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                                View rootView = getWindow().getDecorView();
                                screenShot = ScreenShot(rootView);
                                uriFile = Uri.fromFile(screenShot);
                                if (screenShot != null) {
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

            }
        });

    }

/*    public static void BottomMenuClearSelection(BottomNavigationView view,boolean checkable) {
        final Menu menu = view.getMenu();
        for(int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setCheckable(checkable);

        }
    }*/


    //TODO 하단 메뉴설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File cropFile = screenShot;

        if (requestCode == 100) {
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

    public File ScreenShot(View view) {
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);

        System.out.println("..........." + filename);
        //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
            os.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }

    public void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    //기존액티비티가 재실행될때
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("===========================onNewIntent Call=============");

        //현재 선택된 시간 가져오기
        currentSelectedTime = intent.getLongExtra("currentSelectedTime", 0);

        java.sql.Date date = new java.sql.Date(currentSelectedTime);

        System.out.println("<<<<<<<<<<<<<<<현재 선택된 시간 : " + currentSelectedTime);

        //date.setTime(mill);
        //시간 재설정
        txtDate.setText(DateFormat.getDateInstance().format(date));
        horizontalCalendar.selectDate(date, true); //false는 이벤트를 주고 true는 이벤트를 주지않고 즉시 변경
    }

    /*@Override
    protected void onPostResume() {
        super.onPostResume();
        if(userVO.getSum_calorie() == 0.0){
            imgDiet.setBackgroundResource(R.drawable.ic_neutral);
        }
        if((userVO.getSum_cardio_used_calorie() + userVO.getSum_weight_used_calorie()) == 0.0){
            imgFitness.setBackgroundResource(R.drawable.ic_neutral);
        }
        if(userVO.getSleeping_seconds() == 0){
            imgSleep.setBackgroundResource(R.drawable.ic_neutral);
        }
        if(userVO.getAlcohol_content() == 0.0){
            imgDrink.setBackgroundResource(R.drawable.ic_neutral);
        }
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();

        //선택 초기화
        bottomNavigationView.setSelectedItemId(R.id.HomeMenu);
    }*/

    //gps 권한요청



}

