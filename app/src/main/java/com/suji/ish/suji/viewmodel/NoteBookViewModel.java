package com.suji.ish.suji.viewmodel;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.NoteBookAdapter;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.databinding.FragmentNoteBookBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteBookViewModel {

    private FragmentNoteBookBinding mBinding;
    private ViewGroup mParent;
    private List<NoteBook> mNoteBooks;
    private NoteBookAdapter mNoteBookAdapter;


    public NoteBookViewModel(FragmentNoteBookBinding mBinding, ViewGroup parent) {
        this.mBinding = mBinding;
        this.mParent = parent;
    }

    public void initRecyclerViewData(){
        mNoteBooks = new ArrayList<NoteBook>();
        for (int i = 0; i < 20; ++i) {
            NoteBook noteBook = new NoteBook(i, i * i + 1, i * i * i, i, "name " + i);
            mNoteBooks.add(noteBook);
        }
        mNoteBookAdapter = new NoteBookAdapter(mNoteBooks);
        ViewDataBinding headerSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(mBinding.getRoot().getContext()),
                R.layout.header_search, mParent, false);
        mNoteBookAdapter.setHeaderView(headerSearchBinding);
        mBinding.setAdapter(mNoteBookAdapter);
    }
}
