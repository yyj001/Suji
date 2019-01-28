package com.suji.ish.suji.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.loopeer.shadow.ShadowView;
import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.databinding.ItemNotebookBinding;
import com.suji.ish.suji.utils.ToolsUtils;

import java.util.List;

import androidx.navigation.Navigation;

public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.ViewHolder> {

    private List<NoteBook> list;
    private Activity mActivity;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static ViewDataBinding headerBinding;


    public NoteBookAdapter(List<NoteBook> list, Activity activity) {
        this.list = list;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果头部头部存在
        if (viewType == TYPE_HEADER && headerBinding != null) {
            return new ViewHolder(headerBinding);
        }
        ItemNotebookBinding itemNotebookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_notebook, parent, false);
        return new ViewHolder(itemNotebookBinding);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        int pos = getRealPosition(holder);
        //设置首尾的样式
        int marginLength = ToolsUtils.getInstance().dp2px(mActivity,20);
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

        NoteBook noteBook = list.get(pos);
        ((ItemNotebookBinding) holder.getBinding()).setBook(noteBook);
        holder.getBinding().executePendingBindings();
        String name = ToolsUtils.getInstance().handleText(noteBook.getBookName(),22);
        ((ItemNotebookBinding) holder.getBinding()).notebookName.setText(name);

        //跳转笔记本页面
        Bundle bundle = new Bundle();
        bundle.putString("bookName", noteBook.getBookName());
        bundle.putInt("bookId", noteBook.getId());
        holder.getBinding().getRoot()
                .setOnClickListener(Navigation.createNavigateOnClickListener(
                        R.id.action_noteBookFragment_to_noteBookPageFragment,bundle)
                );
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
        NoteBookAdapter.headerBinding = binding;
        notifyItemInserted(0);
    }

    public boolean hasHeader() {
        if (headerBinding != null) {
            return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return headerBinding == null ? list.size() : list.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
//            if (binding == headerBinding) {
//                return;
//            }
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }
    }
}

