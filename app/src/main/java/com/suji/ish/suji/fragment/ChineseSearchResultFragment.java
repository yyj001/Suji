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
import com.suji.ish.suji.bean.ChineseWord;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.FragmentChineseSearchResultBinding;
import com.suji.ish.suji.listener.OnItemClickListener;
import com.suji.ish.suji.rxjava.InternetEvent;
import com.suji.ish.suji.view.NetWorkAlertDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;

public class ChineseSearchResultFragment extends Fragment {

    private ChineseSearchResultViewModel mViewModel;
    private String mSpell;
    private FragmentChineseSearchResultBinding mBinding;
    private WordAdapter mAdapter;
    private List<Word> mWords;
    private NetWorkAlertDialog mPopupWindow;


    public static ChineseSearchResultFragment newInstance() {
        return new ChineseSearchResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chinese_search_result, container, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSpell = getArguments().getString("spell");

        mWords = new ArrayList<Word>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.chineseWordRecyclerview.setLayoutManager(linearLayoutManager);
        mAdapter = new WordAdapter(mWords);

        mAdapter.setmOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Word word) {
                Bundle bundle = new Bundle();
                bundle.putString("spell", word.getSpell());
                Navigation.findNavController(view).navigate(R.id.action_chineseSearchResultFragment_to_wordDetailFragment, bundle);
            }

            @Override
            public void onItemLongClick(View view, int position, Word word) {

            }
        });

        mBinding.setAdapter(mAdapter);

        //搜索成功监听
        mViewModel = ViewModelProviders.of(this).get(ChineseSearchResultViewModel.class);
        final Observer<ChineseWord> listObserver = new Observer<ChineseWord>() {
            @Override
            public void onChanged(@Nullable ChineseWord chineseWord) {
                mBinding.setWord(chineseWord);
                mBinding.chineseWordDetailLoadingView.cancelAnimation();
                mBinding.chineseWordDetailLoadingView.setVisibility(View.GONE);

                mWords.clear();
                List<String> list = chineseWord.getBasic().getExplains();
                for (String item:list) {
                    Word word = new Word();
                    word.setSpell(item);
                    mWords.add(word);
                }
                mAdapter.setList(mWords);
                mAdapter.notifyDataSetChanged();
            }
        };

        mViewModel.getCurrentWord(mSpell).observe(this, listObserver);

        //监听网络搜索结果
        final Observer<InternetEvent> internetEventObserver = new Observer<InternetEvent>() {
            @Override
            public void onChanged(@Nullable InternetEvent internetEvent) {
                showAlertDialog(internetEvent);
                mBinding.chineseWordDetailLoadingView.cancelAnimation();
                mBinding.chineseWordDetailLoadingView.setVisibility(View.GONE);
            }
        };
        mViewModel.getInternetEvent().observe(this, internetEventObserver);

        mBinding.chineseWordDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void showAlertDialog(InternetEvent event) {
        mPopupWindow = new NetWorkAlertDialog(getActivity());
        mPopupWindow.setBackground(0);
        mPopupWindow.setView(event);
        mPopupWindow.showPopupWindow();
    }

}
