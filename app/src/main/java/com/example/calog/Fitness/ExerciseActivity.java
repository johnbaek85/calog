package com.example.calog.Fitness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Fitness.*;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.FitnessVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class ExerciseActivity extends AppCompatActivity {
    ImageView btnBack;
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
    long stopTime=0;
    int h;
    int m;
    int s;
    String hh;
    String mm;
    String ss;
    BackThread thread;
    String user_id;

    TextView txtCalorie, txtDate, txtStepCount, txtDistance;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

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
                txtCalorie=findViewById(R.id.usedCalorie);
                thread = new BackThread();
                thread.setDaemon(true);

                txtDate = findViewById(R.id.txtDate);
                txtDate.setText(intent.getStringExtra("select_date"));

                //유산소일 경우 걸음 수, 거리 표시
                if(fitness_type_id==1){
                    txtStepCount = findViewById(R.id.stepCount);
                    txtDistance = findViewById(R.id.distance);
                    txtStepCount.setVisibility(View.VISIBLE);
                    txtDistance.setVisibility(View.VISIBLE);
                }




//뒤로가기
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //스톱워치
        timeElapse = (Chronometer)findViewById(R.id.chronometer);
        timeElapse.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = SystemClock.elapsedRealtime() - chronometer.getBase();
                h = (int)(time/36000000);
                m = (int)(time - h*3600000)/60000;
                s= (int)(time - h*3600000- m*60000)/1000 ;
                hh = h < 10 ? "0"+h: h+"";
                mm = m < 10 ? "0"+m: m+"";
                ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);
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
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(ExerciseActivity.this, DrinkingCheckActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExerciseActivity.this, SleepCheckActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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




    //초마다 칼로리 계산하는 thread
    class BackThread extends Thread{
        public void run(){
            while(true){
                calorieCalculator();
                handler.sendEmptyMessage(0);
                try{
                    Thread.sleep(1000);
                }catch(Exception e){  }
            }
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==0){
                String strCalorie = String.format("%.1f",used_calorie);
                txtCalorie.setText("소모칼로리 : "+strCalorie+" kcal");
            }
            super.handleMessage(msg);
        }
    };








    //운동 중 시작, 중지 버튼 선택시
    public void mClick(View view){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr;

        switch(view.getId()){
            case R.id.btnStart:
                timeElapse.setBase(SystemClock.elapsedRealtime());
                btnStartandStop();
                timeElapse.start();
                thread.start();
                break;

            case R.id.btnStop:
                timeElapse.stop();
                stopTime = SystemClock.elapsedRealtime()-timeElapse.getBase();

                tr=fm.beginTransaction();
                Fitness_Fragment_StopWatchContinue fragmentContinue = new Fitness_Fragment_StopWatchContinue();
                tr.replace(R.id.btnFrame, fragmentContinue, "계속");
                tr.commit();
                break;

            case R.id.btnContinue:
                btnStartandStop();
                timeElapse.setBase(SystemClock.elapsedRealtime()-stopTime);
                timeElapse.start();
                break;

            case R.id.btnFinish:
                number_step = 500;
                distance = 240;

                final LinearLayout resultLayout = (LinearLayout)View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
                final AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);

                TextView txtResultTime = resultLayout.findViewById(R.id.resultTime);
                txtResultTime.setText((hh)+"시간 "+(mm)+"분 "+(ss)+"초");
                TextView txtTotalUsedCalorie = resultLayout.findViewById(R.id.totalUsedCal);
                String strCalorie = String.format("%.1f",used_calorie);
                txtTotalUsedCalorie.setText(strCalorie+"kcal");
                if(fitness_type_id==1) {
                    txtStepCount.setText(number_step + "걸음");
                    txtDistance.setText(distance + "m");
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
    public void btnStartandStop(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();
        Fitness_Fragment_StopWatchStop fragmentStop = new Fitness_Fragment_StopWatchStop();
        tr.replace(R.id.btnFrame, fragmentStop, "일시정지");
        tr.commit();
    }




//운동화면 프레그먼트
    public void openImageFrame(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr =fm.beginTransaction();
        switch (fitness_type_id){
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
    public void openButtonFrame(){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction tr =fm.beginTransaction();
                Fitness_Fragment_StopWatchStart fragmentStart = new Fitness_Fragment_StopWatchStart();
                tr.add(R.id.btnFrame, fragmentStart, "시작");
                tr.commit();
    }



    //칼로리 계산메소드 (단위칼로리*초)
    public void calorieCalculator(){
        fitness_seconds = (int)time/1000;
                used_calorie=fitness_seconds* fitness_unit_calorie;
    }



//데이터 저장 메소드
    public void insertData(int fitness_type_id){

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

        switch (fitness_type_id){
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
                Call<Void> weightCall =rs.UserWeightInsert(vo);
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
