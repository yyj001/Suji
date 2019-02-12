package com.suji.ish.suji.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.WordAdapter;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.NoteBookPageFragmentBinding;
import com.suji.ish.suji.global.SujiData;
import com.suji.ish.suji.model.NoteBookModel;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.utils.ToolsUtils;
import com.suji.ish.suji.view.BookSettingWindow;
import com.suji.ish.suji.viewmodel.NoteBookPageViewModel;

import org.litepal.crud.callback.UpdateOrDeleteCallback;

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
    private BookSettingWindow mWindow;

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
        SujiData.getInstance().setBookId(mBookId);


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
                if (list.size() > 0) {
                    mBinding.bookPageEmptyView.setVisibility(View.GONE);
                } else {
                    mBinding.bookPageEmptyView.setVisibility(View.VISIBLE);
                }
            }
        };

        mViewModel.getCurrentWord(mBookId).observe(this, listObserver);
        initView();
    }

    public void initView() {
        //单词本名超长处理
        String shortName = ToolsUtils.getInstance().handleText(mBookName, 19);
        mBinding.bookPageName.setText(shortName);
        mBinding.bookPageCamera.setOnClickListener(this);
        mBinding.bookPageMore.setOnClickListener(this);
        mBinding.notebookPageBack.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.book_page_more: {
                showSettingWindow();
                break;
            }
            case R.id.setting_notebook_delete: {
                AlertDialog.Builder builder = builder = new AlertDialog
                        .Builder(getActivity())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("删除单词本")
                        .setMessage("确定删除 " + mBookName + " ?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new NoteBookModel().deleteNoteBook(mBookId);
                                mWindow.dismiss();
                                hideKeyboard(view);
                                getActivity().onBackPressed();
                            }
                        }).setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
                break;
            }
            case R.id.setting_book_save: {
                final String newBookName = mWindow.getEditText().getText().toString();
                UpdateOrDeleteCallback saveCallback = new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        if (rowsAffected == 1) {
                            mBinding.bookPageName.setText(newBookName);
                            NoteBook noteBook = new NoteBook();
                            noteBook.setBookName(newBookName);
                            noteBook.setId(mBookId);
                            new NoteBookModel().notifyRxBus(noteBook, DataBaseEvent.CHANGE_BOOK_SUCCESS, null);
                            mWindow.dismiss();
                            hideKeyboard(view);
                        }
                    }
                };
                new NoteBookModel().modifyBookName(mBookId, newBookName, saveCallback);
                break;
            }
            case R.id.setting_book_cancel: {
                mWindow.dismiss();
                hideKeyboard(view);
                break;
            }
            case R.id.notebook_page_back:{
                getActivity().onBackPressed();
                break;
            }
            default:
        }
    }

    public void showSettingWindow() {
        mWindow = new BookSettingWindow(getActivity());
        mWindow.setBackground(0);
        mWindow.setBlurBackgroundEnable(true);
        mWindow.setBackgroundColor(ToolsUtils.getInstance().getColor(getActivity(),
                R.color.AlphaColorPrimaryDark));

        TextView deleteTv = mWindow.getDeleteTv();
        deleteTv.setOnClickListener(this);
        TextView cancelTv = mWindow.getCancelTv();
        cancelTv.setOnClickListener(this);
        TextView saveTv = mWindow.getSaveTv();
        saveTv.setOnClickListener(this);
        saveTv.setClickable(false);

        mWindow.showPopupWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SujiData.getInstance().setBookId(0);
    }

    /**
     * 退出时隐藏键盘
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
