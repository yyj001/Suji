package com.suji.ish.suji.viewmodel;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.NoteBookAdapter;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.databinding.FragmentNoteBookBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteBookViewModel implements View.OnClickListener{

    private FragmentNoteBookBinding mBinding;
    private ViewGroup mParent;
    private List<NoteBook> mNoteBooks;
    private NoteBookAdapter mNoteBookAdapter;
    private Activity mActivity;


    public NoteBookViewModel(FragmentNoteBookBinding mBinding, ViewGroup parent,Activity activity) {
        this.mBinding = mBinding;
        this.mParent = parent;
        this.mActivity = activity;
    }


    public void initRecyclerViewData(){
        mNoteBooks = new ArrayList<NoteBook>();
        for (int i = 0; i < 10; ++i) {
            NoteBook noteBook = new NoteBook(i, i * i + 1, i * i * i, i, "name " + i);
            mNoteBooks.add(noteBook);
        }
        mNoteBookAdapter = new NoteBookAdapter(mNoteBooks,mActivity);
        ViewDataBinding headerSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(mBinding.getRoot().getContext()),
                R.layout.header_search, mParent, false);
        mNoteBookAdapter.setHeaderView(headerSearchBinding);
        mBinding.setAdapter(mNoteBookAdapter);
        showViewStub();
    }

    /**show empty view**/
    public void showViewStub(){
        if(mNoteBooks.size()==0){
            mBinding.notebookViewstub.getViewStub().inflate();
        }
    }

    @Override
    public void onClick(View view) {
    }
}
