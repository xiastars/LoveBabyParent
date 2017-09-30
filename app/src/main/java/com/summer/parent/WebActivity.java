package com.summer.parent;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.LoadingDialog;
import com.summer.helper.web.CustomWebView;
import com.summer.parent.base.BaseActivity;


/**
 * Web界面
 */
public class WebActivity extends BaseActivity {
    private CustomWebView mWebView;
    private LoadingDialog loadingDialog;
    protected String url;

    private void initWebView() {
        // TODO Auto-generated method stub
        mWebView = (CustomWebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(hxqWebChrome);

        url = JumpTo.getString(this);

        loadWebUrl(url);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoading("网页加载中");
        setTitle(getIntent().getStringExtra("title"));
        setExplainBtnVisible(false);
    }

    private void loadWebUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if(url.startsWith("http")){
                mWebView.loadUrl(url);
            }else{
                mWebView.loadDataWithBaseURL("", url, "text/html", "utf-8", "");
            }

        }
    }

    protected void reLoadUrl(){
        mWebView.reload();
    }

    protected WebView getWebView() {
        return mWebView;
    }


    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    };


    WebChromeClient hxqWebChrome = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress >= 80)
                loadingDialog.cancelLoading();

        }
    };


    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_web;
    }

    @Override
    protected void initData() {
        initWebView();
    }


    @Override
    protected void onBackClick() {
        super.onBackClick();
    }

    /**
     * 控制分享按钮是否显示
     *
     * @param visible
     */
    public void setShareBtnVisible(boolean visible) {
        Button btnShare = (Button) findViewById(R.id.btn_share);
        if (btnShare != null)
            btnShare.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 控制说明按钮是否显示
     *
     * @param visible
     */
    public void setExplainBtnVisible(boolean visible) {
        Button btnRight = (Button) findViewById(R.id.btn_right);
        if (btnRight != null)
            btnRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setUrlAndTitle(String url ,String title){
        Intent intent = getIntent();
        intent.putExtra(JumpTo.TYPE_STRING,url);
        intent.putExtra("title",title);
    }

}
