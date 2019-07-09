package com.example.calog.WordCloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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
    // 페이지 이동
    ImageView btnBack;
    TextView cloudWords;

    // 크롤링/어답터에 사용
    ArrayList<CrawlingVO> array;
    RecyclerView WordCloudList;
    WordCloudAdapter adapter;
    // 크롤링 변수
    private String keyWordPageUrl = "https://terms.naver.com/list.nhn?cid=51001&categoryId=51001";
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


        WordCloudList = findViewById(R.id.WordCloudList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        WordCloudList.setLayoutManager(manager);
        new JsoupAsyncTask().execute();

        array = new ArrayList<CrawlingVO>();

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
    }
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
                    title = title.substring(0, idx + 1);
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

                    System.out.println("vo:................" + vo.toString() );
                }

                System.out.println("size of Vo Array fffffffffffffffffffffffffffffffff: " + array.size());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error", e.toString());
            }
            return array;
        }

        @Override
        protected void onPostExecute(ArrayList<CrawlingVO> crawlingVOS) {
            super.onPostExecute(crawlingVOS);
            adapter = new WordCloudAdapter(WordCloudActivity.this,array);
            WordCloudList.setAdapter(adapter);
        }
    }
}
