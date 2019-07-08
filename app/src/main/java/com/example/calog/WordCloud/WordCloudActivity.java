package com.example.calog.WordCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class WordCloudActivity extends AppCompatActivity {
    //
    ImageView btnBack;
    TextView cloudWords;

    // 크롤링 변수

    private String htmlPageUrl = "https://terms.naver.com/list.nhn?cid=51001&categoryId=51001";
    private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat;
    //
    ArrayList<String> array;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud);
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
        // 크롤링한 결과를 클릭하면 웹뷰로 이동하여 해당 기사를 보여줄 것.(예정)
        cloudWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordCloudActivity.this, "관련 기사로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudActivity.this, WordCloudWebViewActivity.class);
                startActivity(intent);
            }
        });
        /////////////////////////////////////////////////

        textviewHtmlDocument = (TextView) findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod());

        Button htmlTitleButton = (Button) findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });
// 데이터 크롤링하기
        /*

         */


        // 데이터 준비 ( -> 크롤링 결과를 VO 에 담아서 넘길 예정)
        ArrayList<CrawlingVO> hotKeywordSample = new ArrayList<>();


        //    ArrayList<String> hotKeywordSample = new ArrayList<>();

//        CrawlingVO cvo = new CrawlingVO();
//        hotKeywordSample.add(cvo.getTitle());


        // 샘플 데이터를 배열에 넣기!!
/*

        hotKeywordSample.add("모태솔로");
        hotKeywordSample.add("전립선염");
        hotKeywordSample.add("1cm");

*/

        //어댑터 만들기
        //ArrayAdapter<String> wordCloudAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hotKeywordSample);
        //  ArrayAdapter<String> wordCloudAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hotKeywordSample);

        //    ListView wordlist = (ListView) findViewById(R.id.WordCloudArticleList);
//        wordlist.setAdapter(wordCloudAdapter);


    }

// 크롤링해야지!

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();
                ArrayList<CrawlingVO> crawarray = new ArrayList<CrawlingVO>();

                System.out.println(htmlPageUrl);
                Elements elements = doc.select("ul.content_list li");
//코드백업                Document doc = Jsoup.connect(htmlPageUrl).get();
//코드백업                Elements links = doc.select("a[href]");
                int i = 1;
                for (Element e : elements.select(".title")) {
                    CrawlingVO vo = new CrawlingVO();

                    String title = e.select("strong.title").text();
                    // ]의 인덱스 번호를 알아내어 ]+1 까지만 살려내야 함..
                    int idx = title.indexOf("]");
                    title = title.substring(0, idx + 1);
                    vo.setTitle(title);
                    vo.setLink(e.select("a[href]").attr("href"));




                    // 콘솔에 엘리멘트들의 값을 모두 찍기 (elements)
                    // Log.d("", e.text());
                    // 안드로이드 화면에 엘리멘트들의 값을  찍기 (element)
                    htmlContentInStringFormat += i + ". " + vo.getTitle() + "\n";
                    System.out.println("title : " + vo.getTitle());
                    System.out.println("link : " + vo.getLink());


//                    htmlContentInStringFormat += (e.attr("a[href]")
//                            + i + ". " + e.text().trim() + "\n");
                    if (i == 10) {
                        break;
                    }
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textviewHtmlDocument.setText(htmlContentInStringFormat);
        }
    }

}
