package com.example.calog.Sleeping;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.calog.R;

public class SleepCheckResultDialog extends Dialog {
    LinearLayout DialogLayout;
    SleepCheckResultDialog sleepDialog;
    public SleepCheckResultDialog(@NonNull Context context, int theme_NoTitleBar_Fullscreen) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams window = new WindowManager.LayoutParams();
        window.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        TextView monthName = findViewById(R.id.monthName);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnHome = findViewById(R.id.btnHome);
        getWindow().setAttributes(window);
        setContentView(R.layout.activity_sleep_check_result);
    }
    public void mClick(View v){//수면측정중지버튼

    }
}
