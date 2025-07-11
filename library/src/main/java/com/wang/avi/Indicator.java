package com.wang.avi;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jack Wang on 2016/8/5.
 */

public abstract class Indicator extends Drawable implements Animatable {

    private HashMap<ValueAnimator,ValueAnimator.AnimatorUpdateListener> mUpdateListeners=new HashMap<>();

    private ArrayList<ValueAnimator> mAnimators;
    private int alpha = 255;
    private static final Rect ZERO_BOUNDS_RECT = new Rect();
    protected Rect drawBounds = ZERO_BOUNDS_RECT;

    private boolean mHasAnimators;
    private float mAnimationSpeedMultiplier = 1.0f;

    private Paint mPaint=new Paint();

    public Indicator(){
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    public int getColor() {
        return mPaint.getColor();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * Set the animation speed multiplier.
     * @param speedMultiplier Speed multiplier (1.0f = normal speed, 2.0f = 2x speed, 0.5f = half speed)
     */
    public void setAnimationSpeedMultiplier(float speedMultiplier) {
        this.mAnimationSpeedMultiplier = speedMultiplier;
        // If animators are already created, apply the speed multiplier
        if (mHasAnimators && mAnimators != null) {
            applySpeedMultiplierToAnimators();
        }
    }

    /**
     * Get the current animation speed multiplier.
     * @return Current speed multiplier
     */
    public float getAnimationSpeedMultiplier() {
        return mAnimationSpeedMultiplier;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void draw(Canvas canvas) {
        draw(canvas,mPaint);
    }

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract ArrayList<ValueAnimator> onCreateAnimators();

    @Override
    public void start() {
        ensureAnimators();

        if (mAnimators == null) {
            return;
        }

        // If the animators has not ended, do nothing.
        if (isStarted()) {
            return;
        }
        startAnimators();
        invalidateSelf();
    }

    private void startAnimators() {
        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);

            //when the animator restart , add the updateListener again because they
            // was removed by animator stop .
            ValueAnimator.AnimatorUpdateListener updateListener=mUpdateListeners.get(animator);
            if (updateListener!=null){
                animator.addUpdateListener(updateListener);
            }

            animator.start();
        }
    }

    private void stopAnimators() {
        if (mAnimators!=null){
            for (ValueAnimator animator : mAnimators) {
                if (animator != null && animator.isStarted()) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
    }

    private void ensureAnimators() {
        if (!mHasAnimators) {
            mAnimators = onCreateAnimators();
            mHasAnimators = true;
            // Apply speed multiplier to newly created animators
            applySpeedMultiplierToAnimators();
        }
    }

    /**
     * Apply the speed multiplier to all animators
     */
    private void applySpeedMultiplierToAnimators() {
        if (mAnimators != null && mAnimationSpeedMultiplier != 1.0f) {
            for (ValueAnimator animator : mAnimators) {
                if (animator != null) {
                    long originalDuration = animator.getDuration();
                    long newDuration = (long) (originalDuration / mAnimationSpeedMultiplier);
                    animator.setDuration(newDuration);
                }
            }
        }
    }

    @Override
    public void stop() {
        stopAnimators();
    }

    private boolean isStarted() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isStarted();
        }
        return false;
    }

    @Override
    public boolean isRunning() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isRunning();
        }
        return false;
    }

    /**
     *  Your should use this to add AnimatorUpdateListener when
     *  create animator , otherwise , animator doesn't work when
     *  the animation restart .
     * @param updateListener
     */
    public void addUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener){
        mUpdateListeners.put(animator,updateListener);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        setDrawBounds(bounds);
    }

    public void setDrawBounds(Rect drawBounds) {
        setDrawBounds(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom);
    }

    public void setDrawBounds(int left, int top, int right, int bottom) {
        this.drawBounds = new Rect(left, top, right, bottom);
    }

    public void postInvalidate(){
        invalidateSelf();
    }

    public Rect getDrawBounds() {
        return drawBounds;
    }

    public int getWidth(){
        return drawBounds.width();
    }

    public int getHeight(){
        return drawBounds.height();
    }

    public int centerX(){
        return drawBounds.centerX();
    }

    public int centerY(){
        return drawBounds.centerY();
    }

    public float exactCenterX(){
        return drawBounds.exactCenterX();
    }

    public float exactCenterY(){
        return drawBounds.exactCenterY();
    }

}
