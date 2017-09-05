package com.summer.helper.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class SRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public List<?> items;
    boolean loadFinished;

    public SRecycleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void showLoadFinish() {
        this.loadFinished = true;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void notifyDataChanged(List<?> comments) {
        this.items = comments;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return items != null ? items.size() + (loadFinished ? 1 : 0) : 0;
    }


}
