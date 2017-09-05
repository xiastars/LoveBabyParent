package com.summer.parent.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.parent.server.CUtils;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.recycle.ScollViewRefreshLayout;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.SRecycleView;

import java.lang.ref.WeakReference;

/**
 * Created by xiaqiliang on 2017/4/15.
 */

public abstract class BaseFragment extends Fragment {
    MaterialRefreshLayout sRecycleView;
    public MyHandler myHandlder;
    public long fromId;
    public int pageIndex;
    public Context context;
    boolean isRefresh;
    public static int PAGE_FROM = 0;
    BaseHelper baseHelper;

    boolean isOnRefresh;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = getContext();
        myHandlder = new MyHandler(this);
        baseHelper = new BaseHelper(context, myHandlder);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(setContentView(), null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setSRecyleView(final SRecycleView svContainer) {
        this.sRecycleView = svContainer;
        svContainer.setList();
        svContainer.setLoadMore();
        svContainer.setOverLay();
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isOnRefresh){
                    return;
                }
                fromId = 0;
                pageIndex = PAGE_FROM;
                svContainer.setLoadMore();
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if(isOnRefresh){
                    return;
                }
                pageIndex++;
                loadData();
            }
        });
    }

    public void setSRecyleView(final ScollViewRefreshLayout svContainer) {
        this.sRecycleView = svContainer;
        svContainer.setLoadMore();
        svContainer.setOverLay();
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if(isOnRefresh){
                    return;
                }
                fromId = 0;
                pageIndex = PAGE_FROM;
                svContainer.setLoadMore();
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if(isOnRefresh){
                    return;
                }
                pageIndex++;
                loadData();
            }
        });
    }

    public void handleViewData(Object obj) {
        baseHelper.handleViewData(obj, sRecycleView, pageIndex);
    }

    public void handleViewData(Object obj, NRecycleView view) {
        baseHelper.handleViewData(obj, view, sRecycleView, pageIndex);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<BaseFragment> mActivity;

        public MyHandler(BaseFragment activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseFragment activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case BaseHelper.MSG_SUCCEED:
                        activity.handleData(msg.arg1,msg.obj);
                        break;
                    case BaseHelper.MSG_FINISHLOAD:
                        activity.finishLoad();
                        break;
                    case BaseHelper.MSG_CACHE:
                        activity.handleData(msg.arg1,msg.obj);
                        break;
                    case BaseHelper.MSG_ERRO:
                        activity.dealErrors(msg.arg1, msg.arg2 + "", (String) msg.obj);
                        break;
                    default:
                        activity.handleMsg(msg.what, msg.obj);
                }
            }
        }
    }

    protected void handleMsg(int position, Object object) {

    }

    private void handleData(int requestType,Object object) {
        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                isOnRefresh = false;
            }
        },100);
        if (this.getActivity() == null || this.getActivity().isFinishing()) {
            return;
        }
        if (object == null) {
            return;
        }
        if (object instanceof BaseResp) {
            BaseResp resp = (BaseResp) object;
            /*if (toastMessage) {
                new CodeRespondUtils(context, resp);
            } else {
                new CodeRespondUtils(context, resp.getCode());
            }*/
            String code = resp.getCode();
            //如果不是成功状态不返回
            if (TextUtils.isEmpty(code) || !code.equals("0")) {
                dealErrors(requestType,code,resp.getMessage());
                return;
            }
        }
        dealDatas(requestType,object);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    protected void finishLoad(ScollViewRefreshLayout svContainer) {
        if (svContainer != null) {
            if (pageIndex == PAGE_FROM) {
                svContainer.finishRefresh();
            } else {
                svContainer.finishRefreshLoadMore();
            }
        }
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    protected void finishLoad() {
        if (sRecycleView != null) {
            if (pageIndex == PAGE_FROM) {
                sRecycleView.finishRefresh();
            } else {
                sRecycleView.finishRefreshLoadMore();
            }
        }
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    public void requestData(Class className, SummerParameter params, final String url, boolean post) {
        requestData(0,className,params,url,post);
    }

    public void requestData(int requestCode,Class className, SummerParameter params, final String url, boolean post) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode,className, params, url, post);
    }

    public void requestData(int requestCode,int limitTime,Class className, SummerParameter params, final String url, boolean post) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode,limitTime,className, params, url, post);
    }

    @Override
    public void onResume() {
        super.onResume();
        CUtils.onClick(getClass().getSimpleName() + "_start");
    }

    /**
     * 处理错误
     * @param requestType
     * @param errString
     */
    protected void dealErrors(int requstCode,String requestType,String errString){
        if(baseHelper != null){
            baseHelper.cancelLoading();
            finishLoad();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CUtils.onClick(getClass().getSimpleName() + "_pause");
    }

    protected abstract void initView(View view);

    protected abstract void loadData();

    protected abstract void dealDatas(int requestType,Object obj);

    protected abstract int setContentView();

}
