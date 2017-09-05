package com.summer.parent.base;

import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.recycle.ScollViewRefreshLayout;
import com.summer.helper.view.SRecycleView;

/**
 * Created by xiaqiliang on 2017/3/24.
 */
public abstract class BaseRequestFragment extends BaseFragmentActivity {
    SRecycleView sRecycleView;
    ScollViewRefreshLayout scrollView;
    protected long fromId;

    public void setSRecyleView(SRecycleView svContainer){
        this.sRecycleView = svContainer;
        svContainer.setList();
        svContainer.setLoadMore();
        svContainer.setOverLay();
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                fromId = 0;
                pageIndex = 0;
                isRefresh = true;
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                pageIndex ++;
                isRefresh = true;
                loadData();
            }
        });
    }

    public void setScrollView(ScollViewRefreshLayout scrollView){
        this.scrollView = scrollView;
        scrollView.setOverLay();
        scrollView.setLoadMore();
        scrollView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                fromId = 0;
                isRefresh = true;
                pageIndex = 0;
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                pageIndex++;
                isRefresh = true;
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void finishLoad() {
        if(sRecycleView != null){
            if(pageIndex == 0){
                sRecycleView.finishRefresh();
            }else{
                sRecycleView.finishRefreshLoadMore();
            }
        }
        if(scrollView != null){
            if(pageIndex == 0){
                scrollView.finishRefresh();
            }else{
                scrollView.finishRefreshLoadMore();
            }
        }
    }

}
