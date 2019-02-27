package com.suji.ish.suji.view;

import android.content.Context;
import android.view.View;

import com.suji.ish.suji.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author ish
 */
public class MenuPopupWindow extends BasePopupWindow {

    public MenuPopupWindow(Context context) {
        super(context);
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_menu_window);
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
//    @Override
//    protected Animation onCreateShowAnimation() {
//        return getDefaultScaleAnimation(true);
//    }
//
//    @Override
//    protected Animation onCreateDismissAnimation() {
//        return getDefaultScaleAnimation(false);
//    }

//    ObjectAnimator mAnimator1;
//    ObjectAnimator mAnimator2;
//    LinearLayout mAddNoteBtn;
//    LinearLayout mAddNoteBookBtn;
//
//    public static MenuPopupWindow create(Context context) {
//        return new MenuPopupWindow(context);
//    }
//
//    protected MenuPopupWindow(Context context) {
//        setContext(context);
//    }
//
//    @Override
//    protected void initAttributes() {
//        setContentView(R.layout.popup_menu_window, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        setFocusAndOutsideEnable(false)
//                .setBackgroundDimEnable(true)
//                .setFocusAndOutsideEnable(true)
//                .setDimColor(Color.parseColor("#dfe0d1"))
//                .setDimValue(0.9f);
//    }
//
//    @Override
//    protected void initViews(View view, MenuPopupWindow menuPopupWindow) {
//        mAddNoteBtn = findViewById(R.id.popup_add_note);
//        mAddNoteBookBtn = findViewById(R.id.popup_add_notebook);
//
//
//        float dp = ToolsUtils.getInstance().dp2px(333);
//        mAnimator1 = ObjectAnimator.ofFloat(mAddNoteBookBtn, "translationY",
//                dp, dp/5, -dp/10, dp/20, -dp/50, 0f);
//        mAnimator1.setDuration(700);
//        mAnimator1.start();
//
//        mAnimator2 = ObjectAnimator.ofFloat(mAddNoteBtn, "translationY",
//                dp, dp/5, -dp/10, dp/20, -dp/50, 0f);
//        mAnimator2.setDuration(800);
//        mAnimator2.start();
//    }
//
//    @Override
//    public void onDismiss() {
//        mAnimator1.cancel();
//        mAnimator2.cancel();
//        super.onDismiss();
//    }
}
