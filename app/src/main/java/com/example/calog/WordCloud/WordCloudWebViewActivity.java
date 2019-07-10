package com.example.calog.WordCloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calog.MainHealthActivity;
import com.example.calog.R;

public class WordCloudWebViewActivity extends AppCompatActivity {
    ImageView btnBack, btnMainShortcut;
    WebView webView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud_web_view);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WordCloudWebViewActivity.this, "이전 페이지 Activity로 이동",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudWebViewActivity.this, WordCloudActivity.class);
                startActivity(intent);
            }
        });

        btnMainShortcut = findViewById(R.id.btnMAinShortcut);
        btnMainShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WordCloudWebViewActivity.this, "메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WordCloudWebViewActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        link = intent.getStringExtra("Link");
        // 링크 주소 확인
        // System.out.println("링크 주소 : " + link);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebView());
        WebSettings set = webView.getSettings();
        set.setBuiltInZoomControls(true);
        webView.loadUrl(link);


    }

    public class MyWebView extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
