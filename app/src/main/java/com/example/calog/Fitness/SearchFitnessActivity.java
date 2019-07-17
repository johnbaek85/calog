package com.example.calog.Fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.FitnessVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;


public class SearchFitnessActivity extends AppCompatActivity {
    TextView txtDate, fitnessType;
    ImageView btnBack, btnMAinShortcut;
    RecyclerView list;
    List<FitnessVO> array;
    SearchFitnessAdapter adapter;
    int fitnessTypeId;          //운동타입, fitnessActivity에서 넘겨받음 1 = 유산소, 2 = 무산소
    Intent intent;

    Retrofit retrofit;
    RemoteService rs;
    String fitness_date;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fitness);
        txtDate=findViewById(R.id.txtDate);
        intent = getIntent();
        fitnessTypeId = intent.getIntExtra("운동타입", 0);
        fitness_date = intent.getStringExtra("select_date");
        user_id = intent.getStringExtra("user_id");
        txtDate.setText(fitness_date);

        list=findViewById(R.id.exerciseList);
        LinearLayoutManager manager =new LinearLayoutManager(this);
        list.setLayoutManager(manager);

        array= new ArrayList<>();

        fitnessType = findViewById(R.id.fitnessType);
        switch (fitnessTypeId){
            case 1:
                fitnessType.setText("유산소 운동");
                connect(fitnessTypeId);


                break;
            case 2:
                fitnessType.setText("근력 운동");
                connect(fitnessTypeId);

                break;
        }

        txtDate = findViewById(R.id.txtDate);



        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void connect(int type) {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);


        switch (type) {
            case 1:
                Call<List<FitnessVO>> cardiCall = rs.CardioList();
                cardiCall.enqueue(new Callback<List<FitnessVO>>() {
                        @Override
                        public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response) {
                            array = response.body();
                            adapter =new SearchFitnessAdapter(SearchFitnessActivity.this, array, fitness_date, user_id);
                            list.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<FitnessVO>> call, Throwable t) {
                            System.out.println("유산소 운동 목록 호출 오류 : "+t.toString());


                        }
                    });

                break;
            case 2:
                Call<List<FitnessVO>> weightCall = rs.WeightList();
                weightCall.enqueue(new Callback<List<FitnessVO>>() {
                        @Override
                        public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response) {
                            array = response.body();
                            adapter =new SearchFitnessAdapter(SearchFitnessActivity.this, array, fitness_date, user_id);
                            list.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<List<FitnessVO>> call, Throwable t) {
                            System.out.println("무산소 운동 목록 호출 오류 : "+t.toString());

                        }
                    });
                    break;


        }
    }

}
