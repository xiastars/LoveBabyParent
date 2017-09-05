/*
package com.summer.helper.recycle;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.andview.refreshview.XRefreshView;
import com.summer.helper.view.CustomHeader;

*/
/**
 * Created by xiaqiliang on 2017/7/6.
 *//*


public class SRefreshView extends XRefreshView {
    RecyclerView refreshView;
    Context context;
    MaterialRefreshListener refreshListener;
    boolean isLoadMoreing;

    protected boolean refreshable;
    boolean loadMore;

    public SRefreshView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();

    }

    private void init() {
        refreshView = new RecyclerView(context);
        this.addView(refreshView);
        this.setAutoLoadMore(true);
        this.setCustomHeaderView(new CustomHeader(context, 300));
    }

    public void setMaterialRefreshListener(final MaterialRefreshListener listener) {
        this.refreshListener = listener;
        this.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                listener.onRefresh(null);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                isLoadMoreing = true;
                listener.onRefreshLoadMore(null);
            }
        });
    }

    public RecyclerView getRefreshView() {
        return refreshView;
    }

    public void autoRefreshLoadMore() {

    }

    public boolean canChildScrollDown() {
        if (refreshView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(refreshView, 1) || refreshView.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(refreshView, 1);
        }
    }

    public boolean isLoadMore() {
        return this.getPullLoadEnable();
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
        this.setPullLoadEnable(loadMore);
    }

    public void setAutoLoadmore() {
        if (refreshListener != null && loadMore && !isLoadMoreing) {
            isLoadMoreing = true;
            refreshListener.onRefreshLoadMore(null);
        }
    }

    public void finishRefresh() {
        this.stopRefresh();
    }

    public void finishRefreshLoadMore() {
        isLoadMoreing = false;
        this.stopLoadMore();
    }

    public void setIsOverLay(boolean overLay) {

    }
}
*/
