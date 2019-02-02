package com.suji.ish.suji.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.suji.ish.suji.R;
import com.suji.ish.suji.utils.ToolsUtils;

import razerdp.basepopup.BasePopupWindow;

public class BookSettingWindow extends BasePopupWindow implements TextWatcher {

    private TextView saveTv;
    private MaterialEditText editText;
    private TextView cancelTv;
    private TextView deleteTv;
    public BookSettingWindow(Context context) {
        super(context);
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.popup_book_setting_window);
        saveTv = view.findViewById(R.id.setting_book_save);
        deleteTv = view.findViewById(R.id.setting_notebook_delete);
        cancelTv = view.findViewById(R.id.setting_book_cancel);
        editText = view.findViewById(R.id.setting_notebook_edittext);
        editText.addTextChangedListener(this);
        return view;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int size = charSequence.toString().length();
        if (size > 0 && size <= 30) {
            createNoteEnable();
        } else {
            createNoteDisable();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void createNoteEnable() {
        saveTv.setClickable(true);
        saveTv.setTextColor(ToolsUtils.getInstance().getColor(R.color.colorAccent));
    }

    private void createNoteDisable() {
        saveTv.setClickable(false);
        saveTv.setTextColor(ToolsUtils.getInstance().getColor(R.color.grey8));
    }

    public TextView getSaveTv() {
        return saveTv;
    }

    public void setSaveTv(TextView saveTv) {
        this.saveTv = saveTv;
    }

    public MaterialEditText getEditText() {
        return editText;
    }

    public void setEditText(MaterialEditText editText) {
        this.editText = editText;
    }

    public TextView getCancelTv() {
        return cancelTv;
    }

    public void setCancelTv(TextView cancelTv) {
        this.cancelTv = cancelTv;
    }

    public TextView getDeleteTv() {
        return deleteTv;
    }

    public void setDeleteTv(TextView deleteTv) {
        this.deleteTv = deleteTv;
    }

}
