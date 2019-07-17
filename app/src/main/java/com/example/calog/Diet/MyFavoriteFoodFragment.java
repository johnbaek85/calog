package com.example.calog.Diet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calog.R;
import com.example.calog.RemoteService;
import com.example.calog.VO.DietMenuVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calog.RemoteService.BASE_URL;

class MyFavoriteFoodFragment extends Fragment {

    DietFoodSearchAdapter adapter;
    RecyclerView dietMenuList;
    List<DietMenuVO> dietMenuArray;
    BundleAdapter bundleAdapter;
    List<DietMenuVO> myList;

    Retrofit retrofit;
    RemoteService rs;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_my_favorite_food, container, false);

        dietMenuList = layout.findViewById(R.id.dietMenuList);

        Bundle bundle = getArguments();
        bundleAdapter = (BundleAdapter)bundle.getSerializable("bundleAdapter");
        myList = bundle.getParcelableArrayList("myList");

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        dietMenuList.setLayoutManager(manager);

        //intent = getActivity().getIntent();

        retrofit = new Retrofit.Builder() //Retrofit 빌더생성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rs = retrofit.create(RemoteService.class); //API 인터페이스 생성

        Call<List<DietMenuVO>> call = rs.UserFavoriteMenuList("spider");
        call.enqueue(new Callback<List<DietMenuVO>>() {
            @Override
            public void onResponse(Call<List<DietMenuVO>> call, Response<List<DietMenuVO>> response) {
                dietMenuArray = response.body();
                adapter = new DietFoodSearchAdapter(getContext(), dietMenuArray, myList, bundleAdapter);
                dietMenuList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<DietMenuVO>> call, Throwable t) {
                System.out.println("<<<<<<<<<<<<<<<<<< Error : " + t.toString());
            }
        });

        return layout;
    }
}
