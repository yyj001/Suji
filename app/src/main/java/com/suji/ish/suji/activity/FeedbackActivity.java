package com.suji.ish.suji.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.suji.ish.suji.R;

/**
 * @author ish
 */
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView(){
        mBackBtn = findViewById(R.id.feedback_cancel);
        mBackBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feedback_cancel:{
                hideKeyboard(view);
                finish();
                break;
            }
            default:
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
