package com.wang.avi.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import com.wang.avi.Indicator;

import java.util.ArrayList;

/**
 * Created by maitrungduc1410 on 2025/06/28.
 * Orbit animation - core with satellite and animated rings
 */
public class OrbitIndicator extends Indicator {

    private float ring1Scale = 0.0f;
    private float ring1Alpha = 1.0f;
    private float ring2Scale = 0.0f;
    private float ring2Alpha = 1.0f;
    private float coreScale = 1.0f;
    private float satelliteRotation = 0.0f;

    // Configuration - match iOS values
    private final float satelliteCoreRatio = 0.25f; // iOS: 0.25
    private final float distanceRatio = 1.5f; // iOS: 1.5

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float size = Math.min(getWidth(), getHeight());
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;
        
        // iOS sizing: coreSize = size.width / (1 + satelliteCoreRatio + distanceRatio)
        float coreSize = size / (1 + satelliteCoreRatio + distanceRatio);
        float satelliteSize = coreSize * satelliteCoreRatio;

        // Draw ring 1 - filled pulse area like iOS
        Paint ringPaint1 = new Paint(paint);
        ringPaint1.setStyle(Paint.Style.FILL);
        ringPaint1.setAlpha((int) (ring1Alpha * 255 * 0.6f)); // Semi-transparent
        
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.scale(ring1Scale, ring1Scale);
        canvas.drawCircle(0, 0, coreSize / 2.0f, ringPaint1);
        canvas.restore();

        // Draw ring 2 - filled pulse area like iOS
        Paint ringPaint2 = new Paint(paint);
        ringPaint2.setStyle(Paint.Style.FILL);
        ringPaint2.setAlpha((int) (ring2Alpha * 255 * 0.6f)); // Semi-transparent
        
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.scale(ring2Scale, ring2Scale);
        canvas.drawCircle(0, 0, coreSize / 2.0f, ringPaint2);
        canvas.restore();

        // Draw core
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.scale(coreScale, coreScale);
        canvas.drawCircle(0, 0, coreSize / 2.0f, paint);
        canvas.restore();

        // Draw satellite
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(satelliteRotation);
        canvas.translate((size - satelliteSize) / 2.0f, 0); // iOS satellite positioning
        canvas.drawCircle(0, 0, satelliteSize / 2.0f, paint);
        canvas.restore();
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        int duration = 1900;

        // Ring 1 animations - matches iOS keyframe timing: @[@0, @0.45, @0.45, @1] values @[@0, @0, @1.3, @2.5]
        ValueAnimator ring1Anim = ValueAnimator.ofFloat(0.0f, 1.0f);
        ring1Anim.setDuration(duration);
        ring1Anim.setRepeatCount(-1);
        addUpdateListener(ring1Anim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                
                // iOS keyframe timing: stays 0 until 45%, then scales to 2.5x
                if (progress <= 0.45f) {
                    ring1Scale = 0.0f;
                    ring1Alpha = 1.0f;
                } else {
                    // Scale from 0 to 2.5 over the remaining 55% of animation
                    float scaleProgress = (progress - 0.45f) / 0.55f;
                    ring1Scale = scaleProgress * 2.5f;
                    ring1Alpha = 1.0f - scaleProgress; // Fade out as it scales
                }
                postInvalidate();
            }
        });
        animators.add(ring1Anim);

        // Ring 2 animations - matches iOS keyframe timing: @[@0, @0.55, @0.55, @1] values @[@0, @0, @1.3, @2.6]
        ValueAnimator ring2Anim = ValueAnimator.ofFloat(0.0f, 1.0f);
        ring2Anim.setDuration(duration);
        ring2Anim.setRepeatCount(-1);
        addUpdateListener(ring2Anim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                
                // iOS keyframe timing: stays 0 until 55%, then scales to 2.6x
                if (progress <= 0.55f) {
                    ring2Scale = 0.0f;
                    ring2Alpha = 1.0f;
                } else {
                    // Scale from 0 to 2.6 over the remaining 45% of animation
                    float scaleProgress = (progress - 0.55f) / 0.45f;
                    ring2Scale = scaleProgress * 2.6f;
                    // iOS opacity: @[@1.0, @1.0, @0.3, @0] at @[@0, @0.55, @0.65, @1]
                    if (progress <= 0.65f) {
                        ring2Alpha = 1.0f;
                    } else {
                        float alphaProgress = (progress - 0.65f) / 0.35f;
                        ring2Alpha = 1.0f - alphaProgress;
                    }
                }
                postInvalidate();
            }
        });
        animators.add(ring2Anim);

        // Core scale animation - matches iOS: @[@1, @1.3, @1.3, @1] at @[@0, @0.45, @0.55, @1]
        ValueAnimator coreScaleAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
        coreScaleAnim.setDuration(duration);
        coreScaleAnim.setRepeatCount(-1);
        addUpdateListener(coreScaleAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                
                if (progress <= 0.45f) {
                    // Scale from 1.0 to 1.3 over first 45%
                    float scaleProgress = progress / 0.45f;
                    coreScale = 1.0f + (scaleProgress * 0.3f);
                } else if (progress <= 0.55f) {
                    // Stay at 1.3 from 45% to 55%
                    coreScale = 1.3f;
                } else {
                    // Scale back from 1.3 to 1.0 over remaining 45%
                    float scaleProgress = (progress - 0.55f) / 0.45f;
                    coreScale = 1.3f - (scaleProgress * 0.3f);
                }
                postInvalidate();
            }
        });
        animators.add(coreScaleAnim);

        // Satellite rotation - continuous spinning (faster rotation)
        ValueAnimator satelliteRotationAnim = ValueAnimator.ofFloat(0.0f, 360.0f);
        satelliteRotationAnim.setDuration(duration);
        satelliteRotationAnim.setRepeatCount(-1);
        satelliteRotationAnim.setInterpolator(new LinearInterpolator());
        addUpdateListener(satelliteRotationAnim, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                satelliteRotation = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animators.add(satelliteRotationAnim);

        return animators;
    }
}
