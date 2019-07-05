package com.example.calog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SleepCheckResult extends Activity {
    ImageView imgBackResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgBackResult = findViewById(R.id.imgBackResult);
        System.out.println("wqewqeweqweqeqw===========");

        imgBackResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wqewqeweqwqeqeqweZzzweqeqw===========");
            }
        });
    }
}
