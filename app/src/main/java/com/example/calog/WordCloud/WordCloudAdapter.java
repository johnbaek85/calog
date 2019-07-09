package com.example.calog.WordCloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;

import java.util.ArrayList;

public class WordCloudAdapter extends RecyclerView.Adapter<WordCloudAdapter.ItemViewHolder> {
    Context context;
    ArrayList<CrawlingVO> array;

    public WordCloudAdapter(Context context, ArrayList<CrawlingVO> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_wordcloud_keyword, parent, false
        );

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.wordCloudTitle.setText(array.get(position).getTitle());
        holder.wordCloudLink.setText(array.get(position).getLink());

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView wordCloudTitle, wordCloudContent, wordCloudLink;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            wordCloudTitle = itemView.findViewById(R.id.WordCloudTitle);
            wordCloudLink = itemView.findViewById(R.id.WordClouLink);
        }
    }
}
