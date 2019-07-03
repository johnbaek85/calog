package com.example.calog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CardioFragment extends Fragment {
    ListView list;
    ArrayList<FitnessVO> array;
    CardioAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_cardio, container, false);

        list=view.findViewById(R.id.cardioList);
        array=new ArrayList<>();
        adapter = new CardioAdapter(array);
        list.setAdapter(adapter);

        return view;
    }
}
