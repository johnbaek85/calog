package com.example.calog.Sleeping;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Common.GraphVO;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.WordCloud.WordCloudActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;
import com.example.calog.RemoteService;
import com.example.calog.VO.DrinkingVO;
import com.example.calog.VO.SleepingVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;

import java.io.File;
import java.io.FileOutputStream;
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

public class SleepingActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    TimePicker alarmPicker;
    Context context;
    PendingIntent pendingIntent;
    ImageView btnBack;

    Intent intent;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;


    //DB 용
    Retrofit retrofit;
    RemoteService rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping);

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        //TODO 그래프 스레딩
        GraphBackThread graphBackThread=new GraphBackThread();
        graphBackThread.execute();


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TODO 그래프 BarChart Fragment 장착
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();
        GraphPagerFragment graphFragment = new GraphPagerFragment();
        tr.replace(R.id.barChartFrag,graphFragment);
        //////////////////////////////

        //TODO 하단 메뉴설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.rankingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "랭킹 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(SleepingActivity.this, WordCloudActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(SleepingActivity.this, DrinkingCheckActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        finish();
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(SleepingActivity.this, SleepCheckActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.shareMenu: {
//                         Toast.makeText(MainHealthActivity.this, "공유 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        View rootView = getWindow().getDecorView();
                        screenShot = ScreenShot(rootView);
                        uriFile = Uri.fromFile(screenShot);
                        if(screenShot != null) {
                            Crop.of(uriFile, uriFile).asSquare().start(SleepingActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    public void mClick(View v){
        this.context = getApplicationContext();
        //알람매니저 설정
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //타임피커 설정
        alarmPicker = findViewById(R.id.timepicker);
        //Calendar 객체 설정
        final Calendar calendar = Calendar.getInstance();
        //알림 리시버 설정
        final Intent intent = new Intent(this.context,Alarm_Reciver.class);

        //알람 시작 버튼
        Button alarm_on = findViewById(R.id.btnSleepStart);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //calendar에 시간 셋팅
                calendar.set(Calendar.HOUR_OF_DAY, alarmPicker.getHour());
                calendar.set(Calendar.MINUTE, alarmPicker.getMinute());

                // 시간 가져옴
                int hour = alarmPicker.getHour();
                int minute = alarmPicker.getMinute();
                Toast.makeText(SleepingActivity.this,"기상시간 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                // reveiver에 string 값 넘겨주기
                intent.putExtra("state","alarm on");

                pendingIntent = PendingIntent.getBroadcast(SleepingActivity.this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // 알람셋팅
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);
                //오류 가능
            }
        });
        // 알람 정지 버튼
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_sleep_check,null);

        Button alarm_off = view.findViewById(R.id.btnSleepFinish);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SleepingActivity.this,"Alarm 종료",Toast.LENGTH_SHORT).show();
                // 알람매니저 취소
                alarmManager.cancel(pendingIntent);

                intent.putExtra("state","alarm off");

                // 알람취소
                sendBroadcast(intent);
            }
        });

        Intent gointent = new Intent(SleepingActivity.this, SleepCheckActivity.class);
        startActivity(gointent);
    }

    //TODO 하단 메뉴설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File cropFile = screenShot;

        if(requestCode ==100){
            if (resultCode == RESULT_OK) {
                cropFile = new File(Crop.getOutput(data).getPath());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API 24 이상 일경우..
                uriFile = FileProvider.getUriForFile(getApplicationContext(),
                        getApplicationContext().getPackageName() + ".provider", cropFile);
            } else { // API 24 미만 일경우..
                uriFile = Uri.fromFile(cropFile);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriFile);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "선택"));
        }
    }

    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);

        System.out.println("..........." + filename);
        //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
            os.close();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }


    //그래프 백스레드
    private class GraphBackThread extends AsyncTask<Integer,Integer, ArrayList<GraphFragment>>
    {

        ArrayList<GraphVO> daySumList=new ArrayList<>();
        ArrayList<GraphVO> weekSumList=new ArrayList<>();
        ArrayList<GraphVO> monthSumList=new ArrayList<>();
        ArrayList<GraphVO> yearSumList=new ArrayList<>();

        List<SleepingVO> userSleepVOList=new ArrayList<>();

        @Override
        protected void onPreExecute() {
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
            Call<List<SleepingVO>> call = rs.GraphSleepingData("spider", monday, "week");
            call.enqueue(new Callback<List<SleepingVO>>() {

                @Override
                public void onResponse(Call<List<SleepingVO>> call, Response<List<SleepingVO>> response) {
                    userSleepVOList = response.body();

                    for (int i = 0; i < userSleepVOList.size(); i++) {
                        SleepingVO vo = userSleepVOList.get(i);

                        System.out.println("vo.getSleeping_date()"+vo.getSleeping_date());

                        daySumList.add(new GraphVO((float)((vo.getSleeping_seconds()/3600f)), vo.getSleeping_date())); //날짜중 년도를 짤라냄
                    }

                    //TODO 혹시나 모를 데이터 초기화 (다른액티비에서 데이터가없으면 저장된 데이터를 보여주기떄문)
                    GraphFragment.sum_calorieListWeek = new ArrayList<>();

                    if (daySumList.size() != 0) {
                        GraphFragment.sum_calorieListWeek = daySumList;
                    }
                }

                @Override
                public void onFailure(Call<List<SleepingVO>> call, Throwable t) {
                    System.out.println("LastWeekSleeping error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });


            ////////////////// TODO 해당 date 의 달의 첫일 가져오기
            calendar.set(Calendar.DATE,1);
            String monthFirstDay=formatter.format(calendar.getTime());
            Log.i("monthFirstDay:",monthFirstDay+"");
            ///////

            //TODO 최근 한달간의 데이터 가져오기 - 그래프에서 월에 해당
            Call<List<SleepingVO>> callMonth = rs.GraphSleepingData("spider",monthFirstDay,"month");
            callMonth.enqueue(new Callback<List<SleepingVO>>()
            {

                @Override
                public void onResponse(Call<List<SleepingVO>> call, Response<List<SleepingVO>> response)
                {
                    userSleepVOList = response.body();

                    System.out.println("userSleepVOListMonth"+userSleepVOList);

                    //데이터 파싱
                    for (int i = 0; i < userSleepVOList.size(); i++) {
                        SleepingVO vo = userSleepVOList.get(i);
                        //String date = vo.getDiet_date().substring(5); //년도 잘라내기

                        weekSumList.add(new GraphVO((float)((vo.getSleeping_seconds()/3600f)), vo.getSleeping_date()));
                    }

                    GraphFragment.sum_calorieListMonth=new ArrayList<>();

                    if (weekSumList.size() != 0) {
                        GraphFragment.sum_calorieListMonth = weekSumList;
                    }
                }

                @Override
                public void onFailure(Call<List<SleepingVO>> call, Throwable t)
                {
                    System.out.println("LastMonthSleeping error>>>>>>>>>>>>>>>>>>" + t.toString());
                }
            });


            //캘린더의 정보 가져오기
            String year_date=formatter.format(calendar.getTime());


            //TODO 최근 1년간의 데이터 가져오기 - 그래프에서 년에 해당
            Call<List<SleepingVO>> callYear = rs.GraphSleepingData("spider",monthFirstDay,"month");
            callYear.enqueue(new Callback<List<SleepingVO>>()
            {
                @Override
                public void onResponse(Call<List<SleepingVO>> call, Response<List<SleepingVO>> response)
                {
                    userSleepVOList = response.body();

                    for (int i = 0; i < userSleepVOList.size(); i++) {
                        SleepingVO vo = userSleepVOList.get(i);

                        String date = vo.getSleeping_date().substring(5); //년도 잘라내기
                        date = date.substring(0, 2); //달만 가져오기
                        System.out.println("======================년도정보:"+vo);

                        monthSumList.add(new GraphVO((float)((vo.getSleeping_seconds()/3600f)), date));
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
                public void onFailure(Call<List<SleepingVO>> call, Throwable t)
                {
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

