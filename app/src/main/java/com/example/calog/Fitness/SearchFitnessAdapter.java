package com.example.calog.Fitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.VO.FitnessVO;

import java.util.List;

public class SearchFitnessAdapter extends RecyclerView.Adapter<SearchFitnessAdapter.ViewHolder> {
    Context context;
    List<FitnessVO> array;

    public SearchFitnessAdapter(Context context, List<FitnessVO> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.exerciseName.setText(array.get(i).getFitnessMenuName());
        holder.consumCal.setText(array.get(i).getUsedCalorie());

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseName, consumCal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName=itemView.findViewById(R.id.FitnessName);
            consumCal=itemView.findViewById(R.id.consumeCal);

        }
    }
}
