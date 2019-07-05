package com.example.calog.Drinking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calog.Common.GraphPagerFragment;
import com.example.calog.MainHealthActivity;
import com.example.calog.R;

import java.util.ArrayList;

public class DrinkingActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinking);

        //뒤로가기 이벤트
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

<<<<<<< HEAD:app/src/main/java/com/example/calog/DrinkingActivity.java
<<<<<<< Updated upstream
=======
        //
=======
>>>>>>> master:app/src/main/java/com/example/calog/Drinking/DrinkingActivity.java
        ImageView btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD:app/src/main/java/com/example/calog/DrinkingActivity.java
                Intent intent=new Intent(DrinkingActivity.this,MainHealthActivity.class);
=======
                Intent intent=new Intent(DrinkingActivity.this, MainHealthActivity.class);
>>>>>>> master:app/src/main/java/com/example/calog/Drinking/DrinkingActivity.java
                startActivity(intent);
            }
        });

<<<<<<< HEAD:app/src/main/java/com/example/calog/DrinkingActivity.java
>>>>>>> Stashed changes
=======
>>>>>>> master:app/src/main/java/com/example/calog/Drinking/DrinkingActivity.java
        //알콜check Activity 이동
        TextView alcoholCheck=findViewById(R.id.alcoholCheck);
        alcoholCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(DrinkingActivity.this,DrinkingCheckActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //주량 설정 버튼
        final ImageView liquorSetiing=findViewById(R.id.liquorSetiing);
        liquorSetiing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                View view = getLayoutInflater().inflate(R.layout.custom_dialog,null);

                //spinner 설정
                final ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("1병");
                arrayList.add("2병");
                arrayList.add("3병");
                arrayList.add("기타");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList);

                final Spinner spinner = view.findViewById(R.id.spinner);
                spinner.setAdapter(arrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(),arrayList.get(i)+"이(가) 선택되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });


                //주량설정 다이얼로그창
                AlertDialog.Builder box=new AlertDialog.Builder(DrinkingActivity.this);

                box.setTitle("주량설정");
                box.setView(view);
                box.setPositiveButton("저장", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String txt=spinner.getSelectedItem().toString();
                        TextView liquorTxt=findViewById(R.id.liquorTxt);
                        liquorTxt.setText("현재 주량 설정:" +txt);

                        Toast.makeText(DrinkingActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                box.setNegativeButton("취소",null);
                box.show();
            }
        });

        //그래프 BarChart Fragment
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();

        GraphPagerFragment graphFragment = new GraphPagerFragment();
        tr.replace(R.id.barChartFrag,graphFragment);

    }
}
