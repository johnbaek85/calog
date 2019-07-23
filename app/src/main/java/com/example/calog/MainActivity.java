package com.example.calog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.calog.signUp.MainJoinActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imgRun;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgRun = findViewById(R.id.imgRun);
        GlideDrawableImageViewTarget getImage = new GlideDrawableImageViewTarget(imgRun);
        Glide.with(MainActivity.this).load(R.drawable.calog_run).into(getImage);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(MainActivity.this, MainJoinActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }
}
