package com.suji.ish.suji.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.suji.ish.suji.R;
import com.suji.ish.suji.model.NoteBookModel;
import com.suji.ish.suji.rxjava.DataBaseEvent;
import com.suji.ish.suji.rxjava.RxBus;
import com.suji.ish.suji.utils.ToolsUtils;
import com.suji.ish.suji.view.ResultPopupWindow;
import com.suji.ish.suji.view.ResultView;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * @author ish
 */
public class AddNoteBookActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView mCancelTx;
    private TextView mCreateTx;
    private MaterialEditText mEditText;
    private int maxWordNum = 30;

    private ResultPopupWindow mResultPopupWindow;
    private Handler mHandler;
    private NoteBookModel mNoteBookModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_book);

        mCancelTx = findViewById(R.id.add_notebook_cancel);
        mCancelTx.setOnClickListener(this);

        mEditText = findViewById(R.id.add_notebook_edittext);
        mEditText.addTextChangedListener(this);

        mCreateTx = findViewById(R.id.add_notebook_create);
        mCreateTx.setOnClickListener(this);
        mCreateTx.setClickable(false);

        mHandler = new Handler();
        mNoteBookModel = new NoteBookModel();

        //根据创建结果创建弹窗
        RxBus.getInstance()
                .toObservable()
                .map(new Function<Object, DataBaseEvent>() {
                    @Override
                    public DataBaseEvent apply(Object o) throws Exception {
                        return (DataBaseEvent) o;
                    }
                })
                .subscribe(new Consumer<DataBaseEvent>() {
                    @Override
                    public void accept(DataBaseEvent eventMsg) throws Exception {
                        if (eventMsg != null) {
                            showCreateBookResult(eventMsg);
                        }
                    }
                });

    }

    /**
     * 退出时隐藏键盘
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_notebook_cancel: {
                hideKeyboard(view);
                finish();
                break;
            }
            //创建单词本
            case R.id.add_notebook_create: {
                hideKeyboard(view);
                mNoteBookModel.createNotebook(mEditText.getText().toString());
            }
            default:
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int size = charSequence.toString().length();
        if (size > 0 && size <= maxWordNum) {
            createNoteEnable();
        } else {
            createNoteDisable();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private void createNoteEnable() {
        mCreateTx.setClickable(true);
        mCreateTx.setTextColor(ToolsUtils.getInstance().getColor(this, R.color.colorAccent));
    }

    private void createNoteDisable() {
        mCreateTx.setClickable(false);
        mCreateTx.setTextColor(ToolsUtils.getInstance().getColor(this, R.color.greya));
    }

    /**
     * 根据插入数据库结果来实现弹窗
     *
     * @param event
     */
    private void showCreateBookResult(DataBaseEvent event) {
        mResultPopupWindow = new ResultPopupWindow(AddNoteBookActivity.this);
        mResultPopupWindow.setBackground(0);
        mResultPopupWindow.setBlurBackgroundEnable(true);
        mResultPopupWindow.setBackgroundColor(ToolsUtils.getInstance().getColor(this, R.color.AlphaColorPrimaryDark));
        ResultView resultView = mResultPopupWindow.findViewById(R.id.create_notebook_resultview);
        TextView msgTv = mResultPopupWindow.findViewById(R.id.create_notebook_tv);
        //选中不同的resultView
        switch (event.getEventCode()) {
            case DataBaseEvent.INSERT_SUCCESS:
                resultView.setType(ResultView.TYPE_SUCESS);
                //先关window再退出
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() { mResultPopupWindow.dismiss();
                    }
                }, 800);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1200);
                break;
            case DataBaseEvent.INSERT_FAIL:
                resultView.setType(ResultView.TYPE_FAILD);
                break;
            default:
        }
        msgTv.setText(event.getMsg().toString());
        mResultPopupWindow.showPopupWindow();
        resultView.play();
    }
}
