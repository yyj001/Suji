package com.suji.ish.suji.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.rxjava.InternetEvent;
import com.suji.ish.suji.rxjava.InternetRxBus;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MemoryViewModel extends ViewModel {

    private static final String TAG = "MemoryViewModel";
    public WordModel mWordModel;

    public void loadData(){
        mWordModel = new WordModel();
        mWordModel.getWordFromInternet("apple");
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
                            if(eventMsg.getCode()==1){
                                Word word = eventMsg.getWord();
                                Log.d(TAG, "accept: " + word.getParts() + "\n" + word.getSentence());
                            }
                        }
                    }
                });
    }
}
