package com.example.calog.Diet;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calog.R;
import com.example.calog.VO.UserDietViewVO;

import java.util.List;


public class DietFragment extends Fragment
{

    RecyclerView dietList;
    Button btnSave;
    ImageView btnBack, btnHome;

    Intent intent;
    List<UserDietViewVO> dietMenuArray;
    DietMenuAdapter menuAdapter;

    boolean isSearchView;

    public DietFragment(List dietMenuArray,boolean isSearchView)
    {
        this.dietMenuArray=dietMenuArray;
        this.isSearchView=isSearchView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_diet, container, false);

        //RecyclerVIew 설정
        RecyclerView recyclerView=view.findViewById(R.id.dietList);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        //menuAdapter
        //menuAdapter=new DietMenuAdapter(getContext(),dietMenuArray);
        recyclerView.setAdapter(menuAdapter);

        if(isSearchView)
        {
            EditText search=view.findViewById(R.id.searchEdit);
            search.setVisibility(View.VISIBLE);

            //엔터키 막기
            search.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if(keyCode == event.KEYCODE_ENTER)
                    {
                        Toast.makeText(getContext(),"엔터를 눌렀습니다,",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });

        }

        return view;
    }
}
