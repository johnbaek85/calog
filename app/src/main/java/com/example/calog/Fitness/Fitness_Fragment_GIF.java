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
            case 19:
                Glide.with(getContext()).load(R.drawable.leg_push_up).into(getImage);
                break;
            case 20:
                Glide.with(getContext()).load(R.drawable.clap_push_up).into(getImage);
                break;
            case 21:
                Glide.with(getContext()).load(R.drawable.close_hands_push_up).into(getImage);
                break;
            case 22:         //팔굽혀펴기일 경우
                Glide.with(getContext()).load(R.drawable.pushup).into(getImage);
                break;
            case 23:
                Glide.with(getContext()).load(R.drawable.push_up_to_side_plank).into(getImage);
                break;
            case 24:
                Glide.with(getContext()).load(R.drawable.wide_hand_push_up).into(getImage);
                break;
            case 25:
                Glide.with(getContext()).load(R.drawable.hyperextension_with_no_bench).into(getImage);
                break;
            case 26:
                Glide.with(getContext()).load(R.drawable.spine_twist).into(getImage);
                break;
            case 27:
                Glide.with(getContext()).load(R.drawable.ab_draw_leg_slide).into(getImage);
                break;
            case 28:
                Glide.with(getContext()).load(R.drawable.abdominal_pendulum).into(getImage);
                break;
            case 29:
                Glide.with(getContext()).load(R.drawable.airbike).into(getImage);
                break;
            case 30:
                Glide.with(getContext()).load(R.drawable.alternate_heel_touchers).into(getImage);
                break;
            case 31:
                Glide.with(getContext()).load(R.drawable.alternate_leg_bridge).into(getImage);
                break;
            case 32:
                Glide.with(getContext()).load(R.drawable.alternate_reach_and_catch).into(getImage);
                break;
            case 33:
                Glide.with(getContext()).load(R.drawable.alternating_arm_cobra).into(getImage);
                break;
            case 34:
                Glide.with(getContext()).load(R.drawable.bent_knee_hip_raise).into(getImage);
                break;
            case 35:
                Glide.with(getContext()).load(R.drawable.bent_knee_hundreds).into(getImage);
                break;
            case 36:
                Glide.with(getContext()).load(R.drawable.butt_ups).into(getImage);
                break;
            case 37:
                Glide.with(getContext()).load(R.drawable.cobra).into(getImage);
                break;
            case 38:
                Glide.with(getContext()).load(R.drawable.cross_body_crunch).into(getImage);
                break;
            case 39:
                Glide.with(getContext()).load(R.drawable.crunch_with_hands_overhead).into(getImage);
                break;
            case 40:
                Glide.with(getContext()).load(R.drawable.crunches).into(getImage);
                break;
            case 41:
                Glide.with(getContext()).load(R.drawable.double_leg_hundreds).into(getImage);
                break;
            case 42:
                Glide.with(getContext()).load(R.drawable.frog_sit_ups).into(getImage);
                break;
            case 43:
                Glide.with(getContext()).load(R.drawable.jackknife_sit_up).into(getImage);
                break;
            case 44:
                Glide.with(getContext()).load(R.drawable.janda_sit_up).into(getImage);
                break;
            case 45:
                Glide.with(getContext()).load(R.drawable.leg_raise).into(getImage);
                break;
            case 46:
                Glide.with(getContext()).load(R.drawable.lying_alternate_floor_leg_raise).into(getImage);
                break;
            case 47:
                Glide.with(getContext()).load(R.drawable.lying_floor_knee_raise).into(getImage);
                break;
            case 48:
                Glide.with(getContext()).load(R.drawable.lying_to_side_plank).into(getImage);
                break;
            case 49:
                Glide.with(getContext()).load(R.drawable.plank).into(getImage);
                break;
            case 50:
                Glide.with(getContext()).load(R.drawable.plank_with_side_kick).into(getImage);
                break;
            case 51:
                Glide.with(getContext()).load(R.drawable.situp).into(getImage);
                break;
            case 52:
                Glide.with(getContext()).load(R.drawable.tuck_crunch).into(getImage);
                break;
            case 53:
                Glide.with(getContext()).load(R.drawable.twisting_floor_crunch).into(getImage);
                break;
            case 54:
                Glide.with(getContext()).load(R.drawable.two_leg_slide).into(getImage);
                break;
            case 55:
                Glide.with(getContext()).load(R.drawable.v_ups).into(getImage);
                break;
            case 56:
                Glide.with(getContext()).load(R.drawable.modified_push_up_to_forearms).into(getImage);
                break;
            case 57:
                Glide.with(getContext()).load(R.drawable.bridge).into(getImage);
                break;
            case 58:
                Glide.with(getContext()).load(R.drawable.flutter_kick).into(getImage);
                break;
            case 59:
                Glide.with(getContext()).load(R.drawable.glute_kickback).into(getImage);
                break;
            case 60:
                Glide.with(getContext()).load(R.drawable.leg_lift).into(getImage);
                break;
            case 61:
                Glide.with(getContext()).load(R.drawable.one_leg_kick_back).into(getImage);
                break;
            case 62:
                Glide.with(getContext()).load(R.drawable.single_leg_glute_bridge).into(getImage);
                break;
            case 63:
                Glide.with(getContext()).load(R.drawable.standing_adductor).into(getImage);
                break;
            case 64:
                Glide.with(getContext()).load(R.drawable.standing_glute_kickback).into(getImage);
                break;
            case 65:
                Glide.with(getContext()).load(R.drawable.straight_leg_outer_hip_abductor).into(getImage);
                break;
            case 66:
                Glide.with(getContext()).load(R.drawable.handstand_pushups).into(getImage);
                break;
            case 67:
                Glide.with(getContext()).load(R.drawable.close_triceps_pushup).into(getImage);
                break;
            case 68:
                Glide.with(getContext()).load(R.drawable.bodyweight_lunge).into(getImage);
                break;
            case 69:
                Glide.with(getContext()).load(R.drawable.bodyweight_side_lunge).into(getImage);
                break;
            case 70:
                Glide.with(getContext()).load(R.drawable.body_weight_walking_lunge).into(getImage);
                break;
            case 71:
                Glide.with(getContext()).load(R.drawable.bodyweight_wall_squat).into(getImage);
                break;
            case 72:
                Glide.with(getContext()).load(R.drawable.freehand_jump_squat).into(getImage);
                break;
            case 73:
                Glide.with(getContext()).load(R.drawable.one_leg_bodyweight_squat).into(getImage);
                break;
            case 74:
                Glide.with(getContext()).load(R.drawable.prisoner_squat).into(getImage);
                break;
            case 75:
                Glide.with(getContext()).load(R.drawable.rear_bodyweight_lunge).into(getImage);
                break;
            case 76:
                Glide.with(getContext()).load(R.drawable.rocket_jump).into(getImage);
                break;
            case 77:
                Glide.with(getContext()).load(R.drawable.sit_squat).into(getImage);
                break;
            case 78:
                Glide.with(getContext()).load(R.drawable.star_jump).into(getImage);
                break;
            case 79:
                Glide.with(getContext()).load(R.drawable.body_weight_standing_calf_raise).into(getImage);
                break;
            case 80:
                Glide.with(getContext()).load(R.drawable.one_leg_floor_calf_raise).into(getImage);
                break;
            case 81:
                Glide.with(getContext()).load(R.drawable.standing_one_leg_bodyweight_calf_raise).into(getImage);
                break;
        }



        return view;
    }
}
