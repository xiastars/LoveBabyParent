package com.summer.helper.base.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.summer.helper.R;
import com.summer.helper.utils.SUtils;

/**
 * @author xiaqiliang
 * @time 2016年6月22日
 */
public class TipDialog extends BaseCenterDialog {

    private DialogAfterClickListener listener;

    private int layoutid;
    private TextView tvContent;
    private TextView tvTitle;
    private static TipDialog tipDialog = null;

    String name;
    String okContent;
    String cancelContent;
    String content;
    String title;

    public static TipDialog getInstance(Context context, String name, DialogAfterClickListener listener) {
        if (tipDialog != null) {
            tipDialog.cancel();
            tipDialog = null;
        }
        tipDialog = new TipDialog(context, name, listener);
        return tipDialog;
    }

    public TipDialog(Context context, String name, DialogAfterClickListener listener) {
        super(context, R.style.TagFullScreenDialog);
        this.listener = listener;
        this.name = name;
        layoutid = R.layout.dialog_hxq;
    }

    public TipDialog(Context context, int layoutid, DialogAfterClickListener listener) {
        super(context, R.style.TagFullScreenDialog);
        this.listener = listener;
        this.layoutid = layoutid;
    }

    @Override
    public int setContainerView() {
        return layoutid;
    }

    @Override
    public void initView(View view) {
        tvContent = (TextView) view.findViewById(R.id.tv_content);

        if (tvContent != null) {
            if (!TextUtils.isEmpty(name)) {
                SUtils.setHtmlText(name, tvContent);
            }
            if (!TextUtils.isEmpty(content)) {
                SUtils.setHtmlText(content, tvContent);
            }
        }
        TextView tvOK = (TextView) view.findViewById(R.id.tips_ok_tv);
        if (!TextUtils.isEmpty(okContent)) {
            tvOK.setText(okContent);
        }
        SUtils.clickTransColor(tvOK);
        tvOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onSure();
                }
                cancelDialog();
            }

        });
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if(title != null){
            tvTitle.setText(title);
        }
        TextView tvCancel = (TextView) view.findViewById(R.id.tips_cancel_tv);

        if (tvCancel != null) {
            if (!TextUtils.isEmpty(cancelContent)) {
                tvCancel.setText(cancelContent);
            }
            SUtils.clickTransColor(tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onCancel();
                    }
                   cancelDialog();
                }
            });
        }
    }

    public void setOkContent(String content) {
        this.okContent = content;
    }

    public void setCancelContent(String content) {
        this.cancelContent = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    public interface DialogAfterClickListener {
        void onSure();

        void onCancel();
    }

}
