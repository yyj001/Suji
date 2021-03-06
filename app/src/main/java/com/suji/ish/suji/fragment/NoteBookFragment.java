package com.suji.ish.suji.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suji.ish.suji.R;
import com.suji.ish.suji.databinding.FragmentNoteBookBinding;
import com.suji.ish.suji.viewmodel.NoteBookViewModel;

/**
 * A simple {@link Fragment} subclass.
 * @author ish
 */
public class NoteBookFragment extends Fragment {

    private FragmentNoteBookBinding mBinding;
    private NoteBookViewModel mNoteBookViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //返回时会调用onCreateView
        if(mBinding==null){
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_book, container, false);
        }
        mNoteBookViewModel = new NoteBookViewModel(mBinding,container,getActivity(),this);
        initView();
        return mBinding.getRoot();
    }

    public void initView() {
        super.onStart();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.notebookRecyclerview.setLayoutManager(linearLayoutManager);
        mNoteBookViewModel.initRecyclerViewData();
    }

}
