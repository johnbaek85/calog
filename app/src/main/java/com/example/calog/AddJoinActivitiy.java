package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddJoinActivitiy extends AppCompatActivity {

    Button addJoin, addReset;
    ImageView prevback;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_join_activitiy);

        //상단 좌측 뒤로가기 클릭했을때
        prevback = findViewById(R.id.prevback);

        prevback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddJoinActivitiy.this, MainJoinActivity.class);
                startActivity(intent);
            }
        });

        //addJoin 클릭했을때
        addJoin = findViewById(R.id.addJoin);

        addJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddJoinActivitiy.this, MainHealthActivity.class);
                Toast.makeText(AddJoinActivitiy.this, "추가개인정보가 입력되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //addReset 클릭했을때
        addReset = findViewById(R.id.addReset);

        addReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddJoinActivitiy.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

    }
}

