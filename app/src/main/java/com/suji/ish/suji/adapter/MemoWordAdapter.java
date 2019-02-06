package com.suji.ish.suji.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.FragmentMemoContentBinding;
import com.suji.ish.suji.databinding.ItemMemoWordBinding;
import com.suji.ish.suji.utils.ToolsUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoWordAdapter extends RecyclerView.Adapter<MemoWordAdapter.ViewHolder> {

    private static final String TAG = "MemoWordAdapter";
    private List<Word> list;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static ViewDataBinding headerBinding;
    private Context mActivity;
    private FragmentManager manager;


    public MemoWordAdapter(List<Word> list, Context context) {
        this.list = list;
        this.mActivity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果头部头部存在
        if (viewType == TYPE_HEADER && headerBinding != null) {
            return new ViewHolder(headerBinding);
        }
        ItemMemoWordBinding itemNotebookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_memo_word, parent, false);
        return new ViewHolder(itemNotebookBinding);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        int pos = getRealPosition(holder);
        final Word word = list.get(pos);
        Log.d(TAG, "onBindViewHolder: ");
        ((ItemMemoWordBinding) holder.getBinding()).setWord(word);

//        //清空所有
        ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer.removeAllViews();
//        //再设置例句
//        setSentence((ItemMemoWordBinding) holder.getBinding(), word);

        View frameLayout = FrameLayout.inflate(mActivity, R.layout.layout_memo_empty, null);
        ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer.addView(frameLayout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //移除原有的
                //将内容显示
                showContent(holder, word);
            }
        });

        holder.getBinding().executePendingBindings();

    }

    private void showContent(ViewHolder holder, Word word) {
        ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer.removeAllViews();

        FragmentMemoContentBinding contentBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity),
                R.layout.fragment_memo_content,
                ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer,
                false);
        contentBinding.setWord(word);

        setSentence(contentBinding,word);
        ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer.addView(contentBinding.getRoot());

    }


    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerBinding == null ? position : position - 1;
    }

    /**
     * 重写获取位置的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (headerBinding == null) {
            return TYPE_ITEM;
        }
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    /**
     * 插入头部
     */
    public void setHeaderView(ViewDataBinding binding) {
        MemoWordAdapter.headerBinding = binding;
        notifyItemInserted(0);
    }

    public boolean hasHeader() {
        if (headerBinding != null) {
            return true;
        }
        return false;
    }

    public List getList() {
        return this.list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return headerBinding == null ? list.size() : list.size() + 1;
        }
        return 0;
    }

    private boolean judgeEmpty(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        return false;
    }

    public void setSentence(FragmentMemoContentBinding binding, Word word) {
        String sentence = word.getSentence();
        if (sentence == null) {
            return;
        }

        String[] group = sentence.split("/r/n\r\n");
        for (String part : group) {
            String[] singleSentence = part.split("/r/n     ");
            if (singleSentence.length == 2) {
                String enSentence = singleSentence[0];
                String chSentence = singleSentence[1].split("/r/n")[0] + "\n";

                TextView enTv = new TextView(mActivity);
                enTv.setTextColor(Color.BLACK);
                enTv.setText(getHightLightSentence(enSentence, word.getSpell()));

                TextView chTv = new TextView(mActivity);
                chTv.setTextColor(ToolsUtils.getInstance().getColor(mActivity, R.color.greya));
                chTv.setText(chSentence);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                binding.memoSentenceContainer.addView(enTv, layoutParams);
                binding.memoSentenceContainer.addView(chTv, layoutParams);
            }

        }
    }

    /**
     * 设置高亮
     *
     * @param sourceStr
     * @return
     */
    public SpannableString getHightLightSentence(String sourceStr, String spell) {
        SpannableString s = new SpannableString(sourceStr);
        //不区分大小写
        Pattern p = Pattern.compile(spell, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(ToolsUtils.getInstance().getColor(R.color.colorAccent)),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

    }

}


