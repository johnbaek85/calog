package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class SearchFitnessActivity extends AppCompatActivity {
    Button temporaryBtn;
    ImageView btnBack;
    RecyclerView list;
    List<FitnessVO> array;
    SearchFitnessAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fitness);

        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchFitnessActivity.this, FitnessActivity.class);
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
                Intent intent = new Intent(SearchFitnessActivity.this, ExerciseActivity.class);
                startActivity(intent);
            }
        });

    }

}
