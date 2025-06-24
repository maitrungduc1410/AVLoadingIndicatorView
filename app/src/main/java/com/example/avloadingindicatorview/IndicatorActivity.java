package com.example.avloadingindicatorview;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Jack Wang on 2016/8/5.
 */

public class IndicatorActivity extends AppCompatActivity {

    private AVLoadingIndicatorView avi;
    private SeekBar speedSeekBar;
    private TextView speedValueText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);

        String indicator=getIntent().getStringExtra("indicator");
        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        speedSeekBar = findViewById(R.id.speedSeekBar);
        speedValueText = findViewById(R.id.speedValueText);
        
        avi.setIndicator(indicator);
        
        setupSpeedControl();
    }
    
    private void setupSpeedControl() {
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Convert progress (0-300) to speed multiplier (0.25-3.25)
                    float speedMultiplier = 0.25f + (progress / 100.0f);
                    avi.setAnimationSpeedMultiplier(speedMultiplier);
                    speedValueText.setText(String.format("Current: %.2fx", speedMultiplier));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        // Set initial value
        speedValueText.setText("Current: 1.0x");
    }

    public void hideClick(View view) {
        avi.hide();
        // or avi.smoothToHide();
    }

    public void showClick(View view) {
        avi.show();
        // or avi.smoothToShow();
    }
}
