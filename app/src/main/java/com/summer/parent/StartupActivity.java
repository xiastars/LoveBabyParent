package com.summer.parent;

import android.app.Activity;
import android.os.Bundle;

import com.summer.helper.utils.JumpTo;
import com.summer.parent.base.BaseUtils;

/**
 * 引导页
 * Created by xiaqiliang on 2017/3/27.
 */
public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BaseUtils.jumpToLogin(this)) {
            JumpTo.getInstance().commonJump(this, MainActivity.class);
        }
        StartupActivity.this.finish();
    }

}
