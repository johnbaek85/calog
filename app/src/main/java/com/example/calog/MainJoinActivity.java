package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainJoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_join);
        Button btnjoin = (Button)findViewById(R.id.btnjoin);
        //final EditText editText = (EditText)findViewById(R.id.btnjoins);
    }

}
