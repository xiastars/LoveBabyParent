package com.summer.parent;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.summer.helper.utils.SUtils;
import com.summer.parent.base.BaseActivity;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiastars on 2017/9/28.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, Round_Register {

    @BindView(R.id.register_number)
    EditText register_number;
    @BindView(R.id.register_password)
    EditText register_password;
    @BindView(R.id.register_set_code)
    EditText register_set_code;
    @BindView(R.id.get_code)
    Button get_code;
    @BindView(R.id.register_login)
    Button register_login;
    @BindView(R.id.register_protocol)
    CheckBox register_protocol;

    RegisterHelper registerHelper;

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        registerHelper = new RegisterHelper(this);
        get_code.setOnClickListener(this);
        register_login.setOnClickListener(this);
        register_set_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() != null && !s.toString().equals("")) {
                    register_login.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                registerHelper.SendCode(get_code);
                break;
            case R.id.register_login:
                if (register_protocol.isChecked()) {
                    registerHelper.Register();
                } else {
                    SUtils.makeToast(context, "请勾选协议!");
                }
                break;
        }
    }

    @Override
    public String getPhone() {
        return register_number.getText().toString();
    }

    @Override
    public String getPassword() {
        return register_password.getText().toString();
    }

    @Override
    public String getCode() {
        return register_set_code.getText().toString();
    }

    @Override
    public String getCityCode() {
        return "";
    }

    @Override
    public void setSuccess(Integer result) {

    }

/*    @Override
    public void setSuccess(String result) {
        if (result.equals("验证码")) {
            get_code.setClickable(false);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.obtainMessage().sendToTarget();
                }
            }, 0, 1000);
        } else {
            SUtils.makeToast(context,"账号注册成功");
            Intent intent = getIntent();
            intent.putExtra("defaultlogin", true);
            intent.putExtra("phone", register_number.getText().toString());
            intent.putExtra("psw", register_password.getText().toString());

            finish();
        }
    }*/

    @Override
    public void Error(String cause) {

    }

    @Override
    public void ToastError(String cause) {

    }

    private int i = 180;
    private Timer timer;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            get_code.setTextColor(getResources().getColor(R.color.grey_99));
            get_code.setText("  " + i + "s  ");
            if (i == 0) {
                get_code.setTextColor(getResources().getColor(R.color.red_d4));
                get_code.setClickable(true);
                get_code.setText("获取验证码");
                timer.cancel();
            }
            i--;
        }
    };

    public void read(View view) {
        Intent intent = new Intent(this, ProtocalActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return R.string.free_register;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_register;
    }

}
