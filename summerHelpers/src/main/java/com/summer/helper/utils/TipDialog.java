package com.summer.helper.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.summer.helper.R;

/**
 * @author xiaqiliang
 * @time 2016年6月22日
 */
public class TipDialog extends Dialog {

    private DialogAfterClickListener listener;

    private int layoutid;
    private TextView tvContent;
    private TextView tvTitle;
    private static TipDialog tipDialog = null;

    String name;
    String okContent;
    String cancelContent;
    String content;

    public static TipDialog getInstance(Context context, String name, DialogAfterClickListener listener){
        if(tipDialog != null){
            tipDialog.cancel();
            tipDialog = null;
        }
        tipDialog = new TipDialog(context,name,listener);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutid);

        initView();
    }

    public void setOkContent(String content){
        this.okContent = content;
    }

    public void setCancelContent(String content){
        this.cancelContent = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        tvContent = (TextView) findViewById(R.id.tv_content);

        if(tvContent != null){
            if(!TextUtils.isEmpty(name)){
                SUtils.setHtmlText(name,tvContent);
            }
            if(!TextUtils.isEmpty(content)){
                SUtils.setHtmlText(content, tvContent);
            }
        }
        TextView tvOK = (TextView) findViewById(R.id.tips_ok_tv);
        if(!TextUtils.isEmpty(okContent)){
            tvOK.setText(okContent);
        }
        SUtils.clickTransColor(tvOK);
        tvOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onSure();
                }
                TipDialog.this.cancel();
            }

        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvCancel = (TextView) findViewById(R.id.tips_cancel_tv);

        if (tvCancel != null) {
            if(!TextUtils.isEmpty(cancelContent)){
                tvCancel.setText(cancelContent);
            }
            SUtils.clickTransColor(tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onCancel();
                    }
                    TipDialog.this.cancel();
                }
            });
        }
    }

    public interface DialogAfterClickListener {
        void onSure();

        void onCancel();
    }

}
