package com.example.calog.Fitness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.FitnessVO;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class ExerciseActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    String user_id;

    TextView txtCalorie, txtDate, txtStepCount, txtDistance;


    SensorManager sm;
    int threshold;
    float acceleration;
    float previousY, currentY;


    private GoogleMap mMap;
    ArrayList<Location> distanceArray;
    LocationListener locationListener;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        intent = getIntent();
        fitness_type_id = intent.getIntExtra("운동타입", 0);
        fitness_menu_id = intent.getIntExtra("운동명", 0);
        fitness_unit_calorie = intent.getDoubleExtra("단위칼로리", 0.0);
        user_id = intent.getStringExtra("user_id");

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        openImageFrame();
        openButtonFrame();
        txtCalorie = findViewById(R.id.usedCalorie);
        thread = new BackThread();
        thread.setDaemon(true);

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));


        //유산소일 경우 걸음 수, 거리 표시
        if (fitness_type_id == 1) {
            stepCounter(100);
            txtStepCount = findViewById(R.id.stepCount);
            txtDistance = findViewById(R.id.distance);
            txtStepCount.setVisibility(View.VISIBLE);
            txtDistance.setVisibility(View.VISIBLE);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);*/

            distanceArray = new ArrayList<Location>();
            createLocationRequest();

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
    }

    @Override
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

    }


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


        switch (view.getId()) {
            case R.id.btnStart:
                timeElapse.setBase(SystemClock.elapsedRealtime());
                btnStartandStop();
                timeElapse.start();
                thread.start();
                stepCounter(4);
                break;

            case R.id.btnStop:
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
                stepCounter(4);
                break;

            case R.id.btnFinish:
                distance = 100;

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
                        intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);
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
                Fitness_Fragment_GPS fragment_gps = new Fitness_Fragment_GPS(fitness_menu_id);
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
        vo.setUser_id(user_id);
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
            currentY = y;
//단순히 y방향 가속도의 상대적인 크기가 일정 한계를 넘으면 걸음수를 증가한다.
            if (Math.abs(currentY - previousY) > threshold && fitness_type_id == 1) {
                number_step++;
                txtStepCount.setText(number_step + "걸음");
/*                used_calorie = fitness_seconds*fitness_unit_calorie;
                String strCal = String.format("%.1f", used_calorie);
                //strCal=String.format("%.2f", String.valueOf(doubleCal));
                txtCalorie.setText("소모 칼로리 : "+strCal+" kcal");
*/
            }
            previousY = y;

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


}
