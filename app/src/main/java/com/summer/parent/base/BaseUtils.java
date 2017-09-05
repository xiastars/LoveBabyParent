package com.summer.parent.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.TipDialog;
import com.summer.parent.LoginActivity;
import com.summer.parent.R;
import com.summer.parent.common.ParentUser;

/**
 * Created by xiaqiliang on 2017/5/18.
 */

public class BaseUtils {

    /**
     * 判断用户登录
     *
     * @param context
     * @return
     */
    public static boolean jumpToLogin(Context context) {
        ParentUser.USER_PHONE = SUtils.getStringData(context,"isLogin");
        Logs.i("ParentUser.USER_PHONE"+ParentUser.USER_PHONE);
        if (TextUtils.isEmpty(ParentUser.USER_PHONE)) {
            JumpTo.getInstance().commonJump(context, LoginActivity.class);
            return true;
        }
        return false;
    }

    /**
     * 显示举报对话框
     *
     * @param context
     * @param str
     * @param finishActivity 点知道后，是否关闭当前Activity
     */
    public static void showReportDialog(final Context context, String str, final boolean finishActivity) {
        String content = TextUtils.isEmpty(str) ? "" : str;
        TipDialog dialog = new TipDialog(context, R.layout.dialog_known, new TipDialog.DialogAfterClickListener() {
            @Override
            public void onSure() {
                if (finishActivity) {
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onCancel() {
                if (finishActivity) {
                    ((Activity) context).finish();
                }
            }
        });
        dialog.setContent(content);
        dialog.show();
    }

}
