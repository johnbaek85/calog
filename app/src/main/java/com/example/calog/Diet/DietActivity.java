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
import java.util.Calendar;
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
        protected void onPreExecute() {
            super.onPreExecute();

            ////////////////////// TODO 날짜의 월요일 가져오기 /////////////////////////
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            System.out.println("Calendar.DAY_OF_WEEK:"+Calendar.DAY_OF_WEEK);
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            String monday=formatter.format(calendar.getTime());
            /////////////////////////////////////////////////////////////////

            Log.i("monday:","================"+monday);

//            //오늘 날짜 구하기
//            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
//            final String currentDate = format1.format(System.currentTimeMillis());

            //TODO 최근 일주일의 데이터 가져오기 - 그래프에서 주에 해당
            Call<List<UserTotalCaloriesViewVO>> call = rs.GraphDietData("spider",monday,"week");
            call.enqueue(new Callback<List<UserTotalCaloriesViewVO>>() {

                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response) {
                    userTotalCaloriesViewVOList = response.body();

                    for (int i = 0; i < userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo = userTotalCaloriesViewVOList.get(i);

                        daySumList.add(new GraphVO((float) vo.getSum_calorie(), vo.getDiet_date())); //날짜중 년도를 짤라냄
                    }

                    if (daySumList.size() != 0)
                    {
                        GraphFragment.sum_calorieListWeek = daySumList;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t) {
                    System.out.println("LastWeekTotalCalorie error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });

            ////////////////// TODO 해당 date 의 달의 첫일 가져오기
            calendar.set(Calendar.DATE,1);
            String monthFirstDay=formatter.format(calendar.getTime());
            Log.i("monthFirstDay:",monthFirstDay+"");
            ///////


            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 월에 해당
            Call<List<UserTotalCaloriesViewVO>> callMonth = rs.GraphDietData("spider",monthFirstDay,"month");
            callMonth.enqueue(new Callback<List<UserTotalCaloriesViewVO>>() {

                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response) {
                    userTotalCaloriesViewVOList = response.body();

                    //데이터 파싱
                    for (int i = 0; i < userTotalCaloriesViewVOList.size(); i++) {
                        UserTotalCaloriesViewVO vo = userTotalCaloriesViewVOList.get(i);
                        //String date = vo.getDiet_date().substring(5); //년도 잘라내기

                        weekSumList.add(new GraphVO((float) vo.getSum_calorie(), vo.getDiet_date()));
                    }

                    if (weekSumList.size() != 0) {
                        GraphFragment.sum_calorieListMonth = weekSumList;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t) {
                    System.out.println("LastMonthTotalCalorie error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });

            //캘린더의 정보 가져오기
            String year_date=formatter.format(calendar.getTime());

            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 년에 해당함
            Call<List<UserTotalCaloriesViewVO>> callYear = rs.GraphDietData("spider",year_date,"year");
            callYear.enqueue(new Callback<List<UserTotalCaloriesViewVO>>() {
                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response) {
                    userTotalCaloriesViewVOList = response.body();

                    for (int i = 0; i < userTotalCaloriesViewVOList.size(); i++) {
                        UserTotalCaloriesViewVO vo = userTotalCaloriesViewVOList.get(i);

                        String date = vo.getDiet_date().substring(5); //년도 잘라내기
                        date = date.substring(0, 2); //달만 가져오기
                        System.out.println("======================년도정보:"+vo);

                        monthSumList.add(new GraphVO((float) vo.getSum_calorie(), date));
                    }

                    float sum = 0f;
                    int div = 0;

                    ArrayList<GraphVO> monthSumListRes = new ArrayList<>(); //결과값이 담길곳

                    //기록상 첫번째에 위치한 달을 가져옴
                    String currentMonth = "";
                    if (monthSumList.size() != 0) {
                        currentMonth = monthSumList.get(0).getData_date();
                    }

                    //월단위 데이터 뽑아내기 TODO 주의! 월을 넣어주지 말 것 월넣으면 equals에서 달만 읽기때문
                    for (int i = 0; i < monthSumList.size(); i++)
                    {
                        if (!currentMonth.equals(monthSumList.get(i).getData_date()) || monthSumList.size() - 1 == i) //TODO 리스트의 마지막이거나 달이 바뀔경우에 실행
                        {

                            if(monthSumList.size()-1==i)
                            {
                                sum += monthSumList.get(i).getData_float();
                                div++;
                            }

                            float avg = sum / div;

                            System.out.println("=============="+sum+"/"+div+":"+avg);
                            //System.out.println("currentDate"+currentDate);

                            monthSumListRes.add(new GraphVO(avg, currentMonth));

                            sum = monthSumList.get(i).getData_float();
                            div = 1;

                            currentMonth = monthSumList.get(i).getData_date(); //달을 바꿔줌
                            System.out.println("currentMonth:"+currentMonth);
                            continue; //아래코드는 실행되면 안되니 continue하여 처음부터 실행
                        }

                        sum += monthSumList.get(i).getData_float();
                        div++;
                    }

                    if (monthSumListRes.size() != 0) {
                        GraphFragment.sum_calorieListYear = monthSumListRes;
                    }
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t) {
                    System.out.println("LastYearTotalCalorie error>>>>>>>>>>>>>>>>>>" + t.toString());
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
