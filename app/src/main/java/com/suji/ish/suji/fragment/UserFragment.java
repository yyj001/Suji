package com.suji.ish.suji.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.suji.ish.suji.R;

/**
 * A simple {@link Fragment} subclass.
 * @author ish
 */
public class UserFragment extends Fragment {
    private ProgressBarDeterminate mProgressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mProgressBar = view.findViewById(R.id.user_progressbar);

        return view;
    }

}
