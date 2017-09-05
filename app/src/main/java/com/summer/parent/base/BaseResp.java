package com.summer.parent.base;

import java.io.Serializable;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class BaseResp implements Serializable{
    transient long userId;
    transient long handleDate;
    transient long handleTime;
    transient String code;
    transient String message;
    Object data;

    public long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(long handleTime) {
        this.handleTime = handleTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(long handleDate) {
        this.handleDate = handleDate;
    }
}
