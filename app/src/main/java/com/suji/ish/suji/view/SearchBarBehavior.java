package com.suji.ish.suji.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.suji.ish.suji.R;

public class SearchBarBehavior extends CoordinatorLayout.Behavior<CardView> {
    private int mStartY;
    private float mPreHeight;
    private float mMinHeight;
    private float mMaxMarginLeft;
    private float mMinMarginLeft;
    private float mMinMarginRight;
    private float mMaxMarginRight;
    private float mMinMarginTop;
    private float mMaxMarginTop;
    private float maxScrollY = 150;

    public SearchBarBehavior(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CardView child, View dependency) {
        if (dependency.getId() == R.id.notebook_recyclerview) {
            Resources resources = dependency.getResources();
            mMinHeight = resources.getDimension(R.dimen.fragment_searchbar_minheight);
            mPreHeight = resources.getDimension(R.dimen.fragment_searchbar_maxheight);

            mMaxMarginLeft = resources.getDimension(R.dimen.fragment_searchbar_maxmargin_left);
            mMinMarginLeft = resources.getDimension(R.dimen.fragment_searchbar_minmargin_left);

            mMinMarginRight = resources.getDimension(R.dimen.fragment_searchbar_minmargin_right);
            mMaxMarginRight = resources.getDimension(R.dimen.fragment_searchbar_maxmargin_right);

            mMinMarginTop = resources.getDimension(R.dimen.fragment_searchbar_min_margintop);
            mMaxMarginTop = resources.getDimension(R.dimen.fragment_searchbar_max_margintop);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CardView child, View dependency) {

        if (mStartY == 0) {
            mStartY = (int) dependency.getY();
        }
        float percent = (dependency.getY() - maxScrollY) / (mStartY - maxScrollY);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int spanLeft = (int) (mMaxMarginLeft - mMinMarginLeft);
        int spanRight = (int) (mMaxMarginRight - mMinMarginRight);
        lp.setMargins((int) (mMaxMarginLeft - (percent * spanLeft)),
                (int) (mMinMarginTop + percent * (mMaxMarginTop - mMinMarginTop)),
                (int) (mMaxMarginRight - (percent * spanRight)),
                0);
        lp.height = (int) (mMinHeight + percent * (mPreHeight - mMinHeight));
        child.setLayoutParams(lp);
        Log.d("ccccc", dependency.getY() + " ");
        return true;
    }
}
