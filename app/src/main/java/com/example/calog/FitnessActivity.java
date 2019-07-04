package com.example.calog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FitnessActivity extends AppCompatActivity {
    RelativeLayout btnCardioActivity, btnWeightTrainingActivity, btnStretchingActivity;
    ImageView btnBack;
    Button graphDay, graphWeek, graphMonth, graphYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
//메인페이지로 이동
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FitnessActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });






//유산소운동 목록 출력
        btnCardioActivity=findViewById(R.id.btnCardioActivity);
        btnCardioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FitnessActivity.this, "유산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
               goToSearchActivity();
            }
        });


//무산소운동 목록 출력
        btnWeightTrainingActivity=findViewById(R.id.btnWeightTrainingActivity);
        btnWeightTrainingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FitnessActivity.this, "무산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
                goToSearchActivity();
            }
        });

//스트레칭 검색
        btnStretchingActivity=findViewById(R.id.btnStretchingActivity);
        btnStretchingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FitnessActivity.this, "스트레칭을 검색합니다.", Toast.LENGTH_SHORT).show();
                goToSearchActivity();
            }
        });
    }

    public void goToSearchActivity(){
        Intent intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        startActivity(intent);
    }

}
