package com.suji.ish.suji.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suji.ish.suji.R;
import com.suji.ish.suji.bean.NoteBook;
import com.suji.ish.suji.databinding.ItemNotebookBinding;

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
        NoteBook noteBook = list.get(pos);
        ((ItemNotebookBinding) holder.getBinding()).setBook(noteBook);
        holder.getBinding().executePendingBindings();

        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_noteBookFragment_to_bookPageFragment);
            }
        });
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

