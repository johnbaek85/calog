package com.example.calog.WordCloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import com.example.calog.Sleeping.DecibelCheck.SleepCheckActivity;
import com.example.calog.VO.CrawlingVO;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud);

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
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.drinkingMenu: {
//                         Toast.makeText(MainHealthActivity.this, "알콜 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();

                        intent = new Intent(WordCloudActivity.this, DrinkingCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.HomeMenu: {
                        intent = new Intent(WordCloudActivity.this, MainHealthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    }
                    case R.id.sleepMenu: {
//                         Toast.makeText(MainHealthActivity.this, "수면 Activity로 이동",
//                                 Toast.LENGTH_SHORT).show();
                        intent = new Intent(WordCloudActivity.this, SleepCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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