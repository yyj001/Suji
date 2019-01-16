package com.suji.ish.suji.bean;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.android.databinding.library.baseAdapters.BR;

import org.litepal.crud.LitePalSupport;

/**
 * @author ish
 */
public class NoteBook extends LitePalSupport implements Observable {
    private PropertyChangeRegistry mRegistry = new PropertyChangeRegistry();
    private int id;
    private int noteNumber;
    private long createTime;
    private long editTime;
    private String bookName;
    private String editTimeString;
    private String createTimeString;

    public NoteBook(int noteNumber, long createTime, long editTime, String bookName,String editTimeString,String createTimeString) {
        this.noteNumber = noteNumber;
        this.bookName = bookName;
        this.createTime = createTime;
        this.editTime = editTime;
        this.editTimeString = editTimeString;
        this.createTimeString = createTimeString;
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
    public int getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(int noteNumber) {
        this.noteNumber = noteNumber;
        mRegistry.notifyChange(this, BR.noteNumber);
    }

    @Bindable
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
        mRegistry.notifyChange(this, BR.bookName);

    }

    @Bindable
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
        mRegistry.notifyChange(this, BR.createTime);

    }

    @Bindable
    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
        mRegistry.notifyChange(this, BR.editTime);

    }

    @Bindable
    public String getEditTimeString() {
        return editTimeString;
    }

    public void setEditTimeString(String editTimeString) {
        this.editTimeString = editTimeString;
        mRegistry.notifyChange(this, BR.editTimeString);
    }

    @Bindable
    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
        mRegistry.notifyChange(this, BR.createTimeString);
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
