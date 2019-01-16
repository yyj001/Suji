package com.suji.ish.suji.model;

import android.service.autofill.SaveCallback;
import android.util.Log;

import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.utils.ToolsUtils;

public class NoteBookModel {
    public void createNotebook(String bookName) {
        long createTime = ToolsUtils.getInstance().getInstanceTime();
        long editTime = createTime;
        String createTimeString = ToolsUtils.getInstance().getDateFormat(createTime);
        String editTimeString = ToolsUtils.getInstance().getDateFormat(editTime);
        NoteBook noteBook = new NoteBook(0, createTime, editTime, bookName, editTimeString, createTimeString);
        noteBook.save();
    }
}
