package com.suji.ish.suji.global;


public class SujiData {
    private volatile static SujiData instance = null;
    /**
     * 所在的bookId，进入单词本设置id，退出单词本清空id；
     */
    private int bookId;

    private SujiData(){
    }

    public static SujiData getInstance(){
        if(instance == null){
            synchronized (SujiData.class){
                if(instance == null){
                    instance = new SujiData();
                }
            }
        }
        return instance;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
