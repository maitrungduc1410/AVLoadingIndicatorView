package com.wang.avi.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.animation.ValueAnimator;
import com.wang.avi.Indicator;

import java.util.ArrayList;

/**
 * Created by maitrungduc1410 on 2025/06/28.
 * BallDoubleBounce animation - two overlapping circles with alternating scale animations
 */
public class BallDoubleBounceIndicator extends Indicator {

    public static final float SCALE = 1.0f;

    // Scale values for the two circles
    private float[] scaleFloats = new float[]{SCALE, SCALE};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius = Math.min(getWidth(), getHeight()) / 2.5f; // Use quarter, not half
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        // Draw two overlapping circles with different scales and fixed transparency
        Paint ballPaint = new Paint(paint);
        ballPaint.setAlpha((int) (255 * 0.6f)); // 60% transparency like iOS
        
        for (int i = 0; i < 2; i++) {
            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, ballPaint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        int[] delays = new int[]{0, 1000}; // Second circle starts 1 second later
        
        for (int i = 0; i < 2; i++) {
            final int index = i;

            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1.0f, 0.0f, 1.0f);
            scaleAnim.setDuration(2000); // 2 second duration
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);

            addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }
        return animators;
    }
}
