package com.example.calog.signUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;

public class JoinActivity extends AppCompatActivity {

    //Button btnSave, btnReset 추가
    Button btnSave, btnReset;

    //ImageView back, home 추가
    ImageView back, home;

    //Spinner 추가
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

       back = findViewById(R.id.back);

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
               startActivity(intent);
           }
       });

       // E-mail Spinner 시작
       spin = findViewById(R.id.email);

       ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.email, android.R.layout.simple_spinner_item);
       adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
       spin.setAdapter(adapter);
       // E-mail Spinner 끝

       // Year Spinner 시작
       spin = findViewById(R.id.Year);

       ArrayAdapter adapter4 = ArrayAdapter.createFromResource(this, R.array.Year, android.R.layout.simple_spinner_item);
       adapter4.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
       spin.setAdapter(adapter4);
       // Year Spinner 끝

       // Month Spinner 시작
       spin = findViewById(R.id.Month);

       ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.Month, android.R.layout.simple_spinner_item);
       adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
       spin.setAdapter(adapter1);
       // Month Spinner 끝

       // Day Spinner 시작
       spin = findViewById(R.id.Day);
       spin.setPrompt("월을 골라주세요");

       ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.Day, android.R.layout.simple_spinner_item);
       adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
       spin.setAdapter(adapter2);
       // Day Spinner 끝

        //home 클릭했을때
        home = findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

       //btnSave 클릭했을때
       btnSave = findViewById(R.id.btnSave);

       btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
               Toast.makeText(JoinActivity.this,"회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
               startActivity(intent);
           }
       });

       //btnReset 클릭했을때
       btnReset = findViewById(R.id.btnReset);

       btnReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
               startActivity(intent);
           }
       });


    }


}
