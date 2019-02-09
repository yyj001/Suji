package com.suji.ish.suji.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.utils.MemoPlaner;

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
            loadWodrs();
        }
        return mWords;
    }

    public void loadWodrs() {
//        FindMultiCallback<Word> callback = new FindMultiCallback<Word>() {
//            @Override
//            public void onFinish(List<Word> list) {
//                mWords.setValue(list);
//            }
//        };
//        mWordModel.getAllWordForBook(1, callback);
        memoPlaner = MemoPlaner.getInstance();
        mWords.setValue(memoPlaner.getMemoword());
    }

    public void addWordToBack(Word word){
        List<Word> list= mWords.getValue();
        list.add(word);
        mWords.setValue(list);
    }
}
