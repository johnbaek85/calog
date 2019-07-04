package com.example.calog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;


public class GraphFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_graph, container, false);

        BarChart barChart=view.findViewById(R.id.barChart);

        //바차트 초기화 (한번 해주어야한다고함)
        barChart.invalidate();

        //바차트 x축,y축 데이터 할당 각 x축 간의 차이로 데이터바의 굵기가 할당됨
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(2014, 0));
        entries.add(new BarEntry(2015, 1));
        entries.add(new BarEntry(2016, 2));
        entries.add(new BarEntry(2017, 3));
        entries.add(new BarEntry(2018, 4));
        entries.add(new BarEntry(2019, 5));

        //할당된 리스트 데이터셋에 넣기(자동으로 바차트에 넣을수 있는 데이터로 변경해주는 코드인듯
        BarDataSet bardataset = new BarDataSet(entries, "No Of Employee");

        //데이터 셋을 바차트 데이터 장착.
        BarData data = new BarData(bardataset);

        //바 차트 데이터 장착
        barChart.setData(data);

        //바차트를 그릴때 에니메이션 효과주기 x축과 y축별로 시간 할당 가능
        barChart.animateXY(2000, 2000);

        //bar차트 선의 굵기 (수동으로 주기)
        //bardataset.setBarBorderWidth(10f);

        //바차트 컬러 주기 안주면 기본 하늘색
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        return view;
    }

}
