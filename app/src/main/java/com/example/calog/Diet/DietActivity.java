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

    List<UserTotalCaloriesViewVO> userTotalCaloriesViewVOList;

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

        ArrayList<Float> sum_calorieList;



        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            sum_calorieList=new ArrayList<Float>();

            Call<List<UserTotalCaloriesViewVO>> call = rs.UserTotalCaloriesViewVO("spider");
            call.enqueue(new Callback<List<UserTotalCaloriesViewVO>>() {

                @Override
                public void onResponse(Call<List<UserTotalCaloriesViewVO>> call, Response<List<UserTotalCaloriesViewVO>> response)
                {
                    userTotalCaloriesViewVOList=response.body();

                    for(int i=0; i<userTotalCaloriesViewVOList.size(); i++)
                    {
                        UserTotalCaloriesViewVO vo = userTotalCaloriesViewVOList.get(i);

                        sum_calorieList.add((float)vo.getSum_calorie());
                    }

                    GraphFragment.sum_calorieList2=sum_calorieList;


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //TODO 그래프 BarChart Fragment 장착
                            FragmentManager fm=getSupportFragmentManager();
                            FragmentTransaction tr=fm.beginTransaction();

                            GraphPagerFragment graphFragment = new GraphPagerFragment();
                            tr.replace(R.id.barChartFrag,graphFragment);
                            ////////////////////////
                        }
                    }, 3000 );




                    //System.out.print("===DietAcitivity GraphFragment.sum_calorieList2"+GraphFragment.sum_calorieList2);
                    //System.out.println("userTotalCaloriesViewVOList==================="+userTotalCaloriesViewVOList.toString());
                }

                @Override
                public void onFailure(Call<List<UserTotalCaloriesViewVO>> call, Throwable t) {
                    System.out.println("error >>>>>>>>>>>>>>>>>>>>>>>>>>>"+t.toString());
                }
            });


        }

        @Override
        protected ArrayList<GraphFragment> doInBackground(Integer... integers)
        {

//
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            System.out.println("===================================================="+GraphFragment.sum_calorieList2);



            ArrayList<GraphFragment> graphFragmentList=new ArrayList<>();
            //GraphFragment.sum_calorieList2=sum_calorieList;
//            graphFragmentList.add(new GraphFragment("day",sum_calorieList));
//            graphFragmentList.add(new GraphFragment("day",sum_calorieList));
//            graphFragmentList.add(new GraphFragment("day",sum_calorieList));
//            graphFragmentList.add(new GraphFragment("day",sum_calorieList));


            return graphFragmentList;
        }

        @Override
        protected void onPostExecute(ArrayList<GraphFragment> graphFragments)
        {
            super.onPostExecute(graphFragments);



        }

    }

}
