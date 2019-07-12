package com.example.calog.Fitness;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;

/*
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.calog.R;

import java.util.ArrayList;

public class Fitness_Fragment_Step extends Fragment {
 //   TextView gx, gy, gz;
    TextView txtStep, txtSensitive, consumedCal, txtdistance;
    Button btnReset;
    SeekBar seekBar;
    SensorManager sm;
    int threshold;
    float acceleration;
    float previousY, currentY;
    int steps;
    double doubleCal;
    String strCal;
    double weight=75;
    ArrayList<Location> array;
    String strDistance;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_exercise, container, false);

        txtdistance = view.findViewById(R.id.distance);
        txtStep=view.findViewById(R.id.stepCount);
        consumedCal=view.findViewById(R.id.usedCalorie);
 /*       gx=view.findViewById(R.id.gx);
        gy=view.findViewById(R.id.gy);
        gz=view.findViewById(R.id.gz);
        seekBar=view.findViewById(R.id.seekBar);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            //시크바로 문턱값(스레시홀드)을 지정한다.
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold=seekBar.getProgress();
                txtSensitive.setText(String.valueOf(threshold));
            }
        });

        */
        threshold=5;
 //       txtSensitive.setText(String.valueOf(threshold));
        previousY = currentY = steps = 0;
        acceleration=0.0f;
//가속도계에 이벤트 리스너를 등록
        sm=(SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(stepDetector, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager. SENSOR_DELAY_NORMAL);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                steps=0;
                txtStep.setText(String.valueOf(steps));
                Toast.makeText(getActivity(), "초기화 완료", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    //무명 클래스를 이용하여서 이벤트 리스너를 구현
    private SensorEventListener stepDetector=new SensorEventListener(){
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
        public void onSensorChanged(SensorEvent event) {
            float x=event.values[0];
            float y=event.values[1];
            float z=event.values[2];
            currentY=y;
//단순히 y방향 가속도의 상대적인 크기가 일정 한계를 넘으면 걸음수를 증가한다.
            if(Math.abs(currentY-previousY) > threshold){
                steps++;
                txtStep.setText(String.valueOf(steps));
                doubleCal = steps*(weight*0.00035);
                strCal = String.format("%.2f", doubleCal);
                //strCal=String.format("%.2f", String.valueOf(doubleCal));
                consumedCal.setText("소모 칼로리 : "+strCal+" kcal");
            }
            previousY=y;

  /*
            if(array.size()>1) {
                double eachDistance=0;
                double distance = 0.0;
                for (int i = 0; i < array.size()-1; i++) {
                    eachDistance = array.get(i).distanceTo(array.get(i + 1));
                    distance += eachDistance;
                }
                strDistance = String.format("%.1f",distance);
                txtdistance.setText("이동거리 : "+strDistance+"meter");
            }
    */
        }
    };


}