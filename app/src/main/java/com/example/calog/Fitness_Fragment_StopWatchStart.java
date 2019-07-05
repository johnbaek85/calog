package com.example.calog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fitness_Fragment_StopWatchStart extends Fragment {
    int flag;
    View view;
    public Fitness_Fragment_StopWatchStart(int flag) {
        this.flag = flag;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (flag){
            case 1:
                view = inflater.inflate(R.layout.fragment_stopwatchstart, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_stopwatchstop, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.fragment_stopwatchcontinue, container, false);
                break;
        }
        return view;
    }
}
