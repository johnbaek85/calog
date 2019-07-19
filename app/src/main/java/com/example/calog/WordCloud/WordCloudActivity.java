package com.example.calog.WordCloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Drinking.DrinkingCheckActivity;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.Sleeping.SleepingActivity;
import com.example.calog.VO.CrawlingVO;
import com.example.calog.VO.UserVO;
import com.example.calog.signUp.MainJoinActivity;
import com.example.calog.signUp.UpdateUserInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soundcloud.android.crop.Crop;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;


public class WordCloudActivity extends AppCompatActivity {
    // 페이지 이동
    ImageView btnBack;
    TextView cloudWords, WordCloudTitle;

    // 크롤링/어답터에 사용
    ArrayList<CrawlingVO> array;
    RecyclerView WordCloudList;
    WordCloudAdapter adapter;

    // 크롤링 변수
    private String keyWordPageUrl = "https://terms.naver.com/list.nhn?cid=51001&categoryId=51001";
    // 웹뷰 링크 변수
    WebView webView;
    String link;
    Intent intent;

    TextView txtDate;

    //DB 용
    Retrofit retrofit;
    RemoteService rs;

    UserVO user;

    //TODO 하단 Menu
    File screenShot;
    Uri uriFile;
    BottomNavigationView bottomNavigationView;


   /* 텍스트로 화면에 출력할 떄 사용
   private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat;*/

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

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
                intent = new Intent(WordCloudActivity.this, MainJoinActivity.class);
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

            case R.id.adjust:
                Call<UserVO> call = rs.readUser(strUser_id);
                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        user = response.body();

                        intent = new Intent(WordCloudActivity.this, UpdateUserInfoActivity.class);

                        intent.putExtra("user_id", user.getUser_id());
                        intent.putExtra("password", user.getPassword());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("name", user.getName());
                        intent.putExtra("phone", user.getPhone());
                        intent.putExtra("birthday", user.getBirthday());
                        intent.putExtra("gender", user.getGender());
                        intent.putExtra("height", user.getHeight());
                        intent.putExtra("weight", user.getWeight());
                        intent.putExtra("bmi", user.getBmi());
                        intent.putExtra("address", user.getAddress());

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
                    }
                });
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
                                Toast.makeText(WordCloudActivity.this,
                                        "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", "");
                                editor.commit();
                                user_id.setText("");
                                logInStatus = false;

                                intent = new Intent(WordCloudActivity.this, MainJoinActivity.class);
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
                        Toast.makeText(WordCloudActivity.this,
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
        setContentView(R.layout.activity_word_cloud);

        intent = getIntent();

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(intent.getStringExtra("select_date"));

        //TODO status Bar 색상변경
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        //TODO toolbar 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_id = findViewById(R.id.user_id);
        //TODO sharedpreference에서 userid 값 받아옴
        pref = getSharedPreferences("pjLogin", MODE_PRIVATE);

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

        //TODO 하단 메뉴설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.rankingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "랭킹 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(WordCloudActivity.this, WordCloudActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(WordCloudActivity.this, DrinkingCheckActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu: {
                        intent = new Intent(WordCloudActivity.this, MainHealthActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
                        intent.putExtra("select_date", txtDate.getText().toString());

                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(WordCloudActivity.this, SleepCheckActivity.class);

                        intent.putExtra("user_id", user_id.getText().toString());
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
                        if (screenShot != null) {
                            Crop.of(uriFile, uriFile).asSquare().start(WordCloudActivity.this, 100);
                        }
                        break;
                    }
                }
                return true;
            }
        });

        WordCloudList = findViewById(R.id.WordCloudList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        WordCloudList.setLayoutManager(manager);
        new JsoupAsyncTask().execute();
        pageTrans();

        array = new ArrayList<CrawlingVO>();
        //웹뷰

        Intent intent = getIntent();
        link = intent.getStringExtra("Link");
        // 링크 주소 확인
        // System.out.println("링크 주소 : " + link);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebView());
        WebSettings set = webView.getSettings();
        set.setBuiltInZoomControls(true);
        // 웹뷰 첫 페이지에 보여줄 페이지
        if (link == null) {
            link = "https://terms.naver.com/list.nhn?cid=51001&categoryId=51001";
            webView.loadUrl(link);
        } else {
            webView.loadUrl(link);
        }

    }

    //TODO 하단 메뉴설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File cropFile = screenShot;

        if(requestCode ==100){
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

    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);

        System.out.println("..........." + filename);
        //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
            os.close();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }

/*
        텍스트로 화면에 출력하기 위해 준비
        textviewHtmlDocument = (TextView) findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod());
*/
/*      테스트. 스트링으로 만들어 출력
        ArrayList<String> hotKeywordSample = new ArrayList<>();
        hotKeywordSample.add(cvo.getTitle());
        // 샘플 데이터를 배열에 넣기!!
        hotKeywordSample.add("모태솔로");
        hotKeywordSample.add("전립선염");
        hotKeywordSample.add("1cm");
        어댑터 만들기
        ArrayAdapter<String> wordCloudAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hotKeywordSample);
        ListView wordlist = (ListView) findViewById(R.id.WordCloudArticleList);
        wordlist.setAdapter(wordCloudAdapter);
*/

        /*
        // 버튼을 클릭했을 때 크롤링한 데이터 가져오는 방식
        Button htmlTitleButton = (Button) findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });
        */
    //크롤링 -> list에 VO 담아 넣기 /////////////////////////////////////////////////////////////////////////////////////////

    //                                                < parameter , progress, returnType>
    private class JsoupAsyncTask extends AsyncTask<Void, Void, ArrayList<CrawlingVO>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<CrawlingVO> doInBackground(Void... params) {
            array = new ArrayList<>();

            try {
                Document doc = Jsoup.connect(keyWordPageUrl).get();
                //  System.out.println(keyWordPageUrl); // 주소값 체크
                Elements elements = doc.select("ul.content_list li");
                int i = 1;
                for (Element e : elements.select(".title")) {
                    CrawlingVO vo = new CrawlingVO();
                    // VO에 저장, 타이틀은 "]"의 인덱스 번호 +1 까지만
                    String title = e.select("strong.title").text();
                    int idx = title.indexOf("]");

                    // [] 가 없는 경우에 전부 널값이 들어간다... 수정 필요 //////////////////////////////
                    title = (i + ". ") + title.substring(0, idx + 1);
                    vo.setTitle(title);
                    vo.setLink(e.select("a[href]").attr("href"));
                    array.add(vo);

                    // 콘솔에 엘리멘트들의 값을 모두 찍기 (elements)
                    // Log.d("", e.text());
                    // 안드로이드 화면에 엘리멘트들의 값을  찍기 (element)
                    // htmlContentInStringFormat += i + ". " + vo.getTitle() + "\n";
                    if (i == 10) {
                        break;
                    }
                    i++;

                    // Vo에 담긴 테이터 체크     System.out.println("vo data check:................" + vo.toString());
                }

                // System.out.println("size of Vo Array check: " + array.size());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error", e.toString());
            }
            return array;
        }

        @Override
        protected void onPostExecute(ArrayList<CrawlingVO> crawlingVOS) {
            super.onPostExecute(crawlingVOS);
            adapter = new WordCloudAdapter(WordCloudActivity.this, array);
            WordCloudList.setAdapter(adapter);
        }
    }
    /*
    public void mClick(View view) {
    }
    */
    //페이지 이동 버튼////////////////////////////////////////////////////////////////////////////////////


    public void pageTrans() {

        cloudWords = findViewById(R.id.cloudWords);
        // 뒤로 가기 버튼
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WordCloudActivity.this, "이전 페이지 Activity로 이동",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        WordCloudTitle = findViewById(R.id.WordCloudTitle);
     /*   WordCloudTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordCloudActivity.this, "관련 기사로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudActivity.this, WordCloudWebViewActivity.class);
                intent.putExtra(        WordCloudTitle.getText()        );

                startActivity(intent);
            }
        });*/

 /*
        // 크롤링한 결과를 클릭하면 웹뷰로 이동하여 해당 기사를 보여줄 것.(예정)
        cloudWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordCloudActivity.this, "관련 기사로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudActivity.this, WordCloudWebViewActivity.class);
                startActivity(intent);
            }
        });
*/
    }


    public class MyWebView extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}