package com.suji.ish.suji.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.WordDetailFragmentBinding;
import com.suji.ish.suji.global.SujiData;
import com.suji.ish.suji.model.NoteBookModel;
import com.suji.ish.suji.utils.AudioPlayer;
import com.suji.ish.suji.utils.ToolsUtils;
import com.suji.ish.suji.view.ResultPopupWindow;
import com.suji.ish.suji.view.ResultView;
import com.suji.ish.suji.viewmodel.WordDetailViewModel;

import org.litepal.LitePal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ish
 */
public class WordDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WordDetailFragment";
    private WordDetailViewModel mViewModel;
    private WordDetailFragmentBinding mBinding;
    private String mSpell;
    private NoteBook mSaveNoteBook;
    private Word mSaveword;

    public static WordDetailFragment newInstance() {
        return new WordDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.word_detail_fragment, container, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSpell = getArguments().getString("spell");

        mViewModel = ViewModelProviders.of(this).get(WordDetailViewModel.class);
        final Observer<Word> listObserver = new Observer<Word>() {
            @Override
            public void onChanged(@Nullable Word word) {
                mSaveword = word;
                mBinding.setWord(word);
                setParts(word);
                setExchange(word);
                setSentence(word);
            }
        };
        mViewModel.getCurrentWord(mSpell).observe(this, listObserver);
        initview();
    }

    private void initview() {
        mBinding.wordDetailCancel.setOnClickListener(this);
        mBinding.wordDetailAdd.setOnClickListener(this);
        mBinding.wordDetailModifyBtn.setOnClickListener(this);
        mBinding.wordDetailEnHorn.setOnClickListener(this);
        mBinding.wordDetailAmHorn.setOnClickListener(this);

        //判断当前是否在单词本里面
        if (SujiData.getInstance().getBookId() != 0) {
            mSaveNoteBook = LitePal.find(NoteBook.class, SujiData.getInstance().getBookId());
        }
        //不在某个单词本里面，就去数据库取第一个
        if (mSaveNoteBook == null) {
            mSaveNoteBook = mViewModel.getFirstNoteBook();
        }
        //数据库没有笔记本,需要新建笔记本
        if (mSaveNoteBook == null) {
            long time = ToolsUtils.getInstance().getInstanceTime();
            String timeStr = ToolsUtils.getInstance().getDateFormat(time);
            mSaveNoteBook = new NoteBook(0, time, time, "新建单词本", timeStr, timeStr);
        } else {
            mBinding.wordDetailNotebookName.setText(ToolsUtils.getInstance().handleText(mSaveNoteBook.getBookName(), 20));
        }

        addWordDisable();


    }

    public void setParts(Word word) {
        if (word.getSpell() == null && word.getParts() == null) {
            mBinding.wordDetailPartsShadowview.setVisibility(View.GONE);
        } else {
            mBinding.wordDetailPartsShadowview.setVisibility(View.VISIBLE);
            //将添加按钮变绿
            addWordEnable();
        }
    }

    public void setExchange(Word word) {
        if (judgeEmpty(word.getWordPl()) && judgeEmpty(word.getWordThird()) && judgeEmpty(word.getWordPast()) && judgeEmpty(word.getWordDone())
                && judgeEmpty(word.getWordIng()) && judgeEmpty(word.getWordEr()) && judgeEmpty(word.getWordEst())) {
            mBinding.wordDetailExchangeShadowview.setVisibility(View.GONE);
        } else {
            mBinding.wordDetailExchangeShadowview.setVisibility(View.VISIBLE);
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
            mBinding.wordDetailSentenceShadowview.setVisibility(View.GONE);
            return;
        }

        mBinding.wordDetailSentenceShadowview.setVisibility(View.VISIBLE);
        String[] group = sentence.split("/r/n\r\n");
        for (String part : group) {
            String[] singleSentence = part.split("/r/n     ");
            if (singleSentence.length == 2) {
                String enSentence = singleSentence[0];
                String chSentence = singleSentence[1].split("/r/n")[0] + "\n";

                TextView enTv = new TextView(getActivity());
                enTv.setTextColor(Color.BLACK);
                SpannableString spannableString = ToolsUtils.getInstance().getHightLightSentence(enSentence,word);

                enTv.setText(spannableString);

                TextView chTv = new TextView(getActivity());
                chTv.setTextColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.greya));
                chTv.setText(chSentence);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                mBinding.wordDetailSentenceContainer.addView(enTv, layoutParams);
                mBinding.wordDetailSentenceContainer.addView(chTv, layoutParams);
            }

        }
    }

    /**
     * 设置高亮
     *
     * @param sourceStr
     * @return
     */
    public SpannableString getHightLightSentence(String sourceStr) {
        SpannableString s = new SpannableString(sourceStr);
        //不区分大小写
        Pattern p = Pattern.compile(mSpell, Pattern.CASE_INSENSITIVE);
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
        switch (view.getId()) {
            case R.id.word_detail_cancel: {
                getActivity().onBackPressed();
                break;
            }
            case R.id.word_detail_add: {
                mViewModel.addWordToDb(mSaveNoteBook, mSaveword);
                showAddWordResult();
                break;
            }
            case R.id.word_detail_modify_btn: {
                showPopupMenu(view);
                break;
            }
            case R.id.word_detail_en_horn:{
                AudioPlayer.getInstance().playAudio(mSaveword.getPhEnMp3());
                break;
            }
            case R.id.word_detail_am_horn:{
                AudioPlayer.getInstance().playAudio(mSaveword.getPhAmMp3());
                break;
            }
            default:
        }
    }

    private void addWordEnable() {
        mBinding.wordDetailAdd.setClickable(true);
        mBinding.wordDetailAdd.setTextColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.colorAccent));
    }

    private void addWordDisable() {
        mBinding.wordDetailAdd.setClickable(false);
        mBinding.wordDetailAdd.setTextColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.greya));
    }

    private void showAddWordResult() {
        Handler handler = new Handler();

        final ResultPopupWindow resultPopupWindow = new ResultPopupWindow(getActivity());
        resultPopupWindow.setBackground(0);
        resultPopupWindow.setBlurBackgroundEnable(true);
        resultPopupWindow.setBackgroundColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.AlphaColorPrimaryDark));
        ResultView resultView = resultPopupWindow.findViewById(R.id.create_notebook_resultview);
        resultView.setType(ResultView.TYPE_SUCESS);
        //先设置只有成功样式
        //先关window再退出
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resultPopupWindow.dismiss();
            }
        }, 800);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        }, 1200);
        resultPopupWindow.showPopupWindow();
        resultView.play();
    }

    /**
     * 选择单词本弹窗
     *
     * @param view
     */
    private void showPopupMenu(View view) {
        final List<NoteBook> noteBooks = new NoteBookModel().getAllNoteBooks();

        //华为手机上popupmenu有问题
        Context wrapper = getActivity();
        if ("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            wrapper = new ContextThemeWrapper(getActivity(), R.style.NoPopupAnimation);
        }

        PopupMenu popupMenu = new PopupMenu(wrapper, view, Gravity.CENTER);
        Menu menu = popupMenu.getMenu();
        for (int i = 0; i < noteBooks.size(); ++i) {
            String bookName = ToolsUtils.getInstance().handleText(noteBooks.get(i).getBookName(), 20);
            menu.add(Menu.NONE, i, i, bookName);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSaveNoteBook = noteBooks.get(item.getItemId());
                mBinding.wordDetailNotebookName.setText(ToolsUtils.getInstance()
                        .handleText(mSaveNoteBook.getBookName(), 20));
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
}