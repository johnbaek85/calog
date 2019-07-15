package com.example.calog.signUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calog.JoinRemoteService;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.VO.UserVO;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.JoinRemoteService.BASE_URL;

public class MainJoinActivity extends AppCompatActivity implements View.OnClickListener  {

    private OAuthLoginButton naverLogInButton;
    private static OAuthLogin naverLoginInstance;
    Button btnGetApi, btnLogout;

    EditText user_id, password;
    Retrofit retrofit;
    JoinRemoteService rs;
    UserVO user;
    static final String CLIENT_ID = "yLznJEv7RDN1ugZEgKc8";
    static final String CLIENT_SECRET = "rFyzvuJvZN";
    static final String CLIENT_NAME = "네이버 아이디로 로그인 테스트";

    TextView tv_mail;
    static Context context;

    //추가
    Button btnLogin, btnJoin, btnNaverLogin;
    ImageView back, home;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_join);
        init();
        init_View();

        user_id = findViewById(R.id.user_Id);
        password = findViewById(R.id.password);



        // RemoteServcie, Retrofit
        retrofit = new Retrofit.Builder()                           // Retrofit 빌더 생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(JoinRemoteService.class);              // API 인터페이스 생성

        //back 클릭했을때
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
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

                Call<UserVO> call = rs.readUser(user_id.getText().toString(), password.getText().toString());
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {

                        user = response.body();

                        // 프레퍼런스
                        SharedPreferences pref = getSharedPreferences("pjLogin", 0);
                        // edit 만들기
                        SharedPreferences.Editor edit = pref.edit();

                        edit.putString("user_Id", user.getUser_id().toString());
                        edit.putString("password", user.getPassword().toString());
                        edit.commit();

                        //Toast.makeText(MainJoinActivity.this ,user.getUser_id().toString(), Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(MainJoinActivity.this, MainHealthActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        Toast.makeText(MainJoinActivity.this, "아이디와 비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                });



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

    //초기화
    private void init() {
        context = this;
        naverLoginInstance = OAuthLogin.getInstance();
        naverLoginInstance.init(this, CLIENT_ID, CLIENT_SECRET, CLIENT_NAME);
    }

    //변수 붙이기
    private void init_View() {
        naverLogInButton = (OAuthLoginButton) findViewById(R.id.buttonNaverLogin);

        //로그인 핸들러
        OAuthLoginHandler naverLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {//로그인 성공
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainJoinActivity.this, MainHealthActivity.class);
                    startActivity(intent);
                } else {//로그인 실패
                    String errorCode = naverLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = naverLoginInstance.getLastErrorDesc(context);
                    Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }

        };


        naverLogInButton.setOAuthLoginHandler(naverLoginHandler);
        tv_mail = (TextView) findViewById(R.id.tv_mailaddress);
        btnGetApi = (Button) findViewById(R.id.btngetapi);
        btnGetApi.setOnClickListener(this);
        btnLogout = (Button) findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btngetapi){
            new RequestApiTask().execute();//static이 아니므로 클래스 만들어서 시행.
        }
        if(v.getId() == R.id.btnlogout){
            naverLoginInstance.logout(context);
            tv_mail.setText((String) "");//메일 란 비우기
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {//작업이 실행되기 전에 먼저 실행.
            tv_mail.setText((String) "");//메일 란 비우기
        }

        @Override
        protected String doInBackground(Void... params) {//네트워크에 연결하는 과정이 있으므로 다른 스레드에서 실행되어야 한다.
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = naverLoginInstance.getAccessToken(context);
            return naverLoginInstance.requestApi(context, at, url);//url, 토큰을 넘겨서 값을 받아온다.json 타입으로 받아진다.
        }

        protected void onPostExecute(String content) {//doInBackground 에서 리턴된 값이 여기로 들어온다.
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONObject response = jsonObject.getJSONObject("response");
                String email = response.getString("email");
                tv_mail.setText(email);//메일 란 채우기
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 프레퍼런스// 액티비티가 끝날떄 저장하는 용도
   /* @Override
    public void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("prefTest",0 );
        SharedPreferences.Editor edit = pref.edit();
        strId = user_id.getText().toString();
        strPassword = password.getText().toString();
        edit.putString("Id", strId);
        edit.putString("password", strPassword);
        edit.commit();
    }*/

}
