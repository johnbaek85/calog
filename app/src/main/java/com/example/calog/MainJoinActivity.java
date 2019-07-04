package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainJoinActivity extends AppCompatActivity {
    //추가
    Button btnLogin, btnJoin;
    ImageView prevback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_join);

        //prevback 클릭했을때
        prevback = findViewById(R.id.prevback);

        prevback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainJoinActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        //btnLogin 클릭했을때
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainJoinActivity.this, AddJoinActivitiy.class);
                startActivity(intent);
            }
        });

        //btnJoin 클릭했을때
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainJoinActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });


    }

}
