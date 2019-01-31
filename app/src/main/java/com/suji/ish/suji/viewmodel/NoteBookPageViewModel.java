package com.suji.ish.suji.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;

import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

/**
 * @author ish
 */
public class NoteBookPageViewModel extends AndroidViewModel {
    private String TAG= "NoteBookPageViewModel";

    private MutableLiveData<List<Word>> mWords;
    private WordModel mModel;
    private int mbookId;

    public NoteBookPageViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Word>> getCurrentWord(int bookId) {
        mbookId = bookId;
        if (mWords == null) {
            mWords = new MutableLiveData<List<Word>>();
            loadWodrs();
        }
        return mWords;
    }

    public void loadWodrs(){
        mModel = new WordModel();

        FindMultiCallback<Word> callback = new FindMultiCallback<Word>() {
            @Override
            public void onFinish(List<Word> list) {
                mWords.setValue(list);
            }
        };

       mModel.getAllWordForBook(mbookId,callback);
    }
}
