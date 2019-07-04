package com.example.calog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietMenuAdapter extends RecyclerView.Adapter<DietMenuAdapter.ViewHolder> {

    Context context;
    ArrayList<DietMenuVO> dietMenus = new ArrayList<>();

    public DietMenuAdapter(Context context, ArrayList<DietMenuVO> dietMenus) {
        this.context = context;
        this.dietMenus = dietMenus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diet_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMenuName.setText(dietMenus.get(position).getDietMenuName());
        holder.txtCalorie.setText(dietMenus.get(position).getCalorie());

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMenuName, txtCalorie;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
            txtCalorie = itemView.findViewById(R.id.txtCalorie);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}