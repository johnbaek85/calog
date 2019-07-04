package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {

    //Button btnSave, btnReset 추가
    Button btnSave, btnReset;

    //ImageView prevback 추가
    ImageView prevback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

       prevback = findViewById(R.id.prevback);

       prevback.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
               startActivity(intent);
           }
       });

       //Spinner 시작
       Spinner spin = findViewById(R.id.spinner);
       spin.setPrompt("이메일을 고르세요");

       final ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.email, android.R.layout.simple_spinner_item);
       adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
       spin.setAdapter(adapter);
       //Spinner 끝

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
