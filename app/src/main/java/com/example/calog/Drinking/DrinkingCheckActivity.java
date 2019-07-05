package com.example.calog.Drinking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;

public class DrinkingCheckActivity extends AppCompatActivity
{

    private ProgressBar circleProgress;

    private  TextView checkText;

    private  int value;

    private boolean isClick=true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinkingcheck);

        //TODO 뒤로가기 이벤트
        ImageView btnBack= findViewById(R.id.btnBack);
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
                Intent intent=new Intent(DrinkingCheckActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        circleProgress=findViewById(R.id.circleProgress);
        checkText=findViewById(R.id.checkText);

        //클릭시 알콜측정 이벤트
        checkText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isClick)
                {
                    UIBackThread uiBack = new UIBackThread();
                    uiBack.execute();

                    isClick=false;
                }
            }
        });
    }

    private class UIBackThread extends AsyncTask<Integer, Integer, Integer>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            value = 0;
            circleProgress.setProgress(value);

            //측정중 글자 애니메이션 처리
            checkText.setText("측정중입니다...");
            Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
            checkText.startAnimation(startAnimation);
        }

        @Override
        protected Integer doInBackground(Integer... integers)
        {
            //progress 값을 실시간으로 넣어줌
            while (isCancelled() == false)
            {
                value++;
                if (value >= 100) //100일떄 빠져나감
                {
                    break;
                }
                else
                {
                    circleProgress.setProgress(value);
                }

                try
                {
                    Thread.sleep(5);
                }
                catch (InterruptedException ex) {}
            }

            return value;
        }


        @Override
        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);
            circleProgress.setProgress(0);
            checkText.setText("결과.");
            checkText.clearAnimation();

            LinearLayout linearLayout=findViewById(R.id.confirm);

            linearLayout.setVisibility(View.VISIBLE);


            //취소시 결과화면으로
            Button cancelBtn = findViewById(R.id.cancelBtn);

            cancelBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getApplicationContext(), "결과 통계 화면으로 이동", Toast.LENGTH_SHORT).show();
                }
            });

            //저장시 DB에 데이터 넣기
            Button saveBtn=findViewById(R.id.saveBtn);
            saveBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getApplicationContext(), "DB에 데이터 저장", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
