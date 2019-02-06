package com.suji.ish.suji.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.MemoWordAdapter;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.FragmentMemoryBinding;
import com.suji.ish.suji.viewmodel.MemoryViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemoryFragment extends Fragment {

    private static String TAG = "MemoryFragment";
    private FragmentMemoryBinding mBinding;
    private MemoryViewModel mViewModel;
    private MemoWordAdapter mAdapter;
    private List<Word> mWords;

    public MemoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_memory, container, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBinding.memoRecyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.memoRecyclerview.setLayoutManager(linearLayoutManager);

        mAdapter = new MemoWordAdapter(mWords,getActivity());
        mBinding.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        //监听
        mViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        final Observer<List<Word>> listObserver = new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> list) {
                mWords = list;
                mAdapter.setList(mWords);
                Log.d(TAG, "onChanged: " + mWords.size());
                mAdapter.notifyDataSetChanged();
            }
        };

        mViewModel.getCurrentWord().observe(this, listObserver);
    }


}
