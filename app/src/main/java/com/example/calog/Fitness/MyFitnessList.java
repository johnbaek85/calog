package com.example.calog.Fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.FitnessVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class MyFitnessList extends AppCompatActivity {
    TextView myListTitle, txtDate;
    ImageView btnBack, btnMAinShortcut;
    RecyclerView list;
    List<FitnessVO> array;
    MyFitnessListAdapter adapter;

    int fitnessTypeId;          //운동타입, fitnessActivity에서 넘겨받음 1 = 유산소, 2 = 무산소
    Intent intent;
    Retrofit retrofit;
    RemoteService rs;
    String user_id;
    String fitness_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fitness_list);


        myListTitle = findViewById(R.id.myListTitle);
        intent = getIntent();
        fitnessTypeId = intent.getIntExtra("운동타입", 0);
        user_id = intent.getStringExtra("user_id");


        fitness_date = intent.getStringExtra("select_date");
        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(fitness_date);

        list = findViewById(R.id.OnedayFitnessList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnMAinShortcut = findViewById(R.id.btnHome);
        btnMAinShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyFitnessList.this, "메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(MyFitnessList.this, MainHealthActivity.class);
                startActivity(intent);


            }
        });


        switch (fitnessTypeId) {
            case 1:
                myListTitle.setText("오늘 실행한 유산소 운동입니다.");
                connect(fitnessTypeId);


                break;
            case 2:
                myListTitle.setText("오늘 실행한 근력 운동입니다.");
                connect(fitnessTypeId);

                break;

        }

    }

    public void connect(final int type) {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);


        switch (type) {
            case 1:
                Call<List<FitnessVO>> cardiCall = rs.OneDayCardioList(user_id, fitness_date);
                cardiCall.enqueue(new Callback<List<FitnessVO>>() {
                    @Override
                    public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response) {
                        array = response.body();
                        adapter =new MyFitnessListAdapter(MyFitnessList.this, array, type);
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<FitnessVO>> call, Throwable t) {
                        System.out.println("당일 유산소 운동 목록 호출 오류 : "+t.toString());


                    }
                });

                break;
            case 2:
                Call<List<FitnessVO>> weightCall = rs.OneDayWeightList(user_id, fitness_date);
                weightCall.enqueue(new Callback<List<FitnessVO>>() {
                    @Override
                    public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response) {
                        array = response.body();
                        adapter =new MyFitnessListAdapter(MyFitnessList.this, array, type);
                        list.setAdapter(adapter);

                    }

                    @Override
                    public void onFailure(Call<List<FitnessVO>> call, Throwable t) {
                        System.out.println("당일 근력 운동 목록 호출 오류 : "+t.toString());

                    }
                });
                break;


        }
    }



}
