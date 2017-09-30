package com.summer.parent;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;

/**
 * Created by xiastars on 2017/9/28.
 */

public class RegisterHelper implements RegisterPresenter {

        private Round_Register round_register;

        public RegisterHelper(Round_Register round_register) {
            this.round_register = round_register;
        }

        /**
         * 获取验证码
         */
        @Override
        public void SendCode(View Button) {
            String phone = round_register.getPhone();
            if (phone != null && !phone.equals("")) {
                SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sendTime = format.format(new Date());
                BmobSMS.requestSMS(Button.getContext(), phone, "欢迎使用爱宝桌面",sendTime,new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId,BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//
                            round_register.setSuccess(smsId);
                        }else{
                            round_register.Error("发送失败");
                        }
                    }
                });
            } else {
                round_register.ToastError("手机号码不能为空");
            }
        }

        /**
         * 注册
         */
        @Override
        public void Register() {
            String phone = round_register.getPhone();
            String password = round_register.getPassword();
            String code = round_register.getCode();
            if (phone != null && !phone.equals("")) {
                if (password != null && !password.equals("")) {
                    if (code != null && !code.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("tel", phone);
                            jsonObject.put("psw", password);
                            jsonObject.put("code", code);
                            jsonObject.put("citycode", round_register.getCityCode());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        round_register.ToastError("验证码不能为空");
                    }
                } else {
                    round_register.ToastError("密码不能为空");
                }
            } else {
                round_register.ToastError("手机号码不能为空");
            }
        }


        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {

                    case 4003:
                        round_register.ToastError("账号已被注册!");
                        break;
                }
            }
        };

    }
