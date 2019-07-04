package com.example.calog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExerciseActivity extends AppCompatActivity {
    ImageView btnBack;
    Button stopBtn;
    TextView timer;
    int fitnessTypeId=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_exercise);

        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, SearchFitnessActivity.class);
                startActivity(intent);
            }
        });
        timer=findViewById(R.id.timer);
        stopBtn=findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            final LinearLayout resultLayout = (LinearLayout)View.inflate(ExerciseActivity.this, R.layout.result_exercise, null);
            @Override
            public void onClick(View v) {
                AlertDialog.Builder resultBox = new AlertDialog.Builder(ExerciseActivity.this);
                resultBox.setTitle("운동결과화면");
                resultBox.setView(resultLayout);
                resultBox.setNegativeButton("저장 안함", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(ExerciseActivity.this, "기록 저장 취소", Toast.LENGTH_SHORT).show();
                    }
                });
                resultBox.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       Toast.makeText(ExerciseActivity.this, "운동기록을 저장합니다.", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(ExerciseActivity.this, FitnessActivity.class);
                       startActivity(intent);
                    }
                });
                resultBox.show();
            }
        });




    }
}
