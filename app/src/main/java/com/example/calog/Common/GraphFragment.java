package com.example.calog.Common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calog.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphFragment extends Fragment
{
    String[] labels;

    public GraphFragment(){}

    ArrayList<Float> sum_calorieList;

    public static ArrayList<Float> sum_calorieListDay; //일에 대한 데이터
    public static ArrayList<Float> sum_calorieListWeek= new ArrayList<Float>(Arrays.asList(10.0f, 20.0f, 30.0f,40.0f,50.0f));; //주에 대한 데이터
    public static ArrayList<Float> sum_calorieListMonth= new ArrayList<Float>(Arrays.asList(100f,200f,130f,160f,300f,400f,500f,450f,330f,220f,180f,270f)); //달에 대한 데이터
    public static ArrayList<Float> sum_calorieListYear=new ArrayList<Float>(Arrays.asList(300f,100f,230f,60f,190f)); //년에 대한 데이터

    ArrayList<BarEntry> entries;

    String unitDate;

    public GraphFragment(String unitDate)
    {
        this.unitDate=unitDate;
        //라벨 작업
       if(unitDate.equals("day"))
       {
           this.labels = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
       }
       else if(unitDate.equals("week"))
       {
           this.labels = new String[]{"1~7", "8~14", "15~21", "22~26", "26~end"};
       }
       else if(unitDate.equals("month"))
       {
           this.labels = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
       }
       else if(unitDate.equals("year"))
       {
           this.labels = new String[]{"2015","2016","2017","2018","2019"};
       }

       System.out.println("===========================그래프 프래그먼트 sum CalorieLis"+sum_calorieListDay);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_graph, container, false);

        BarChart barChart=view.findViewById(R.id.barChart);

        //바차트 초기화 (한번 해주어야한다고함)
        barChart.invalidate();

        //바차트 x축,y축 데이터 할당 각 x축 간의 차이로 데이터바의 굵기가 할당됨
        ArrayList<BarEntry> entries = new ArrayList<>();

        //x축에 String을 넣으려면 String리스트를 던져야함 대신에 BarEntry의 X값은 리스트에 position값이여야한다.


        if(unitDate.equals("day"))
        {
            System.out.println("entries for 일=================");
            for(int x=0; x<sum_calorieListDay.size(); x++)
            {
                entries.add(new BarEntry(x,sum_calorieListDay.get(x)));
                //System.out.println("entries for실행================="+entries);
            }
        }
        else if(unitDate.equals("week"))
        {
            System.out.println("entries for 주=================");
            for(int x=0; x<sum_calorieListWeek.size(); x++)
            {
                entries.add(new BarEntry(x,sum_calorieListWeek.get(x)));
                //System.out.println("entries for실행================="+entries);
            }
        }
        else if(unitDate.equals("month"))
        {
            System.out.println("entries for 월=================");
            for(int x=0; x<sum_calorieListMonth.size(); x++)
            {
                entries.add(new BarEntry(x,sum_calorieListMonth.get(x)));
                //System.out.println("entries for실행================="+entries);
            }
        }
        else if(unitDate.equals("year"))
        {
            System.out.println("entries for 년=================");
            for(int x=0; x<sum_calorieListYear.size(); x++)
            {
                entries.add(new BarEntry(x,sum_calorieListYear.get(x)));
                //System.out.println("entries for실행================="+entries);
            }
        }
        else
        {
            //entries.add(new BarEntry(0, sum_calorieList2.get(0))); //x의 0은 String 배열의 0번째 값을 말한다
            entries.add(new BarEntry(0, 10));
            entries.add(new BarEntry(1, 20));
            entries.add(new BarEntry(2, 30));
            entries.add(new BarEntry(3, 40));
            entries.add(new BarEntry(4, 50));
            entries.add(new BarEntry(5, 60));
            entries.add(new BarEntry(6, 70));
        }

        //bar데이터 폭설정
        //BarData data = new BarData(set1, set2);
        //data.setBarWidth(barWidth); // set the width of each bar

        //x축에 String 데이터를 넣기
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        //x축 포지션 Bottom은 그래프에서 아래에 위치하게됨
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //각 X축 데이터 간의 거리를 수동으로 설정해줄수있음
        //TODO 1f로 맞춰줘야 제대로 나옴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);

        //할당된 리스트 데이터셋에 넣기(자동으로 바차트에 넣을수 있는 데이터로 변경해주는 코드인듯
        BarDataSet bardataset = new BarDataSet(entries, "");

        //데이터 셋을 바차트 데이터 장착.
        BarData data = new BarData(bardataset);

        //바 차트 데이터 장착
        barChart.setData(data);

        //바차트 Description 제거
        barChart.getDescription().setEnabled(false);
        //바차트 각 바 설명 제거
        barChart.getLegend().setEnabled(false);

        //바차트를 그릴때 에니메이션 효과주기 x축과 y축별로 시간 할당 가능
        barChart.animateXY(2000, 2000);

        //bar차트 선의 굵기 (수동으로 주기)
        //bardataset.setBarBorderWidth(10f);

        //바차트 컬러 주기 안주면 기본 하늘색
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        return view;
    }


}