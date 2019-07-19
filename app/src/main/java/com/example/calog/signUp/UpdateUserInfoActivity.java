package com.example.calog.signUp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.Fitness.SearchFitnessActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.UserVO;
import com.example.calog.WordCloud.WordCloudActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

public class UpdateUserInfoActivity extends AppCompatActivity {

    RemoteService rs;
    UserVO user;
    Retrofit retrofit;

    Intent intent;

    //Button btnSave, btnReset 추가
    Button btnSave, btnReset;

    //ImageView back, home 추가
    ImageView btnBack;

    EditText txt_user_id, password, checkPassword, email, name,
            phone, birthday, gender, height, weight, address, bmi;

    TextView txtDate;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;

    //TODO user용 toolbar 관련
    TextView user_id;
    String strUser_id;
    Toolbar toolbar;
    SharedPreferences pref;
    boolean logInStatus = false;

    //=============TODO 로그인 관련
    //옵션 메뉴 user 로그인 여부
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.loginmenu, menu);
        return true;
    }

    //로그인 상태
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (logInStatus) { // 로그인 한 상태: 로그인은 안보이게, 로그아웃은 보이게
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        } else { // 로그 아웃 한 상태 : 로그인 보이게, 로그아웃은 안보이게
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);

        }

        //logInStatus = !logInStatus;   // 값을 반대로 바꿈

        return super.onPrepareOptionsMenu(menu);
    }

    //로그인, 회원정보 수정, 회원 탈퇴
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.login:
                intent = new Intent(UpdateUserInfoActivity.this, MainJoinActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Toast.makeText(this, "로그아웃이 완료되었습니다", Toast.LENGTH_SHORT).show();
                //로그인 정보 프레퍼런스에 로그인정보 삭제
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_id", "");
                editor.commit();
                user_id.setText("");
                logInStatus = false;
                break;

            case R.id.withdraw:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("회원탈퇴");
                builder.setMessage("탈퇴하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> call = rs.deleteUser(strUser_id);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(UpdateUserInfoActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(UpdateUserInfoActivity.this, MainJoinActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                            }
                        });
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UpdateUserInfoActivity.this,
                                "회원탈퇴가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        intent = getIntent();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        txt_user_id = (EditText) findViewById(R.id.txt_user_id);
        password = (EditText) findViewById(R.id.password);
        checkPassword = findViewById(R.id.checkPassword);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        birthday = findViewById(R.id.birthday);
        gender = (EditText) findViewById(R.id.gender);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        bmi = findViewById(R.id.bmi);
        address = findViewById(R.id.address);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);

        intent = getIntent();

        txt_user_id.setText(intent.getStringExtra("user_id"));
        password.setText(intent.getStringExtra("password"));
        email.setText(intent.getStringExtra("email"));
        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        birthday.setText(intent.getStringExtra("birthday"));
        gender.setText(intent.getStringExtra("gender"));
        height.setText(String.valueOf(intent.getDoubleExtra("height", 0)));
        weight.setText(String.valueOf(intent.getDoubleExtra("weight", 0)));
        bmi.setText(String.valueOf(intent.getDoubleExtra("bmi", 0)));
        address.setText(intent.getStringExtra("address"));

        //TODO toolbar 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_id = findViewById(R.id.user_id);
        //TODO sharedpreference에서 userid 값 받아옴
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);

        retrofit = new Retrofit.Builder()                           // Retrofit 빌더 생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class);

        //TODO User Login
        //로그인 정보 프레퍼런스에서 불러오기
        strUser_id = pref.getString("user_id", "");
        user_id.setText(strUser_id);

        if (strUser_id.equals("")) {
            user_id.setText("");
            logInStatus = false;
        } else {
            user_id.setText(strUser_id + "님 환영합니다!");
            logInStatus = true;
        }

        // '아이디' 설정 ( 조건에 따른 색상 변경및 안내메세지 )
        txt_user_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txt_user_id.getText().toString().trim().length() <= 20) {
                    txt_user_id.setTextColor(Color.GREEN);
                } else {
                    txt_user_id.setTextColor(Color.RED);
                    Toast.makeText(UpdateUserInfoActivity.this, "아이디는 20자리 이하로 입력해주세요", Toast.LENGTH_LONG).show();
                    txt_user_id.requestFocus();
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
                if (checkPassword.getText().toString().length() >= 8 && checkPassword.getText().toString().equals(password.getText().toString())) {
                    //비밀번호확인이 8자리 이상이고 비밀번호와 같을때 검은색
                    checkPassword.setTextColor(Color.GREEN);
                    password.setTextColor(Color.GREEN);
                } else {
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
                if (email.getText().toString().length() <= 20) {
                    email.setTextColor(Color.GREEN);
                } else {
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
                if (phone.getText().toString().length() == 11) {
                    phone.setTextColor(Color.GREEN);
                } else {
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
                if (phone.getText().toString().length() == 8) {
                    phone.setTextColor(Color.GREEN);
                } else {
                    phone.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // EditText 'password'에서 다른곳으로 이동할때, 'password'를 클릭할 때 발생하는 이벤트
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (password.getText().toString().length() < 8 && password.getText().toString().length() > 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "8자리 이상 입력하여주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EditText 'checkPassword'에서 다른곳으로 이동할때, 'checkPassword'를 클릭할 때 발생하는 이벤트

        checkPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (checkPassword.getText().toString().length() < 8 && checkPassword.getText().toString().length() > 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "8자리 이상 입력하여주세요", Toast.LENGTH_SHORT).show();
                } else if (checkPassword.getText().toString().length() >= 8 && password.getText().toString().equals(checkPassword.getText().toString())) {
                    Toast.makeText(UpdateUserInfoActivity.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateUserInfoActivity.this, "비밀번호가 일치하지않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // back 클릭했을때 이전 Activity로 이동
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // home 클릭했을때 MainHealthActivity로 이동


        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                user = new UserVO();

                user.setUser_id(txt_user_id.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setAddress(address.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setGender(gender.getText().toString());
                user.setHeight(Double.parseDouble(height.getText().toString()));
                user.setWeight(Double.parseDouble(weight.getText().toString()));
                user.setBmi((Double.parseDouble(weight.getText().toString()) / (Double.parseDouble(height.getText().toString())) / (Double.parseDouble(height.getText().toString()))) * 10000);

                Call<Void> call = rs.updateUser(user);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(UpdateUserInfoActivity.this,
                                "회원 정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                    }
                });

                // 빈값일 때 Toast띄우고 입력하게 하기 시작
                if (txt_user_id.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    txt_user_id.requestFocus();
                    return;
                }

                if (txt_user_id.getText().toString().length() > 20) {
                    Toast.makeText(UpdateUserInfoActivity.this, "아이디는 20자 이하로 입력해주세요", Toast.LENGTH_SHORT).show();
                    txt_user_id.requestFocus();
                    return;
                }

                if (password.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if (checkPassword.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "비밀번호확인을 입력해주세요", Toast.LENGTH_SHORT).show();
                    checkPassword.requestFocus();
                    return;
                }

                if (name.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }

                if (phone.getText().toString().length() == 0) {
                    Toast.makeText(UpdateUserInfoActivity.this, "번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                    return;
                }

                if (!password.getText().toString().equals(checkPassword.getText().toString())) {
                    Toast.makeText(UpdateUserInfoActivity.this, "비밀번호와 비밀번호확인이 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if (birthday.getText().toString().length() != 8) {
                    Toast.makeText(UpdateUserInfoActivity.this, "년도와 개월 일자를 8자리로 적어주세요", Toast.LENGTH_SHORT).show();
                    birthday.requestFocus();
                    return;
                }
                // 빈값일 때 Toast띄우고 입력하게 하기 끝

                // btnSave 클릭했을때 MainJoinActivity로 이동
                Intent intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);
                Toast.makeText(UpdateUserInfoActivity.this, "회원 정보 수정이 완료되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // btnReset 클릭했을때 MainJoinActivity로 이동
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);
                startActivity(intent);

            }
        });

        //TODO 하단 메뉴설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.rankingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "랭킹 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(UpdateUserInfoActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(UpdateUserInfoActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu:{
                        intent = new Intent(UpdateUserInfoActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(UpdateUserInfoActivity.this, SleepCheckActivity.class);

                        intent.putExtra("user_id", strUser_id);
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.shareMenu: {
//                         Toast.makeText(MainHealthActivity.this, "공유 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        View rootView = getWindow().getDecorView();
                        screenShot = ScreenShot(rootView);
                        uriFile = Uri.fromFile(screenShot);
                        if(screenShot != null) {
                            Crop.of(uriFile, uriFile).asSquare().start(UpdateUserInfoActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });

    }

    //TODO 하단 메뉴설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File cropFile = screenShot;

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                cropFile = new File(Crop.getOutput(data).getPath());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API 24 이상 일경우..
                uriFile = FileProvider.getUriForFile(getApplicationContext(),
                        getApplicationContext().getPackageName() + ".provider", cropFile);
            } else { // API 24 미만 일경우..
                uriFile = Uri.fromFile(cropFile);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriFile);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "선택"));
        }
    }

    public File ScreenShot(View view) {
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);

        System.out.println("..........." + filename);
        //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
            os.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }
}
