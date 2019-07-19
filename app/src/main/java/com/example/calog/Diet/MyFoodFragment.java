package com.example.calog.Diet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.DietMenuVO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Retrofit;

class MyFoodFragment extends Fragment {
    ArrayList<DietMenuVO> array;
    RecyclerView myMenuList;
    BundleAdapter adapter;
    FloatingActionButton btnInsert;

    Intent intent;
    String user_id;
    int diet_type_id;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_my_food, container, false);

        myMenuList = layout.findViewById(R.id.myMenuList);
        btnInsert = layout.findViewById(R.id.btnInsert);

        intent = getActivity().getIntent();

        user_id = intent.getStringExtra("user_id");
        diet_type_id = intent.getIntExtra("diet_type_id", 0);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> MyFoodFragment user_id : " + user_id);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.insertFood(user_id, diet_type_id);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> MyFoodFragment user_id(Insert 버튼) : " + user_id
                        + " + 날짜 : " + intent.getStringExtra("select_date") + " + diet_type_id : " + diet_type_id);
            }
        });

        Bundle bundle = getArguments();
        adapter = (BundleAdapter) bundle.getSerializable("bundleAdapter");

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        myMenuList.setLayoutManager(manager);
        myMenuList.setAdapter(adapter);
        return layout;
    }
}