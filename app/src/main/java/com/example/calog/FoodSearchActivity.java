package com.example.calog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FoodSearchActivity extends AppCompatActivity {

    RecyclerView dietList;
    Button btnSave;

    Intent intent;
    List<DietMenuVO> dietMenuArray;
    DietMenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        dietList = findViewById(R.id.dietList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        dietList.setLayoutManager(manager);
        dietMenuArray = new ArrayList<DietMenuVO>();
//        menuAdapter = new DietMenuAdapter(FoodSearchActivity.this, dietMenuArray);
//        dietList.setAdapter(menuAdapter);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(FoodSearchActivity.this, DietActivity.class);
                AlertDialog.Builder alert = new AlertDialog.Builder(FoodSearchActivity.this);
                alert.setTitle("음식 선택 목록");
                alert.setMessage("선택 목록을 저장하시겠습니까?");
                alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FoodSearchActivity.this, "저장되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FoodSearchActivity.this, "취소되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
