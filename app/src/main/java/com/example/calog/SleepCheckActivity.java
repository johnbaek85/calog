package com.example.calog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

public class SleepCheckActivity extends AppCompatActivity {
    ImageView btnBackStop,btnBackResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_check);

        btnBackStop = findViewById(R.id.btnBackStop);
        btnBackStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SleepCheckActivity.this,SleepingActivity.class);
                startActivity(intent);
            }
        });
        btnBackResult = findViewById(R.id.btnBackResult);
        btnBackResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SleepCheckActivity.this,MainHealthActivity.class);
                startActivity(intent);
            }
        });
    }
    public void mClick(View v){
        Fragment fr;
        fr = new SleepCheckResult();
        setContentView(R.layout.activity_sleep_check_result);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.commit();
    }
}
