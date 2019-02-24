package com.suji.ish.suji.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.suji.ish.suji.R;
import com.suji.ish.suji.utils.ToolsUtils;

public class SearchLoadingView extends LinearLayout {

    private ImageView imageView;
    private ImageView imageViewShadow;
    ObjectAnimator mAnimator;
    ObjectAnimator mAnimator2;
    ObjectAnimator mAnimator3;
    ObjectAnimator mAnimator4;

    public SearchLoadingView(@NonNull Context context) {
        super(context);
    }

    public SearchLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_search_loading, this, true);
        imageView = findViewById(R.id.loading_search);
        imageViewShadow = findViewById(R.id.loading_shadow);
        playAnimation();
    }

    public SearchLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void playAnimation(){
        float dp = ToolsUtils.getInstance().dp2px(50);
        mAnimator= ObjectAnimator
                .ofFloat(imageView, "translationY",
                0,dp);
        mAnimator.setInterpolator(new CycleInterpolator(-0.5f));
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(1000);

        //旋转
        mAnimator2 = ObjectAnimator
                .ofFloat(imageView, "rotation",
                        0,40);
        mAnimator2.setInterpolator(new CycleInterpolator(-0.5f));
        mAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator2.setDuration(2000);

        mAnimator.start();
        mAnimator2.start();

        //阴影动画
//        //变大
        float dpWidthStart = ToolsUtils.getInstance().dp2px(60);
        float dpWidthEnd = ToolsUtils.getInstance().dp2px(30);
        mAnimator3 = ObjectAnimator
                .ofFloat(imageViewShadow, "scaleX",
                        1,1.5f);
        mAnimator3.setInterpolator(new CycleInterpolator(-0.5f));
        mAnimator3.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator3.setDuration(1000);

        //渐变
        mAnimator4 = ObjectAnimator
                .ofFloat(imageViewShadow, "alpha",
                        0.6f,1f);
        mAnimator4.setInterpolator(new CycleInterpolator(-0.5f));
        mAnimator4.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator4.setDuration(1000);
//
        mAnimator3.start();
        mAnimator4.start();
    }

    public void cancelAnimation(){
        mAnimator.cancel();
        mAnimator2.cancel();
        mAnimator3.cancel();
        mAnimator4.cancel();
    }

}
