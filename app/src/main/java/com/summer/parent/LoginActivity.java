package com.summer.parent;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.summer.helper.utils.JumpTo;
import com.summer.parent.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiastars on 2017/9/5.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.register_text)
    TextView register_text;
    @BindView(R.id.forget_pass)
    TextView forget_pass;
    @BindView(R.id.login_user_name)
    EditText loginUserName;
    @BindView(R.id.login_user_psw)
    EditText loginUserPsw;
    @BindView(R.id.login_double)
    TextView login_double;

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        register_text.setOnClickListener(this);
        removeTitle();
    }

    @OnClick({R.id.register_text})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_text:
                JumpTo.getInstance().commonJump(context,RegisterActivity.class);
               // startActivityForResult(new Intent(LoginActivity.this, .class), IntentCode.RequestCode.TOREGISTER);
                break;
            case R.id.forget_pass:
                //Skip(RetrieveActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == IntentCode.ResultCode.BACKTOLOGIN) {
            boolean defaultlogin = data.getBooleanExtra("defaultlogin", false);
            if (defaultlogin) {
                loginUserName.setText(data.getStringExtra("phone"));
                loginUserPsw.setText(data.getStringExtra("psw"));
                mutual_login.startLogin();
            }
        }*/
    }

}
