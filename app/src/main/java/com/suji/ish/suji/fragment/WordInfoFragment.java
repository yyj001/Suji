package com.suji.ish.suji.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.FragmentWordInfoBinding;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.utils.AudioPlayer;
import com.suji.ish.suji.utils.ToolsUtils;
import com.suji.ish.suji.viewmodel.WordInfoViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordInfoFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "WordInfoFragment";
    private WordInfoViewModel mViewModel;
    private FragmentWordInfoBinding mBinding;
    private int mWordId;
    private Word mWord;


    public static WordInfoFragment newInstance() {
        return new WordInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_info, container, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWordId = getArguments().getInt("wordId");

        mViewModel = ViewModelProviders.of(this).get(WordInfoViewModel.class);
        final Observer<Word> listObserver = new Observer<Word>() {
            @Override
            public void onChanged(@Nullable Word word) {
                mWord = word;
                mBinding.setWord(word);
                setExchange(word);
                setSentence(word);
            }
        };
        mViewModel.getCurrentWord(mWordId).observe(this, listObserver);

        mBinding.wordInfoDelete.setOnClickListener(this);
        mBinding.wordInfoCancel.setOnClickListener(this);
        mBinding.wordInfoEnHorn.setOnClickListener(this);
        mBinding.wordInfoAmHorn.setOnClickListener(this);
    }

    public void setExchange(Word word) {
        if (judgeEmpty(word.getWordPl()) && judgeEmpty(word.getWordThird()) && judgeEmpty(word.getWordPast()) && judgeEmpty(word.getWordDone())
                && judgeEmpty(word.getWordIng()) && judgeEmpty(word.getWordEr()) && judgeEmpty(word.getWordEst())) {
            mBinding.wordInfoExchangeShadowview.setVisibility(View.GONE);
        } else {
            mBinding.wordInfoExchangeShadowview.setVisibility(View.VISIBLE);
        }
    }

    private boolean judgeEmpty(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        return false;
    }

    public void setSentence(Word word) {
        String sentence = word.getSentence();
        //分割成一组
        if (judgeEmpty(sentence)) {
            mBinding.wordInfoSentenceShadowview.setVisibility(View.GONE);
            return;
        }

        mBinding.wordInfoSentenceShadowview.setVisibility(View.VISIBLE);
        String[] group = sentence.split("/r/n\r\n");
        for (String part : group) {
            String[] singleSentence = part.split("/r/n     ");
            if (singleSentence.length == 2) {
                String enSentence = singleSentence[0];
                String chSentence = singleSentence[1].split("/r/n")[0] + "\n";

                TextView enTv = new TextView(getActivity());
                enTv.setTextColor(Color.BLACK);
                enTv.setText(getHightLightSentence(enSentence,word.getSpell()));

                TextView chTv = new TextView(getActivity());
                chTv.setTextColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.greya));
                chTv.setText(chSentence);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                mBinding.wordInfoSentenceContainer.addView(enTv, layoutParams);
                mBinding.wordInfoSentenceContainer.addView(chTv, layoutParams);
            }

        }
    }

    /**
     * 设置高亮
     *
     * @param sourceStr
     * @return
     */
    public SpannableString getHightLightSentence(String sourceStr,String spell) {
        SpannableString s = new SpannableString(sourceStr);
        //不区分大小写
        Pattern p = Pattern.compile(spell, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(ToolsUtils.getInstance().getColor(getActivity(), R.color.colorAccent)),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.word_info_delete:{
                AlertDialog.Builder builder = builder = new AlertDialog
                        .Builder(getActivity())
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("删除单词")
                        .setMessage("确定删除 " + mWord.getSpell() + " ?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new WordModel().deleteWord(mWord);
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
            case R.id.word_info_cancel:{
                getActivity().onBackPressed();
                break;
            }
            case R.id.word_info_en_horn:{
                AudioPlayer.getInstance().playAudio(mWord.getPhEnMp3());
                break;
            }
            case R.id.word_info_am_horn:{
                AudioPlayer.getInstance().playAudio(mWord.getPhAmMp3());
                break;
            }
            default:
        }
    }
}
