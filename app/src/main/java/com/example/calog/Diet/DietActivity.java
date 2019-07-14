package com.example.calog.Diet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.Common.GraphVO;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.MainHealthVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class DietActivity extends AppCompatActivity {

    Intent intent;

    //DB 용
    Retrofit retrofit;
    RemoteService rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성


        GraphBackThread graphBackThreah = new GraphBackThread();
        graphBackThreah.execute();

        SlidingDrawer dietDrawer = findViewById(R.id.dietDrawer);
        dietDrawer.animateClose();
    }

    public void mClick(View view) {
        intent = new Intent(DietActivity.this, FoodSearchActivity.class);
        switch (view.getId()){
            case R.id.btnBreakfast:
                Toast.makeText(DietActivity.this, "아침", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnLunch:
                Toast.makeText(DietActivity.this, "점심", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnDinner:
                Toast.makeText(DietActivity.this, "저녁", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnSnack:
                Toast.makeText(DietActivity.this, "간식", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnHome:
                intent = new Intent(DietActivity.this, MainHealthActivity.class);
                startActivity(intent);
                break;
        }
    }

    //그래프 백스레드
    private class GraphBackThread extends AsyncTask<Integer,Integer, ArrayList<GraphFragment>>
    {

        ArrayList<GraphVO> daySumList=new ArrayList<>();
        ArrayList<GraphVO> weekSumList=new ArrayList<>();
        ArrayList<GraphVO> monthSumList=new ArrayList<>();
        ArrayList<GraphVO> yearSumList=new ArrayList<>();

        List<UserTotalCaloriesViewVO> userTotalCaloriesViewVOList=new ArrayList<>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //오늘 날짜 구하기
            SimpleDateFormat format1 = new SimpleDateFormat ( "MM-dd");
            final String currentDate = format1.format (System.currentTimeMillis());

            //TODO 최근 일주일의 데이터 가져오기 - 그래프에서 일에 해당
            Call<List<UserTotalCaloriesViewVO>> call = rs.LastWeekTotalCalorie("spider");
            call.enqueue(new Callback<List<UserTotalCaloriesViewVO>>()
            {

                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response)
                {
                    userTotalCaloriesViewVOList=response.body();

                    for(int i=0; i<userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo=userTotalCaloriesViewVOList.get(i);

                        String date=vo.getDiet_date().substring(5); //년도를 잘라냄
                        if(date.equals(currentDate)) //날짜가 오늘날짜면 Today로 바꿔줌
                        {
                            date="Today";
                        }

                        daySumList.add(new GraphVO((float)vo.getSum_calorie(), date)); //날짜중 년도를 짤라냄
                    }

                    if(daySumList.size()!=0) {
                        GraphFragment.sum_calorieListDay = daySumList;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t)
                {
                    System.out.println("LastWeekTotalCalorie error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 주에 해당
            Call<List<UserTotalCaloriesViewVO>> callMonth = rs.LastMonthTotalCalorie("spider");
            callMonth.enqueue(new Callback<List<UserTotalCaloriesViewVO>>()
            {

                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response)
                {
                    userTotalCaloriesViewVOList=response.body();

                    //데이터 파싱
                    for(int i=0; i<userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo=userTotalCaloriesViewVOList.get(i);
                        String date=vo.getDiet_date().substring(5); //년도 잘라내기

                        weekSumList.add(new GraphVO((float)vo.getSum_calorie(), date));
                    }

                    float sum=0f;
                    int div=0;

                    ArrayList<GraphVO> weekSumListRes=new ArrayList<>(); //결과값이 담길곳

                    String begindate=new String(weekSumList.get(0).getData_date());

                    //한주 짜리 데이터 만드는 작업
                    Log.i("begindate:",begindate+"");
                    Log.i("weekSumList사이즈:",weekSumList.size()+"");
                    for(int i=0; i<weekSumList.size(); i++)
                    {
                        if((div >=7 && div%7==0) || weekSumList.size()-1==i) //TODO 카운트(div)가 7보다 크거나같고 7로 나누어떨어질때 또는 마지막 for문일때 그동한 더한 sum을 div로 나누어서 평균을 내어 주단위 데이터에 담는다. 그리고 sum 초기화
                        {

                            sum+=weekSumList.get(i).getData_float();
                            div++;

                            Log.i("div:","========================================="+div+""); //Thread(백그라운드) 에서는 Log로 찍고 Logcat으로 확인해야함. 스레드는 print로하면 터미널에선 안보임
                            float avg=sum/div; //평균내기 - i가 0 일때는 나눌수없으므로 +1을해준다.
                            //vo.setData_float(avg);
                            String str=begindate+"~"+weekSumList.get(i).getData_date();

                            weekSumListRes.add(new GraphVO(avg,str)); //TODO 위에서 생성한 GraphVO를 add하면 같은것이 담긴다 필드값이 변경되도 말이다. 왜냐하면 vo의 주소값이 같기떄문에 값이 변경되면 같이변경됨 ,따라서 new를 통해 새로 생성해야함.

                            if(weekSumList.size()!=i+1) //다음 값이 있는지 없는지 확인후 다음값이 있다면 날짜를 변경한다.
                            {
                                begindate = weekSumList.get(i+1).getData_date();
                            }

                            sum=0;
                            div=0; //카운트 초기화

                            continue;
                        }

                        sum+=weekSumList.get(i).getData_float(); //데이터를 하나하나 꺼내서 다더함
                        div++; //평균내기위한 카운트
                    }

                    if(weekSumListRes.size()!=0)
                    {
                        GraphFragment.sum_calorieListWeek = weekSumListRes;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t)
                {
                    System.out.println("LastMonthTotalCalorie error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 달에 해당함
            Call<List<UserTotalCaloriesViewVO>> callYear = rs.LastYearTotalCalorie("spider");
            callYear.enqueue(new Callback<List<UserTotalCaloriesViewVO>>()
            {
                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response)
                {
                    userTotalCaloriesViewVOList=response.body();

                    for(int i=0; i<userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo=userTotalCaloriesViewVOList.get(i);

                        String date=vo.getDiet_date().substring(5); //년도 잘라내기
                        date=date.substring(0,2); //달만 가져오기

                        monthSumList.add(new GraphVO((float)vo.getSum_calorie(), date));
                    }


                    float sum=0f;
                    int div=0;

                    ArrayList<GraphVO> monthSumListRes=new ArrayList<>(); //결과값이 담길곳

                    //기록상 첫번째에 위치한 달을 가져옴
                    String currentMonth="";
                    if(monthSumList.size()!=0)
                    {
                        currentMonth = monthSumList.get(0).getData_date();
                    }

                    //월단위 데이터 뽑아내기 TODO 주의! 월을 넣어주지 말 것 월넣으면 equals에서 달만 읽기때문
                    for(int i=0; i<monthSumList.size(); i++)
                    {
                        if(!currentMonth.equals(monthSumList.get(i).getData_date()) || monthSumList.size()-1==i) //TODO 리스트의 마지막이거나 달이 바뀔경우에 실행
                        {
                            float avg=sum/div;
                            //System.out.println("currentDate"+currentDate);

                            monthSumListRes.add(new GraphVO(avg,currentMonth+"월"));

                            sum=monthSumList.get(i).getData_float();
                            div=1;

                            currentMonth=monthSumList.get(i).getData_date(); //달을 바꿔줌

                            continue; //아래코드는 실행되면 안되니 continue하여 처음부터 실행
                        }

                        sum+=monthSumList.get(i).getData_float();
                        div++;
                    }

                    if(monthSumListRes.size()!=0)
                    {
                        GraphFragment.sum_calorieListMonth=monthSumListRes;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t)
                {
                    System.out.println("LastYearTotalCalorie error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 5년간의 데이터 가져오기 - 그래프에서 년에 해당함
            Call<List<UserTotalCaloriesViewVO>> callAll = rs.LastAllTotalCalorie("spider");
            callAll.enqueue(new Callback<List<UserTotalCaloriesViewVO>>()
            {
                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response)
                {
                    userTotalCaloriesViewVOList=response.body();

                    for(int i=0; i<userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo=userTotalCaloriesViewVOList.get(i);

                        String date=vo.getDiet_date().substring(0,4); //년도 가져오기
                        yearSumList.add(new GraphVO((float)vo.getSum_calorie(), date));
                    }

                    float sum=0f;
                    int div=0;

                    ArrayList<GraphVO> yearSumListRes=new ArrayList<>(); //결과값이 담길곳

                    //기록상 첫번째에 위치한 년을 가져옴
                    String currentYear="";
                    if(yearSumList.size()!=0)
                    {
                        currentYear = yearSumList.get(0).getData_date();
                    }

                    //년단위 데이터 뽑아내기
                    for(int i=0; i<yearSumList.size(); i++)
                    {

                        if(!currentYear.equals(yearSumList.get(i).getData_date()) || yearSumList.size()-1==i) //TODO 리스트의 마지막이거나 년이 바뀔경우에 실행
                        {
                            //sum+=yearSumList.get(i).getData_float();
                            float avg=sum/div;

                            yearSumListRes.add(new GraphVO(avg,currentYear));

                            sum=yearSumList.get(i).getData_float();
                            div=1;

                            currentYear=yearSumList.get(i).getData_date(); //달을 바꿔줌

                            continue;
                        }

                        sum+=yearSumList.get(i).getData_float();
                        div++;
                    }

                    if(yearSumListRes.size()!=0)
                    {
                        GraphFragment.sum_calorieListYear=yearSumListRes;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t)
                {
                    System.out.println("LastAllTotalCalorie error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });
        }

        //Thread
        @Override
        protected ArrayList<GraphFragment> doInBackground(Integer... integers)
        {

            while(true)
            {
                //TODO 마지막 데이터가 들어오기 전까지 무한루프를 빠져나가지못함.
                if(yearSumList.size()!=0)
                {
                    break;
                }
            }

            return null;
        }

        //doinback이 끝났을때
        @Override
        protected void onPostExecute(ArrayList<GraphFragment> graphFragments)
        {
            super.onPostExecute(graphFragments);
            //TODO 그래프 BarChart Fragment 장착
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction tr=fm.beginTransaction();

            GraphPagerFragment graphFragment = new GraphPagerFragment();
            tr.replace(R.id.barChartFrag,graphFragment);

        }
    }
}
