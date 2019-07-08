package com.example.calog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class ExerciseActivity extends AppCompatActivity {
    ImageView btnBack, btnMAinShortcut;
    int fitnessTypeId;
    Intent intent;
    Chronometer timeElapse;
    TextView usedCalorie;
    int fitnessMenuId;
    long time;
    long stopTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_exercise);

                intent = getIntent();
                fitnessTypeId = intent.getIntExtra("운동타입", 0);
                fitnessMenuId = intent.getIntExtra("운동명", 0);
                System.out.println("Exercise Activity = "+fitnessTypeId);

                openImageFrame();
                openButtonFrame();



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
                int h = (int)(time/36000000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);
            }
        });
        timeElapse.setText("00:00:00");

    }


    //운동 중 시작, 중지 버튼 선택시
    public void mClick(View view){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr;

        switch(view.getId()){
            case R.id.btnStart:
                timeElapse.setBase(SystemClock.elapsedRealtime());
                btnStartandStop();
                timeElapse.start();
                calorieCalculator(fitnessMenuId);
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
 //               System.out.println("stopTime = "+ stopTime);
                timeElapse.start();
                break;

            case R.id.btnFinish:

                final LinearLayout resultLayout = (LinearLayout)View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
                final AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);
                resultBox.setTitle("운동결과화면");
                resultBox.setView(resultLayout);
                resultBox.setNegativeButton("저장 안함", null);
                resultBox.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(ExerciseActivity.this, "운동기록을 저장합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ExerciseActivity.this, FitnessActivity.class);
                        startActivity(intent);
                        timeElapse.setBase(SystemClock.elapsedRealtime());
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
        switch (fitnessTypeId){
            case 1: //유산소 운동이면 gps프레그먼트
                Fitness_Fragment_GPS fragment_gps = new Fitness_Fragment_GPS(fitnessMenuId);
                tr.add(R.id.exerciseFrame, fragment_gps, "gps");
                tr.commit();
                break;
            case 2: //무산소 운동이면 gif프레그먼트
                Fitness_Fragment_GIF fragment_gif = new Fitness_Fragment_GIF(fitnessMenuId);
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

    public void calorieCalculator(int fitnessMenuId){
        double baseCalorie;
        double conCalorie;
        String strCalorie;
        usedCalorie=findViewById(R.id.usedCalorie);
        switch (fitnessMenuId){
            case 1:         //팔굽혀펴기 일 경우 소모칼로리
                baseCalorie=0.04;
                conCalorie=time* baseCalorie;
                strCalorie = String.valueOf(conCalorie);
                usedCalorie.setText("소모칼로리 : "+strCalorie+" kcal");
                break;
        }

    }



}