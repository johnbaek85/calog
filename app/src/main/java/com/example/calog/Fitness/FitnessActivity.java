package com.example.calog.Fitness;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.Common.GraphVO;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.FitnessVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class FitnessActivity extends AppCompatActivity {
    RelativeLayout btnCardioActivity, btnWeightTrainingActivity, btnStretchingActivity;
    ImageView btnBack, btnHome;
    Intent intent;


    //DB 용
    Retrofit retrofit;
    RemoteService rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);


        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        GraphBackThread graphBackThread=new GraphBackThread();
        graphBackThread.execute();

//메인페이지로 이동
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(FitnessActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });


//유산소운동 목록 출력
        btnCardioActivity=findViewById(R.id.btnCardioActivity);
        btnCardioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "유산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
               goToSearchActivity(1);
            }
        });


//무산소운동 목록 출력
        btnWeightTrainingActivity=findViewById(R.id.btnWeightTrainingActivity);
        btnWeightTrainingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "무산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
                goToSearchActivity(2);
            }
        });

//스트레칭 검색
        btnStretchingActivity=findViewById(R.id.btnStretchingActivity);
        btnStretchingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "스트레칭을 검색합니다.", Toast.LENGTH_SHORT).show();
                String searchWord = "스트레칭";
                intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", searchWord);
                try {
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                }
            }
        });
    }

    public void goToSearchActivity(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        intent.putExtra("운동타입", fitnessTypeid);
        startActivity(intent);
    }

    //그래프 백스레드
    private class GraphBackThread extends AsyncTask<Integer,Integer, ArrayList<GraphFragment>>
    {

        ArrayList<GraphVO> daySumList=new ArrayList<>();
        ArrayList<GraphVO> weekSumList=new ArrayList<>();
        ArrayList<GraphVO> monthSumList=new ArrayList<>();
        ArrayList<GraphVO> yearSumList=new ArrayList<>();

        List<FitnessVO> userTotalBurnCalVOList=new ArrayList<>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //오늘 날짜 구하기
            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
            final String currentDate = format1.format(System.currentTimeMillis());

            //TODO 최근 일주일의 데이터 가져오기 - 그래프에서 일에 해당
            Call<List<FitnessVO>> call = rs.LastWeekTotalBurnCal("spider");
            call.enqueue(new Callback<List<FitnessVO>>()
            {

                @Override
                public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response)
                {
                    userTotalBurnCalVOList=response.body();

                    for(int i=0; i<userTotalBurnCalVOList.size(); i++)
                    {
                        FitnessVO vo=userTotalBurnCalVOList.get(i);

                        String date=vo.getFitness_date().substring(5); //년도를 잘라냄
                        if(date.equals(currentDate)) //날짜가 오늘날짜면 Today로 바꿔줌
                        {
                            date="Today";
                        }

                        daySumList.add(new GraphVO((float) (vo.getSum_weight_used_calorie()+vo.getSum_cardio_used_calorie()), date)); //날짜중 년도를 짤라냄
                    }

                    if(daySumList.size()!=0)
                    {
                        GraphFragment.sum_calorieListWeek = daySumList;
                    }
                }

                @Override
                public void onFailure(Call<List<FitnessVO>> call, Throwable t)
                {
                    System.out.println("LastWeekTotalBurnCal error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 주에 해당
            Call<List<FitnessVO>> callMonth = rs.LastMonthTotalBurnCal("spider");
            callMonth.enqueue(new Callback<List<FitnessVO>>()
            {
                @Override
                public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response)
                {
                    userTotalBurnCalVOList=response.body();

                    //데이터 파싱
                    for(int i=0; i<userTotalBurnCalVOList.size(); i++)
                    {
                        FitnessVO vo=userTotalBurnCalVOList.get(i);
                        String date=vo.getFitness_date().substring(5); //년도 잘라내기

                        weekSumList.add(new GraphVO((float)(vo.getSum_cardio_used_calorie()+vo.getSum_weight_used_calorie()), date));
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
                        if ((div >= 7 && div % 7 == 0) || weekSumList.size() - 1 == i) //TODO 카운트(div)가 7보다 크거나같고 7로 나누어떨어질때 또는 마지막 for문일때 그동한 더한 sum을 div로 나누어서 평균을 내어 주단위 데이터에 담는다. 그리고 sum 초기화
                        {

                            sum += weekSumList.get(i).getData_float();
                            div++;

                            Log.i("div:", "=========================================" + div + ""); //Thread(백그라운드) 에서는 Log로 찍고 Logcat으로 확인해야함. 스레드는 print로하면 터미널에선 안보임
                            float avg = sum / div; //평균내기 - i가 0 일때는 나눌수없으므로 +1을해준다.
                            //vo.setData_float(avg);
                            String str = begindate + "~" + weekSumList.get(i).getData_date();

                            weekSumListRes.add(new GraphVO(avg, str)); //TODO 위에서 생성한 GraphVO를 add하면 같은것이 담긴다 필드값이 변경되도 말이다. 왜냐하면 vo의 주소값이 같기떄문에 값이 변경되면 같이변경됨 ,따라서 new를 통해 새로 생성해야함.

                            if (weekSumList.size() != i + 1) //다음 값이 있는지 없는지 확인후 다음값이 있다면 날짜를 변경한다.
                            {
                                begindate = weekSumList.get(i + 1).getData_date();
                            }

                            sum = 0;
                            div = 0; //카운트 초기화

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
                public void onFailure(Call<List<FitnessVO>> call, Throwable t)
                {
                    System.out.println("LastMonthTotalCalorie error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 달에 해당함
            Call<List<FitnessVO>> callYear = rs.LastYearTotalBurnCal("spider");
            callYear.enqueue(new Callback<List<FitnessVO>>()
            {
                //userTotalBurnCalVOList=response.body();

                @Override
                public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response)
                {

                }

                @Override
                public void onFailure(Call<List<FitnessVO>> call, Throwable t)
                {

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
