package com.example.calog.Diet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.example.calog.Common.GraphFragment;
import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.MainHealthVO;
import com.example.calog.VO.UserTotalCaloriesViewVO;

import java.util.ArrayList;
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

    private class GraphBackThread extends AsyncTask<Integer,Integer, ArrayList<GraphFragment>>
    {

        ArrayList<Float> daySumList=new ArrayList<>();
        ArrayList<Float> weekSumList=new ArrayList<>();
        ArrayList<Float> monthSumList=new ArrayList<>();
        ArrayList<Float> yearSumList=new ArrayList<>();

        List<UserTotalCaloriesViewVO> userTotalCaloriesViewVOList=new ArrayList<>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            Call<List<UserTotalCaloriesViewVO>> call = rs.UserTotalCaloriesViewVO("spider");
            call.enqueue(new Callback<List<UserTotalCaloriesViewVO>>() {

                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response)
                {
                    userTotalCaloriesViewVOList=response.body();

                    for(int i=0; i<userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo = userTotalCaloriesViewVOList.get(i);

                        daySumList.add((float)vo.getSum_calorie());
                    }

                    weekSumList.add(10.0f);


                    //FIXME 기타 잡다한 오류로 bundle및 생성자로 데이터를 못던졌음. 연구 필요. 임시방편으로 static으로 하면 데이터 전송가능.
                    GraphFragment.sum_calorieListDay=daySumList; //일에 대한 데이터
                   // GraphFragment.sum_calorieListWeek=daySumList; //주에 대한 데이터
                   // GraphFragment.sum_calorieListMonth=daySumList; //달에 대한 데이터
                   // GraphFragment.sum_calorieListYear=daySumList; //년에 대한 데이터

                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t) {
                    System.out.println("error >>>>>>>>>>>>>>>>>>>>>>>>>>>"+t.toString());
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
                if(userTotalCaloriesViewVOList.size()!=0)
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
