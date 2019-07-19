package com.example.calog.Fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.Common.GraphVO;
import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.FitnessVO;
import com.example.calog.VO.UserVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.example.calog.signUp.MainJoinActivity;
import com.example.calog.signUp.UpdateUserInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
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
    TextView txtDate, txtCardioCal, txtCardioTime, txtCardioDistance, txtWeightCal, txtWeightTime, txtCardioStep;
    ImageView cardioList, weightList;

    //임시 사용자정보, 메인페이지에서 전달받아야함
    String str_user_id;
    String fitness_date;

    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

    //DB 용
    Retrofit retrofit;
    RemoteService rs;

    UserVO user;

    //TODO user용 toolbar 관련
    TextView user_id;
    String strUser_id;
    Toolbar toolbar;
    SharedPreferences pref;
    boolean logInStatus = false;

    //=============TODO 로그인 관련
    //옵션 메뉴 user 로그인 여부
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.loginmenu, menu);
        return true;
    }

    //로그인 상태
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (logInStatus) { // 로그인 한 상태: 로그인은 안보이게, 로그아웃은 보이게
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        } else { // 로그 아웃 한 상태 : 로그인 보이게, 로그아웃은 안보이게
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);

        }

        //logInStatus = !logInStatus;   // 값을 반대로 바꿈

        return super.onPrepareOptionsMenu(menu);
    }

    //로그인, 회원정보 수정, 회원 탈퇴
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.login:
                intent = new Intent(FitnessActivity.this, MainJoinActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Toast.makeText(this, "로그아웃이 완료되었습니다", Toast.LENGTH_SHORT).show();
                //로그인 정보 프레퍼런스에 로그인정보 삭제
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_id", "");
                editor.commit();
                user_id.setText("");
                logInStatus = false;
                break;

            case R.id.adjust:
                Call<UserVO> call = rs.readUser(strUser_id);
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        user = response.body();

                        intent = new Intent(FitnessActivity.this, UpdateUserInfoActivity.class);

                        intent.putExtra("user_id", user.getUser_id());
                        intent.putExtra("password", user.getPassword());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("name", user.getName());
                        intent.putExtra("phone", user.getPhone());
                        intent.putExtra("birthday", user.getBirthday());
                        intent.putExtra("gender", user.getGender());
                        intent.putExtra("height", user.getHeight());
                        intent.putExtra("weight", user.getWeight());
                        intent.putExtra("bmi", user.getBmi());
                        intent.putExtra("address", user.getAddress());

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                    }
                });
                break;

            case R.id.withdraw:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("회원탈퇴");
                builder.setMessage("탈퇴하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = rs.deleteUser(strUser_id);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(FitnessActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(FitnessActivity.this, MainJoinActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                            }
                        });
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FitnessActivity.this,
                                "회원탈퇴가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        //TODO toolbar 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_id = findViewById(R.id.user_id);
        //TODO sharedpreference에서 userid 값 받아옴
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);

        //TODO User Login
        //로그인 정보 프레퍼런스에서 불러오기
        strUser_id = pref.getString("user_id", "");
        user_id.setText(strUser_id);

        if (strUser_id.equals("")) {
            user_id.setText("");
            logInStatus = false;
        } else {
            user_id.setText(strUser_id + "님 환영합니다!");
            logInStatus = true;
        }

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        GraphBackThread graphBackThread=new GraphBackThread();
        graphBackThread.execute();

        intent = getIntent();
        fitness_date = intent.getStringExtra("select_date");
        str_user_id = intent.getStringExtra("user_id");


        txtDate=findViewById(R.id.txtDate);
        txtDate.setText(fitness_date);



//메인페이지로 이동
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
//당일 유산소운동 목록
        cardioList = findViewById(R.id.cardioList);
        cardioList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyFitnessList(1);
            }
        });


//근력운동 목록 출력
        btnWeightTrainingActivity=findViewById(R.id.btnWeightTrainingActivity);
        btnWeightTrainingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FitnessActivity.this, "무산소운동 목록을 출력합니다.", Toast.LENGTH_SHORT).show();
                goToSearchActivity(2);
            }
        });
//당일 근력운동 목록
        weightList = findViewById(R.id.weightList);
        weightList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyFitnessList(2);
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



//유산소 데이터 출력

        Call<FitnessVO> cardioCall = rs.OneDayCardioTotalCalorie(str_user_id, fitness_date);
        cardioCall.enqueue(new Callback<FitnessVO>() {
            @Override
            public void onResponse(Call<FitnessVO> call, Response<FitnessVO> response) {
                FitnessVO vo = response.body();
                System.out.println("vo값 출력" + vo.toString());
                txtCardioCal = findViewById(R.id.CardioCal);
                txtCardioTime = findViewById(R.id.CardioTime);
                txtCardioDistance = findViewById(R.id.CardioDistance);
                txtCardioStep = findViewById(R.id.CardioStep);
                txtCardioCal.setText(vo.getSum_cardio_used_calorie()+"kcal");



                long fitnessTime = vo.getSum_cardio_seconds();

                    int fth = (int) (fitnessTime / 3600);
                    int ftm = (int) (fitnessTime - fth * 3600) / 60;
                    int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
                    String strH = fth < 10 ? "0" + fth : fth + "";
                    String strM = ftm < 10 ? "0" + ftm : ftm + "";
                    String strS = fts < 10 ? "0" + fts : fts + "";
                    txtCardioTime.setText(strH + "시간 " + strM + "분 " + strS + "초");

                    txtCardioStep.setText(vo.getSum_cardio_number_steps()+"걸음");

                    txtCardioDistance.setText(vo.getSum_cardio_distance()+"m");
            }

            @Override
            public void onFailure(Call<FitnessVO> call, Throwable t) {
                System.out.println("유산소운동 출력 오류" + t.toString());
            }
        });


//무산소운동 데이터 출력
        txtWeightCal = findViewById(R.id.WeightCal);
        txtWeightTime = findViewById(R.id.WeightTime);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs=retrofit.create(RemoteService.class);
        Call<FitnessVO> wieghtCall =rs.OneDayWeightTotalCalorie(str_user_id, fitness_date);
        wieghtCall.enqueue(new Callback<FitnessVO>() {
            @Override
            public void onResponse(Call<FitnessVO> call, Response<FitnessVO> response) {
                FitnessVO vo = response.body();
                txtWeightCal.setText((vo.getSum_weight_used_calorie()+"kcal"));

                long fitnessTime = vo.getSum_weight_seconds();
                    int fth = (int) (fitnessTime / 3600);
                    int ftm = (int) (fitnessTime - fth * 3600) / 60;
                    int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
                    String strH = fth < 10 ? "0" + fth : fth + "";
                    String strM = ftm < 10 ? "0" + ftm : ftm + "";
                    String strS = fts < 10 ? "0" + fts : fts + "";
                    txtWeightTime.setText(strH + "시간 " + strM + "분 " + strS + "초");

            }

            @Override
            public void onFailure(Call<FitnessVO> call, Throwable t) {
                System.out.println("무산소운동 출력 오류" + t.toString());

            }
        });


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

                        Intent intent = new Intent(FitnessActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(FitnessActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(FitnessActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(FitnessActivity.this, SleepCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

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
                            Crop.of(uriFile, uriFile).asSquare().start(FitnessActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });

        //바텀메뉴 초기화
        //BottomMenuClearSelection(bottomNavigationView,false);



    }


    //Activity 이동 Method
    public void goToSearchActivity(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, SearchFitnessActivity.class);
        intent.putExtra("운동타입", fitnessTypeid);
        intent.putExtra("user_id", str_user_id);
        intent.putExtra("select_date", fitness_date);
        startActivity(intent);
    }


    public void goToMyFitnessList(int fitnessTypeid){
        intent = new Intent(FitnessActivity.this, MyFitnessListActivity.class);
        intent.putExtra("운동타입", fitnessTypeid);
        intent.putExtra("select_date", fitness_date);
        intent.putExtra("user_id", str_user_id);
        startActivity(intent);
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

                        /*if(vo.getWeight_fitness_date()!=null)
                        {
                            date = vo.getWeight_fitness_date(); //년도 잘라내기
                        }
                        else
                        {
                            date=vo.getCardio_fitness_date();
                        }*/

                        System.out.println("============================date:"+date);

                        System.out.println("============================weekVOCardiodate"+vo.getCardio_fitness_date());
                        daySumList.add(new GraphVO((float)(vo.getSum_weight_used_calorie() + vo.getSum_cardio_used_calorie()),vo.getFitness_date()));
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

//                        String date="";
//                        if(vo.getWeight_fitness_date()!=null)
//                        {
//                            date = vo.getWeight_fitness_date();
//                        }
//                        else
//                        {
//                            date=vo.getCardio_fitness_date();
//                        }

                        weekSumList.add(new GraphVO((float)(vo.getSum_weight_used_calorie() + vo.getSum_cardio_used_calorie()), vo.getFitness_date()));
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

                        String date=vo.getFitness_date().substring(5);
//                        if(vo.getWeight_fitness_date()!=null)
//                        {
//                            date = vo.getWeight_fitness_date().substring(5); //년도 잘라내기
//                        }
//                        else
//                        {
//                            date=vo.getCardio_fitness_date().substring(5);
//                        }
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
                if(monthSumList.size()!=0)
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
