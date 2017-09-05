package com.summer.parent.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.parent.R;
import com.summer.parent.server.CUtils;
import com.summer.helper.listener.OnShareListener;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.BitmapUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.ReceiverUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.resize.RRelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by xiaqiliang on 2017/3/24.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    TextView tvTitle;
    View viewBack;
    RelativeLayout rlBaseTop;
    RRelativeLayout rlTitle;
    FrameLayout flContainer;
    public Context context;
    protected MyHandler myHandlder;
    protected int pageIndex;
    boolean isRefresh;

    BaseHelper baseHelper;
    ReceiverUtils receiverUtils;

    /**
     * 是否显示空界面
     */
    boolean isEmptyViewShowing;

    boolean isActivityFinished;
    OnShareListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        myHandlder = new MyHandler(this);
        baseHelper = new BaseHelper(context, myHandlder);
        setContentView(R.layout.activity_base);
        initTitleView();
        checkView();
    }

    private void checkView() {
        if (!SUtils.isNetworkAvailable(context)) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_network_broken, null);
            flContainer.addView(view);
        } else {
            initView();
            initData();
        }
    }

    public void showEmptyView() {
        isEmptyViewShowing = true;
        flContainer.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
        flContainer.addView(view);
    }

    public void stripEmptyView() {
        if (isEmptyViewShowing) {
            flContainer.removeAllViews();
            initView();
        }
    }

    /**
     * 初始化分享按钮
     */
    protected void initShareButton() {
        Button btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onShare();
                }
                Logs.i("onshare:::::");
            }
        });
    }

    protected void setShareListener(OnShareListener listener) {
        this.listener = listener;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void changeHeaderStyleTrans(int color) {
        if (rlTitle != null) {
            rlTitle.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    private void initTitleView() {
        rlBaseTop = (RelativeLayout) findViewById(R.id.rl_base_parent);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (setTitleId() != 0) {
            tvTitle.setText(getString(setTitleId()));
        }
        rlTitle = (RRelativeLayout) findViewById(R.id.title);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        viewBack = findViewById(R.id.view_back);
        SUtils.clickTransColor(llBack);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragmentActivity.this.finish();
            }
        });
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
                onMsgReceiver(position);
            }
        });
    }

    protected void onMsgReceiver(int type) {

    }

    private void initView() {
        if (setContentView() != 0) {
            View view = LayoutInflater.from(this).inflate(setContentView(), null);
            flContainer.addView(view);
        }
    }

    public void requestData(Class className, SummerParameter params, final String url, boolean post) {
        requestData(0, className, params, url, post);
    }

    public void requestData(int requestCode, Class className, SummerParameter params, final String url, boolean post) {
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, className, params, url, post);
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setLayoutFullscreen() {
        baseHelper.setLayoutFullscreen(false);
    }


    /**
     * 设置沉浸式状态栏
     */
    public void setLayoutFullscreen(boolean fullscreen) {
        baseHelper.setLayoutFullscreen(fullscreen);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<BaseFragmentActivity> mActivity;

        public MyHandler(BaseFragmentActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseFragmentActivity activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        activity.handleData(msg.obj);
                        activity.cancelLoading();
                        break;
                    case 1:
                        activity.finishLoad();
                        break;
                    default:
                        activity.handleMsg(msg.what, msg.obj);
                }
            }
        }
    }

    public void handleMsg(int position, Object object) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void handleData(Object object) {
        if (BaseFragmentActivity.this.isFinishing()) {
            return;
        }
        dealDatas(object);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
        BitmapUtils.getInstance().clearBitmaps(getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CUtils.onClick(getClass().getSimpleName() + "_resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CUtils.onClick(getClass().getSimpleName() + "_pause");
        if (tvTitle != null) {
            SUtils.hideSoftInpuFromWindow(tvTitle);
        }
    }

    public RelativeLayout getTitleParent() {
        return rlTitle;
    }

    private void cancelLoading() {
        baseHelper.cancelLoading();
    }

    protected abstract void loadData();

    /**
     * 分页加载时结束加载
     */
    protected abstract void finishLoad();

    /**
     * 处理返回的数据
     */
    protected abstract void dealDatas(Object obj);

    /**
     * 设置当前界面标题
     */
    protected abstract int setTitleId();

    /**
     * 设置当前界面主体内容
     */
    protected abstract int setContentView();

    /**
     * 初始化界面与数据
     */
    protected abstract void initData();
}
