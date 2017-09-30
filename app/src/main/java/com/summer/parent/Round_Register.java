package com.summer.parent;

/**
 * Created by xiastars on 2017/9/28.
 */

public interface Round_Register {

    String getPhone();

    String getPassword();

    String getCode();

    String getCityCode();

    void setSuccess(Integer result);

    void Error(String cause);

    void ToastError(String cause);
}
