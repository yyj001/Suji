package com.suji.ish.suji.rxjava;

public class DataBaseEvent<T> {
    private T msg;
    private int eventCode = -1;

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
}
