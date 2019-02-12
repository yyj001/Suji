package com.suji.ish.suji.utils;

import android.util.Log;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;

import java.util.List;

/**
 *
 */
public class MemoPlaner {
    private static final String TAG = "MemoPlaner";
    private WordModel mWordModel;
    private String mCurrentDateStr;
    //一次最多取出单词数目
    private int maxSize = 8;
    //还剩
    private int mOffSet = 0;

    private static final String EMPTY_WORD= "0";
    private static final String FI_WORD= "1";


    private volatile static MemoPlaner instance = null;

    private MemoPlaner() {
    }

    public static MemoPlaner getInstance() {
        if (instance == null) {
            synchronized (MemoPlaner.class) {
                if (instance == null) {
                    instance = new MemoPlaner();
                    instance.mWordModel = new WordModel();
                    instance.mCurrentDateStr = ToolsUtils.getInstance().getInstanceTimeStr();
                }
            }
        }
        return instance;
    }


    /**
     * 获取记忆的单词
     * 规则，获取
     * 一半是已经记过单词，一半是新词
     *
     * @return
     */
    public List<Word> getMemoWord() {
        List<Word> words = mWordModel.getMemoWords(maxSize, mCurrentDateStr, mOffSet);
        List<Word> unRememberWord = mWordModel.getUnRememberWord(maxSize);
        words.addAll(unRememberWord);
        return words;
    }

    public long getNextRememberTime(int rate, float er) {
        int days = (int) Math.pow(er, rate);
        Log.d(TAG, "getNextRememberTime: 下一次" + days + "天后");
        return ToolsUtils.getInstance().getInstanceTime() + days * 24 * 3600;
    }

}
