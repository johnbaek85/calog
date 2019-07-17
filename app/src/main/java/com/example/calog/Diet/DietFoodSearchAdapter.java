package com.example.calog.Diet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.VO.DietMenuVO;

import java.util.List;

public class DietFoodSearchAdapter extends RecyclerView.Adapter<DietFoodSearchAdapter.ViewHolder> {

    Context context;
    List<DietMenuVO> dietMenus;
    BundleAdapter bundleAdapter;
    List<DietMenuVO> mylist;

    public DietFoodSearchAdapter(Context context, List<DietMenuVO> dietMenus, List<DietMenuVO> mylist, BundleAdapter bundleAdapter) {
        this.context = context;
        this.dietMenus = dietMenus;
        this.mylist=mylist;
        this.bundleAdapter = bundleAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diet_food_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        holder.txtMenuName.setText(dietMenus.get(position).getDiet_menu_name());
        holder.txtCalorie.setText(String.valueOf(dietMenus.get(position).getCalorie())+" kal");
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.add(dietMenus.get(position));
                bundleAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dietMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMenuName, txtCalorie;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
            txtCalorie = itemView.findViewById(R.id.txtCalorie);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
