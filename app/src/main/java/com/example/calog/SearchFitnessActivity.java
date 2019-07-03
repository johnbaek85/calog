package com.example.calog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchFitnessActivity extends AppCompatActivity {
    TabLayout tab;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fitness);

        tab=findViewById(R.id.tab);
        pager=findViewById(R.id.pager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        tab.setupWithViewPager(pager);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter{
        ArrayList<Fragment> fragments =new ArrayList<>();
        String[] tabTitle ={"유산소운동, 무산소운동, 스트레칭, 즐겨찾기"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new CardioFragment());
            fragments.add(new WeightTrainingFragment());
            fragments.add(new StretchingFragment());

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
            return tabTitle[position];
        }
    }
}
