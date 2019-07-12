package com.example.calog.Common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calog.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class GraphPagerFragment extends Fragment {

//    private FragmentManager fragmentManager;
//
//    public GraphPagerFragment(FragmentManager fm)
//    {
//        fragmentManager=fm;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_graph_pager, container, false);

        TabLayout tabLayout=view.findViewById(R.id.tabLayout);
        ViewPager viewPager=view.findViewById(R.id.graphPager);

        //어댑터 설정
        PagerAdapter pagerAdapter=new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //tablayout과 pager연결
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter
    {
        ArrayList<Fragment> fragments=new ArrayList<>();
        String[] tabTitle={"일","주","월","년"};

        //생성자로 데이터를 던져서 바차트를 다르게 표현해야함.
        public PagerAdapter(FragmentManager fm)
        {
            super(fm);
            fragments.add(new GraphFragment("day")); //일
            fragments.add(new GraphFragment("week")); //주
            fragments.add(new GraphFragment("month")); //월
            fragments.add(new GraphFragment("year")); //년
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
}
