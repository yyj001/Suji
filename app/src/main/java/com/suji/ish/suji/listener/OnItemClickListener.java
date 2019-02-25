package com.suji.ish.suji.listener;

import android.view.View;

import com.suji.ish.suji.bean.Word;

public interface OnItemClickListener {
    void onItemClick(View view, int position, Word word);
    void onItemLongClick(View view,int position,Word word);
}
