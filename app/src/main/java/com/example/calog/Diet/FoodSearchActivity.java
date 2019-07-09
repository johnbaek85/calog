package com.example.calog.Diet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FoodSearchActivity extends AppCompatActivity {

    RecyclerView dietList;
    Button btnSave;
    ImageView btnBack, btnHome;

    Intent intent;
    DietMenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(FoodSearchActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ViewPager viewPager=findViewById(R.id.foodLitsPager);

        //어댑터 설정
        PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //tablayout과 pager연결
        tabLayout.setupWithViewPager(viewPager);


        //가짜 데이터 집어넣기


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    //검색 페이저
    private class PagerAdapter extends FragmentStatePagerAdapter
    {
        ArrayList<Fragment> fragments=new ArrayList<>();
        String[] tabTitle={"검색","자주 찾는 음식","내음식"};


        //생성자로 데이터를 던져서 바차트를 다르게 표현해야함.
        public PagerAdapter(FragmentManager fm)
        {
            super(fm);

            //가짜 데이터 집어넣기 실제 구현할때 DB와 연계할것
            List<DietMenuVO> dietMenuArray = new ArrayList<DietMenuVO>();
            dietMenuArray.add(new DietMenuVO("짜장면",300));
            dietMenuArray.add(new DietMenuVO("우동",200));
            dietMenuArray.add(new DietMenuVO("탕수육",400));

            //자주 찾는 음식
            List<DietMenuVO> MyDietList=new ArrayList<DietMenuVO>();

            //내 음식
            fragments.add(new DietFragment(dietMenuArray,true)); //검색
            fragments.add(new DietFragment(dietMenuArray,false)); //자주 찾는 음식
            fragments.add(new DietFragment(dietMenuArray,false)); //내 음식
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitle[position]; //탭레이아웃 타이틀설정
        }
    }

}
