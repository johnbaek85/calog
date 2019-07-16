package com.example.calog.Fitness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.FitnessVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class MyFitnessList extends AppCompatActivity {
    TextView myListTitle, txtDate;
    ImageView btnBack;
    RecyclerView list;
    List<FitnessVO> array;
    MyFitnessListAdapter adapter;

    int fitnessTypeId;          //운동타입, fitnessActivity에서 넘겨받음 1 = 유산소, 2 = 무산소
    Intent intent;
    Retrofit retrofit;
    RemoteService rs;
    String user_id;
    String fitness_date;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;


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

                        intent = new Intent(MyFitnessList.this, WordCloudActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(MyFitnessList.this, DrinkingCheckActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(MyFitnessList.this, MainHealthActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(MyFitnessList.this, SleepCheckActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                            Crop.of(uriFile, uriFile).asSquare().start(MyFitnessList.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });

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

    //TODO 하단 메뉴설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File cropFile = screenShot;

        if(requestCode ==100){
            if (resultCode == RESULT_OK) {
                cropFile = new File(Crop.getOutput(data).getPath());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API 24 이상 일경우..
                uriFile = FileProvider.getUriForFile(getApplicationContext(),
                        getApplicationContext().getPackageName() + ".provider", cropFile);
            } else { // API 24 미만 일경우..
                uriFile = Uri.fromFile(cropFile);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriFile);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "선택"));
        }
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
