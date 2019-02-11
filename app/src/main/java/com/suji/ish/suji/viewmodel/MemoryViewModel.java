package com.suji.ish.suji.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.utils.MemoPlaner;
import com.suji.ish.suji.utils.ToolsUtils;

import java.util.ArrayList;
import java.util.List;

public class MemoryViewModel extends ViewModel {

    private static final String TAG = "MemoryViewModel";
    public WordModel mWordModel;
    public MemoPlaner memoPlaner;
    private MutableLiveData<List<Word>> mWords;

    public LiveData<List<Word>> getCurrentWord() {
        mWordModel = new WordModel();
        if (mWords == null) {
            mWords = new MutableLiveData<List<Word>>();
            List<Word> list = new ArrayList<Word>();
            mWords.setValue(list);
            memoPlaner = MemoPlaner.getInstance();
            loadWodrs();
        }
        return mWords;
    }

    public void loadWodrs() {
        List<Word> list = mWords.getValue();
        List<Word> addList = memoPlaner.getMemoWord();
        if (addList == null || addList.size() == 0) {
            Log.d(TAG, "loadWodrs: 已经没有单词数据客加载");
        }
        list.addAll(addList);
        mWords.setValue(list);
        Log.d(TAG, "rememberWord: 加载更多" + addList.size() + "个单词");
    }

    public void addWordToBack(Word word) {
        List<Word> list = mWords.getValue();
        list.add(word);
        mWords.setValue(list);
    }

    public void forgetWord(Word word) {
        Log.d(TAG, "forgetWord: " + word.getSpell());
        //忘记该单词，将rate重新设为0，修改记忆时间
        word.setRate(0);
        word.setNextTime(memoPlaner.getNextRememberTime(word.getRate(), word.getEr()));
        word.setUpdateTimeStr(ToolsUtils.getInstance().getInstanceTimeStr());
        mWordModel.update(word);
        //將该单词插到队尾
        addWordToBack(word);
    }

    public void rememberWord(Word word, int pos) {
        Log.d(TAG, "rememberWord: " + word.getSpell());
        word.setNextTime(memoPlaner.getNextRememberTime(word.getRate(), word.getEr()));
        word.setUpdateTimeStr(ToolsUtils.getInstance().getInstanceTimeStr());
        //记住该单词，和遗忘该词顺序相反，先设置下一次时间，如果为0，设置第二天继续背单词，再设置时间，保证第二天还能继续见到该单词
        //否则将变成两天后再见到该单词
        if (word.getRate() < 5) {
            word.setRate(word.getRate() + 1);
        }
        mWordModel.update(word);
    }
}
