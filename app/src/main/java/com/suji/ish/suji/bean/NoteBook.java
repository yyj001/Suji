package com.suji.ish.suji.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.litepal.crud.LitePalSupport;

public class NoteBook extends BaseObservable {
    private int id;
    private int noteNumber;
    private int createTime;
    private int editTime;
    private String bookName;

    public NoteBook(int id, int noteNumber, int createTime, int editTime, String bookName) {
        this.id = id;
        this.noteNumber = noteNumber;
        this.bookName = bookName;
        this.createTime = createTime;
        this.editTime = editTime;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public int getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(int noteNumber) {
        this.noteNumber = noteNumber;
    }

    @Bindable
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Bindable
    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Bindable
    public int getEditTime() {
        return editTime;
    }

    public void setEditTime(int editTime) {
        this.editTime = editTime;
    }
}
