package com.summer.helper.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.helper.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * 当需要上拉加载时，底部添加加载完了
 */
public abstract class SRecycleMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public List<?> items;
    protected int headerCount;
    protected int bottomCount = 1;
    boolean bottomVisible = false;
    protected boolean showEmptyView;

    ViewGroup headerView;

    /**
     * 正在刷新中
     */
    protected boolean isAdapterRefresh;

    public SRecycleMoreAdapter(Context context) {
        this.context = context;
    }


    public SRecycleMoreAdapter(Context context, ViewGroup headerView) {
        this.context = context;
        this.headerView = headerView;
        setHeaderCount(1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.TYPE_BOTTOM) {
            Logs.i("showEmp:" + showEmptyView + ",,," + getRealItemCount());
            View view = LayoutInflater.from(context).inflate(R.layout.view_bottom_nomore, parent, false);
            return new BottomHolder(view);
        } else if (viewType == ViewType.TYPE_CONTENT) {
            return setContentView(parent);
        } else if (viewType == ViewType.TYPE_TOP) {
            if (headerView != null) {
                return new TopHolder(headerView);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position < headerCount) {
            bindHeaderView(holder, position);
        } else if (position < getItemCount() - 1) {
            bindContentView(holder, position - headerCount);
        } else {
            RelativeLayout rlView = ((BottomHolder) holder).rlParent;
            if (rlView == null) {
                return;
            }
            ViewGroup.LayoutParams params = rlView.getLayoutParams();
            boolean showBottom = getRealItemCount() == 0 && showEmptyView;
            if (bottomVisible) {
                rlView.setVisibility(View.VISIBLE);
                params.height = showBottom ? SUtils.getDip(context, 300) : SUtils.getDip(context, 42);
                params.width = SUtils.screenWidth;
                if(showBottom){
                    ((BottomHolder) holder).llEmpty.setVisibility(View.VISIBLE);
                    ((BottomHolder) holder).tvNomore.setVisibility(View.GONE);
                }else{
                    ((BottomHolder) holder).llEmpty.setVisibility(View.GONE);
                    ((BottomHolder) holder).tvNomore.setVisibility(View.VISIBLE);
                }
            } else {
                rlView.setVisibility(View.GONE);
                params.height = SUtils.getDip(context, 1);
                params.width = 1;
            }
        }
    }

    /**
     * 处理头部数据
     *
     * @param holder
     * @param position
     */
    protected void bindHeaderView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < headerCount; i++) {
            if (position == i) {
                return ViewType.TYPE_TOP;
            }
        }
        if (position == getItemCount() - 1) {
            return ViewType.TYPE_BOTTOM;
        }
        return ViewType.TYPE_CONTENT;
    }

    public void notifyDataChanged(List<?> comments) {
        this.items = comments;
        if (comments != null && !comments.isEmpty()) {
            setBottomViewGONE();
        }
        notifyDataSetChanged();
    }

    /**
     * SRecycleView有自定义的空界面
     */
    public void showEmptyView() {
        this.items = null;
        setBottomViewGONE();
        notifyDataSetChanged();
    }

    /**
     * NRecycleVIew显示空页面
     */
    public void showNEmptyView(){
        items = new ArrayList<>();
        setBottomViewVisible();
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<?> comments, boolean hideBottom) {
        this.items = comments;
        if (hideBottom && comments != null && !comments.isEmpty()) {
            setBottomViewGONE();
        } else {
            setBottomViewVisible();
        }
        if (comments != null)
            notifyDataSetChanged();
    }

    public int getRealItemCount() {
        return items != null ? items.size() : 0;
    }

    public int getItemCount() {
        return items != null ? items.size() + bottomCount + headerCount : bottomCount;
    }

    public void setHeaderView(ViewGroup headerView) {
        this.headerView = headerView;
    }

    private class BottomHolder extends RecyclerView.ViewHolder {

        TextView tvNomore;
        RelativeLayout rlParent;
        LinearLayout llEmpty;

        public BottomHolder(View itemView) {
            super(itemView);
            tvNomore = (TextView) itemView.findViewById(R.id.tv_nomore);
            rlParent = (RelativeLayout) itemView.findViewById(R.id.rl_parent);
            llEmpty = (LinearLayout) itemView.findViewById(R.id.ll_empty);
        }
    }

    private class TopHolder extends RecyclerView.ViewHolder {

        public TopHolder(View itemView) {
            super(itemView);
        }
    }

    public void setBottomViewVisible() {
        bottomVisible = true;
        if (isAdapterRefresh) {
            isAdapterRefresh = false;
            return;
        }
        isAdapterRefresh = true;
        Logs.i("xia", "没有更多了");
        try {
            notifyItemChanged(getItemCount() - 1);
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
        isAdapterRefresh = false;
    }

    public boolean isShowEmptyView() {
        return showEmptyView;
    }

    public void setShowEmptyView() {
        this.showEmptyView = true;
    }

    public void setBottomViewGONE() {
        bottomVisible = false;
    }

    public void setHeaderCount(int count) {
        this.headerCount = count;
    }

    public class ViewType {

        /**
         * 主体内容
         */
        public static final int TYPE_CONTENT = 1;

        /**
         * 底部
         */
        public static final int TYPE_BOTTOM = 2;

        /**
         * 头部
         */
        public static final int TYPE_TOP = 0;
    }

    public abstract RecyclerView.ViewHolder setContentView(ViewGroup parent);

    public abstract void bindContentView(RecyclerView.ViewHolder holder, int position);


}
