package com.example.calog.Fitness;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
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

import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.FitnessVO;

import java.sql.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class FitnessActivity extends AppCompatActivity {
    RelativeLayout btnCardioActivity, btnWeightTrainingActivity, btnStretchingActivity;
    ImageView btnBack, btnHome;
    Intent intent;
    TextView txtCardioCal, txtCardioTime, txtCardioDistance, txtWeightCal, txtWeightTime;
    Retrofit retrofit;
    RemoteService rs;

    //임시 사용자정보, 메인페이지에서 전달받아야함
    String user_id="spider";
    String fitness_date = "2019-07-01";

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



//유산소 데이터 출력
        txtCardioCal = findViewById(R.id.CardioCal);
        txtCardioTime = findViewById(R.id.CardioTime);
        txtCardioDistance = findViewById(R.id.CardioDistance);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs=retrofit.create(RemoteService.class);
        Call<FitnessVO> cardioCall = rs.OneDayCardioTotalCalorie(user_id, fitness_date);
        cardioCall.enqueue(new Callback<FitnessVO>() {
            @Override
            public void onResponse(Call<FitnessVO> call, Response<FitnessVO> response) {
                FitnessVO vo = response.body();
                txtCardioCal.setText(vo.getSum_cardio_used_calorie()+"kcal");



                long fitnessTime = vo.getSum_cardio_seconds();

                    int fth = (int) (fitnessTime / 3600);
                    int ftm = (int) (fitnessTime - fth * 3600) / 60;
                    int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
                    String strH = fth < 10 ? "0" + fth : fth + "";
                    String strM = ftm < 10 ? "0" + ftm : ftm + "";
                    String strS = fts < 10 ? "0" + fts : fts + "";
                    txtCardioTime.setText(strH + "시간 " + strM + "분 " + strS + "초");


                    txtCardioDistance.setText(vo.getSum_cardio_distance()+"km");
            }

            @Override
            public void onFailure(Call<FitnessVO> call, Throwable t) {
                System.out.println("유산소운동 출력 오류" + t.toString());
            }
        });


//무산소운동 데이터 출력
        txtWeightCal = findViewById(R.id.WeightCal);
        txtWeightTime = findViewById(R.id.WeightTime);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs=retrofit.create(RemoteService.class);
        Call<FitnessVO> wieghtCall =rs.OneDayWeightTotalCalorie(user_id, fitness_date);
        wieghtCall.enqueue(new Callback<FitnessVO>() {
            @Override
            public void onResponse(Call<FitnessVO> call, Response<FitnessVO> response) {
                FitnessVO vo = response.body();
                txtWeightCal.setText((vo.getSum_weight_used_calorie()+"kcal"));

                long fitnessTime = vo.getSum_weight_seconds();
                    int fth = (int) (fitnessTime / 3600);
                    int ftm = (int) (fitnessTime - fth * 3600) / 60;
                    int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
                    String strH = fth < 10 ? "0" + fth : fth + "";
                    String strM = ftm < 10 ? "0" + ftm : ftm + "";
                    String strS = fts < 10 ? "0" + fts : fts + "";
                    txtWeightTime.setText(strH + "시간 " + strM + "분 " + strS + "초");

            }

            @Override
            public void onFailure(Call<FitnessVO> call, Throwable t) {
                System.out.println("무산소운동 출력 오류" + t.toString());

            }
        });


    }
//Activity 이동 Method
    public void goToSearchActivity(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        intent.putExtra("운동타입", fitnessTypeid);
        startActivity(intent);
    }

}
