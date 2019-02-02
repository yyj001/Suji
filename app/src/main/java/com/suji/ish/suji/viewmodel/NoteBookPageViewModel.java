package com.suji.ish.suji.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.rxjava.RxBus;

import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author ish
 */
public class NoteBookPageViewModel extends AndroidViewModel {
    private String TAG = "NoteBookPageViewModel";

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

    public void loadWodrs() {
        mModel = new WordModel();

        FindMultiCallback<Word> callback = new FindMultiCallback<Word>() {
            @Override
            public void onFinish(List<Word> list) {
                mWords.setValue(list);
            }
        };

        mModel.getAllWordForBook(mbookId, callback);

        //监听插入单词
        RxBus.getInstance()
                .toObservable()
                .map(new Function<Object, DataBaseEvent>() {
                    @Override
                    public DataBaseEvent apply(Object o) throws Exception {
                        return (DataBaseEvent) o;
                    }
                })
                .subscribe(new Consumer<DataBaseEvent>() {
                    @Override
                    public void accept(DataBaseEvent eventMsg) throws Exception {
                        if (eventMsg != null) {
                            changeWithDataBaseChange(eventMsg);
                        }
                    }
                });
    }

    /**
     * 根据数据库变化刷新列表
     */
    private void changeWithDataBaseChange(DataBaseEvent event) {
        Log.d(TAG, "changeWithDataBaseChange: ");
        switch (event.getEventCode()) {
            //插入单词
            case DataBaseEvent.ADD_WORD_SUCCESS: {
                NoteBook changedNoteBook = event.getNoteBook();
                if(changedNoteBook.getId() == mbookId){
                    Word addWord = event.getWord();
                    List<Word> words = mWords.getValue();
                    words.add(0,addWord);
                    mWords.setValue(words);
                }
                break;
            }
            case DataBaseEvent.DELETE_WORD_SUCCESS: {
                Word deleteWord = event.getWord();
                List<Word> words = mWords.getValue();
                for (int i = 0; i < words.size(); ++i) {
                    if(words.get(i).getId() == deleteWord.getId()){
                        words.remove(i);
                        mWords.setValue(words);
                        break;
                    }
                }
                break;
            }
            default:
        }
    }
}
