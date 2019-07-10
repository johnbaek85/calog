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

import com.example.calog.JoinRemoteService;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.VO.UserVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;


public class JoinActivity extends AppCompatActivity {

    Retrofit retrofit;
    JoinRemoteService rs;
    List<UserVO> user;

    //Button btnSave, btnReset 추가
    Button btnSave, btnReset, btnPost;

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
             post, address, detailAddress;
    Spinner  email, year, month, day;
    CheckBox man, girl;

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
        detailAddress = findViewById(R.id.detailAddress);
        email = findViewById(R.id.email);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        man = findViewById(R.id.man);
        girl = findViewById(R.id.girl);
        btnPost = findViewById(R.id.btnPost);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);

        String E_mail = emailId;
        +=  "@"+email;

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
        emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailId.getText().toString().length() <= 20){
                    emailId.setTextColor(Color.GREEN);
                }else{
                    emailId.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // '번호1' 설정( 조건에 따른 색상 변경및 안내메세지 )
        phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // phone1이 3자리수면 연두색 아니면 빨강색
                if(phone1.getText().toString().length() == 3){
                    phone1.setTextColor(Color.GREEN);
                }else{
                    phone1.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // '번호2' 설정( 조건에 따른 색상 변경및 안내메세지 )
        phone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone2.getText().toString().length() == 4){
                    phone2.setTextColor(Color.GREEN);
                }else{
                    phone2.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // '번호3' 설정( 조건에 따른 색상 변경및 안내메세지 )
        phone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone3.getText().toString().length() ==4){
                    phone3.setTextColor(Color.GREEN);
                }else{
                    phone3.setTextColor(Color.RED);
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


        // E-mail Spinner 시작
        spin = findViewById(R.id.email);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.email, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter);
        // E-mail Spinner 끝

        // Year Spinner 시작
        spin = findViewById(R.id.year);

        ArrayAdapter adapter4 = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter4);
        // Year Spinner 끝

        // Month Spinner 시작
        spin = findViewById(R.id.month);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter1);
        // Month Spinner 끝

        // Day Spinner 시작
        spin = findViewById(R.id.day);
        spin.setPrompt("월을 골라주세요");

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter2);
        // Day Spinner 끝

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
                if(emailId.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "이메일아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    emailId.requestFocus();
                    return;
                }

                if(email.getSelectedItem().toString().equals("")){
                    Toast.makeText(JoinActivity.this, "이메일을 선택해주세요", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
                
                if(name.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                
                if(phone1.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone1.requestFocus();
                    return;
                }

                if(phone2.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone2.requestFocus();
                    return;
                }

                if(phone3.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone3.requestFocus();
                    return;
                }

                if(year.getSelectedItem().toString().equals("")){
                    Toast.makeText(JoinActivity.this, "년도를 선택해주세요", Toast.LENGTH_SHORT).show();
                    year.requestFocus();
                    return;
                }

                if(month.getSelectedItem().toString().equals("")){
                    Toast.makeText(JoinActivity.this, "개월을 선택해주세요", Toast.LENGTH_SHORT).show();
                    month.requestFocus();
                    return;
                }

                if(day.getSelectedItem().toString().equals("")){
                    Toast.makeText(JoinActivity.this, "일자를 선택해주세요", Toast.LENGTH_SHORT).show();
                    day.requestFocus();
                    return;
                }

                // checkbox 'man', 'girl' 둘중 하나만 선택하도록 하기
                man.isChecked();
                if(man.isChecked() && girl.isChecked()){
                    Toast.makeText(JoinActivity.this, "성별을 하나만 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(height.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "키를 입력해주세요", Toast.LENGTH_SHORT).show();
                    height.requestFocus();
                    return;
                }

                if(weight.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "몸무게를 입력해주세요", Toast.LENGTH_SHORT).show();
                    weight.requestFocus();
                    return;
                }

                if(post.getText().toString().length() ==0){
                    Toast.makeText(JoinActivity.this, "우편번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    post.requestFocus();
                    return;
                }

                if(address.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    address.requestFocus();
                    return;
                }

                if(detailAddress.getText().toString().length() == 0){
                    Toast.makeText(JoinActivity.this, "상세주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    detailAddress.requestFocus();
                    return;
                }

                if(!password.getText().toString().equals(checkPassword.getText().toString())){
                    Toast.makeText(JoinActivity.this, "비밀번호와 비밀번호확인이 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if(!phone1.getText().toString().equals("010") || !phone1.getText().toString().equals("011")){
                    Toast.makeText(JoinActivity.this, "앞번호는 '010'과 011'중 하나를 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone1.requestFocus();
                    return;
                }

                if(phone2.getText().toString().length() != 4){
                    Toast.makeText(JoinActivity.this, "중앙번호는 4자리만 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone2.requestFocus();
                    return;
                }
                
                if(phone3.getText().toString().length() != 4){
                    Toast.makeText(JoinActivity.this, "마지막번호는 4자리만 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone3.requestFocus();
                    return;
                }
                // 빈값일 때 Toast띄우고 입력하게 하기 끝

                Call<UserVO> call = rs.insertUser(user_Id.getText().toString(), password.getText().toString(), name.getText().toString(), )

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

}
