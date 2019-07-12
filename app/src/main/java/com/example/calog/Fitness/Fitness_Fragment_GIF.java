package com.example.calog.Fitness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.calog.R;

public class Fitness_Fragment_GIF extends Fragment {
    int FitnessMenuId;
    public Fitness_Fragment_GIF(int fitnessMenuId) {
        this.FitnessMenuId = fitnessMenuId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif, container, false);

        ImageView gifworkout = (ImageView)view.findViewById(R.id.imageGif);
        GlideDrawableImageViewTarget getImage = new GlideDrawableImageViewTarget(gifworkout);
        switch (FitnessMenuId){
            case 21:
                Glide.with(getContext()).load(R.drawable.close_hands_push_up).into(getImage);
                break;

            case 22:         //팔굽혀펴기일 경우
                Glide.with(getContext()).load(R.drawable.pushup).into(getImage);
                break;
            case 29:
                Glide.with(getContext()).load(R.drawable.airbike).into(getImage);
                break;
            case 40:
                Glide.with(getContext()).load(R.drawable.crunch).into(getImage);
                break;

            case 48:
                Glide.with(getContext()).load(R.drawable.plank).into(getImage);
                break;
            case 51:
                Glide.with(getContext()).load(R.drawable.situp).into(getImage);
                break;
        }



        return view;
    }
}
