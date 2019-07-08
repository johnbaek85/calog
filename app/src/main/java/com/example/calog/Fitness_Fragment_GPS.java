package com.example.calog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fitness_Fragment_GPS extends Fragment {
    int FitnessMenuId;
    public Fitness_Fragment_GPS(int fitnessMenuId) {
        this.FitnessMenuId = fitnessMenuId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);
        return view;
    }
}
