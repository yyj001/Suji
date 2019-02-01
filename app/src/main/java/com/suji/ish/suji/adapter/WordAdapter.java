package com.suji.ish.suji.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.loopeer.shadow.ShadowView;
import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.Word;
import com.suji.ish.suji.databinding.ItemWordBinding;
import com.suji.ish.suji.utils.ToolsUtils;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private List<Word> list;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static ViewDataBinding headerBinding;


    public WordAdapter(List<Word> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果头部头部存在
        if (viewType == TYPE_HEADER && headerBinding != null) {
            return new ViewHolder(headerBinding);
        }
        ItemWordBinding itemNotebookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_word, parent, false);
        return new ViewHolder(itemNotebookBinding);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        int pos = getRealPosition(holder);
        Word word = list.get(pos);

        int marginLength = ToolsUtils.getInstance().dp2px(10);
        if(pos==0 && headerBinding==null && list.size()>1){
            ((ShadowView)(holder.getBinding().getRoot())).setShadowMargin(marginLength,marginLength,marginLength,0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTL(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTR(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBL(0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBR(0);
        }else if(pos==list.size()-1 && list.size()>1){
            ((ShadowView)(holder.getBinding().getRoot())).setShadowMargin(marginLength,0,marginLength,marginLength);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBL(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBR(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTL(0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTR(0);
        }else if(list.size()==1) {
            ((ShadowView)(holder.getBinding().getRoot())).setShadowMargin(marginLength,marginLength,marginLength,marginLength);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBL(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBR(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTL(marginLength/2);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTR(marginLength/2);
        }else{
            ((ShadowView)(holder.getBinding().getRoot())).setShadowMargin(marginLength,0,marginLength,0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBL(0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusBR(0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTL(0);
            ((ShadowView)(holder.getBinding().getRoot())).setCornerRadiusTR(0);
        }

        ((ItemWordBinding) holder.getBinding()).setWord(word);
        holder.getBinding().executePendingBindings();

        //跳转笔记本页面
//        Bundle bundle = new Bundle();
//        bundle.putString("bookName", noteBook.getBookName());
//        bundle.putInt("bookId", noteBook.getId());
//        holder.getBinding().getRoot()
//                .setOnClickListener(Navigation.createNavigateOnClickListener(
//                        R.id.action_noteBookFragment_to_noteBookPageFragment,bundle)
//                );
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
        WordAdapter.headerBinding = binding;
        notifyItemInserted(0);
    }

    public boolean hasHeader() {
        if (headerBinding != null) {
            return true;
        }
        return false;
    }

    public List getList(){
        return this.list;
    }

    public void setList(List list){
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return headerBinding == null ? list.size() : list.size() + 1;
        }
        return 0;
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
    }

}


