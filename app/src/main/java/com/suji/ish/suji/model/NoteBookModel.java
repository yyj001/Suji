package com.suji.ish.suji.model;

import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.rxjava.RxBus;
import com.suji.ish.suji.utils.ToolsUtils;

import org.litepal.LitePal;

/**
 * @author ish
 */
public class NoteBookModel {

    /**
     * 创建单词本
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
            return ;
        }
        //创建新笔记本
        long createTime = ToolsUtils.getInstance().getInstanceTime();
        long editTime = createTime;
        String createTimeString = ToolsUtils.getInstance().getDateFormat(createTime);
        String editTimeString = ToolsUtils.getInstance().getDateFormat(editTime);
        NoteBook noteBook = new NoteBook(0, createTime, editTime, bookName, editTimeString, createTimeString);

        //在子线程插入，监听插入结果
        noteBook.saveAsync().listen(new org.litepal.crud.callback.SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                DataBaseEvent<String> eventMsg = new DataBaseEvent<>();
                if (success) {
                    eventMsg.setEventCode(DataBaseEvent.INSERT_SUCCESS);
                    eventMsg.setMsg("创建成功");
                } else {
                    eventMsg.setEventCode(DataBaseEvent.INSERT_FAIL);
                    eventMsg.setMsg("创建失败");
                }
                RxBus.getInstance().post(eventMsg);
            }
        });
    }
}
