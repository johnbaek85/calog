package com.example.calog;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CardioAdapter extends BaseAdapter {
    ArrayList<FitnessVO> array;

    public CardioAdapter(ArrayList<FitnessVO> array) {
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardio, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        TextView FitnessName = convertView.findViewById(R.id.FitnessName);
        TextView consumeCal = convertView.findViewById(R.id.consumeCal);

        final FitnessVO vo = array.get(position);
        FitnessName.setText(vo.getFitnessMenuNmae());
        consumeCal.setText(vo.getUsedCalorie());

        return convertView;
    }
}
