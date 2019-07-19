package com.example.calog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.UserVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.example.calog.signUp.UpdateUserInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class CalendarActivity extends AppCompatActivity
{

    ImageView btnBack;

    CalendarView calendarView;
    Calendar calendar;
    Intent intent;

    TextView user_id;
    String strUser_id;
    Toolbar toolbar;

    //DB 용
    Retrofit retrofit;
    RemoteService rs;

    UserVO user;

    TextView txtDate;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

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
                intent = new Intent(CalendarActivity.this, MainJoinActivity.class);
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
                break;

            case R.id.adjust:
                Call<UserVO> call = rs.readUser(strUser_id);
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        user = response.body();

                        intent = new Intent(CalendarActivity.this, UpdateUserInfoActivity.class);

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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("회원탈퇴");
                builder.setMessage("탈퇴하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = rs.deleteUser(strUser_id);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(CalendarActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(CalendarActivity.this, MainJoinActivity.class);
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
                        Toast.makeText(CalendarActivity.this,
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
        setContentView(R.layout.activity_calendar);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        user_id = findViewById(R.id.user_id);

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        //CalendarView 인스턴스 만들기
        calendarView = (CalendarView)findViewById(R.id.calendar);
        //리스너 등록

        //TODO sharedpreference에서 userid 값 받아옴
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);

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

        ////////////현재 선택된 시간값을 가져옴 값유지.
        //intent=getIntent();
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
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티를 생성하지않고 위로 올리겠다. onNewIntent를 실행하기위한 작업

                //달력을 선택하면 값을 유지하고 있어야한다.
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                intent.putExtra("currentSelectedTime",calendar.getTimeInMillis());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                /////////////////////////////////

                finish();
                startActivity(intent);
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

                        Intent intent = new Intent(CalendarActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(CalendarActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(CalendarActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(CalendarActivity.this, SleepCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

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
                            Crop.of(uriFile, uriFile).asSquare().start(CalendarActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    //TODO 하단 메뉴설정
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

}
