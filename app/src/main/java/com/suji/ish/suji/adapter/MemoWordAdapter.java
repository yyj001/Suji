package com.suji.ish.suji.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.FragmentMemoContentBinding;
import com.suji.ish.suji.databinding.ItemMemoWordBinding;
import com.suji.ish.suji.fragment.MemoryFragment;
import com.suji.ish.suji.utils.AudioPlayer;
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
    private MemoryFragment mFragment;


    public MemoWordAdapter(List<Word> list, Context context, MemoryFragment fragment) {
        this.list = list;
        this.mActivity = context;
        this.mFragment = fragment;
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
        final int pos = getRealPosition(holder);
        final Word word = list.get(pos);
        ((ItemMemoWordBinding) holder.getBinding()).setWord(word);

//        //清空所有
        ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer.removeAllViews();

        View frameLayout = FrameLayout.inflate(mActivity, R.layout.layout_memo_empty, null);
        ((ItemMemoWordBinding) holder.getBinding()).memoContentContainer.addView(frameLayout);

        //点击替换
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContent(holder, word);
            }
        });

        //发音
        ((ItemMemoWordBinding) holder.getBinding()).memoPlayVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (word.getPhEnMp3() == null || word.getPhEnMp3().equals("")) {
                    Toast.makeText(mActivity, "获取发音失败", Toast.LENGTH_SHORT).show();
                } else {
                    AudioPlayer.getInstance().playAudio(word.getPhEnMp3());
                }
            }
        });
        ((ItemMemoWordBinding) holder.getBinding()).memoPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (word.getPhEnMp3() == null || word.getPhEnMp3().equals("")) {
                    Toast.makeText(mActivity, "获取发音失败", Toast.LENGTH_SHORT).show();
                } else {
                    AudioPlayer.getInstance().playAudio(word.getPhEnMp3());
                }
            }
        });

        ((ItemMemoWordBinding) holder.getBinding()).memoRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.rememberWord(word, pos);
            }
        });



        //恢复为未点击状态，为了保证滚动动画不卡顿，放在下一次进行
        if(!holder.isForgetClick()){
            ((ItemMemoWordBinding) holder.getBinding()).memoForget.setImageResource(R.drawable.error);
        }
        //两次点击不一样用holder里的变量标识，否则第一次点击后会刷新item
        ((ItemMemoWordBinding) holder.getBinding()).memoForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.isForgetClick()){
                    ((ImageView) view).setImageResource(R.drawable.next);
                    mFragment.forgetWord(word);
                    showContent(holder, word);
                    holder.setForgetClick(true);
                }else{
                    holder.setForgetClick(false);
                    mFragment.nextWord(word, pos);
                }
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

        setSentence(contentBinding, word);
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

                SpannableString spannableString = ToolsUtils.getInstance().getHightLightSentence(enSentence, word);
//                enTv.setText(getHightLightSentence(enSentence, word.getSpell()));
                enTv.setText(spannableString);

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

        private boolean forgetClick = false;

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

        public boolean isForgetClick() {
            return forgetClick;
        }

        public void setForgetClick(boolean forgetClick) {
            this.forgetClick = forgetClick;
        }
    }

}


