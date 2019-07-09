package com.example.calog.signUp;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;


public class JoinActivity extends AppCompatActivity {

    //Button btnSave, btnReset 추가
    Button btnSave, btnReset;

    //ImageView back, home 추가
    ImageView back, home;

    //Spinner 추가
    Spinner spin;

    //item 값의 nameID 추가
    ClipData.Item nameID;

    EditText user_Id, password, checkPassword,
            emailId, name,
            phone1, phone2, phone3,
            height, weight,

            post, address, detailaddress;

    Spinner email, Year, Month, Day;

    CheckBox Man, Girl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        user_Id = findViewById(R.id.user_Id);
        password = findViewById(R.id.password);
        checkPassword = findViewById(R.id.checkPassword);
        emailId = findViewById(R.id.emailId);
        name = findViewById(R.id.name);
        phone1 = findViewById(R.id.phone1);
        phone2 = findViewById(R.id.phone2);
        phone3 = findViewById(R.id.phone3);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        post = findViewById(R.id.post);
        address = findViewById(R.id.address);
        detailaddress = findViewById(R.id.detailaddress);
        email = findViewById(R.id.email);
        Year = findViewById(R.id.Year);
        Month = findViewById(R.id.Month);
        Day = findViewById(R.id.Day);
        Man = findViewById(R.id.Man);
        Girl = findViewById(R.id.Girl);

        // '비밀번호' 설정 ( 조건에 따른 색상 변경및 안내메세지 )
        password.addTextChangedListener(new TextWatcher() {
            @Override
            // 입력하기 전에 호출되는 API
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            // EditText에 변화가 있을 떄
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().length() >= 8 && password.getText().toString().equals(checkPassword.getText().toString())) {
                    //비밀번호확인이 8자리 이상이고 비밀번호와 같을때 검은색
                    password.setTextColor(Color.BLACK);
                    checkPassword.setTextColor(Color.BLACK);
                } else {
                    //비밀번호확인을 8자리 미만으로 입력하면은 빨강색
                    password.setTextColor(Color.RED);
                    checkPassword.setTextColor(Color.RED);
                }
            }
            @Override
            // 입력이 끝났을 때
            public void afterTextChanged(Editable s) {
            }
        });

        // '비밀번호확인' 설정( 조건에 따른 색상 변경및 안내메세지 )
        checkPassword.addTextChangedListener(new TextWatcher() {
            @Override
            // 입력하기 전에 호출되는 API
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            // EditText에 변화가 있을 떄
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(checkPassword.getText().toString().length() >= 8 && checkPassword.getText().toString().equals(password.getText().toString())){
                    //비밀번호확인이 8자리 이상이고 비밀번호와 같을때 검은색
                    checkPassword.setTextColor(Color.BLACK);
                    password.setTextColor(Color.BLACK);
                }else{
                    //비밀번호확인을 8자리 미만으로 입력하면은 빨강색
                    checkPassword.setTextColor(Color.RED);
                    password.setTextColor(Color.RED);
                }
            }
            @Override
            // 입력이 끝났을 때
            public void afterTextChanged(Editable s) {
            }
        });

        // EditText 'password'에서 다른곳으로 이동할때 발생하는 이벤트
        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(password.getText().toString().length() < 8 && password.getText().toString().length() > 0){
                    Toast.makeText(JoinActivity.this, "8자리 이상 입력하여주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EditText 'checkPassword'에서 다른곳으로 이동할때 발생하는 이벤트
        checkPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(checkPassword.getText().toString().length() < 8 && checkPassword.getText().toString().length() > 0){
                    Toast.makeText(JoinActivity.this, "8자리 이상 입력하여주세요", Toast.LENGTH_SHORT).show();
                }else if(checkPassword.getText().toString().length() >= 8 && password.getText().toString().equals(checkPassword.getText().toString())){
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
                startActivity(intent);
            }
        });

        // Item의 직접입력 값 누를때의 Action시작

        // E-mail Spinner 시작
        spin = findViewById(R.id.email);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.email, android.R.layout.simple_spinner_item);
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
                Toast.makeText(JoinActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
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
