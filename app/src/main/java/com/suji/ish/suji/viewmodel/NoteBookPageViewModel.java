package com.suji.ish.suji.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.suji.ish.suji.bean.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ish
 */
public class NoteBookPageViewModel extends AndroidViewModel {
    private String TAG= "NoteBookPageViewModel";

    private MutableLiveData<List<Word>> mWords;

    public NoteBookPageViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Word>> getCurrentName() {

        if (mWords == null) {
            mWords = new MutableLiveData<List<Word>>();
            loadWodrs();
        }
        return mWords;
    }

    public void loadWodrs(){
        List<Word> list = new ArrayList<>();
        for(int i=0;i<20;++i){
            Word word = new Word();
            word.setSpell("apple");
//            word.setWordType("n;");
//            word.setTranslation("行为的魏晨魏晨魏晨魏晨魏晨；曹魏洧长城网");
            list.add(word);
        }
        mWords.setValue(list);
    }
}
