package com.suji.ish.suji.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.suji.ish.suji.R;
import com.zyyoona7.popup.BasePopup;

/**
 * @author ish
 */
public class MenuPopupWindow extends BasePopup<MenuPopupWindow> implements View.OnClickListener{

    ObjectAnimator mAnimator1;
    ObjectAnimator mAnimator2;
    LinearLayout mAddNoteBtn;
    LinearLayout mAddNoteBookBtn;

    public static MenuPopupWindow create(Context context) {
        return new MenuPopupWindow(context);
    }

    protected MenuPopupWindow(Context context) {
        setContext(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.popup_menu_window, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(false)
                .setBackgroundDimEnable(true)
                .setFocusAndOutsideEnable(true)
                .setDimColor(Color.parseColor("#dfe0d1"))
                .setDimValue(0.8f);
    }

    @Override
    protected void initViews(View view, MenuPopupWindow menuPopupWindow) {
        mAddNoteBtn = findViewById(R.id.popup_add_note);
        mAddNoteBookBtn = findViewById(R.id.popup_add_notebook);


        mAnimator1 = ObjectAnimator.ofFloat(mAddNoteBookBtn, "translationY",
                1000f, 200f, -100f, 50f, -20f, 0f);
        mAnimator1.setDuration(700);
        mAnimator1.start();

        mAnimator2 = ObjectAnimator.ofFloat(mAddNoteBtn, "translationY",
                1000f, 200f, -100f, 50f, -20f, 0f);
        mAnimator2.setDuration(800);
        mAnimator2.start();


    }

    @Override
    public void onDismiss() {
        mAnimator1.cancel();
        mAnimator2.cancel();
        super.onDismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_notebook_btn:{
                break;
            }
            case R.id.add_word_btn:{
                break;
            }
            default:
        }
    }
}
