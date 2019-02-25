package com.suji.ish.suji.rxjava;

import com.suji.ish.suji.bean.ChineseWord;
import com.suji.ish.suji.bean.Word;

/**
 * 获取网络数据
 */
public class InternetEvent {
    private int code;
    private String message;
    private Word word;
    private ChineseWord chineseWord;
    public final static int SUCESS = 1;
    public final static int FAIL_NO_NETWORK = 2;
    public final static int FAIL_NO_RESOURCE = 3;
    public final static int CHINESE_SUCESS = 4;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public ChineseWord getChineseWord() {
        return chineseWord;
    }

    public void setChineseWord(ChineseWord chineseWord) {
        this.chineseWord = chineseWord;
    }
}
