package com.example.avloadingindicatorview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Demo activity to showcase animation speed multiplier feature
 */
public class SpeedDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_demo);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Animation Speed Demo");
        }
    }
}
