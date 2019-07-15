package com.example.calog.Drinking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.Common.GraphVO;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.DrinkingVO;
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

public class DrinkingActivity extends AppCompatActivity
{

    //DB 용
    Retrofit retrofit;
    RemoteService rs;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinking);

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        GraphBackThread graphBackThread=new GraphBackThread();
        graphBackThread.execute();

        //뒤로가기 이벤트
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        ImageView btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DrinkingActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        //알콜check Activity 이동
        TextView alcoholCheck=findViewById(R.id.alcoholCheck);
        alcoholCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(DrinkingActivity.this,DrinkingCheckActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //주량 설정 버튼
        final ImageView liquorSetiing=findViewById(R.id.liquorSetiing);
        liquorSetiing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                View view = getLayoutInflater().inflate(R.layout.custom_dialog,null);

                //spinner 설정
                final ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("1병");
                arrayList.add("2병");
                arrayList.add("3병");
                arrayList.add("기타");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList);

                final Spinner spinner = view.findViewById(R.id.spinner);
                spinner.setAdapter(arrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(),arrayList.get(i)+"이(가) 선택되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                //주량설정 다이얼로그창
                AlertDialog.Builder box=new AlertDialog.Builder(DrinkingActivity.this);

                box.setTitle("주량설정");
                box.setView(view);
                box.setPositiveButton("저장", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String txt=spinner.getSelectedItem().toString();
                        TextView liquorTxt=findViewById(R.id.liquorTxt);
                        liquorTxt.setText("현재 주량 설정:" +txt);

                        Toast.makeText(DrinkingActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                box.setNegativeButton("취소",null);
                box.show();
            }
        });

        //그래프 BarChart Fragment
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();

        GraphPagerFragment graphFragment = new GraphPagerFragment();
        tr.replace(R.id.barChartFrag,graphFragment);

    }

    //그래프 백스레드
    private class GraphBackThread extends AsyncTask<Integer,Integer, ArrayList<GraphFragment>>
    {

        ArrayList<GraphVO> daySumList=new ArrayList<>();
        ArrayList<GraphVO> weekSumList=new ArrayList<>();
        ArrayList<GraphVO> monthSumList=new ArrayList<>();
        ArrayList<GraphVO> yearSumList=new ArrayList<>();

        List<DrinkingVO> userDrinkingVOList=new ArrayList<>();

        @Override
        protected void onPreExecute()
        {
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
            Call<List<DrinkingVO>> call = rs.GraphDrinkingData("spider",monday,"week");
            call.enqueue(new Callback<List<DrinkingVO>>() {

                @Override
                public void onResponse(Call<List<DrinkingVO>> call, Response<List<DrinkingVO>> response) {
                    userDrinkingVOList = response.body();

                    for (int i = 0; i < userDrinkingVOList.size(); i++)
                    {
                        DrinkingVO vo = userDrinkingVOList.get(i);

                        daySumList.add(new GraphVO((float) vo.getAlcohol_content(), vo.getDrinking_date())); //날짜중 년도를 짤라냄
                    }

                    //TODO 혹시나 모를 데이터 초기화 다른액티비에서 데이터가없으면 이전데이터가 나오니.
                    GraphFragment.sum_calorieListWeek = new ArrayList<>();

                    if (daySumList.size() != 0)
                    {
                        GraphFragment.sum_calorieListWeek = daySumList;
                    }
                }

                @Override
                public void onFailure(Call<List<DrinkingVO>> call, Throwable t) {
                    System.out.println("LastWeekTotalCalorie error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });

            ////////////////// TODO 해당 date 의 달의 첫일 가져오기
            calendar.set(Calendar.DATE,1);
            String monthFirstDay=formatter.format(calendar.getTime());
            Log.i("monthFirstDay:",monthFirstDay+"");
            ///////


            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 월에 해당
            Call<List<DrinkingVO>>  callMonth = rs.GraphDrinkingData("spider",monthFirstDay,"month");
            callMonth.enqueue(new Callback<List<DrinkingVO>>() {

                @Override
                public void onResponse(Call<List<DrinkingVO>>  call, Response<List<DrinkingVO>>  response) {
                    userDrinkingVOList = response.body();

                    //데이터 파싱
                    for (int i = 0; i < userDrinkingVOList.size(); i++) {
                        DrinkingVO vo = userDrinkingVOList.get(i);
                        //String date = vo.getDiet_date().substring(5); //년도 잘라내기

                        weekSumList.add(new GraphVO((float) vo.getAlcohol_content(), vo.getDrinking_date()));
                    }

                    GraphFragment.sum_calorieListMonth=new ArrayList<>();

                    if (weekSumList.size() != 0) {
                        GraphFragment.sum_calorieListMonth = weekSumList;
                    }
                }

                @Override
                public void onFailure(Call<List<DrinkingVO>> call, Throwable t) {
                    System.out.println("LastMonthTotalCalorie error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });

            //캘린더의 정보 가져오기
            String year_date=formatter.format(calendar.getTime());

            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 년에 해당함
            Call<List<DrinkingVO>> callYear = rs.GraphDrinkingData("spider",year_date,"year");
            callYear.enqueue(new Callback<List<DrinkingVO>>() {
                @Override
                public void onResponse(Call<List<DrinkingVO>> call, Response<List<DrinkingVO>> response) {
                    userDrinkingVOList = response.body();

                    System.out.println("userDrinkingVOList:"+userDrinkingVOList);

                    for (int i = 0; i < userDrinkingVOList.size(); i++) {
                        DrinkingVO vo = userDrinkingVOList.get(i);

                        System.out.println("DrinkingVO:"+vo);
                        String date = vo.getDrinking_date().substring(5); //년도 잘라내기
                        date = date.substring(0, 2); //달만 가져오기
                        System.out.println("======================년도정보:"+vo);

                        monthSumList.add(new GraphVO((float) vo.getAlcohol_content(), date));
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
                public void onFailure(Call<List<DrinkingVO>> call, Throwable t) {
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
                //TODO 데이터가 들어오기 전까지 무한루프를 빠져나가지못함.
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
