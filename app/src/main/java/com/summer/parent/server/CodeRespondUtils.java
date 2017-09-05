package com.summer.parent.server;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.summer.parent.base.BaseResp;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * 根据返回状态码弹出相应提示
 *
 * @author xiastars@vip.qq.com
 */
public class CodeRespondUtils {

    Context context;
    String msg;

    public CodeRespondUtils(final Context context, final String code) {
        this.context = context;
        if (TextUtils.isEmpty(code)) {
            return;
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dealWithCode(context, code);
            }
        });
    }

    public CodeRespondUtils(final Context context, final BaseResp baseResp) {
        this.context = context;
        if (baseResp == null) {
            return;
        }
        final String code = baseResp.getCode();
        msg = baseResp.getMessage();
        if (TextUtils.isEmpty(code)) {
            return;
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dealWithCode(context, code);
            }
        });
    }

    private void dealWithCode(Context context, String code) {
        if (code.contains("#")) {
            code = code.replaceAll("#", "");
        }
        Logs.i("code:::" + code);
        if (code.equals("101")) {
            SUtils.makeToast(context, "请求过于频繁，请稍后重试！");
        } else if (code.equals("98") || code.equals("97")) {
            showTipDialog();
        } else if (code.equals("0")) {

        } else {
            String toastMsg = "操作失败，请稍后重试！";
            if (!TextUtils.isEmpty(msg)) {
                toastMsg = msg;
            }
            SUtils.makeToast(context, toastMsg);
        }
    }

    private void showTipDialog() {
    }
}
