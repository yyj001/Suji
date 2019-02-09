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
    private int maxSize = 5;
    //还剩
    private int mOffSet = 0;

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
     * @return
     */
    public List<Word> getMemoword(){
        List<Word> words = mWordModel.getMemoWords(maxSize,mCurrentDateStr,mOffSet);
        Log.d(TAG, "getMemoword: " + words.size()
        );
        return words;
    }

}
