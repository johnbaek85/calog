package com.example.calog.WordCloud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;

import java.util.ArrayList;

public class WordCloudAdapter extends RecyclerView.Adapter<WordCloudAdapter.ItemViewHolder> {
    Context context;
    ArrayList<CrawlingVO> array;
    String siteUrl = "https://terms.naver.com";

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
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.wordCloudTitle.setText(array.get(position).getTitle());
        //    holder.wordCloudLink.setText(array.get(position).getLink());
        holder.wordCloudTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link = siteUrl + array.get(position).getLink();
                Intent intent = new Intent(context, WordCloudActivity.class);
                // 링크 주소 확인
                //   System.out.println("adapter Link : " + link);
                intent.putExtra("Link", link);

                context.startActivity(intent);
            }
        });

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
            //    wordCloudLink = itemView.findViewById(R.id.WordClouLink);
        }
    }
}
