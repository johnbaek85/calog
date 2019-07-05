package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SleepCheckActivity extends AppCompatActivity {
    ImageView imgBackCheckStop,imgBackResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_check);
        imgBackCheckStop = findViewById(R.id.imgBackCheckStop);
        imgBackCheckStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void mClick(View v){//수면측정중지버튼

        setContentView(R.layout.activity_sleep_check_result);
        imgBackResult = findViewById(R.id.imgBackResult);
        imgBackResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
