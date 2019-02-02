package com.suji.ish.suji.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;

/**
 * @author ish
 */
public class WordInfoViewModel extends ViewModel {
    private MutableLiveData<Word> mWord;
    private int mWordId;
    private WordModel mModel;

    public LiveData<Word> getCurrentWord(int wordId) {
        this.mModel = new WordModel();
        this.mWordId = wordId;

        if (mWord == null) {
            mWord = new MutableLiveData<Word>();
            loadWord();
        }
        return mWord;
    }

    public void loadWord(){
        Word word = mModel.getLocalWordById(mWordId);
        mWord.setValue(word);
    }
}
