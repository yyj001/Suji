package com.suji.ish.suji.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.suji.ish.suji.fragment.MemoryBlankFragment;
import com.suji.ish.suji.fragment.NoteBookBlankFragment;
import com.suji.ish.suji.fragment.StatisticFragment;
import com.suji.ish.suji.fragment.UserBlankFragment;

/**
 * @author ish
 * @date 2018/5/6.
 */

public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
    private Context mActivity;
    public TabPageIndicatorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
//                fragment = new NoteBookFragment();
                fragment = new NoteBookBlankFragment();
                break;
            case 1:
                fragment = new MemoryBlankFragment();
                break;
            case 2:
                fragment = new StatisticFragment();
                break;
            case 3:
                fragment = new UserBlankFragment();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
