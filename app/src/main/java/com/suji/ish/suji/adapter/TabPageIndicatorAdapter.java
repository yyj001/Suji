package com.suji.ish.suji.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.suji.ish.suji.fragment.MemoryFragment;
import com.suji.ish.suji.fragment.NoteBookFragment;
import com.suji.ish.suji.fragment.StatisticFragment;
import com.suji.ish.suji.fragment.UserFragment;

/**
 * @author ish
 * @date 2018/5/6.
 */

public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
    public TabPageIndicatorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new NoteBookFragment();
                break;
            case 1:
                fragment = new MemoryFragment();
                break;
            case 2:
                fragment = new StatisticFragment();
                break;
            case 3:
                fragment = new UserFragment();
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
