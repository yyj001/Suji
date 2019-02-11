package com.suji.ish.suji.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.MemoWordAdapter;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.FragmentMemoryBinding;
import com.suji.ish.suji.view.ScrollSpeedLinearLayoutManger;
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
    private ScrollSpeedLinearLayoutManger mLinearLayoutManager;

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
        //设置recyclerView
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBinding.memoRecyclerview);

        mLinearLayoutManager = new ScrollSpeedLinearLayoutManger(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.memoRecyclerview.setLayoutManager(mLinearLayoutManager);
        mBinding.memoRecyclerview.addOnScrollListener(new MemoRvScrollListener());

        mAdapter = new MemoWordAdapter(mWords,getActivity(),this);
        mBinding.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        //监听
        mViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        final Observer<List<Word>> listObserver = new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> list) {
                mWords = list;
                mAdapter.setList(mWords);
                Log.d(TAG, "记忆页面单词改变成" + mWords.size() + "，刷新页面");
                //Todo 优化，不必刷新整个列表
                mAdapter.notifyDataSetChanged();
            }
        };

        mViewModel.getCurrentWord().observe(this, listObserver);

    }

    /**
     * 执行这个方法适合不能和其它复杂操作一起进行，否则滚动动画将卡顿
     * @param word
     * @param pos 当前位置
     */
    public void nextWord(Word word,int pos){
        if(pos<mWords.size()-1){
            mBinding.memoRecyclerview.smoothScrollToPosition(pos+1);
        }
    }

    public void preWord(Word word,int pos){
        if(pos>0){
            mBinding.memoRecyclerview.smoothScrollToPosition(pos-1);
        }
    }

    public void rememberWord(Word word,int pos){
        nextWord(word,pos);
    }

    public void forgetWord(Word word){
        mViewModel.forgetWord(word);
    }

    public void loadMore(int pos){
        if(pos==mWords.size()-1){
            mViewModel.loadWodrs();
        }
    }

    /**
     * 滚动监听 ，滚到最后一个时候自动加载更多单词
     */
    public class MemoRvScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(!recyclerView.canScrollHorizontally(1)){
                mViewModel.loadWodrs();
            }
        }
    }


}
