package com.suji.ish.suji.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.suji.ish.suji.R;
import com.suji.ish.suji.utils.ToolsUtils;

import androidx.navigation.Navigation;

public class AddWordFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private MaterialEditText materialEditText;
    private LinearLayout mOCRView;
    private ImageView mCancelTv;
    private TextView mSearchTv;
    private int maxWordNum = 40;
    private View mWiew;

    public static AddWordFragment newInstance() {
        return new AddWordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mWiew == null) {
            mWiew = inflater.inflate(R.layout.add_word_fragment, container, false);
            materialEditText = mWiew.findViewById(R.id.add_word_edittext);
            materialEditText.addTextChangedListener(this);

            mCancelTv = mWiew.findViewById(R.id.add_word_cancel);
            mCancelTv.setOnClickListener(this);

            mSearchTv = mWiew.findViewById(R.id.add_word_search);
            mSearchTv.setOnClickListener(this);
            mSearchTv.setClickable(false);

            mOCRView = mWiew.findViewById(R.id.add_word_ocr);
            mOCRView.setOnClickListener(this);
        }
        return mWiew;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_word_cancel: {
                hideKeyboard(view);
                getActivity().finish();
                break;
            }
            case R.id.add_word_search: {
                hideKeyboard(view);

                String spell = materialEditText.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("spell", spell);

                //检查是否含有中文
                boolean flag = true;
                for (int num = 0; num < spell.length(); ++num) {
                    if (!judgeLegal(spell.charAt(num))) {
                        flag = false;
                        break;
                    }
                }
                //英文搜索
                if (flag) {
                    Navigation.findNavController(view).navigate(R.id.action_addWordFragment_to_wordDetailFragment, bundle);
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_addWordFragment_to_chineseSearchResultFragment, bundle);
                }
                break;
            }
            case R.id.add_word_ocr: {
                break;
            }
            default:
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("dwdwed", "onTextChanged: " + i + i1 + i2);

        int size = charSequence.toString().length();
        if (size > 0 && size <= maxWordNum) {
            createNoteEnable();
        } else {
            createNoteDisable();
        }

//        //检查是否含有中文
//        boolean flag = true;
//        for (int num = 0; num < size; ++num) {
//            if (!judgeLegal(charSequence.toString().charAt(num))) {
//                createNoteDisable();
//                flag = false;
//                break;
//            }
//        }
//        //todo 应该给这里添加错误提示，还没有时间做
//        if (!flag) {
//            createNoteDisable();
//        }
    }

    /**
     * 判断是否含有中文
     *
     * @param c
     * @return
     */
    private boolean judgeLegal(char c) {
        if ((int) c > 0 && (int) c < 256) {
            return true;
        }
        return false;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void createNoteEnable() {
        mSearchTv.setClickable(true);
        mSearchTv.setTextColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.colorAccent));
    }

    private void createNoteDisable() {
        mSearchTv.setClickable(false);
        mSearchTv.setTextColor(ToolsUtils.getInstance().getColor(getActivity(), R.color.greya));
    }

    /**
     * 退出时隐藏键盘
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
