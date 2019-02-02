package com.suji.ish.suji.model;

import android.util.Log;

import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.rxjava.RxBus;
import com.suji.ish.suji.utils.ToolsUtils;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

/**
 * @author ish
 */
public class NoteBookModel {

    private static final String TAG = "NoteBookModel";

    /**
     * 通过笔记本名创建单词本
     *
     * @param bookName
     */
    public void createNotebook(String bookName) {
        //先判断是否有同名单词本
        NoteBook n = LitePal.where("bookname = ?", bookName).findFirst(NoteBook.class);
        if (n != null) {
            DataBaseEvent<String> eventMsg = new DataBaseEvent<>();
            eventMsg.setEventCode(DataBaseEvent.INSERT_FAIL);
            eventMsg.setMsg("已存在同名笔记本");
            RxBus.getInstance().post(eventMsg);
            return;
        }
        //创建新笔记本
        long createTime = ToolsUtils.getInstance().getInstanceTime();
        long editTime = createTime;
        String createTimeString = ToolsUtils.getInstance().getDateFormat(createTime);
        String editTimeString = ToolsUtils.getInstance().getDateFormat(editTime);
        final NoteBook noteBook = new NoteBook(0, createTime, editTime, bookName, editTimeString, createTimeString);

        //在子线程插入，监听插入结果
        saveNoteBook(noteBook);
    }

    /**
     * 通过notebook变量保存
     *
     * @param noteBook
     */
    public void saveNoteBook(final NoteBook noteBook) {

        noteBook.saveAsync().listen(new org.litepal.crud.callback.SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                DataBaseEvent<String> eventMsg = new DataBaseEvent<>();
                if (success) {
                    eventMsg.setEventCode(DataBaseEvent.INSERT_SUCCESS);
                    eventMsg.setMsg("创建成功");
                    eventMsg.setNoteBook(noteBook);
                    Log.d(TAG, "插入id: " + noteBook.getId());
                } else {
                    eventMsg.setEventCode(DataBaseEvent.INSERT_FAIL);
                    eventMsg.setMsg("创建失败");
                }
                RxBus.getInstance().post(eventMsg);
            }
        });
    }

    /**
     * 不开子线程创建笔记本
     *
     * @param noteBook
     */
    public void saveNoteBookMainThread(NoteBook noteBook, int state, Word word) {
        noteBook.save();
        notifyRxBus(noteBook, state, word);
    }

    public void notifyRxBus(NoteBook noteBook, int state, Word word) {
        DataBaseEvent<String> eventMsg = new DataBaseEvent<>();

        if (state == DataBaseEvent.INSERT_SUCCESS) {
            eventMsg.setEventCode(DataBaseEvent.INSERT_SUCCESS);
            eventMsg.setMsg("创建成功");
            eventMsg.setNoteBook(noteBook);
        } else if (state == DataBaseEvent.INSERT_FAIL) {
            eventMsg.setEventCode(DataBaseEvent.INSERT_FAIL);
            eventMsg.setMsg("创建失败");
        } else if (state == DataBaseEvent.ADD_WORD_SUCCESS) {
            eventMsg.setEventCode(DataBaseEvent.ADD_WORD_SUCCESS);
            eventMsg.setMsg("添加成功");
            eventMsg.setWord(word);
        } else if (state == DataBaseEvent.CHANGE_BOOK_SUCCESS) {
            eventMsg.setEventCode(DataBaseEvent.CHANGE_BOOK_SUCCESS);
            eventMsg.setMsg("修改成功");
        } else if (state == DataBaseEvent.DELETE_BOOK_SUCCESS) {
            eventMsg.setEventCode(DataBaseEvent.DELETE_BOOK_SUCCESS);
            eventMsg.setMsg("删除单词本成功");
        } else if (state == DataBaseEvent.DELETE_WORD_SUCCESS) {
            eventMsg.setEventCode(state);
            eventMsg.setMsg("删除单词");
            eventMsg.setWord(word);
        }
        eventMsg.setNoteBook(noteBook);
        RxBus.getInstance().post(eventMsg);
    }

    /**
     * 笔记本不会很多，直接在主线程操作
     */
    public List<NoteBook> getAllNoteBooks() {
        List<NoteBook> list = LitePal.findAll(NoteBook.class);
        return list;
    }

    /**
     * 获取第一本笔记本，没有就返回null
     *
     * @return
     */
    public NoteBook getFirstNoteBook() {
        NoteBook noteBook = LitePal.findFirst(NoteBook.class);
        return noteBook;
    }

    public void modifyBookName(int id, String name, UpdateOrDeleteCallback callback) {
        NoteBook noteBook = new NoteBook();
        noteBook.setBookName(name);
        noteBook.updateAsync(id).listen(callback);
    }

    public void deleteNoteBook(int id) {
        //删单词本
        LitePal.delete(NoteBook.class, id);
        //删所有单词
        new WordModel().deleteByBookId(id);

        NoteBook noteBook = new NoteBook();
        noteBook.setId(id);
        notifyRxBus(noteBook, DataBaseEvent.DELETE_BOOK_SUCCESS, null);
    }

    public void deleteWord(Word word) {
        int bookId = word.getBookId();
        NoteBook noteBook = LitePal.find(NoteBook.class, bookId);
        long editTime = ToolsUtils.getInstance().getInstanceTime();
        String editTimeString = ToolsUtils.getInstance().getDateFormat(editTime);

        noteBook.setEditTime(editTime);
        noteBook.setEditTimeString(editTimeString);
        noteBook.setNoteNumber(noteBook.getNoteNumber()-1);
        noteBook.save();
        notifyRxBus(noteBook,DataBaseEvent.DELETE_WORD_SUCCESS,word);
    }

}
