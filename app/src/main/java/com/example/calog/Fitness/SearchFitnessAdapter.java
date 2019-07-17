package com.example.calog.Fitness;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.VO.FitnessVO;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class SearchFitnessAdapter extends RecyclerView.Adapter<SearchFitnessAdapter.ViewHolder> {
    Context context;
    List<FitnessVO> array;
    String fitness_date;
    String user_id;

    public SearchFitnessAdapter(Context context, List<FitnessVO> array, String date, String user_id) {
        this.context = context;
        this.array = array;
        this.fitness_date = date;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, final int i) {
        //new ThreadImage(viewholder.fitness_menu_image, array.get(i).getFitness_menu_image()).execute();
        //System.out.println("array.get(i).getFitness_menu_image()"+array.get(i).getFitness_menu_image());

        //피카소로 사진 넣기
        Picasso.with(context).load(array.get(i).getFitness_menu_image()).into(viewholder.fitness_menu_image);
        viewholder.fitness_menu_name.setText(array.get(i).getFitness_menu_name());
        viewholder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseActivity.class);
                intent.putExtra("운동타입", array.get(i).getFitness_type_id());
                intent.putExtra("운동명", array.get(i).getFitness_menu_id());
                intent.putExtra("단위칼로리", array.get(i).getUnit_calorie());
                intent.putExtra("select_date", fitness_date);
                intent.putExtra("user_id", user_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fitness_menu_name;
        ImageView fitness_menu_image;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fitness_menu_name = itemView.findViewById(R.id.FitnessName);
            fitness_menu_image = itemView.findViewById(R.id.image);

            layout = itemView.findViewById(R.id.layout);

        }
    }

    /*//이미지 호출용
    public class ThreadImage extends AsyncTask<String, Integer, Bitmap> {
        ImageView image;
        String url;
        Bitmap bitmap;
        public ThreadImage(ImageView image, String url) {
            this.image = image;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            bitmap = null;
            if(bitmap == null)
            {
                try {
                    InputStream is = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(is);

                    is.close();
                } catch (Exception e) {
                    System.out.println("에러>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.toString());
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            image.setImageBitmap(bitmap);
        }
    }*/
}
