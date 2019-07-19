package com.example.calog.Fitness;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.VO.FitnessVO;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MyFitnessListAdapter extends RecyclerView.Adapter<MyFitnessListAdapter.ViewHolder> {
    Context context;
    List<FitnessVO> array;
    int type;

    public MyFitnessListAdapter(Context context, List<FitnessVO> array, int type) {
        this.context = context;
        this.array = array;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ondayexercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, final int i) {
        //new ThreadImage(viewholder.fitness_menu_image, array.get(i).getFitness_menu_image()).execute();
        //피카소로 사진 넣기
        Picasso.with(context).load(array.get(i).getFitness_menu_image()).into(viewholder.fitness_menu_image);
        viewholder.fitness_menu_name.setText(array.get(i).getFitness_menu_name());
        double unitCal = array.get(i).getUnit_calorie();
        int seconds = array.get(i).getFitness_seconds();
        int steps = array.get(i).getNumber_steps();
        String eachConsumedCalrorie = String.format("%.1f", unitCal * seconds);
        viewholder.eachCalorie.setText(eachConsumedCalrorie + "kcal");


        long fitnessTime = array.get(i).getFitness_seconds();

        int fth = (int) (fitnessTime / 3600);
        int ftm = (int) (fitnessTime - fth * 3600) / 60;
        int fts = (int) (fitnessTime - fth * 3600 - ftm * 60);
        String strH = fth < 10 ? "0" + fth : fth + "";
        String strM = ftm < 10 ? "0" + ftm : ftm + "";
        String strS = fts < 10 ? "0" + fts : fts + "";
        viewholder.eachTime.setText(strH + "시간 " + strM + "분 " + strS + "초");


        switch (type) {
            case 1:
                viewholder.eachDistance.setText(array.get(i).getDistance() + "m");
                viewholder.eachStep.setText(steps + "걸음");
                break;
            case 2:
                viewholder.eachDistance.setVisibility(View.INVISIBLE);
                viewholder.eachStep.setVisibility(View.INVISIBLE);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fitness_menu_name, eachTime, eachCalorie, eachDistance, eachStep;
        ImageView fitness_menu_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fitness_menu_name = itemView.findViewById(R.id.FitnessName);
            fitness_menu_image = itemView.findViewById(R.id.image);
            eachTime = itemView.findViewById(R.id.eachTime);
            eachCalorie = itemView.findViewById(R.id.eachCalorie);
            eachDistance = itemView.findViewById(R.id.eachDistance);
            eachStep = itemView.findViewById(R.id.eachStep);


        }
    }


    /*//이미지 호출용
    public class ThreadImage extends AsyncTask<String, Integer, Bitmap> {
        ImageView image;
        String url;

        public ThreadImage(ImageView image, String url) {
            this.image = image;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }*/


}
