package com.example.calog.WordCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;

import java.util.ArrayList;


public class WordCloudActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView cloudWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud);
        cloudWords = findViewById(R.id.cloudWords);

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

        cloudWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordCloudActivity.this, "관련 기사로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudActivity.this, WordCloudWebViewActivity.class);
                startActivity(intent);
            }
        });

        // 데이터 준비 ( -> 크롤링 결과를 VO 에 담아서 넘길 예정)
        ArrayList<String> hotKeywordSample = new ArrayList<>();
        hotKeywordSample.add("발기부전");
        hotKeywordSample.add("조루");
        hotKeywordSample.add("모태솔로");
        hotKeywordSample.add("전립선염");
        hotKeywordSample.add("1cm");


        //어댑터 만들기

        ArrayAdapter<String> wordCloudAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hotKeywordSample);

        ListView wordlist = (ListView) findViewById(R.id.WordCloudArticleList);
        wordlist.setAdapter(wordCloudAdapter);


    }

}
