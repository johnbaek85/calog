package com.example.calog.Fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.SleepCheckActivity;
import com.example.calog.VO.FitnessVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.List;

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
    TextView txtDate, txtCardioCal, txtCardioTime, txtCardioDistance, txtWeightCal, txtWeightTime, txtCardioStep;
    Retrofit retrofit;
    RemoteService rs;
    ImageView cardioList, weightList;

    //임시 사용자정보, 메인페이지에서 전달받아야함
    String user_id;
    String fitness_date;

    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

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

        intent = getIntent();
        fitness_date = intent.getStringExtra("select_date");
        user_id = intent.getStringExtra("user_id");


        txtDate=findViewById(R.id.txtDate);
        txtDate.setText(fitness_date);



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
//당일 유산소운동 목록
        cardioList = findViewById(R.id.cardioList);
        cardioList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyFitnessList(1);
            }
        });


//근력운동 목록 출력
        btnWeightTrainingActivity=findViewById(R.id.btnWeightTrainingActivity);
        btnWeightTrainingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "무산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
                goToSearchActivity(2);
            }
        });
//당일 근력운동 목록
        weightList = findViewById(R.id.weightList);
        weightList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyFitnessList(2);
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


        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs=retrofit.create(RemoteService.class);
        Call<FitnessVO> cardioCall = rs.OneDayCardioTotalCalorie(user_id, fitness_date);
        cardioCall.enqueue(new Callback<FitnessVO>() {
            @Override
            public void onResponse(Call<FitnessVO> call, Response<FitnessVO> response) {
                FitnessVO vo = response.body();
                System.out.println("vo값 출력" + vo.toString());
                txtCardioCal = findViewById(R.id.CardioCal);
                txtCardioTime = findViewById(R.id.CardioTime);
                txtCardioDistance = findViewById(R.id.CardioDistance);
                txtCardioStep = findViewById(R.id.CardioStep);
                txtCardioCal.setText(vo.getSum_cardio_used_calorie()+"kcal");



                long fitnessTime = vo.getSum_cardio_seconds();

                    int fth = (int) (fitnessTime / 3600);
                    int ftm = (int) (fitnessTime - fth * 3600) / 60;
                    int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
                    String strH = fth < 10 ? "0" + fth : fth + "";
                    String strM = ftm < 10 ? "0" + ftm : ftm + "";
                    String strS = fts < 10 ? "0" + fts : fts + "";
                    txtCardioTime.setText(strH + "시간 " + strM + "분 " + strS + "초");

                    txtCardioStep.setText(vo.getSum_cardio_number_steps()+"걸음");

                    txtCardioDistance.setText(vo.getSum_cardio_distance()+"m");
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


        //TODO 하단 메뉴설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.rankingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "랭킹 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(FitnessActivity.this, WordCloudActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(FitnessActivity.this, DrinkingCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(FitnessActivity.this, MainHealthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(FitnessActivity.this, SleepCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.shareMenu: {
//                         Toast.makeText(MainHealthActivity.this, "공유 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        View rootView = getWindow().getDecorView();
                        screenShot = ScreenShot(rootView);
                        uriFile = Uri.fromFile(screenShot);
                        if(screenShot != null) {
                            Crop.of(uriFile, uriFile).asSquare().start(FitnessActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });

        //바텀메뉴 초기화
        //BottomMenuClearSelection(bottomNavigationView,false);



    }


    //Activity 이동 Method
    public void goToSearchActivity(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        intent.putExtra("운동타입", fitnessTypeid);
        intent.putExtra("user_id", user_id);
        intent.putExtra("select_date", fitness_date);
        startActivity(intent);
    }


    public void goToMyFitnessList(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, MyFitnessList.class);
        intent.putExtra("운동타입", fitnessTypeid);
        intent.putExtra("select_date", fitness_date);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }


    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);

        System.out.println("..........." + filename);
        //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
            os.close();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }

}
