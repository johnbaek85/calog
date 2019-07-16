package com.example.calog.Diet;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.VO.DietMenuVO;

import java.util.List;

public class DietFragment extends Fragment
{

    /*현재 사용 안하는 중*/

    RecyclerView dietList;
    Button btnSave;
    ImageView btnBack, btnHome;

    Intent intent;
    List<DietMenuVO> dietMenuArray;
    DietFoodSearchAdapter menuAdapter;

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
        menuAdapter=new DietFoodSearchAdapter(getContext(),dietMenuArray,null,null);
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
