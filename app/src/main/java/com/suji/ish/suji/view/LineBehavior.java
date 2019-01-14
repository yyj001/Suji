package com.suji.ish.suji.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.suji.ish.suji.R;

public class LineBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    private int mStartY;
    private float maxScrollY = 153;
    private float mMinMarginTop;
    private float mMaxMarginTop;

    public LineBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (dependency.getId() == R.id.notebook_recyclerview) {
            Resources resources = dependency.getResources();
            mMinMarginTop = resources.getDimension(R.dimen.fragment_headerline_min_margintop);
            mMaxMarginTop = resources.getDimension(R.dimen.fragment_headerline_max_margintop);

            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (mStartY == 0) {
            mStartY = (int) dependency.getY();
        }
        float percent = (dependency.getY() - maxScrollY) / (mStartY - maxScrollY);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.setMargins(0, (int) (mMinMarginTop + percent * (mMaxMarginTop - mMinMarginTop)),0,15);
        child.setLayoutParams(lp);
        LinearLayout headerNotice = child.findViewById(R.id.header_notice);
        View line = child.findViewById(R.id.header_line);
        headerNotice.setAlpha((float) (percent-0.3));
        //线到顶才显示
        if (percent==0){
            line.setAlpha(1);
        }else{
            line.setAlpha(0);
        }
        return true;
    }
}

