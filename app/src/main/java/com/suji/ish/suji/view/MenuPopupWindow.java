package com.suji.ish.suji.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.suji.ish.suji.R;
import com.suji.ish.suji.utils.ToolsUtils;
import com.zyyoona7.popup.BasePopup;

/**
 * @author ish
 */
public class MenuPopupWindow extends BasePopup<MenuPopupWindow>{

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
                .setDimValue(0.9f);
    }

    @Override
    protected void initViews(View view, MenuPopupWindow menuPopupWindow) {
        mAddNoteBtn = findViewById(R.id.popup_add_note);
        mAddNoteBookBtn = findViewById(R.id.popup_add_notebook);


        float dp = ToolsUtils.getInstance().dp2px(view.getContext(),333);
        mAnimator1 = ObjectAnimator.ofFloat(mAddNoteBookBtn, "translationY",
                dp, dp/5, -dp/10, dp/20, -dp/50, 0f);
        mAnimator1.setDuration(700);
        mAnimator1.start();

        mAnimator2 = ObjectAnimator.ofFloat(mAddNoteBtn, "translationY",
                dp, dp/5, -dp/10, dp/20, -dp/50, 0f);
        mAnimator2.setDuration(800);
        mAnimator2.start();
    }

    @Override
    public void onDismiss() {
        mAnimator1.cancel();
        mAnimator2.cancel();
        super.onDismiss();
    }
}
