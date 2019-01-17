package com.suji.ish.suji.rxjava;

import com.suji.ish.suji.bean.NoteBook;

public class DataBaseEvent<T> {
    private T msg;
    private int eventCode = -1;
    private NoteBook noteBook;

    public static final int INSERT_FAIL = 0;
    public static final int INSERT_SUCCESS = 1;

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public NoteBook getNoteBook() {
        return noteBook;
    }

    public void setNoteBook(NoteBook noteBook) {
        this.noteBook = noteBook;
    }
}
