package com.wang.avi.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.animation.ValueAnimator;
import com.wang.avi.Indicator;

import java.util.ArrayList;

/**
 * Created by maitrungduc1410 on 2025/06/28.
 * AudioEqualizer animation - 4 vertical bars with different heights and rhythms
 */
public class AudioEqualizerIndicator extends Indicator {

    private static final int BAR_COUNT = 4;
    private float[] scaleFloats = new float[BAR_COUNT];

    public AudioEqualizerIndicator() {
        // Initialize with different starting values
        for (int i = 0; i < BAR_COUNT; i++) {
            scaleFloats[i] = 0.5f;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float barWidth = getWidth() / 9.0f; // 4 bars with spacing
        float spacing = barWidth / 2.0f;
        float totalBarsWidth = BAR_COUNT * barWidth + (BAR_COUNT - 1) * spacing;
        float startX = (getWidth() - totalBarsWidth) / 2.0f;
        float maxBarHeight = getHeight() * 0.8f; // Leave some margin
        float bottom = getHeight() - getHeight() * 0.1f; // Align to bottom with margin

        for (int i = 0; i < BAR_COUNT; i++) {
            float x = startX + i * (barWidth + spacing);
            float barHeight = maxBarHeight * scaleFloats[i];
            float top = bottom - barHeight;

            canvas.drawRect(x, top, x + barWidth, bottom, paint);
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        // Different durations for each bar to create audio equalizer effect
        int[] durations = new int[]{4300, 2500, 1700, 3100};
        float[] values = new float[]{0.0f, 0.7f, 0.4f, 0.05f, 0.95f, 0.3f, 0.9f, 0.4f, 0.15f, 0.18f, 0.75f, 0.01f};

        for (int i = 0; i < BAR_COUNT; i++) {
            final int index = i;

            ValueAnimator heightAnim = ValueAnimator.ofFloat(values);
            heightAnim.setDuration(durations[i]);
            heightAnim.setRepeatCount(-1);

            addUpdateListener(heightAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(heightAnim);
        }
        return animators;
    }
}
