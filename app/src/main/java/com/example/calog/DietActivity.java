package com.example.calog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.slice.Slice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class DietActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        SlidingDrawer dietDrawer = findViewById(R.id.dietDrawer);
        dietDrawer.animateClose();
    }

    public void mClick(View view) {
        intent = new Intent(DietActivity.this, FoodSearchActivity.class);
        switch (view.getId()){
            case R.id.btnBreakfast:
                Toast.makeText(DietActivity.this, "아침", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnLunch:
                Toast.makeText(DietActivity.this, "점심", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnDinner:
                Toast.makeText(DietActivity.this, "저녁", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnSnack:
                Toast.makeText(DietActivity.this, "간식", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.btnBack:
                intent = getIntent();
                finish();
                break;
            case R.id.btnHome:
                intent = new Intent(DietActivity.this, MainHealthActivity.class);
                startActivity(intent);
                break;
        }
    }
}
