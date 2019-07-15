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

            //오늘 날짜 구하기
            SimpleDateFormat format1 = new SimpleDateFormat ( "MM-dd");
            final String currentDate = format1.format (System.currentTimeMillis());

            //TODO 최근 일주일의 데이터 가져오기 - 그래프에서 일에 해당
            Call<List<DrinkingVO>> call = rs.LastWeekTotalBac("spider");
            call.enqueue(new Callback<List<DrinkingVO>>()
            {

                @Override
                public void onResponse(Call<List<DrinkingVO>> call, Response<List<DrinkingVO>> response)
                {
                    userDrinkingVOList=response.body();

                    Log.i("userTotalBacViewVOList",userDrinkingVOList+"");

                    for(int i=0; i< userDrinkingVOList.size(); i++)
                    {
                        DrinkingVO vo=userDrinkingVOList.get(i);

                        String date=vo.getDrinking_date().substring(5); //년도를 잘라냄
                        if(date.equals(currentDate)) //날짜가 오늘날짜면 Today로 바꿔줌
                        {
                            date="Today";
                        }

                        daySumList.add(new GraphVO((float)vo.getAlcohol_content(), date)); //날짜중 년도를 짤라냄
                    }

                    if(daySumList.size()!=0) {
                        GraphFragment.sum_calorieListWeek = daySumList;
                    }
                }

                @Override
                public void onFailure(Call<List<DrinkingVO>> call, Throwable t)
                {
                    System.out.println("LastWeekTotalBac error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 주에 해당
            Call<List<DrinkingVO>> callMonth = rs.LastMonthTotalBac("spider");
            callMonth.enqueue(new Callback<List<DrinkingVO>>()
            {

                @Override
                public void onResponse(Call<List<DrinkingVO>> call, Response<List<DrinkingVO>> response)
                {
                    userDrinkingVOList=response.body();

                    //데이터 파싱
                    for(int i=0; i<userDrinkingVOList.size(); i++)
                    {
                        DrinkingVO vo=userDrinkingVOList.get(i);
                        String date=vo.getDrinking_date().substring(5); //년도 잘라내기

                        weekSumList.add(new GraphVO((float)vo.getAlcohol_content(), date));
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
                        GraphFragment.sum_calorieListMonth = weekSumListRes;
                    }
                }

                @Override
                public void onFailure(Call<List<DrinkingVO>> call, Throwable t)
                {
                    System.out.println("LastMonthTotalBac error>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });

            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 달에 해당함
            Call<List<DrinkingVO>> callYear = rs.LastYearTotalBac("spider");
            callYear.enqueue(new Callback<List<DrinkingVO>>()
            {

                @Override
                public void onResponse(Call<List<DrinkingVO>> call, Response<List<DrinkingVO>> response)
                {
                    userDrinkingVOList=response.body();

                    for(int i=0; i<userDrinkingVOList.size(); i++)
                    {
                        DrinkingVO vo=userDrinkingVOList.get(i);

                        String date=vo.getDrinking_date().substring(5); //년도 잘라내기
                        date=date.substring(0,2); //달만 가져오기

                        monthSumList.add(new GraphVO((float)vo.getAlcohol_content(), date));
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
                            //sum+=monthSumList.get(i).getData_float();
                            //div++;

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
                        GraphFragment.sum_calorieListYear=monthSumListRes;
                    }
                }

                @Override
                public void onFailure(Call<List<DrinkingVO>> call, Throwable t)
                {
                    System.out.println("LastYearTotalBac error>>>>>>>>>>>>>>>>>>"+t.toString());
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
