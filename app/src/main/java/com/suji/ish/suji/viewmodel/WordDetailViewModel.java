package com.suji.ish.suji.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.NoteBookModel;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.rxjava.InternetEvent;
import com.suji.ish.suji.rxjava.InternetRxBus;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author ish
 */
public class WordDetailViewModel extends AndroidViewModel {
    private static String TAG = "WordDetailViewModel";
    private MutableLiveData<Word> mWord;
    private String mSpell;
    private WordModel mModel;
    private MutableLiveData<InternetEvent> mInternetEvent;

    @SuppressLint("CheckResult")
    public WordDetailViewModel(@NonNull Application application) {
        super(application);

        //网络查词返回监听
        InternetRxBus.getInstance()
                .toObservable()
                .map(new Function<Object, InternetEvent>() {
                    @Override
                    public InternetEvent apply(Object o) throws Exception {
                        return (InternetEvent) o;
                    }
                })
                .subscribe(new Consumer<InternetEvent>() {
                    @Override
                    public void accept(InternetEvent eventMsg) throws Exception {
                        if (eventMsg != null) {
                            if (eventMsg.getCode() == InternetEvent.SUCESS && mWord != null) {
                                mWord.setValue(eventMsg.getWord());
                            }
                            else if(eventMsg.getCode() == InternetEvent.FAIL_NO_NETWORK){
                                mInternetEvent.setValue(eventMsg);
                            }else if(eventMsg.getCode() == InternetEvent.FAIL_NO_RESOURCE){
                                mInternetEvent.setValue(eventMsg);
                            }
                        }
                    }
                });
    }

    public LiveData<Word> getCurrentWord(String spell) {
        this.mSpell = spell;
        this.mModel = new WordModel();

        if (mWord == null) {
            mWord = new MutableLiveData<Word>();
            searchWord();
        }
        return mWord;
    }

    public LiveData<InternetEvent> getInternetEvent() {
        if (mInternetEvent == null) {
            mInternetEvent = new MutableLiveData<InternetEvent>();
        }
        return mInternetEvent;
    }

    public void searchWord() {
        mModel.getWordFromInternet(mSpell);
    }

    public NoteBook getFirstNoteBook() {
        NoteBookModel model = new NoteBookModel();
        return model.getFirstNoteBook();
    }

    public void addWordToDb(NoteBook noteBook, final Word word) {
        word.setAddType(Word.TYPE_AUTHORITY);
        mModel.insertWordToDb(word,noteBook);
    }

}
