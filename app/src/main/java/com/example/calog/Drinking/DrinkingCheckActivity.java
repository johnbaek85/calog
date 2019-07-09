package com.example.calog.Drinking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.driver.UsbSerialDriver;
import com.example.calog.Drinking.driver.UsbSerialPort;
import com.example.calog.Drinking.driver.UsbSerialProber;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;

import java.io.IOException;
import java.util.List;

public class DrinkingCheckActivity extends AppCompatActivity
{

    private ProgressBar circleProgress;

    private  TextView checkText=null;

    private  int value;

    private boolean isClick=true;

    //아두이노 연결용 객체
    private ActivityHandler mHandler = null;
    private SerialListener mListener = null;
    private SerialConnector mSerialConn = null;
    private Context mContext = null;  //context

    //아두이노 결과값
    private String resultA;

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

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        //시리얼 연결 종료
        mSerialConn.finalize();
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

            //TODO 아두이노 통신 백그라운드 실행///
            mListener = new SerialListener();
            mHandler = new ActivityHandler(); //스레드와 같은 클래스

            mSerialConn = new SerialConnector(getApplicationContext(), mListener, mHandler); //컨텍스트, 두번쨰는 로그용,세번쨰는 찍을데이터 장소(handler)
            mSerialConn.initialize(); //시작
            ///////////////////////////////
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
                    //circleProgress.setProgress(value);
                    //TODO 실시간 Ui변경은 이곳에서 하지말라고하여 onProgressUpdate로 대체
                    publishProgress(value);

                }

                //로딩바 시간
                try
                {
                    Thread.sleep(35);
                }
                catch (InterruptedException ex) {}
            }

            return value;
        }

        //TODO publish progress 함수를 통해 호출됨.
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            circleProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);
            circleProgress.setProgress(0);
            //checkText.setText("결과:"+String.valueOf(numBytesRead)+"");
            checkText.clearAnimation();

            LinearLayout linearLayout=findViewById(R.id.confirm);

            linearLayout.setVisibility(View.VISIBLE);

            //취소시 결과화면으로
            Button cancelBtn = findViewById(R.id.cancelBtn);


            //시리얼 연결 종료
            mSerialConn.finalize();

            //결과데이터 출력
            checkText.setText(resultA);

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

    ////////////////////////////////////////////////////////// 아두이노와 통신하는 백그라운드 객체

    public class ActivityHandler extends Handler //핸들러란 다른 객체들이 보낸 데이터를 받고 이 데이터를 처리하는 객체입니다.
    {
        @Override
        public void handleMessage(Message msg) //들어오는 값에따라 메시지가 변하게 하기위한 핸들링
        {
            switch(msg.what)
            {
                case Constants.MSG_READ_DATA:
                    //실제로 들어온 아두이노 데이터 작업
                    if(msg.obj != null)
                    {
                        resultA=(String)msg.obj;
                    }
                    break;
            }
        }
    }

    public class SerialListener
    {
        public void onReceive(int msg, int arg0, int arg1, String arg2, Object arg3)
        {
            switch(msg)
            {
                case Constants.MSG_READ_DATA:
                    if(arg3 != null)
                    {
                        resultA=(String)arg3;
                    }
                    break;
            }
        }
    }
}
