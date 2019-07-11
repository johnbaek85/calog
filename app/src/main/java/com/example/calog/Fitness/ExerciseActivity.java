package com.example.calog.Fitness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Fitness.*;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.FitnessVO;

import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class ExerciseActivity extends AppCompatActivity {
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

    TextView txtCalorie, txtDate, stepCount, distance;
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

    //            System.out.println("운동타입" + fitness_type_id+"운동명" +fitness_menu_id+ "단위칼로리 = "+fitness_unit_calorie);

                openImageFrame();
                openButtonFrame();
                txtCalorie=findViewById(R.id.usedCalorie);
                thread = new BackThread();
                thread.setDaemon(true);

                txtDate = findViewById(R.id.txtDate);
                txtDate.setText(intent.getStringExtra("select_date"));

                if(fitness_type_id==1){
                    stepCount = findViewById(R.id.stepCount);
                    distance = findViewById(R.id.distance);
                    stepCount.setVisibility(View.VISIBLE);
                    distance.setVisibility(View.VISIBLE);
                }




//뒤로가기
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//메인페이지로 이동
        btnMAinShortcut = findViewById(R.id.btnMAinShortcut);
        btnMAinShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExerciseActivity.this, "메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });


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

    }



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

    android.os.Handler handler = new Handler(){
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

                final LinearLayout resultLayout = (LinearLayout)View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
                final AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);

                TextView txtResultTime = resultLayout.findViewById(R.id.resultTime);
                txtResultTime.setText((hh)+"시간 "+(mm)+"분 "+(ss)+"초");
                TextView txtTotalUsedCalorie = resultLayout.findViewById(R.id.totalUsedCal);
                String strCalorie = String.format("%.1f",used_calorie);
                txtTotalUsedCalorie.setText(strCalorie+"kcal");

                resultBox.setIcon(R.drawable.ic_fitness_center_black_24dp);
                resultBox.setTitle("운동결과화면");
                resultBox.setView(resultLayout);
                resultBox.setNegativeButton("저장 안함", null);
                resultBox.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        insertData();
                        intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);
                        startActivity(intent);

                    }
                });
                resultBox.show();
                break;
        }
    }



    public void btnStartandStop(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();
        Fitness_Fragment_StopWatchStop fragmentStop = new Fitness_Fragment_StopWatchStop();
        tr.replace(R.id.btnFrame, fragmentStop, "일시정지");
        tr.commit();
    }





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
    public void openButtonFrame(){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction tr =fm.beginTransaction();
                Fitness_Fragment_StopWatchStart fragmentStart = new Fitness_Fragment_StopWatchStart();
                tr.add(R.id.btnFrame, fragmentStart, "시작");
                tr.commit();
    }

    public void calorieCalculator(){
        fitness_seconds = (int)time/1000;
                used_calorie=fitness_seconds* fitness_unit_calorie;

    }


    public void insertData(){
        FitnessVO vo = new FitnessVO();
        vo.setUser_id(user_id);
        vo.setFitness_menu_id(fitness_menu_id);
        vo.setFitness_seconds(fitness_seconds);
        vo.setUsed_calorie(used_calorie);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);
        Call<Void> call =rs.UserWeightInsert(vo);
        call.enqueue(new Callback<Void>() {
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
    }


}
