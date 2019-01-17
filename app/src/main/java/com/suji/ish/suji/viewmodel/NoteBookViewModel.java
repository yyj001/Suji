package com.suji.ish.suji.viewmodel;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.suji.ish.suji.R;
import com.suji.ish.suji.activity.SearchActivity;
import com.suji.ish.suji.adapter.NoteBookAdapter;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.databinding.FragmentNoteBookBinding;
import com.suji.ish.suji.model.NoteBookModel;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.rxjava.RxBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author ish
 */
public class NoteBookViewModel implements View.OnClickListener {

    private FragmentNoteBookBinding mBinding;
    private ViewGroup mParent;
    private List<NoteBook> mNoteBooks;
    private NoteBookAdapter mNoteBookAdapter;
    private Activity mActivity;
    private NoteBookModel mNoteBookModel;
    //记录stubView是否已经被inflate
    private boolean mInflateVb = false;


    public NoteBookViewModel(FragmentNoteBookBinding mBinding, ViewGroup parent, Activity activity) {
        this.mBinding = mBinding;
        this.mParent = parent;
        this.mActivity = activity;

        mBinding.notebookSearchbar.setOnClickListener(this);
        mBinding.notebookMoreBtn.setOnClickListener(this);
    }


    public void initRecyclerViewData() {
        mNoteBookModel = new NoteBookModel();
        mNoteBooks = mNoteBookModel.getAllNoteBooks();

        //按编辑时间排序
        Collections.sort(mNoteBooks, new SortByEdittime());
        mNoteBookAdapter = new NoteBookAdapter(mNoteBooks, mActivity);
        ViewDataBinding headerSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(mBinding.getRoot().getContext()),
                R.layout.header_rv_notebook, mParent, false);
//        mNoteBookAdapter.setHeaderView(headerSearchBinding);
        mBinding.setAdapter(mNoteBookAdapter);
        showViewStub();

        //监听数据库是否变化
        RxBus.getInstance()
                .toObservable()
                .map(new Function<Object, DataBaseEvent>() {
                    @Override
                    public DataBaseEvent apply(Object o) throws Exception {
                        return (DataBaseEvent) o;
                    }
                })
                .subscribe(new Consumer<DataBaseEvent>() {
                    @Override
                    public void accept(DataBaseEvent eventMsg) throws Exception {
                        if (eventMsg != null) {
                            changeWithDataBaseChange(eventMsg);
                        }
                    }
                });
    }

    /**
     * 根据数据库变化刷新列表
     */
    private void changeWithDataBaseChange(DataBaseEvent event) {
        switch (event.getEventCode()) {
            case DataBaseEvent.INSERT_SUCCESS: {
                NoteBook newNoteBook = event.getNoteBook();
                int pos = 0;
                if (mNoteBookAdapter.hasHeader()) {
                    pos = 1;
                }
                mNoteBooks.add(0, newNoteBook);
                mNoteBookAdapter.notifyItemChanged(pos);
                //别忘了刷新stubView
                showViewStub();
                break;
            }
            default:
        }
    }

    /**
     * show empty view
     **/
    public void showViewStub() {
        if (mNoteBooks.size() == 0) {
            mBinding.notebookViewstub.setVisibility(View.VISIBLE);
        } else {
            mBinding.notebookViewstub.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notebook_searchbar:
                ActivityOptions transitionActivityOptions = ActivityOptions
                        .makeSceneTransitionAnimation(mActivity, view, "searchbar");

                mActivity.startActivity(new Intent(mActivity, SearchActivity.class),
                        transitionActivityOptions.toBundle());
                break;
            case R.id.notebook_more_btn:
                showPopupMenu(view);
            default:
        }
    }

    private void showPopupMenu(View view) {
        //华为手机上popupmenu有问题
        Context wrapper = mActivity;
        if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            wrapper = new ContextThemeWrapper(mActivity, R.style.NoPopupAnimation);
        }
        PopupMenu popupMenu = new PopupMenu(wrapper, view, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.notebook_menu, popupMenu.getMenu());

        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.option_update:
                        Collections.sort(mNoteBooks, new SortByEdittime());
                        mNoteBookAdapter.notifyDataSetChanged();
                        break;
                    case R.id.option_create:
                        Collections.sort(mNoteBooks, new SortByCreatetime());
                        mNoteBookAdapter.notifyDataSetChanged();
                        break;
                    case R.id.option_name:
                        Collections.sort(mNoteBooks, new SortByName());
                        mNoteBookAdapter.notifyDataSetChanged();
                        break;
                    case R.id.option_number:
                        Collections.sort(mNoteBooks, new SortByNumber());
                        mNoteBookAdapter.notifyDataSetChanged();
                        break;
                    default:

                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

    /**
     * 排序列表类
     */
    class SortByEdittime implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            NoteBook b1 = (NoteBook) o1;
            NoteBook b2 = (NoteBook) o2;
            if (b1.getEditTime() < b2.getEditTime()) {
                return 1;
            }
            return -1;
        }
    }

    class SortByCreatetime implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            NoteBook b1 = (NoteBook) o1;
            NoteBook b2 = (NoteBook) o2;
            if (b1.getCreateTime() < b2.getCreateTime()) {
                return 1;
            }
            return -1;
        }
    }

    class SortByNumber implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            NoteBook b1 = (NoteBook) o1;
            NoteBook b2 = (NoteBook) o2;
            if (b1.getNoteNumber() > b2.getNoteNumber()) {
                return 1;
            }
            return -1;
        }
    }

    class SortByName implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            NoteBook b1 = (NoteBook) o1;
            NoteBook b2 = (NoteBook) o2;
            return b1.getBookName().compareTo(b2.getBookName());
        }
    }


}
