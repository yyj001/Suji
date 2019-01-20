package com.suji.ish.suji.bean;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.android.databinding.library.baseAdapters.BR;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Word extends LitePalSupport implements Observable {
    private PropertyChangeRegistry mRegistry = new PropertyChangeRegistry();
    private int id;
    private long addTime;
    private String addTimeString;
    private int addType; // 是否为词库里面的单词
    private String spell; //单词拼写
    private String phoneticSymbol; //音标
    private String pronunciationLink; //发音地址
    private String translation; //翻译
    private String sentence; //例句
    private String wordType; //词性
    private String passTense; //过去式
    private String goneTense; //完成式

    @Column(ignore = true)
    private static final int TYPE_PERSONAL = 0;
    @Column(ignore = true)
    private static final int TYPE_AUTHORITY = 1;

    @Bindable
    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
        mRegistry.notifyChange(this, BR.addTime);
    }

    @Bindable
    public String getAddTimeString() {
        return addTimeString;
    }

    public void setAddTimeString(String addTimeString) {
        this.addTimeString = addTimeString;
        mRegistry.notifyChange(this, BR.addTimeString);
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        mRegistry.notifyChange(this, BR.id);
    }

    @Bindable
    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
        mRegistry.notifyChange(this, BR.addType);
    }

    @Bindable
    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
        mRegistry.notifyChange(this, BR.spell);
    }

    @Bindable
    public String getPhoneticSymbol() {
        return phoneticSymbol;
    }

    public void setPhoneticSymbol(String phoneticSymbol) {
        this.phoneticSymbol = phoneticSymbol;
        mRegistry.notifyChange(this, BR.phoneticSymbol);
    }

    @Bindable
    public String getPronunciationLink() {
        return pronunciationLink;
    }

    public void setPronunciationLink(String pronunciationLink) {
        this.pronunciationLink = pronunciationLink;
        mRegistry.notifyChange(this, BR.pronunciationLink);
    }

    @Bindable
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
        mRegistry.notifyChange(this, BR.translation);
    }

    @Bindable
    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
        mRegistry.notifyChange(this, BR.sentence);
    }

    @Bindable
    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
        mRegistry.notifyChange(this, BR.wordType);
    }

    @Bindable
    public String getPassTense() {
        return passTense;
    }

    public void setPassTense(String passTense) {
        this.passTense = passTense;
        mRegistry.notifyChange(this, BR.passTense);
    }

    @Bindable
    public String getGoneTense() {
        return goneTense;
    }

    public void setGoneTense(String goneTense) {
        this.goneTense = goneTense;
        mRegistry.notifyChange(this, BR.goneTense);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mRegistry.remove(callback);
    }
}
