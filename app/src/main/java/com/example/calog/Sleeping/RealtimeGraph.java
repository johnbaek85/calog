//package com.example.calog;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.Viewport;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.Random;
//
//public class RealtimeGraph extends Fragment {
//
//    private static final Random RANDOM = new Random();
//    private LineGraphSeries<DataPoint> series;
//    private int lastX = 0;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        View view = inflater.inflate(R.layout.activity_realtime_graph,container,false);
//
//        //get view instance
//        GraphView graph = (GraphView) view.findViewById(R.id.graph);
//        //data
//        series = new LineGraphSeries<DataPoint>();
//        graph.addSeries(series);
//        //customize a little bit viewport
//        Viewport viewport = graph.getViewport();
//        viewport.setYAxisBoundsManual(true);
//        viewport.setMinY(0);
//        viewport.setMaxY(10);
//        viewport.setScrollable(true);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //we are going to simulate real time with thread that append data
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            addEntry();
//                        }
//                    });
//                    // sleep to slow down the add of entries
//                    try {
//                        Thread.sleep(600);
//                    } catch (InterruptedException e) {
//
//                    }
//                }
//            }
//        }).start();
//    }
//
//    //add random data to graph
//    private void addEntry() {
//        //here, we choose to display max 10points on the viewport and we scroll to end
//        series.appendData(new DataPoint(lastX++,RANDOM.nextDouble()*10d),true,10);
//    }
//}