package com.example.calog.Sleeping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;

public class SleepCheckActivity extends AppCompatActivity {
    ImageView btnBack, btnHome;
    TextView monthName;
    Button btnSleepFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_check);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SleepCheckActivity.this, MainHealthActivity.class);
                startActivity(intent);
            }
        });

    }

    public void mClick(View v){//수면측정중지버튼
        Dialog sleepDialog = new SleepCheckResultDialog(this,android.R.style.Theme_NoTitleBar_Fullscreen);
        sleepDialog.setCancelable(true);
        sleepDialog.show();
    }
}
