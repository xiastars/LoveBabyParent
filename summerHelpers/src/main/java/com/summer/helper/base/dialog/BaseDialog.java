package com.summer.helper.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.summer.helper.R;
import com.summer.helper.utils.SUtils;

/**
 * 底部弹出框基本样式
 * Created by xiaqiliang on 2017/6/20.
 */

public abstract class BaseDialog extends Dialog {
    protected Context context;
    FrameLayout flParent;
    RelativeLayout rlParent;

    protected boolean isShowAnim = true;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.TagFullScreenDialog);
        this.context = context;
    }

    public BaseDialog(@NonNull Context context, int style) {
        super(context, style);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_base);
        rlParent = (RelativeLayout) findViewById(R.id.rl_base_parent);
        rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
        flParent = (FrameLayout) findViewById(R.id.fl_parent);
        View view = LayoutInflater.from(context).inflate(getContainerLayoutResId(), null);
        flParent.addView(view);
        initView(view);
    }

    /**
     * 将对话框置在底部，而且宽度全屏
     */
    protected void setDialogBottom() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = SUtils.screenWidth;
            lp.gravity = Gravity.BOTTOM;
        }
    }

    /**
     * 返回容器视图资源Id
     */
    protected int getContainerLayoutResId() {
        return setContainerView();
    }

    public abstract int setContainerView();

    public abstract void initView(View view);

    @Override
    public void show() {
        super.show();
        if (!isShowAnim) {
            return;
        }
        int animRes = showEnterAnim();
        if (animRes != 0) {
            Animation anim = AnimationUtils.loadAnimation(context,
                    animRes);
            flParent.startAnimation(anim);
        }
    }

    /**
     * 取消显示必须调用此方法，展现动画
     */
    public void cancelDialog() {
        if (!isShowAnim) {
            BaseDialog.this.cancel();
            return;
        }
        int animRes = showQuitAnim();
        if(animRes != 0){
            flParent.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(context,
                    animRes);
            flParent.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    BaseDialog.this.cancel();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{
            BaseDialog.this.cancel();
        }

    }

    /**
     * 设置是否显示动画
     *
     * @param showAnim
     */
    protected void setShowAnim(boolean showAnim) {
        this.isShowAnim = showAnim;
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelDialog();
        }
        return false;
    }

    //显示出场动画
    protected abstract int showEnterAnim();

    //显示退场动画
    protected abstract int showQuitAnim();
}
