package com.example.calog.signUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calog.JoinRemoteService;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.VO.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.JoinRemoteService.BASE_URL;


public class JoinActivity extends AppCompatActivity {

    JoinRemoteService rs;
    UserVO user;
    Retrofit retrofit;

    //Button btnSave, btnReset 추가
    Button btnSave, btnReset;

    //ImageView back, home 추가
    ImageView back, home;


    EditText user_Id, password, checkPassword,
             email, name,
             phone, gender, birthday,
             height, weight, address;

    String strId, strPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        retrofit = new Retrofit.Builder()                           // Retrofit 빌더 생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(JoinRemoteService.class);              // API 인터페이스 생성

        user_Id = findViewById(R.id.user_Id);
        password = findViewById(R.id.password);
        checkPassword = findViewById(R.id.checkPassword);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        birthday = findViewById(R.id.birthday);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        address = findViewById(R.id.address);
        gender = (EditText)findViewById(R.id.gender);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);

        // 프레퍼런스
        SharedPreferences pref = getSharedPreferences("prefTest", 0);

        strId = pref.getString("Id", "아이디");
        user_Id.setText(strId);
        strPassword = pref.getString("password", "비밀번호");
        password.setText(""+strPassword);

        // '아이디' 설정 ( 조건에 따른 색상 변경및 안내메세지 )
        user_Id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(user_Id.getText().toString().trim().length() <= 20){
                    user_Id.setTextColor(Color.GREEN);
                }else{
                    user_Id.setTextColor(Color.RED);
                    Toast.makeText(JoinActivity.this, "아이디는 20자리 이하로 입력해주세요", Toast.LENGTH_LONG).show();
                    user_Id.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // '비밀번호' 설정 ( 조건에 따른 색상 변경및 안내메세지 )
        password.addTextChangedListener(new TextWatcher() {

            @Override
            // 입력하기 전에 호출되는 API
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // EditText에 변화가 있을 떄
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().trim().length() >= 8 && password.getText().toString().equals(checkPassword.getText().toString())) {
                    //비밀번호확인이 8자리 이상이고 비밀번호와 같을때 검은색
                    password.setTextColor(Color.GREEN);
                    checkPassword.setTextColor(Color.GREEN);
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
                    checkPassword.setTextColor(Color.GREEN);
                    password.setTextColor(Color.GREEN);
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

        // '이메일아이디' 설정( 조건에 따른 색상 변경및 안내메세지 )
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.getText().toString().length() <= 20){
                    email.setTextColor(Color.GREEN);
                }else{
                    email.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // '번호1' 설정( 조건에 따른 색상 변경및 안내메세지 )
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // phone1이 3자리수면 연두색 아니면 빨강색
                if(phone.getText().toString().length() == 11){
                    phone.setTextColor(Color.GREEN);
                }else{
                    phone.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone.getText().toString().length() == 8){
                    phone.setTextColor(Color.GREEN);
                }else{
                    phone.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // EditText 'password'에서 다른곳으로 이동할때, 'password'를 클릭할 때 발생하는 이벤트
        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(password.getText().toString().length() < 8 && password.getText().toString().length() > 0){
                    Toast.makeText(JoinActivity.this, "8자리 이상 입력하여주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EditText 'checkPassword'에서 다른곳으로 이동할때, 'checkPassword'를 클릭할 때 발생하는 이벤트

        checkPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (checkPassword.getText().toString().length() < 8 && checkPassword.getText().toString().length() > 0) {
                    Toast.makeText(JoinActivity.this, "8자리 이상 입력하여주세요", Toast.LENGTH_SHORT).show();
                } else if (checkPassword.getText().toString().length() >= 8 && password.getText().toString().equals(checkPassword.getText().toString())) {
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // back 클릭했을때 MainJoinActivity로 이동
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
                startActivity(intent);
            }
        });

        // home 클릭했을때 MainHealthActivity로 이동
        home = findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user = new UserVO();

                user.setUser_id(user_Id.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setAddress(address.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setGender(gender.getText().toString());
                user.setHeight(Double.parseDouble(height.getText().toString()));
                user.setWeight(Double.parseDouble(weight.getText().toString()));
                user.setBmi((Double.parseDouble(weight.getText().toString())/(Double.parseDouble(height.getText().toString()))/(Double.parseDouble(height.getText().toString())))*10000);

                Call<Void> call = rs.insertUser(user);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(JoinActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

                // 빈값일 때 Toast띄우고 입력하게 하기 시작
                if(user_Id.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    user_Id.requestFocus();
                    return;
                }

                if(user_Id.getText().toString().length() > 20){
                    Toast.makeText(JoinActivity.this, "아이디는 20자 이하로 입력해주세요", Toast.LENGTH_SHORT).show();
                    user_Id.requestFocus();
                    return;
                }

                if(password.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if(checkPassword.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "비밀번호확인을 입력해주세요", Toast.LENGTH_SHORT).show();
                    checkPassword.requestFocus();
                    return;
                }
                
                if(name.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                
                if(phone.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                    return;
                }

                if(!password.getText().toString().equals(checkPassword.getText().toString())){
                    Toast.makeText(JoinActivity.this, "비밀번호와 비밀번호확인이 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if(birthday.getText().toString().length() != 8){
                    Toast.makeText(JoinActivity.this, "년도와 개월 일자를 8자리로 적어주세요", Toast.LENGTH_SHORT).show();
                    birthday.requestFocus();
                    return;
                }
                // 빈값일 때 Toast띄우고 입력하게 하기 끝

                // btnSave 클릭했을때 MainJoinActivity로 이동
                Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
                Toast.makeText(JoinActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // btnReset 클릭했을때 MainJoinActivity로 이동
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, MainJoinActivity.class);
                startActivity(intent);

            }
        });


    }

    // 프레퍼런스
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("prefTest",0);
        SharedPreferences.Editor edit = pref.edit();
        strId = user_Id.getText().toString();
        strPassword = password.getText().toString();
        edit.putString("Id", strId);
        edit.putString("password", strPassword);
        edit.commit();
        Toast.makeText(JoinActivity.this, "onPause", Toast.LENGTH_SHORT).show();
    }

}
