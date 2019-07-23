package com.example.calog.Fitness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.FitnessVO;
import com.example.calog.VO.UserVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.example.calog.signUp.UpdateUserInfoActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.sql.Date;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class ExerciseActivity extends AppCompatActivity{
    ImageView btnBack, btnMAinShortcut;
    Intent intent;
    Chronometer timeElapse;

    int fitness_type_id;
    int fitness_menu_id;
    double fitness_unit_calorie;
    Retrofit retrofit;
    RemoteService rs;

    //총운동시간(초)
    int fitness_seconds;
    //총소모칼로리
    double used_calorie;
    //총걸음수
    int number_step;
    //총이동거리
    int distance;

    long time;
    long stopTime = 0;
    int h;
    int m;
    int s;
    String hh;
    String mm;
    String ss;
    BackThread thread;
    String str_user_id;
    double foot = 0;

    TextView txtCalorie, txtDate, txtStepCount, txtDistance;


    SensorManager sm;
    int threshold;
    float acceleration;
    float previousY, currentY;


    private GoogleMap mMap;
    ArrayList<Location> distanceArray;
    LocationListener locationListener;
    LocationRequest mLocationRequest;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

    UserVO user;

    //TODO user용 toolbar 관련
    TextView user_id;
    String strUser_id;
    Toolbar toolbar;
    SharedPreferences pref;
    boolean logInStatus = false;


    //카카오맵
    Fitness_Fragment_GPS fragment_gps;

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
                intent = new Intent(ExerciseActivity.this, MainJoinActivity.class);
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

                        intent = new Intent(ExerciseActivity.this, UpdateUserInfoActivity.class);

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
                                Toast.makeText(ExerciseActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(ExerciseActivity.this, MainJoinActivity.class);
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
                        Toast.makeText(ExerciseActivity.this,
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
        setContentView(R.layout.activity_exercise);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();
        fitness_type_id = intent.getIntExtra("운동타입", 0);
        fitness_menu_id = intent.getIntExtra("운동명", 0);
        fitness_unit_calorie = intent.getDoubleExtra("단위칼로리", 0.0);
        str_user_id = intent.getStringExtra("user_id");

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

        openImageFrame();
        openButtonFrame();
        txtCalorie = findViewById(R.id.usedCalorie);
        thread = new BackThread();
        thread.setDaemon(true);


        //유산소일 경우 걸음 수, 거리 표시
        if (fitness_type_id == 1) {
            stepCounter(100);
            txtStepCount = findViewById(R.id.stepCount);
            txtDistance = findViewById(R.id.distance);
            txtStepCount.setVisibility(View.VISIBLE);
            txtDistance.setVisibility(View.VISIBLE);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
          /*  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);*/

          /*  distanceArray = new ArrayList<Location>();
            createLocationRequest();*/

        }
        //뒤로가기
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //스톱워치
        timeElapse = (Chronometer) findViewById(R.id.chronometer);
        timeElapse.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = SystemClock.elapsedRealtime() - chronometer.getBase();
                h = (int) (time / 36000000);
                m = (int) (time - h * 3600000) / 60000;
                s = (int) (time - h * 3600000 - m * 60000) / 1000;
                hh = h < 10 ? "0" + h : h + "";
                mm = m < 10 ? "0" + m : m + "";
                ss = s < 10 ? "0" + s : s + "";
                chronometer.setText(hh + ":" + mm + ":" + ss);
            }
        });
        timeElapse.setText("00:00:00");

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

                        intent = new Intent(ExerciseActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(ExerciseActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExerciseActivity.this, SleepCheckActivity.class);

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
                            Crop.of(uriFile, uriFile).asSquare().start(ExerciseActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });


    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMap(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                checkProvider(provider);
            }
        };
        int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, locationListener);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateMap(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //     distanceArray.add(location);

        Toast.makeText(ExerciseActivity.this, latitude + " + " + longitude, Toast.LENGTH_SHORT).show();
        if (distanceArray.size() != 0) {
            Toast.makeText(ExerciseActivity.this, "array size = " + distanceArray.size(), Toast.LENGTH_SHORT).show();
        }

        final LatLng LOCATION = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, 19));
        Marker mk = mMap.addMarker(new MarkerOptions()
                .position(LOCATION)
                .title("현재위치")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dotp1))
        );
        mk.showInfoWindow();
    }

    private void checkProvider(String provider) {
        Toast.makeText(this, provider + "에 의한 위치서비스가 꺼져있습니다. 켜주세요.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);

    }*/


    //초마다 칼로리 계산 및 데이터 업데이트 thread
    class BackThread extends Thread {
        public void run() {
            while (true) {
                calorieCalculator();
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String strCalorie = String.format("%.1f", used_calorie);
                txtCalorie.setText("소모칼로리 : " + strCalorie + " kcal");
                if (fitness_type_id == 1) {
                    txtStepCount.setText(number_step + "걸음");
                    txtDistance.setText(distance + "m");
                }
            }
            super.handleMessage(msg);
        }
    };


    //운동 중 시작, 중지 버튼 선택시
    public void mClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr;

        final Handler handler;
        switch (view.getId()) {
            case R.id.btnStart:
                if(fitness_type_id==1) {

                    //TODO 시작을 눌렀을때
                    fragment_gps.mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading); //taking mode-현재 자신의 위치로 이동하며 실시간 gps좌표에 따라 이동함

                    //TODO
                    // 컴파일이 될때 자바 클래
                    // thread.sleep 은 안드로이드에서 UI를 변경할때 먹히지 않는다. UI를 틈을주고 변경하려면 Handler라는 것을 사용해야한다
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //마커 찍기
                            MapPOIItem marker = new MapPOIItem();
                            marker.setItemName("Default Marker");
                            marker.setTag(0);
                            marker.setMapPoint(fragment_gps.mapView.getMapCenterPoint()); //위치의 마커 찍기
                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                            MapPoint mp = fragment_gps.mapView.getMapCenterPoint();

                            fragment_gps.mapView.addPOIItem(marker);
                            ///////////////////////////////////////////////////////////////
                            fragment_gps.polyline.setTag(1000);
                            fragment_gps.polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

                            // Polyline 좌표 지정.
                            fragment_gps.polyline.addPoint(marker.getMapPoint());
                            fragment_gps.polyline.addPoint(marker.getMapPoint());
                            fragment_gps.polyline.addPoint(marker.getMapPoint());
                            fragment_gps.polyline.addPoint(marker.getMapPoint());

                            // Polyline 지도에 올리기.
                            fragment_gps.mapView.addPolyline(fragment_gps.polyline);


                            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                            MapPointBounds mapPointBounds = new MapPointBounds(fragment_gps.polyline.getMapPoints());
                            int padding = 100; // px
                            //fragment_gps.mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                        }

                    }, 2000); //안전하게 데이터를 가져오려면 3초가 충분

                }

                timeElapse.setBase(SystemClock.elapsedRealtime());
                btnStartandStop();
                timeElapse.start();
                thread.start();
                stepCounter(5);
                break;

            case R.id.btnStop:

                if(fitness_type_id==1) {
                    //TODO
                    // 컴파일이 될때 자바 클래
                    // thread.sleep 은 안드로이드에서 UI를 변경할때 먹히지 않는다. UI를 틈을주고 변경하려면 Handler라는 것을 사용해야한다
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //마커 찍기
                            MapPOIItem marker = new MapPOIItem();
                            marker.setItemName("Default Marker");
                            marker.setTag(0);
                            marker.setMapPoint(fragment_gps.mapView.getMapCenterPoint()); //위치의 마커 찍기
                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.


                            ///////////////////////////////////////////////////////////////
                            // Polyline 좌표 지정.
                            fragment_gps.polyline.addPoint(marker.getMapPoint());
                            fragment_gps.polyline.addPoint(marker.getMapPoint());
                            fragment_gps.polyline.addPoint(marker.getMapPoint());
                            fragment_gps.polyline.addPoint(marker.getMapPoint());

                            // Polyline 지도에 올리기.
                            fragment_gps.mapView.addPolyline(fragment_gps.polyline);


                            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                            MapPointBounds mapPointBounds = new MapPointBounds(fragment_gps.polyline.getMapPoints());
                            int padding = 100; // px
                            //fragment_gps.mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                        }

                    }, 2000); //안전하게 데이터를 가져오려면 3초가 충분
                }
                timeElapse.stop();
                stopTime = SystemClock.elapsedRealtime() - timeElapse.getBase();
                stepCounter(100);
                tr = fm.beginTransaction();
                Fitness_Fragment_StopWatchContinue fragmentContinue = new Fitness_Fragment_StopWatchContinue();
                tr.replace(R.id.btnFrame, fragmentContinue, "계속");
                tr.commit();
                break;

            case R.id.btnContinue:
                btnStartandStop();
                timeElapse.setBase(SystemClock.elapsedRealtime() - stopTime);
                timeElapse.start();
                stepCounter(5);
                break;

            case R.id.btnFinish:

                final LinearLayout resultLayout = (LinearLayout) View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
                final AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);

                TextView txtResultTime = resultLayout.findViewById(R.id.resultTime);
                txtResultTime.setText((hh) + "시간 " + (mm) + "분 " + (ss) + "초");
                TextView txtTotalUsedCalorie = resultLayout.findViewById(R.id.totalUsedCal);
                String strCalorie = String.format("%.1f", used_calorie);
                txtTotalUsedCalorie.setText(strCalorie + "kcal");


                if (fitness_type_id == 1) {

                    LinearLayout stepLayout = resultLayout.findViewById(R.id.stepLayout);
                    stepLayout.setVisibility(View.VISIBLE);
                    LinearLayout distanceLayout = resultLayout.findViewById(R.id.distanceLayout);
                    distanceLayout.setVisibility(View.VISIBLE);
                    TextView txtTotalStep = resultLayout.findViewById(R.id.totalStep);
                    txtTotalStep.setText(number_step + "걸음");
                    TextView txtTotalDistance = resultLayout.findViewById(R.id.totalDistance);
                    txtTotalDistance.setText(distance + "m");
                }


                resultBox.setIcon(R.drawable.ic_fitness_center_black_24dp);
                resultBox.setTitle("운동결과화면");
                resultBox.setView(resultLayout);
                resultBox.setNegativeButton("저장 안함", null);
                resultBox.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        insertData(fitness_type_id);
                        intent = new Intent(ExerciseActivity.this, FitnessActivity.class);
                        intent.putExtra("select_date", txtDate.getText().toString());
                        intent.putExtra("user_id", strUser_id);
                        startActivity(intent);

                    }
                });
                resultBox.show();
                break;
        }
    }


    //버튼 변환 프레그먼트
    public void btnStartandStop() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        Fitness_Fragment_StopWatchStop fragmentStop = new Fitness_Fragment_StopWatchStop();
        tr.replace(R.id.btnFrame, fragmentStop, "일시정지");
        tr.commit();
    }


    //운동화면 프레그먼트
    public void openImageFrame() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        switch (fitness_type_id) {
            case 1: //유산소 운동이면 gps프레그먼트
                fragment_gps = new Fitness_Fragment_GPS(fitness_menu_id);
                tr.add(R.id.exerciseFrame, fragment_gps, "gps");
                tr.commit();
                break;
            case 2: //무산소 운동이면 gif프레그먼트
                Fitness_Fragment_GIF fragment_gif = new Fitness_Fragment_GIF(fitness_menu_id);
                tr.add(R.id.exerciseFrame, fragment_gif, "gif");
                tr.commit();
                break;
        }
    }


    //스톱워치 버튼변환 프레그먼트
    public void openButtonFrame() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        Fitness_Fragment_StopWatchStart fragmentStart = new Fitness_Fragment_StopWatchStart();
        tr.add(R.id.btnFrame, fragmentStart, "시작");
        tr.commit();
    }


    //칼로리 계산메소드 (단위칼로리*초)
    public void calorieCalculator() {
        fitness_seconds = (int) time / 1000;
        used_calorie = fitness_seconds * fitness_unit_calorie;
    }


    //데이터 저장 메소드
    public void insertData(int fitness_type_id) {

        FitnessVO vo = new FitnessVO();
        vo.setUser_id(str_user_id);
        vo.setFitness_date(intent.getStringExtra("select_date"));
        vo.setFitness_menu_id(fitness_menu_id);
        vo.setFitness_seconds(fitness_seconds);
        vo.setUsed_calorie(used_calorie);
        vo.setNumber_steps(number_step);
        vo.setDistance(distance);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        switch (fitness_type_id) {
            case 1:     //유산소일경우
                Call<Void> cardioCall = rs.UserCardioInsert(vo);
                cardioCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ExerciseActivity.this, "운동기록을 저장합니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ExerciseActivity.this, "저장 실패.", Toast.LENGTH_SHORT).show();
                        System.out.println("저장 오류: " + t.toString());
                    }
                });
                break;
            case 2:     //근력운동일경우
                Call<Void> weightCall = rs.UserWeightInsert(vo);
                weightCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ExerciseActivity.this, "운동기록을 저장합니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ExerciseActivity.this, "저장 실패.", Toast.LENGTH_SHORT).show();
                        System.out.println("저장 오류: " + t.toString());
                    }
                });
                break;
        }
    }


    //만보기
    public void stepCounter(int sensitive) {
        threshold = sensitive;
        //       previousY = currentY = number_step = 0;
        previousY = currentY = 0;
        acceleration = 0.0f;
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(stepDetector, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    private SensorEventListener stepDetector = new SensorEventListener() {
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            currentY = z;
//단순히 y방향 가속도의 상대적인 크기가 일정 한계를 넘으면 걸음수를 증가한다.
            if (Math.abs(currentY - previousY) > threshold && fitness_type_id == 1) {
                number_step++;
                txtStepCount.setText(number_step + "걸음");

                switch (fitness_menu_id){

                    case 1:
                        foot = number_step* 0.4;
                        break;
                    case 2:
                        foot = number_step * 0.6;
                        break;
                    case 3:
                        foot = number_step* 0.4;
                        break;
                    case 4:
                        foot = number_step*0.8;
                        break;
                    case 5:
                        foot = number_step*1.2;
                        break;
                        default:
                            foot=number_step*1.6;
                            break;

                }
                txtDistance.setText(String.format("%.1f",foot)+"m");
                distance = (int)foot;
/*                used_calorie = fitness_seconds*fitness_unit_calorie;
                String strCal = String.format("%.1f", used_calorie);
                //strCal=String.format("%.2f", String.valueOf(doubleCal));
                txtCalorie.setText("소모 칼로리 : "+strCal+" kcal");
*/
            }
            previousY = z;

/*
            if(array.size()>1) {
                double eachDistance=0;
                double distance = 0.0;
                for (int i = 0; i < array.size()-1; i++) {
                    eachDistance = array.get(i).distanceTo(array.get(i + 1));
                    distance += eachDistance;
                }
                strDistance = String.format("%.1f",distance);
                txtdistance.setText("이동거리 : "+strDistance+"meter");
            }
  */
        }
    };

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
