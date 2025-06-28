package com.example.avloadingindicatorview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack Wang on 2016/8/5.
 */

public class SampleActivity extends AppCompatActivity {

    private LinearLayout indicatorsContainer;
    private SeekBar speedSlider;
    private TextView speedLabel;
    private List<AVLoadingIndicatorView> allIndicators = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        indicatorsContainer = findViewById(R.id.indicatorsContainer);
        speedSlider = findViewById(R.id.speedSlider);
        speedLabel = findViewById(R.id.speedLabel);

        setupSpeedControl();
        createIndicatorGrid();
    }

    private void setupSpeedControl() {
        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Convert progress (0-375) to speed (0.25x - 4.0x)
                float speed = 0.25f + (progress / 100.0f);
                speedLabel.setText(String.format("Speed: %.2fx", speed));
                
                // Update all indicators
                updateAllIndicatorSpeeds(speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Set initial speed to 1.0x
        speedSlider.setProgress(75); // 75/100 = 0.75, so 0.25 + 0.75 = 1.0
    }

    private void createIndicatorGrid() {
        LayoutInflater inflater = getLayoutInflater();
        
        // Create rows with 4 columns each (matching iOS)
        LinearLayout currentRow = null;
        
        for (int i = 0; i < INDICATORS.length; i++) {
            // Create new row every 4 items
            if (i % 4 == 0) {
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                indicatorsContainer.addView(currentRow);
            }
            
            // Inflate item view
            View itemView = inflater.inflate(R.layout.item_indicator, currentRow, false);
            
            // Set equal weight for 4 columns
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
            );
            itemView.setLayoutParams(params);
            
            // Configure the indicator
            setupIndicatorItem(itemView, i);
            
            currentRow.addView(itemView);
        }
    }

    private void setupIndicatorItem(View itemView, final int position) {
        AVLoadingIndicatorView indicatorView = itemView.findViewById(R.id.indicator);
        TextView animationName = itemView.findViewById(R.id.animationName);
        View itemLayout = itemView.findViewById(R.id.itemLayout);
        
        String indicatorName = INDICATORS[position];
        
        // Set animation name with number (1-based): "1. Ball Pulse"
        String readableName = getReadableAnimationName(indicatorName);
        animationName.setText((position + 1) + ". " + readableName);
        
        // Configure and start the indicator
        indicatorView.setIndicator(indicatorName);
        indicatorView.setAnimationSpeedMultiplier(getCurrentSpeed());
        indicatorView.show();
        
        // Add to our list for speed control
        allIndicators.add(indicatorView);
        
        // Set click listener
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SampleActivity.this, IndicatorActivity.class);
                intent.putExtra("indicator", INDICATORS[position]);
                startActivity(intent);
            }
        });
    }

    private void updateAllIndicatorSpeeds(float speed) {
        for (AVLoadingIndicatorView indicator : allIndicators) {
            if (indicator != null) {
                indicator.setAnimationSpeedMultiplier(speed);
                // Just set the speed - don't restart the animations
                // The speed will be applied to existing running animations
            }
        }
    }

    private float getCurrentSpeed() {
        int progress = speedSlider.getProgress();
        return 0.25f + (progress / 100.0f);
    }

    private String getReadableAnimationName(String className) {
        // Convert class names like "BallPulseIndicator" to "Ball Pulse"
        if (className.contains(".")) {
            className = className.substring(className.lastIndexOf(".") + 1);
        }
        
        if (className.endsWith("Indicator")) {
            className = className.substring(0, className.length() - "Indicator".length());
        }
        
        // Add spaces before capital letters
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < className.length(); i++) {
            char c = className.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                result.append(' ');
            }
            result.append(c);
        }
        
        return result.toString();
    }

    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",              // 1
            "BallGridPulseIndicator",          // 2
            "BallClipRotateIndicator",         // 3
            "SquareSpinIndicator",             // 4
            "BallClipRotatePulseIndicator",    // 5
            "BallClipRotateMultipleIndicator", // 6
            "BallPulseRiseIndicator",          // 7
            "BallRotateIndicator",             // 8
            "CubeTransitionIndicator",         // 9
            "BallZigZagIndicator",             // 10
            "BallZigZagDeflectIndicator",      // 11
            "BallTrianglePathIndicator",       // 12
            "BallScaleIndicator",              // 13
            "LineScaleIndicator",              // 14
            "LineScalePartyIndicator",         // 15
            "BallScaleMultipleIndicator",      // 16
            "BallPulseSyncIndicator",          // 17
            "BallBeatIndicator",               // 18
            "LineScalePulseOutIndicator",      // 19
            "LineScalePulseOutRapidIndicator", // 20
            "BallScaleRippleIndicator",        // 21
            "BallScaleRippleMultipleIndicator", // 22
            "BallSpinFadeLoaderIndicator",     // 23
            "LineSpinFadeLoaderIndicator",     // 24
            "TriangleSkewSpinIndicator",       // 25
            "PacmanIndicator",                 // 26
            "BallGridBeatIndicator",           // 27
            "SemiCircleSpinIndicator",         // 28
            // Removed: "BallRotateChaseIndicator" (was 29)
            "OrbitIndicator",                  // 29 (was 30)
            "AudioEqualizerIndicator",         // 30 (was 31)
            // Removed: "CircleStrokeSpinIndicator" (was 32)
            "BallDoubleBounceIndicator"        // 31 (was 33)
    };
}
