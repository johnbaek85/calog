package com.example.calog.Diet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.DietMenuVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class FoodSearchActivity extends AppCompatActivity {

    RecyclerView dietList;
    Button btnSave, btnSearch;
    ImageView btnBack;
    EditText searchEdit;
    Intent intent;
    Retrofit retrofit;
    RemoteService rs;
    List<DietMenuVO> array;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<DietMenuVO> dietMenuArray;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        View view = getLayoutInflater().inflate(R.layout.fragment_diet, null);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.foodListPager);
        dietList = findViewById(R.id.dietList);
        searchEdit = findViewById(R.id.searchEdit);
        btnSearch = findViewById(R.id.btnSearch);
        searchEdit = view.findViewById(R.id.searchEdit);
        btnSearch = view.findViewById(R.id.btnSearch);

        // 페이지 이동소스 메서드로 만드름.
        pageTrans();

        // 레트로핏빌더 생성 - 통신
        rsBuilder();

        //어댑터 설정 ( 데이터 VO에 넣음)
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //tablelayout과 pager연결
        tabLayout.setupWithViewPager(viewPager);

        // 검색 버튼 클릭시
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dietMenuArray = new ArrayList<DietMenuVO>();
                String keyword = searchEdit.getText().toString();
                System.out.println(" <<<<<<<<<<<<<<<<<<<<<< keyword : " + keyword);

                Call<List<DietMenuVO>> call = rs.listDiet(keyword);
                call.enqueue(new Callback<List<DietMenuVO>>() {
                    @Override
                    public void onResponse(Call<List<DietMenuVO>> call, Response<List<DietMenuVO>> response) {
                        array = response.body();
                        //    System.out.println("<<<<<<<<<<<<<<<<<onResponse" + array.toString());
                        for (int i = 0; i < array.size(); i++) {
                            dietMenuArray.add(new DietMenuVO(array.get(i).getDiet_menu_name(), array.get(i).getCalorie()));
                            i++;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DietMenuVO>> call, Throwable t) {
                        //  Log("Main 통신", "에러 "+t.getLocalizedMessage());
                    }
                });
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

                        intent = new Intent(FoodSearchActivity.this, WordCloudActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(FoodSearchActivity.this, DrinkingCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(FoodSearchActivity.this, MainHealthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(FoodSearchActivity.this, SleepCheckActivity.class);
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
                            Crop.of(uriFile, uriFile).asSquare().start(FoodSearchActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    //검색 페이저
    private class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();
        String[] tabTitle = {"검색", "자주 찾는 음식", "내음식"};

        //생성자로 데이터를 던져서 바차트를 다르게 표현해야함.
        public PagerAdapter(FragmentManager fm) {
            super(fm);

            //가짜 데이터 집어넣기 실제 구현할때 DB와 연계할것
            dietMenuArray = new ArrayList<DietMenuVO>();
            try {
                String keyword = searchEdit.getText().toString();
                //String keyword = "달걀";
                System.out.println(" <<<<<<<<<<<<<<<<<<<<<< keyword : " + keyword);

                Call<List<DietMenuVO>> call = rs.listDiet(keyword);
                call.enqueue(new Callback<List<DietMenuVO>>() {
                    @Override
                    public void onResponse(Call<List<DietMenuVO>> call, Response<List<DietMenuVO>> response) {
                        array = response.body();
                        //    System.out.println("<<<<<<<<<<<<<<<<<onResponse" + array.toString());
                        for (int i = 0; i < array.size(); i++) {
                            dietMenuArray.add(new DietMenuVO(array.get(i).getDiet_menu_name(), array.get(i).getCalorie()));
                            i++;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DietMenuVO>> call, Throwable t) {
                        //  Log("Main 통신", "에러 "+t.getLocalizedMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }

            //자주 찾는 음식
            List<DietMenuVO> MyDietList = new ArrayList<DietMenuVO>();

            //내 음식
            fragments.add(new DietFragment(dietMenuArray, true)); //검색
            fragments.add(new DietFragment(dietMenuArray, false)); //자주 찾는 음식
            fragments.add(new DietFragment(dietMenuArray, false)); //내 음식
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitle[position]; //탭레이아웃 타이틀설정
        }
    }


    public void pageTrans() {

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void rsBuilder() {
        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성
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
