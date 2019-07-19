package com.example.calog.Diet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.CalendarActivity;
import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.Common.GraphVO;
import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.DietFourMealTotalVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;
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

public class DietActivity extends AppCompatActivity {

    Intent intent;

    TextView txtMorningMeal, txtAfternoonMeal, txtEveningMeal, txtSideMeal;
    TextView txtDate;
    Button btnDetailView;

    Retrofit retrofit;
    RemoteService rs;
    List<DietFourMealTotalVO> dailyCalorie;

    ImageView btnBack;

    UserVO user;

    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

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
                intent = new Intent(DietActivity.this, MainJoinActivity.class);
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

                        intent = new Intent(DietActivity.this, UpdateUserInfoActivity.class);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("회원탈퇴");
                builder.setMessage("탈퇴하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = rs.deleteUser(strUser_id);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(DietActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(DietActivity.this, MainJoinActivity.class);
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
                        Toast.makeText(DietActivity.this,
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
        setContentView(R.layout.activity_diet);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        //TODO Graph backthread
        GraphBackThread graphBackThread=new GraphBackThread();
        graphBackThread.execute();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        txtMorningMeal = findViewById(R.id.txtMorningMeal);
        txtAfternoonMeal = findViewById(R.id.txtAfternoonMeal);
        txtEveningMeal = findViewById(R.id.txtEveningMeal);
        txtSideMeal = findViewById(R.id.txtSideMeal);

        btnDetailView = findViewById(R.id.btnDetailView);
        btnDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DietActivity.this, DietDailyDetailActivity.class);
                intent.putExtra("user_id", user_id.getText().toString());
                intent.putExtra("select_date", txtDate.getText().toString());

                startActivity(intent);
            }
        });

        SlidingDrawer dietDrawer = findViewById(R.id.dietDrawer);
        dietDrawer.animateClose();

        Call<List<DietFourMealTotalVO>> call = rs.userDietDailyCalorie(intent.getStringExtra("user_id"), intent.getStringExtra("select_date"));
        call.enqueue(new Callback<List<DietFourMealTotalVO>>() {
            @Override
            public void onResponse(Call<List<DietFourMealTotalVO>> call, Response<List<DietFourMealTotalVO>> response)
            {
                dailyCalorie = new ArrayList<DietFourMealTotalVO>();
                dailyCalorie = response.body();

                try{

                    txtMorningMeal.setText("아침 :   " + dailyCalorie.get(0).getSum_calorie() + "kcal");
                    txtAfternoonMeal.setText("점심 :   " + dailyCalorie.get(1).getSum_calorie() + "kcal");
                    txtEveningMeal.setText("점심 :   " + dailyCalorie.get(2).getSum_calorie() + "kcal");
                    txtSideMeal.setText("저녁 :   " + dailyCalorie.get(3).getSum_calorie() + "kcal");

                }catch (IndexOutOfBoundsException e){
                    System.out.println("<<<<<<<<<<<<<<<<<< Error : "+ e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<DietFourMealTotalVO>> call, Throwable t) {
                System.out.println("<<<<<<<<<<<<<<<<<< Error : "+ t.toString());
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

                        intent = new Intent(DietActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(DietActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                       intent = new Intent(DietActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
                        intent.putExtra("select_date", txtDate.getText().toString());

                       startActivity(intent);
                       break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(DietActivity.this, SleepCheckActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
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
                            Crop.of(uriFile, uriFile).asSquare().start(DietActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    public void mClick(View view) {
        intent = new Intent(DietActivity.this, FoodRegisterActivity.class);
        switch (view.getId()){
            case R.id.btnBreakfast:
                Toast.makeText(DietActivity.this, "아침", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", user_id.getText().toString());
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 1);
                startActivity(intent);
                break;
            case R.id.btnLunch:
                Toast.makeText(DietActivity.this, "점심", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", user_id.getText().toString());
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 2);
                startActivity(intent);
                break;
            case R.id.btnDinner:
                Toast.makeText(DietActivity.this, "저녁", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", user_id.getText().toString());
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 3);
                startActivity(intent);
                break;
            case R.id.btnSnack:
                Toast.makeText(DietActivity.this, "간식", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_id", user_id.getText().toString());
                intent.putExtra("select_date", txtDate.getText().toString());
                intent.putExtra("diet_type_id", 4);
                startActivity(intent);
                break;
            case R.id.btnBack:
                finish();
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

                        System.out.println("UserTotalCaloriesViewVO vo"+vo.getDiet_date());
                        daySumList.add(new GraphVO((float) vo.getSum_calorie(), vo.getDiet_date())); //날짜중 년도를 짤라냄
                    }

                    //TODO 혹시나 모를 데이터 초기화 (다른액티비에서 데이터가없으면 저장된 데이터를 보여주기떄문)
                    GraphFragment.sum_calorieListWeek=new ArrayList<>();

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

                    GraphFragment.sum_calorieListMonth=new ArrayList<>();

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

                    GraphFragment.sum_calorieListYear=new ArrayList<>();

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
}
