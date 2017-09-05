package com.summer.parent.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.helper.view.resize.RLinearLayout;
import com.summer.parent.R;
import com.summer.parent.server.CUtils;
import com.summer.parent.server.CodeRespondUtils;
import com.summer.helper.listener.OnShareListener;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.BitmapUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.resize.RRelativeLayout;

import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

import butterknife.ButterKnife;

/**
 * Created by xiaqiliang on 2017/3/24.
 */
public abstract class BaseRequestActivity extends Activity {
    TextView tvTitle;
    public FrameLayout flContainer;
    RelativeLayout rlBaseTop;
    RRelativeLayout rlTitle;
    View viewBack;
    public Context context;
    protected MyHandler myHandlder;
    protected int pageIndex;
    protected long fromId;
    protected boolean toastMessage;//是否弹出错误信息
    protected boolean isReturnFailureMsg;//请求错误状态下是否返回
    boolean isRefresh;
    BaseHelper baseHelper;
    OnShareListener listener;
    View line1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SUtils.initScreenDisplayMetrics(this);
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
            initContentView();
            ButterKnife.bind(this);
            initData();
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

    /**
     * 如果不想要默认的头部，则移除，自己在Activity里写
     */
    public void removeTitle() {
        rlBaseTop.removeView(rlTitle);
        line1.setVisibility(View.GONE);
    }

    protected void setBlankTitleView(){
        removeTitle();
        RLinearLayout view = (RLinearLayout) LayoutInflater.from(context).inflate(R.layout.include_back,null);
        TextView viewBack = (TextView) view.findViewById(R.id.view_back);
        viewBack.setBackgroundResource(R.drawable.title_icon_return_default);
        flContainer.addView(view);
        view.reLayout(55,45,0,0);
        setLayoutFullscreen();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = SUtils.getStatusBarHeight(this)+SUtils.getDip(context,20);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
    }


    /**
     * 当此界面有EditText时，Full模式下不弹出 ，所以不能设为Full模式
     */
    protected void setBlankTitleViewWithEditMode(){
        removeTitle();
        RLinearLayout view = (RLinearLayout) LayoutInflater.from(context).inflate(R.layout.include_back,null);
        TextView viewBack = (TextView) view.findViewById(R.id.view_back);
        viewBack.setBackgroundResource(R.drawable.title_icon_return_default);
        flContainer.addView(view);
        view.reLayout(55,45,0,0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = SUtils.getDip(context,20);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
    }

    private void initTitleView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (setTitleId() != 0) {
            tvTitle.setText(getString(setTitleId()));
        }
        rlBaseTop = (RelativeLayout) findViewById(R.id.rl_base_parent);
        rlTitle = (RRelativeLayout) findViewById(R.id.title);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        SUtils.clickTransColor(llBack);
        viewBack = findViewById(R.id.view_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        line1 = findViewById(R.id.line1);
    }

    public void setTitle(String content) {
        if(!TextUtils.isEmpty(content)){
            tvTitle.setText(content);
        }
    }

    protected void onBackClick() {
        CUtils.onClick(getClass().getSimpleName() + "_onback");
        BaseRequestActivity.this.finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            onBackClick();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void initContentView() {
        if (setContentView() != 0) {
            View view = LayoutInflater.from(this).inflate(setContentView(), null);
            flContainer.addView(view);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void changeHeaderStyleTrans(int color) {
        if (rlTitle != null) {
            rlTitle.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (viewBack != null) {
            viewBack.setBackgroundResource(R.drawable.title_icon_return_black);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                getWindow().setStatusBarColor(color);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestData(Class className, SummerParameter params, final String url, boolean post) {
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(0, className, params, url, post);
    }

    public void requestData(int requestCode, Class className, SummerParameter params, final String url, boolean post) {
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, className, params, url, post);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapUtils.getInstance().clearBitmaps(getClass().getSimpleName());
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
        private final WeakReference<BaseRequestActivity> mActivity;

        public MyHandler(BaseRequestActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseRequestActivity activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case BaseHelper.MSG_SUCCEED:
                        activity.handleRequest(msg, false);
                        break;
                    case BaseHelper.MSG_FINISHLOAD:
                        activity.cancelLoading();
                        activity.finishLoad();
                        break;
                    case BaseHelper.MSG_CACHE:
                        activity.handleRequest(msg, true);
                        break;
                    case BaseHelper.MSG_ERRO:
                        activity.dealErrors(msg.arg1, msg.arg2 + "", (String) msg.obj, false);
                        break;
                    default:
                        activity.handleMsg(msg.what, msg.obj);
                }
            }
        }
    }

    /**
     * 处理MyHandler派发的消息
     *
     * @param position
     * @param object
     */
    public void handleMsg(int position, Object object) {

    }

    /**
     * 返回数据给请求Activity
     *
     * @param msg
     * @param fromCache
     */
    private void handleRequest(Message msg, boolean fromCache) {
        Object object = msg.obj;
        if (object == null) {
            return;
        }
        Logs.i("obj:" + object + ",,," + toastMessage);
        if (object instanceof BaseResp) {
            BaseResp resp = (BaseResp) object;
            if (toastMessage && !isReturnFailureMsg) {
                new CodeRespondUtils(context, resp);
            } else {
            }
            String code = resp.getCode();
            //如果不是成功状态不返回
            if (TextUtils.isEmpty(code) || !code.equals("0")) {
                if (isReturnFailureMsg) {
                    //1.4以前版本
                    dealDatas(msg.arg1, msg.obj);
                }
                dealErrors(msg.arg1, code, resp.getMessage(), true);
                return;
            }
        }
        dealDatas(msg.arg1, msg.obj);
        if (fromCache) {
            fromId = 0;
            pageIndex = 0;
        }
    }

    /**
     * 处理错误
     *
     * @param requstCode  请求数据标识码
     * @param requestType 返回错误码，根据requestCode判断是返回的Code还是@ErroCode
     * @param errString   错误信息
     * @param requestCode 如果是返回的Code则为true
     */
    protected void dealErrors(int requstCode, String requestType, String errString, boolean requestCode) {
        if (baseHelper != null) {
            baseHelper.cancelLoading();
            finishLoad();
        }
    }

    private void cancelLoading() {
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    /**
     * 显示空状态页面
     *
     * @param msg  显示文本信息
     * @param resp
     */
    protected void showEmptyView(String msg, int resp) {
        flContainer.removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty, null);
        flContainer.addView(view);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_hint_content);
        if (!TextUtils.isEmpty(msg)) {
            tvContent.setText(msg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CUtils.onClick(getClass().getSimpleName() + "_resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CUtils.onClick(getClass().getSimpleName() + "_pause");
        if (tvTitle != null) {
            SUtils.hideSoftInpuFromWindow(tvTitle);
        }
    }

    public void setToastMessage() {
        this.toastMessage = true;
    }

    public boolean isReturnFailureMsg() {
        return isReturnFailureMsg;
    }

    public void setReturnFailureMsg(boolean returnFailureMsg) {
        isReturnFailureMsg = returnFailureMsg;
    }

    public void setToastMessage(boolean isShow) {
        this.toastMessage = isShow;
    }

    protected abstract void loadData();

    protected abstract void finishLoad();

    protected abstract void dealDatas(int requestCode, Object obj);

    protected abstract int setTitleId();

    protected abstract int setContentView();

    protected abstract void initData();


}
