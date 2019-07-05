package com.example.calog.Fitness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;

public class ExerciseActivity extends AppCompatActivity {
    ImageView btnBack, btnMAinShortcut;
    Button stopBtn;
    int fitnessTypeId;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_exercise);

                intent = getIntent();
                fitnessTypeId = intent.getIntExtra("운동타입", 0);
                System.out.println("Exercise Activity = "+fitnessTypeId);

                openFrame();

        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMAinShortcut = findViewById(R.id.btnMAinShortcut);
        btnMAinShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExerciseActivity.this, "메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        stopBtn=findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            final LinearLayout resultLayout = (LinearLayout)View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
            @Override
            public void onClick(View v) {
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
                    }
                });
                resultBox.show();

            }
        });

        Chronometer timeElapse = (Chronometer)findViewById(R.id.chronometer);
        timeElapse.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int)(time/36000000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);
            }
        });
        timeElapse.setBase(SystemClock.elapsedRealtime());
        timeElapse.start();
    }

    public void openFrame(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr =fm.beginTransaction();
        switch (fitnessTypeId){
            case 1:
                Fitness_Fragment_GPS fragment_gps = new Fitness_Fragment_GPS();
                tr.add(R.id.exerciseFrame, fragment_gps, "gps");
                tr.commit();
                break;
            case 2:
                Fitness_Fragment_GIF fragment_gif = new Fitness_Fragment_GIF();
                tr.add(R.id.exerciseFrame, fragment_gif, "gif");
                tr.commit();
                break;


        }
    }



}
