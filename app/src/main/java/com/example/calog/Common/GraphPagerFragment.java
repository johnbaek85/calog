package com.example.calog.Common;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.calog.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphPagerFragment extends Fragment {

//    private FragmentManager fragmentManager;
//
  //  GraphFragment[] graphFragments;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<GraphFragment> fragments;

    ArrayList<Float> sum_calorieList=null;

    TabLayout tabLayout;
    ViewPager viewPager;

    public GraphPagerFragment(){};

//    public GraphPagerFragment(ArrayList<Float> sum_calorieList)
//    {
//        this.sum_calorieList=sum_calorieList;
//        System.out.println("그래프 페이저 프래그먼트 테스트1=================================="+sum_calorieList);
//
//        GraphFragment.sum_calorieList2=sum_calorieList;
//        System.out.print(" GraphFragment.sum_calorieList2"+GraphFragment.sum_calorieList2);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view=inflater.inflate(R.layout.fragment_graph_pager, container, false);

        //TODO
        // 컴파일이 될때 자바 클래
        // thread.sleep 은 안드로이드에서 UI를 변경할때 먹히지 않는다. UI를 틈을주고 변경하려면 Handler라는 것을 사용해야한다
        //
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TabLayout tabLayout=view.findViewById(R.id.tabLayout);
                ViewPager viewPager=view.findViewById(R.id.graphPager);

                //어댑터 설정
                PagerAdapter pagerAdapter=new PagerAdapter(getFragmentManager());
                viewPager.setAdapter(pagerAdapter);

                //tablayout과 pager연결
                tabLayout.setupWithViewPager(viewPager);

                //progressBar 숨기기
                ProgressBar circleProgress=view.findViewById(R.id.progress_circular);
                circleProgress.setVisibility(View.GONE);
            }
        }, 3000 ); //안전하게 데이터를 가져오려면 3초가 충분

        return view;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter  //FragmentStatePagerAdapter는 화면변환시 메모리 삭제 후 기존 데이터로 다시만듬
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
