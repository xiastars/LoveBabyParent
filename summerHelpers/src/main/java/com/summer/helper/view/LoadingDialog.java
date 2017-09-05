package com.summer.helper.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.helper.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.lang.ref.WeakReference;

/**
 * 加载进度条
 *
 * @author xiastars@vip.qq.com
 */
public class LoadingDialog {
    TextView loadingText;
    RelativeLayout rlLoading;

    Dialog dialog;
    static LoadingDialog loadingDialog;
    MyHandler myHandler;
    Context context;
    int indexCount;
    int mLoadingIndex = 0;
    String loadingWord = "加载中";
    final String sPot = ".";

    public LoadingDialog(Context context) {
        this.context = context;
        myHandler = new MyHandler(this);
        dialog = new Dialog(context, R.style.TagFullScreenDialog);
        dialog.setContentView(R.layout.dialog_loading);
        rlLoading = (RelativeLayout) dialog.findViewById(R.id.rl_loading);
        rlLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelLoading();
            }
        });
        loadingText = (TextView) dialog.findViewById(R.id.tv_loading);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog.findViewById(R.id.progressbar).getLayoutParams();
        params.width = SUtils.getSWidth((Activity) context, 25);
        params.height = params.width;
        //startLoading();
    }

    public static synchronized LoadingDialog getInstance(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        return loadingDialog;
    }

    public void cancelLoading() {
        if (dialog != null) {
            try {
                dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rlLoading.setVisibility(View.GONE);
        myHandler.removeCallbacks(mRunnable);
    }

    public void startLoading() {
        indexCount = 0;
        loadingWord = "加载中";
        show();
    }

    public void startLoading(String text) {
        indexCount = 0;
        loadingWord = text;
        show();
    }

    private void show(){
        Logs.i("dialog::::"+dialog);
        rlLoading.setVisibility(View.VISIBLE);
        if (dialog != null) {
            try {
                Activity activity = (Activity) context;
                Logs.i("activity:::"+activity+",,,,"+activity.isFinishing());
                if(activity.isFinishing()){
                    return;
                }
                circleTextLoading(0);
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getVisibility() {
        return rlLoading.getVisibility();
    }

    /**
     * 设置loading的文字变动
     */
    private void circleTextLoading(int delayTime) {
        indexCount++;
        myHandler.postDelayed(mRunnable,delayTime);
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLoadingIndex == 3) {
                mLoadingIndex = 0;
            }
            if (mLoadingIndex == 0) {
                loadingText.setText(loadingWord + sPot);
            } else if (mLoadingIndex == 1) {
                Logs.i("text:" + loadingWord + sPot + sPot);
                loadingText.setText(loadingWord + sPot + sPot);
            } else if (mLoadingIndex == 2) {
                loadingText.setText(loadingWord + sPot + sPot + sPot);
            }
            mLoadingIndex++;
            if (rlLoading.getVisibility() == View.VISIBLE) {
                circleTextLoading(1000);
            }
        }
    };

    public static class MyHandler extends Handler {
        private final WeakReference<LoadingDialog> mActivity;

        public MyHandler(LoadingDialog activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadingDialog activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        break;
                }
            }
        }
    }

}
