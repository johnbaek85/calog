package com.example.calog.Fitness;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;

public class FitnessActivity extends AppCompatActivity {
    RelativeLayout btnCardioActivity, btnWeightTrainingActivity, btnStretchingActivity;
    ImageView btnBack, btnHome;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);


        //TODO 그래프 BarChart Fragment 장착
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();
        GraphPagerFragment graphFragment = new GraphPagerFragment();
        tr.replace(R.id.barChartFrag,graphFragment);
        //////////////////////////////


//메인페이지로 이동
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(FitnessActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });


//유산소운동 목록 출력
        btnCardioActivity=findViewById(R.id.btnCardioActivity);
        btnCardioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "유산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
               goToSearchActivity(1);
            }
        });


//무산소운동 목록 출력
        btnWeightTrainingActivity=findViewById(R.id.btnWeightTrainingActivity);
        btnWeightTrainingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "무산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
                goToSearchActivity(2);
            }
        });

//스트레칭 검색
        btnStretchingActivity=findViewById(R.id.btnStretchingActivity);
        btnStretchingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "스트레칭을 검색합니다.", Toast.LENGTH_SHORT).show();
                String searchWord = "스트레칭";
                intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", searchWord);
                try {
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                }
            }
        });
    }

    public void goToSearchActivity(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        intent.putExtra("운동타입", fitnessTypeid);
        startActivity(intent);
    }

}
