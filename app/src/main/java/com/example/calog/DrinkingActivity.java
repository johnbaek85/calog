package com.example.calog;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

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



        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction tr=fm.beginTransaction();

        GraphFragment graphFragment= new GraphFragment();

        //추후에 fragment 값 전달.
//        Bundle bundle = new Bundle();
//        bundle.putString("param1", param1); // Key, Value
//        bundle.putString("param2", param2); // Key, Value
        //graphFragment.setArgument();

        tr.replace(R.id.barChartFrag,graphFragment);




    }
}