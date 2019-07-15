package com.example.calog.Common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GraphFragment extends Fragment
{
    String[] labels;

    public GraphFragment(){}

   // ArrayList<Float> sum_calorieList;

    //값이없을때 에러나지 않게 초기값을 준다.
    public static ArrayList<GraphVO> sum_calorieListWeek=
            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today"))); //일에 대한 데이터
    public static ArrayList<GraphVO> sum_calorieListMonth=
            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today"))); //주에 대한 데이터
    public static ArrayList<GraphVO> sum_calorieListYear=
            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today"))); //달에 대한 데이터
//    public static ArrayList<GraphVO> sum_calorieListYear=
//            new ArrayList<GraphVO>(Arrays.asList(new GraphVO(0, "Today"))); //년에 대한 데이터

    ArrayList<BarEntry> entries;

    String unitDate;

    public GraphFragment(String unitDate)
    {
        this.unitDate=unitDate;
        //라벨 작업
       if(unitDate.equals("week"))
       {
           this.labels = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
       }
       else if(unitDate.equals("month"))
       {
           this.labels=
                   new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

       }
       else if(unitDate.equals("year"))
       {
           this.labels = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
       }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
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

        if(unitDate.equals("week"))
        {
            GraphVO vo;


            for(int i=0; i<labels.length; i++) //TODO 처음에 0으로 전부 초기화 set을 사용하기 위해서
            {
                entries.add(new BarEntry(i, 0));
            }

            for (int i = 0; i<labels.length; i++)
            {
                if(i < sum_calorieListWeek.size()) //다음값이 있는지 없는지 확인
                {
                    vo=sum_calorieListWeek.get(i);

                    String week="";

                    //TODO 특정날짜에 해당하는 요일구하기 (메소드로만듬) 중간에 날짜가 비어있으면 건너뛰어야하기때문에 필요함.
                    try
                    {
                        week=getDateDay(vo.getData_date());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    if(week.equals(labels[i])) //요일과같은 라벨과 매핑
                    {
                        entries.set(i,new BarEntry(i, vo.getData_float()));
                    }
                }
            }
        }
        else if(unitDate.equals("month"))
        {
            GraphVO vo;

            for(int i=0; i<labels.length; i++) //TODO 처음에 0으로 전부 초기화 set을 사용하기 위해서
            {
                entries.add(new BarEntry(i, 0));
            }

            for (int i = 0; i<labels.length; i++)
            {

                if(i < sum_calorieListMonth.size()) //다음값이 있는지 없는지 확인
                {
                    vo=sum_calorieListMonth.get(i); //TODO 데이터 집어넣기

                    int indexday=Integer.parseInt(vo.getData_date().substring(8))-1; //label과 매칭하기
                    entries.set(indexday,new BarEntry(indexday, vo.getData_float()));
                }
            }
        }
        else if(unitDate.equals("year"))
        {
            GraphVO vo;

            for(int i=0; i<labels.length; i++) //TODO 처음에 0으로 전부 초기화 set을 사용하기 위해서
            {
                entries.add(new BarEntry(i, 0));
            }

            for (int i = 0; i<labels.length; i++)
            {
                if(i < sum_calorieListYear.size()) //다음값이 있는지 없는지 확인
                {
                    vo=sum_calorieListYear.get(i);

                    int indexMonth=Integer.parseInt(vo.getData_date())-1; //label과 매칭하기
                    entries.set(indexMonth,new BarEntry(indexMonth,vo.getData_float()));
                }
            }
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


    //TODO 특정 날짜의 요일 구하기
    private String getDateDay(String date) throws Exception {


        String day = "" ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        switch(dayNum){
            case 1:
                day = "Sun";
                break ;
            case 2:
                day = "Mon";
                break ;
            case 3:
                day = "Tue";
                break ;
            case 4:
                day = "Wed";
                break ;
            case 5:
                day = "Thu";
                break ;
            case 6:
                day = "Fri";
                break ;
            case 7:
                day = "Sat";
                break ;

        }

        return day ;
    }

}