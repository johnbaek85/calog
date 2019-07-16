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
import com.example.calog.VO.SleepingVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

            ////////////////////// TODO 날짜의 월요일 가져오기 /////////////////////////
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            System.out.println("Calendar.DAY_OF_WEEK:" + Calendar.DAY_OF_WEEK);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String monday = formatter.format(calendar.getTime());
            /////////////////////////////////////////////////////////////////

            Log.i("monday:", "================" + monday);

//            //오늘 날짜 구하기
//            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
//            final String currentDate = format1.format(System.currentTimeMillis());

            //TODO 최근 일주일의 데이터 가져오기 - 그래프에서 주에 해당
            Call<List<FitnessVO>> call = rs.GraphFitnessData("spider", monday, "week");
            call.enqueue(new Callback<List<FitnessVO>>()
            {

                @Override
                public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response) {
                    userTotalBurnCalVOList = response.body();

                    String date="";
                    for (int i = 0; i < userTotalBurnCalVOList.size(); i++)
                    {
                        FitnessVO vo = userTotalBurnCalVOList.get(i);

                        if(vo.getWeight_fitness_date()!=null)
                        {
                            date = vo.getWeight_fitness_date(); //년도 잘라내기
                        }
                        else
                        {
                            date=vo.getCardio_fitness_date();
                        }

                        System.out.println("============================date:"+date);

                        System.out.println("============================weekVOCardiodate"+vo.getCardio_fitness_date());
                        daySumList.add(new GraphVO((float)(vo.getSum_weight_used_calorie() + vo.getSum_cardio_used_calorie()),String.valueOf(date)));
                    }


                    //TODO 혹시나 모를 데이터 초기화 (다른액티비에서 데이터가없으면 저장된 데이터를 보여주기떄문)
                    GraphFragment.sum_calorieListWeek = new ArrayList<>();

                    if (daySumList.size() != 0) {
                        GraphFragment.sum_calorieListWeek = daySumList;
                    }
                }
                @Override
                public void onFailure(Call<List<FitnessVO>> call, Throwable t)
                {
                    System.out.println("LastWeekFitness error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });

            ////////////////// TODO 해당 date 의 달의 첫일 가져오기
            calendar.set(Calendar.DATE,1);
            String monthFirstDay=formatter.format(calendar.getTime());
            Log.i("monthFirstDay:",monthFirstDay+"");
            ///////

            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 월에 해당
            Call<List<FitnessVO>> callMonth = rs.GraphFitnessData("spider", monthFirstDay, "month");
            callMonth.enqueue(new Callback<List<FitnessVO>>()
            {

                  @Override
                  public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response)
                  {

                      userTotalBurnCalVOList = response.body();

                      System.out.println("userSleepVOListMonth"+ userTotalBurnCalVOList);

                      //데이터 파싱
                      for (int i = 0; i <  userTotalBurnCalVOList.size(); i++) {
                          FitnessVO vo =  userTotalBurnCalVOList.get(i);
                          //String date = vo.getDiet_date().substring(5); //년도 잘라내기

                          String date="";
                          if(vo.getWeight_fitness_date()!=null)
                          {
                              date = vo.getWeight_fitness_date();
                          }
                          else
                          {
                              date=vo.getCardio_fitness_date();
                          }

                          weekSumList.add(new GraphVO((float)(vo.getSum_weight_used_calorie() + vo.getSum_cardio_used_calorie()), date));
                      }

                      GraphFragment.sum_calorieListMonth=new ArrayList<>();

                      if (weekSumList.size() != 0) {
                          GraphFragment.sum_calorieListMonth = weekSumList;
                      }
                  }

                  @Override
                  public void onFailure(Call<List<FitnessVO>> call, Throwable t)
                  {
                      System.out.println("LastMonthFitness error>>>>>>>>>>>>>>>>>>" + t.toString());
                  }
            });

            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 년에 해당
            Call<List<FitnessVO>> callYear = rs.GraphFitnessData("spider", monday, "year");
            callYear.enqueue(new Callback<List<FitnessVO>>()
             {

                 @Override
                 public void onResponse(Call<List<FitnessVO>> call, Response<List<FitnessVO>> response)
                 {
                     userTotalBurnCalVOList  = response.body();
                     System.out.println("userTotalBurnCalVOList:"+userTotalBurnCalVOList);

                     for (int i = 0; i < userTotalBurnCalVOList .size(); i++) {
                         FitnessVO vo = userTotalBurnCalVOList .get(i);

                         String date="";
                         if(vo.getWeight_fitness_date()!=null)
                         {
                             date = vo.getWeight_fitness_date().substring(5); //년도 잘라내기
                         }
                         else
                         {
                             date=vo.getCardio_fitness_date().substring(5);
                         }
                         date = date.substring(0, 2); //달만 가져오기
                        // System.out.println("======================년도정보:"+vo);

                         monthSumList.add(new GraphVO((float)(vo.getSum_weight_used_calorie() + vo.getSum_cardio_used_calorie()), date));
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

                     GraphFragment.sum_calorieListYear=new ArrayList<>();

                     if (monthSumListRes.size() != 0) {
                         GraphFragment.sum_calorieListYear = monthSumListRes;
                     }

                 }

                 @Override
                 public void onFailure(Call<List<FitnessVO>> call, Throwable t)
                 {
                     System.out.println("LastYearFitness error>>>>>>>>>>>>>>>>>>" + t.toString());
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
