package com.suji.ish.suji.viewmodel;

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
                            if (eventMsg.getCode() == 1 && mWord != null) {
                                mWord.setValue(eventMsg.getWord());
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

    public void searchWord() {
        mModel.getWordFromInternet(mSpell);
    }

    public NoteBook getFirstNoteBook() {
        NoteBookModel model = new NoteBookModel();
        return model.getFirstNoteBook();
    }

    public void addWordToDb(NoteBook noteBook, final Word word) {
        word.setBookId(noteBook.getId());
        word.setAddType(Word.TYPE_AUTHORITY);
        mModel.insertWordToDb(word);
    }

}
