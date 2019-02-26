package com.suji.ish.suji.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.suji.ish.suji.bean.ChineseWord;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.rxjava.InternetEvent;
import com.suji.ish.suji.rxjava.InternetRxBus;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author ish
 */
public class ChineseSearchResultViewModel extends AndroidViewModel {
    private MutableLiveData<ChineseWord> mWord;
    private String mSpell;
    private WordModel mModel;
    private MutableLiveData<InternetEvent> mInternetEvent;


    @SuppressLint("CheckResult")
    public ChineseSearchResultViewModel(@NonNull Application application) {
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
                            if (eventMsg.getCode() == InternetEvent.CHINESE_SUCESS && mWord != null) {
                                mWord.setValue(eventMsg.getChineseWord());
                            }
                            else if(eventMsg.getCode() == InternetEvent.FAIL_NO_NETWORK){
                                if(eventMsg.getChineseWord()!=null){
                                    mInternetEvent.setValue(eventMsg);
                                }
                            }else if(eventMsg.getCode() == InternetEvent.FAIL_NO_RESOURCE){
                                if(eventMsg.getChineseWord()!=null){
                                    mInternetEvent.setValue(eventMsg);
                                }
                            }
                        }
                    }
                });
    }

    public LiveData<ChineseWord> getCurrentWord(String spell) {
        this.mSpell = spell;
        this.mModel = new WordModel();

        if (mWord == null) {
            mWord = new MutableLiveData<ChineseWord>();
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
        mModel.getChineseWordFromInternet(mSpell);
    }

}
