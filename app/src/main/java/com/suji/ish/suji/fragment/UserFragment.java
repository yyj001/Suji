package com.suji.ish.suji.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.loopeer.shadow.ShadowView;
import com.suji.ish.suji.R;
import com.suji.ish.suji.activity.BaseActivity;
import com.suji.ish.suji.activity.FeedbackActivity;

/**
 * A simple {@link Fragment} subclass.
 * @author ish
 */
public class UserFragment extends Fragment implements View.OnClickListener{
    private ProgressBarDeterminate mProgressBar;
    private ShadowView mFeedbackShadowView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mProgressBar = view.findViewById(R.id.user_progressbar);
        initView(view);
        return view;
    }

    private void initView(View view){
        mFeedbackShadowView = view.findViewById(R.id.user_feedback);
        mFeedbackShadowView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.user_feedback:{
                ((BaseActivity)getActivity()).goTo(new FeedbackActivity());
                break;
            }
            default:
        }
    }
}
