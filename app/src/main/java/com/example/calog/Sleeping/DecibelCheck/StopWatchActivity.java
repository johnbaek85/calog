package com.example.calog.Sleeping.DecibelCheck;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import com.example.calog.R;

public class StopWatchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_check);

        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        Button buttonStart = (Button) findViewById(R.id.btnSleepStart);
        Button buttonReset = (Button) findViewById(R.id.btnSleepFinish);
        buttonStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                chronometer.start();
            }
        });

        buttonReset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });
    }
}
