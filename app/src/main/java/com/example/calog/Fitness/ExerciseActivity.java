package com.example.calog.Fitness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Fitness.*;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;

import java.util.logging.LogRecord;

public class ExerciseActivity extends AppCompatActivity {
    ImageView btnBack, btnMAinShortcut;
    Intent intent;
    Chronometer timeElapse;

    int fitness_type_id;
    int fitness_menu_id;
    double fitness_unit_calorie;

    //총운동시간(초)
    int calorieSecond;
    //총소모칼로리
    double totalCalorie;

    long time;
    long stopTime=0;
    int h;
    int m;
    int s;
    String hh;
    String mm;
    String ss;
    BackThread thread;

    TextView usedCalorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_exercise);

                intent = getIntent();
                fitness_type_id = intent.getIntExtra("운동타입", 0);
                fitness_menu_id = intent.getIntExtra("운동명", 0);
                fitness_unit_calorie = intent.getDoubleExtra("단위칼로리", 0.0);
                System.out.println("운동타입" + fitness_type_id+"운동명" +fitness_menu_id+ "단위칼로리 = "+fitness_unit_calorie);

                openImageFrame();
                openButtonFrame();
                usedCalorie=findViewById(R.id.usedCalorie);
                thread = new BackThread();
                thread.setDaemon(true);



//뒤로가기
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//메인페이지로 이동
        btnMAinShortcut = findViewById(R.id.btnMAinShortcut);
        btnMAinShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExerciseActivity.this, "메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(ExerciseActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });


        timeElapse = (Chronometer)findViewById(R.id.chronometer);

        timeElapse.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time = SystemClock.elapsedRealtime() - chronometer.getBase();
                h = (int)(time/36000000);
                m = (int)(time - h*3600000)/60000;
                s= (int)(time - h*3600000- m*60000)/1000 ;
                hh = h < 10 ? "0"+h: h+"";
                mm = m < 10 ? "0"+m: m+"";
                ss = s < 10 ? "0"+s: s+"";
                chronometer.setText(hh+":"+mm+":"+ss);
            }
        });
        timeElapse.setText("00:00:00");

    }



    class BackThread extends Thread{
        public void run(){
            while(true){
                calorieCalculator();
                handler.sendEmptyMessage(0);
                try{
                    Thread.sleep(1000);
                }catch(Exception e){  }
            }
        }
    }

    android.os.Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==0){
                String strCalorie = String.format("%.1f",totalCalorie);
                usedCalorie.setText("소모칼로리 : "+strCalorie+" kcal");
            }
            super.handleMessage(msg);
        }
    };








    //운동 중 시작, 중지 버튼 선택시
    public void mClick(View view){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr;

        switch(view.getId()){
            case R.id.btnStart:
                timeElapse.setBase(SystemClock.elapsedRealtime());
                btnStartandStop();
                timeElapse.start();
                thread.start();
                break;

            case R.id.btnStop:
                timeElapse.stop();
                stopTime = SystemClock.elapsedRealtime()-timeElapse.getBase();
//                System.out.println("정지시간 : "+(int)(stopTime - h*3600000- m*60000)/1000);

                tr=fm.beginTransaction();
                Fitness_Fragment_StopWatchContinue fragmentContinue = new Fitness_Fragment_StopWatchContinue();
                tr.replace(R.id.btnFrame, fragmentContinue, "계속");
                tr.commit();
                break;

            case R.id.btnContinue:
                btnStartandStop();
                timeElapse.setBase(SystemClock.elapsedRealtime()-stopTime);
 //               System.out.println("stopTime = "+ stopTime);
                timeElapse.start();
                break;

            case R.id.btnFinish:

                final LinearLayout resultLayout = (LinearLayout)View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
                final AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);
/*               stopTime =SystemClock.elapsedRealtime()-timeElapse.getBase();
                System.out.println("완료 : "+ (int)(stopTime - h*3600000- m*60000)/1000);

                int bh = (int)(stopTime/36000000);
                int bm = (int)(stopTime - bh*3600000)/60000;
                int bs= (int)(stopTime - bh*3600000- bm*60000)/1000 ;
                String tbh = bh < 10 ? "0"+bh: bh+"";
                String tbm = bm < 10 ? "0"+bm: bm+"";
                String tbs = bs < 10 ? "0"+bs: bs+"";
*/
                TextView txtResultTime = resultLayout.findViewById(R.id.resultTime);
                txtResultTime.setText((hh)+"시간 "+(mm)+"분 "+(ss)+"초");
//                TextView txtBreakTime = resultLayout.findViewById(R.id.breakTime);
//                txtBreakTime.setText((tbh)+"시간 "+(tbm)+"분 "+(tbs)+"초");
                TextView txtTotalUsedCalorie = resultLayout.findViewById(R.id.totalUsedCal);
                String strCalorie = String.format("%.1f",totalCalorie);
                txtTotalUsedCalorie.setText(strCalorie+"kcal");

                resultBox.setIcon(R.drawable.ic_fitness_center_black_24dp);
                resultBox.setTitle("운동결과화면");
                resultBox.setView(resultLayout);
                resultBox.setNegativeButton("저장 안함", null);
                resultBox.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(ExerciseActivity.this, "운동기록을 저장합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ExerciseActivity.this, FitnessActivity.class);
                        startActivity(intent);
                        timeElapse.setBase(SystemClock.elapsedRealtime());
                    }
                });
                resultBox.show();
                break;
        }
    }



    public void btnStartandStop(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();
        Fitness_Fragment_StopWatchStop fragmentStop = new Fitness_Fragment_StopWatchStop();
        tr.replace(R.id.btnFrame, fragmentStop, "일시정지");
        tr.commit();
    }





    public void openImageFrame(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr =fm.beginTransaction();
        switch (fitness_type_id){
            case 1: //유산소 운동이면 gps프레그먼트
                Fitness_Fragment_GPS fragment_gps = new Fitness_Fragment_GPS(fitness_menu_id);
                tr.add(R.id.exerciseFrame, fragment_gps, "gps");
                tr.commit();
                break;
            case 2: //무산소 운동이면 gif프레그먼트
                Fitness_Fragment_GIF fragment_gif = new Fitness_Fragment_GIF(fitness_menu_id);
                tr.add(R.id.exerciseFrame, fragment_gif, "gif");
                tr.commit();
                break;
        }
    }
    public void openButtonFrame(){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction tr =fm.beginTransaction();
                Fitness_Fragment_StopWatchStart fragmentStart = new Fitness_Fragment_StopWatchStart();
                tr.add(R.id.btnFrame, fragmentStart, "시작");
                tr.commit();
    }

    public void calorieCalculator(){
        calorieSecond = (int)time/1000;
                totalCalorie=calorieSecond* fitness_unit_calorie;

    }



}
