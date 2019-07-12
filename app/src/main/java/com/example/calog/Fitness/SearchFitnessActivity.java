package com.example.calog.Fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.VO.FitnessVO;

import java.util.List;


public class SearchFitnessActivity extends AppCompatActivity {
    Button temporaryBtn;
    ImageView btnBack, btnMAinShortcut;
    RecyclerView list;
    List<FitnessVO> array;
    SearchFitnessAdapter adapter;
    int fitnessTypeId;          //운동타입, fitnessActivity에서 넘겨받음 1 = 유산소, 2 = 무산소
    int fitnessMenuId=1;        //임시 1= 팔굽혀펴기
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fitness);

        intent = getIntent();
        fitnessTypeId = intent.getIntExtra("운동타입", 0);


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
                Toast.makeText(SearchFitnessActivity.this, "메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(SearchFitnessActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

 /*       list=findViewById(layout.exerciseList);
        LinearLayoutManager manager =new LinearLayoutManager(this);
        list.setLayoutManager(manager);

        array= new ArrayList<>();
        adapter = new SearchFitnessAdapter(SearchFitnessActivity.this, array);
        list.setAdapter(adapter);
*/

        temporaryBtn=findViewById(R.id.temporaryBtn);
        temporaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SearchFitnessActivity.this, ExerciseActivity.class);
                intent.putExtra("운동타입", fitnessTypeId);
                intent.putExtra("운동명", fitnessMenuId);          //임시 운동명, 1 = 팔굽혀펴기
                System.out.println("SearchFitness Activity = "+fitnessTypeId);
                startActivity(intent);
            }
        });

    }

}
