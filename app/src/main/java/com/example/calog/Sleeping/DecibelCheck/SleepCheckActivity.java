package com.example.calog.Sleeping.DecibelCheck;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Fitness.SearchFitnessActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.SleepingActivity;
import com.example.calog.VO.SleepingVO;
import com.example.calog.VO.UserVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.example.calog.signUp.UpdateUserInfoActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class SleepCheckActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_AUDIO = 1001;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1002;
    private static final int MY_PERMISSIONS_REQUEST_RINGSTONE = 1003;
    private Thread timeThread = null;
    private Boolean isRunning = true;
    ImageView btnBack;
    ArrayList<Entry> yVals;
    boolean refreshed = false;
    public static Typeface tf;
    LineChart mChart;
    TextView minVal;
    TextView maxVal;
    TextView mmVal;
    TextView curVal;
    long currentTime = 0;
    long savedTime = 0;
    boolean isChart = false;
    Retrofit retrofit;
    RemoteService rs;

    TextView txtDate;

    //시간설정
    TextView timeset;
    int timeput = 0;
    TextView snoreTimer;
    int snoretimeput = 0;

    /* Decibel */
    private boolean bListener = true;
    private boolean isThreadRun = true;
    private Thread thread;
    float volume = 10000;
    int refresh = 0;
    private MyMediaRecorder mRecorder;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DecimalFormat df1 = new DecimalFormat("####.0");
            if (msg.what == 1) {
                if (!isChart) {
                    initChart();
                    return;
                }
                minVal.setText(df1.format(World.minDB));
                mmVal.setText(df1.format((World.minDB + World.maxDB) / 2));
                maxVal.setText(df1.format(World.maxDB));
                curVal.setText(df1.format(World.dbCount));

                updateData(World.dbCount, 0);
                if (refresh == 1) {
                    long now = new Date().getTime();
                    now = now - currentTime;
                    now = now / 1000;
                    refresh = 0;
                } else {
                    refresh++;
                }
            }
        }
    };

    Handler timehandler = new Handler() {
        String stringTimer= "00:00:00";
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                stringTimer = String.format("%02d:%02d:%02d", timeput / (60 * 60) , (timeput/60)%60, (timeput % 60));
                timeset.setText(stringTimer);
            }
        }
    };

    Handler snorehandler = new Handler() {
        String stringTimer= "00:00:00";
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                stringTimer = String.format("%02d:%02d:%02d", snoretimeput / (60 * 60) , (snoretimeput/60)%60, (snoretimeput % 60));
                snoreTimer.setText(stringTimer);
            }
        }
    };

    Intent intent;

    UserVO user;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

    TextView user_id;
    String strUser_id;
    Toolbar toolbar;
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
                intent = new Intent(SleepCheckActivity.this, MainJoinActivity.class);
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

                        intent = new Intent(SleepCheckActivity.this, UpdateUserInfoActivity.class);

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
                                Toast.makeText(SleepCheckActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(SleepCheckActivity.this, MainJoinActivity.class);
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
                        Toast.makeText(SleepCheckActivity.this,
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
        setContentView(R.layout.activity_sleep_check);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        //TODO toolbar 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_id = findViewById(R.id.user_id);
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

        //수면 시작 하기
        timeset = (TextView) findViewById(R.id.Timer);
        snoreTimer = (TextView) findViewById(R.id.snoreTimer);
        final TimeCatch timecatch = new TimeCatch();
        final SnoreTimeCatch snoretimecatch = new SnoreTimeCatch();

        timecatch.setDaemon(true);
        timecatch.start();

        //수면 종료
        Button btnSleepFinish = (Button) findViewById(R.id.btnSleepFinish);
        btnSleepFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //수면 종료 시키기
                TimeCatch.interrupted();

                //다이얼 로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(SleepCheckActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.activity_sleep_check_result, null);

                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //값 보내기

                        int SleepSeconds = timeput;
                        SleepingVO vo = new SleepingVO();
                        vo.setUser_id("spider");
                        vo.setSleeping_seconds(SleepSeconds);
                        vo.setSnoring_seconds(2000);

                        RemoteService rs = retrofit.create(RemoteService.class);
                        Call<Void> call = rs.sleepResultInsert(vo);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(SleepCheckActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(SleepCheckActivity.this,"에러발생"+t.toString(),Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(SleepCheckActivity.this, MainHealthActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SleepCheckActivity.this, MainHealthActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setView(view);

                //총 수면 시간
                final TextView TotalSleep = (TextView) view.findViewById(R.id.TotalSleep);
                long time = timeput;
                int hour = (int) (time / 3600);
                int minute = (int) (time % 3600 / 60);
                int second = (int) (time % 3600 % 60);
                TotalSleep.setText("총 수면 시간: " + String.valueOf(hour + "시간" + minute + "분" + second + "초"));

                //코골이
                final TextView SnoreTime = (TextView)view.findViewById(R.id.SleepSnoring);
                long snoretime = snoretimeput;
                int snorehour = (int) (snoretime / 3600);
                int snoreminute = (int) (snoretime % 3600 / 60);
                int snoresecond = (int) (snoretime % 3600 % 60);
                SnoreTime.setText("총 코골이 시간: " + String.valueOf(snorehour + "시간" + snoreminute + "분" + snoresecond + "초"));

                //평균소음
                final TextView SleepDecibel = (TextView) view.findViewById(R.id.SleepDecibel);
                SleepDecibel.setText("평균 소음 : " + mmVal.getText().toString() + "db");
                double mmval = Double.parseDouble(mmVal.getText().toString());

                //수면의 질
                final TextView SleepQuality = (TextView) view.findViewById(R.id.SleepQuality);
                if (mmval >= 0 && mmval <= 40) {
                    SleepQuality.setText("수면의 질 : 좋음");
                } else if (mmval >= 40 && mmval <= 52.5) {
                    SleepQuality.setText("수면의 질 : 좋음");
                } else if (mmval > 52.5 && mmval <= 60) {
                    SleepQuality.setText("수면의 질 : 보통");
                } else if (mmval > 60 && mmval <= 80) {
                    SleepQuality.setText("수면의 질 : 나쁨");
                } else {
                    SleepQuality.setText("수면의 질 : 나쁨");
                }
                builder.show();
            }
        });

        //데시벨 측정 종류
        tf = Typeface.createFromAsset(this.getAssets(), "fonts/Let_s go Digital Regular.ttf");
        minVal = (TextView) findViewById(R.id.minval);
        minVal.setTypeface(tf);
        mmVal = (TextView) findViewById(R.id.mmval);
        mmVal.setTypeface(tf);
        maxVal = (TextView) findViewById(R.id.maxval);
        maxVal.setTypeface(tf);
        curVal = (TextView) findViewById(R.id.curval);
        curVal.setTypeface(tf);

        //음성 녹음
        mRecorder = new MyMediaRecorder();

        //권한 주기
        int permssionCheckAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permssionCheckStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permssionCheckRingstone = ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK);

        if (permssionCheckAudio != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "수면 품질 체크를 위해 녹음 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_AUDIO);
                Toast.makeText(this, "수면 품질 체크를 위해 녹음 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
        }
        if (permssionCheckStorage != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "수면 품질 체크를 위해 저장 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
                Toast.makeText(this, "수면 품질 체크를 위해 저장 권한이 필요합니다.", Toast.LENGTH_LONG).show();

            }
        }
        if (permssionCheckRingstone != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WAKE_LOCK)) {
                Toast.makeText(this, "알람을 위해 저장 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WAKE_LOCK},
                        MY_PERMISSIONS_REQUEST_RINGSTONE);
                Toast.makeText(this, "알람을 위해 저장 권한이 필요합니다.", Toast.LENGTH_LONG).show();

            }
        }

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

                        intent = new Intent(SleepCheckActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(SleepCheckActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(SleepCheckActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(SleepCheckActivity.this, SleepCheckActivity.class);

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
                            Crop.of(uriFile, uriFile).asSquare().start(SleepCheckActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    class TimeCatch extends Thread {
        public void run() {
            while (true) {
                timeput++;
                timehandler.sendEmptyMessage(0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//한 핸들러에 코고는값이랑 안코고는값 더해서 총시간 나타내는 방법
    class SnoreTimeCatch extends Thread{
        public void run(){
            mmVal = (TextView) findViewById(R.id.mmval);
            double mmval = Double.parseDouble(mmVal.getText().toString());
            maxVal = (TextView) findViewById(R.id.maxval);
            double maxval = Double.parseDouble(maxVal.getText().toString());
            double Snoring = (maxval-mmval)/24.771213;
//            while(true){
//                if(Snoring < mmval){
//                    snoretimeput++;
//                    snorehandler.sendEmptyMessage(0);
//                }else{
//                 //인터럽트로 정지
//                    SnoreTimeCatch.interrupted();
//                }
//            }
        }
    }

    //권한 묻기
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "아직 승인을 받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_RINGSTONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //소음 측정 갱신
    private void updateData(float val, long time) {
        if (mChart == null) {
            return;
        }
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            LineDataSet set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            Entry entry = new Entry(savedTime, val);
            set1.addEntry(entry);
            if (set1.getEntryCount() > 200) {
                set1.removeFirst();
                set1.setDrawFilled(false);
            }
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
            savedTime++;
        }
    }

    private void initChart() {//차트 삽입
        if (mChart != null) {
            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                savedTime++;
                isChart = true;
            }
        } else {
            currentTime = new Date().getTime();
            mChart = (LineChart) findViewById(R.id.chart1);
            mChart.setViewPortOffsets(0, 0, 0, 0);//차트위치
            // no description text
            mChart.setTouchEnabled(false);//터치 기능 사용
            // enable scaling and dragging
            mChart.setDragEnabled(false);
            mChart.setScaleEnabled(true);
            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(false);
            mChart.setDrawGridBackground(false);
            mChart.setMaxHighlightDistance(200);
            //범례삭제
            mChart.getDescription();
            mChart.setEnabled(false);
            XAxis x = mChart.getXAxis();
            x.setLabelCount(8, false);
            x.setEnabled(false);
            x.setTypeface(tf);
            x.setTextColor(Color.WHITE);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setDrawGridLines(false);
            x.setDrawAxisLine(false);
            x.setAxisLineColor(Color.TRANSPARENT);//컬러 없앰
            YAxis y = mChart.getAxisLeft();
            y.setLabelCount(6, false);
            y.setTextColor(Color.WHITE);
            y.setTypeface(tf);
            y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            y.setDrawGridLines(false);

            y.setEnabled(false);
            y.setDrawAxisLine(false);
            mChart.getAxisRight().setEnabled(false);
            yVals = new ArrayList<Entry>();
            yVals.add(new Entry(0, 0));
            LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setValueTypeface(tf);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.02f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });
            LineData data;

            if (mChart.getData() != null &&
                    mChart.getData().getDataSetCount() > 0) {
                data = mChart.getLineData();
                data.clearValues();
                data.removeDataSet(0);
                data.addDataSet(set1);
            } else {
                data = new LineData(set1);
            }

            data.setValueTextSize(9f);
            data.setDrawValues(false);
            mChart.setData(data);
            mChart.getLegend().setEnabled(false);
            mChart.animateXY(2000, 2000);
            // dont forget to refresh the drawing
            mChart.invalidate();
            isChart = true;
        }
    }

    //녹음 시작
    private void startListenAudio() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRun) {
                    try {
                        if (bListener) {
                            volume = mRecorder.getMaxAmplitude();  //Get the sound pressure value
                            if (volume > 0 && volume < 1000000) {
                                World.setDbCount(20 * (float) (Math.log10(volume)));  //Change the sound pressure value to the decibel value
                                // Update with thread
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }
                        if (refreshed) {
                            Thread.sleep(1200);
                            refreshed = false;
                        } else {
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        bListener = false;
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * 녹음 파일 생성
     *
     * @param fFile
     */
    public void startRecord(File fFile) {
        try {
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            } else {
                Toast.makeText(this, getString(R.string.activity_recStartErr), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.activity_recBusyErr), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.activity_recFileErr), Toast.LENGTH_LONG).show();
        }
        bListener = true;
    }

    /**
     * 녹음 정지
     */
    @Override
    protected void onPause() {
        super.onPause();
        bListener = false;
        mRecorder.delete(); //Stop recording and delete the recording file
        thread = null;
        isChart = false;
    }

    //생성된 파일 삭제
    @Override
    protected void onDestroy() {
        if (thread != null) {
            isThreadRun = false;
            thread = null;
        }
        mRecorder.delete();
        super.onDestroy();
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