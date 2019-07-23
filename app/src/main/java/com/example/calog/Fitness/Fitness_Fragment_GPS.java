package com.example.calog.Fitness;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.calog.R;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.zip.Inflater;

public class Fitness_Fragment_GPS extends Fragment
{
    int FitnessMenuId;
    public Fitness_Fragment_GPS(int fitnessMenuId) {
        this.FitnessMenuId = fitnessMenuId;
    }

    //다음 맵
    MapView mapView;
    MapPolyline polyline=new MapPolyline(); // 경로 그리기 용도

    Button btnStart;
    Button btnStop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getMyLocation();

        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        mapView=new MapView(getActivity());

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.55110549926758, 126.92088317871094), true);

        LinearLayout linearMap = view.findViewById(R.id.map);

        linearMap.addView(mapView);

//
//        View exerView=getLayoutInflater().inflate(R.layout.activity_exercise,null);
//
//        exerView.findViewById(R.id.btnStart);
//
//        exerView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
//            }
//        });

        return view;
    }

    //gps 권한요청
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }
}
