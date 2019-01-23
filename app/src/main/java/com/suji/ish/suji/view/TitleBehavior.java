package com.suji.ish.suji.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suji.ish.suji.R;

public class TitleBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    private int mStartY;
    private float mPreTextSize;
    private float mMinTextSize;
    private float maxScrollY;

    public TitleBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (dependency.getId() == R.id.notebook_recyclerview) {
            Resources resources = dependency.getResources();
            mPreTextSize = resources.getDimension(R.dimen.fragment_title_size_origin);
            mMinTextSize = resources.getDimension(R.dimen.fragment_title_size_min);
            maxScrollY = resources.getDimension(R.dimen.behavior_recycler_scroll);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        if (mStartY == 0) {
            mStartY = (int) dependency.getY();
        }
        float percent = (dependency.getY() - maxScrollY) / (mStartY-maxScrollY);
        TextView tx = child.findViewById(R.id.notebook_title);
        tx.setTextSize(TypedValue.COMPLEX_UNIT_PX,mMinTextSize + percent * (mPreTextSize - mMinTextSize));
        Log.d("aaaaa",percent+"");
        return true;
    }


}
