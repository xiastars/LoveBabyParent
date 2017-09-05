package com.summer.parent.base;

import android.content.Intent;

import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.recycle.ScollViewRefreshLayout;
import com.summer.helper.utils.ReceiverUtils;
import com.summer.helper.view.LoadingDialog;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.SRecycleView;
import com.summer.parent.MainActivity;

/**
 * Created by xiaqiliang on 2017/3/24.
 */
public abstract class BaseActivity extends BaseRequestActivity {
    SRecycleView sRecycleView;
    ScollViewRefreshLayout scrollView;

    ReceiverUtils receiverUtils;

    public void setSRecyleView(final SRecycleView svContainer) {
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
                svContainer.setLoadMore();
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

    public void handleViewData(Object obj) {
        if (sRecycleView == null) {
            return;
        }
        if (sRecycleView.getRefreshView() == null) {
            return;
        }
        baseHelper.handleViewData(obj, sRecycleView, pageIndex);
    }

    public void handleViewData(Object obj, NRecycleView nRecycleView) {
        baseHelper.handleViewData(obj, nRecycleView, sRecycleView == null ? scrollView : sRecycleView, pageIndex);
    }

    protected void initBroadcast(String... action) {
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
        receiverUtils = new ReceiverUtils(this);
        receiverUtils.setActionsAndRegister(action);
        receiverUtils.setOnReceiverListener(new ReceiverUtils.ReceiverListener() {
            @Override
            public void doSomething(int position, Intent intent) {
                onMsgReceiver(position, intent);
            }
        });
    }

    protected void sendBroadcast(String action){
        Intent intent=new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    public void setScrollView(final ScollViewRefreshLayout scrollView) {
        this.scrollView = scrollView;
        scrollView.setOverLay();
        scrollView.setLoadMore();
        scrollView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if (!isRefresh) {
                    fromId = 0;
                    isRefresh = true;
                    pageIndex = 0;
                    scrollView.setLoadMore();
                    loadData();
                }
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if (!isRefresh) {
                    pageIndex++;
                    isRefresh = true;
                    loadData();
                }
            }
        });
    }

    protected void cancelLoading(LoadingDialog dialog) {
        if (dialog != null) {
            dialog.cancelLoading();
        }
    }

    protected void onMsgReceiver(int type, Intent intent) {

    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void finishLoad() {
        isRefresh = false;
        if (sRecycleView != null) {
            if (pageIndex == 0) {
                sRecycleView.finishRefresh();
            } else {
                sRecycleView.finishRefreshLoadMore();
            }
        }

        if (scrollView != null) {
            if (pageIndex == 0) {
                scrollView.finishRefresh();
            } else {
                scrollView.finishRefreshLoadMore();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
    }
}
