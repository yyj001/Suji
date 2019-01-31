package com.suji.ish.suji.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.WordAdapter;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.NoteBookPageFragmentBinding;
import com.suji.ish.suji.utils.ToolsUtils;
import com.suji.ish.suji.viewmodel.NoteBookPageViewModel;

import java.util.List;

/**
 * @author ish
 */
public class NoteBookPageFragment extends Fragment implements View.OnClickListener {

    private String TAG = "NoteBookPageFragment";
    private NoteBookPageViewModel mViewModel;
    private NoteBookPageFragmentBinding mBinding;
    private int mBookId;
    private String mBookName;
    private WordAdapter mAdapter;
    private List<Word> mWords;

    public static NoteBookPageFragment newInstance() {
        return new NoteBookPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //返回时会调用onCreateView
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.note_book_page_fragment, container, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBookName = (String) getArguments().get("bookName");
        mBookId = (int) getArguments().get("bookId");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.wordRecyclerview.setLayoutManager(linearLayoutManager);

        mAdapter = new WordAdapter(mWords);
        mBinding.setAdapter(mAdapter);

        //监听
        mViewModel = ViewModelProviders.of(this).get(NoteBookPageViewModel.class);
        final Observer<List<Word>> listObserver = new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> list) {
                mWords = list;
                mAdapter.setList(mWords);
                mAdapter.notifyDataSetChanged();
                //显示emptyView
                if(list.size()>0){
                    mBinding.bookPageEmptyView.setVisibility(View.GONE);
                }else{
                    mBinding.bookPageEmptyView.setVisibility(View.VISIBLE);
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getCurrentWord(mBookId).observe(this, listObserver);
        initView();
    }

    public void initView() {
        //单词本名超长处理
        String shortName = ToolsUtils.getInstance().handleText(mBookName,19);
        mBinding.bookPageName.setText(shortName);
        mBinding.bookPageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.loadWodrs();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
