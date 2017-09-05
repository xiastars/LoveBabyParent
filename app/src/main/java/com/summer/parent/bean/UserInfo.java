package com.summer.parent.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by xiastars on 2017/9/5.
 */

public class UserInfo extends BmobObject {

    String passport;
    String userID;
    String version;
    int limiteTime;
    String fromWhere;
    String phone;
    int isVip;

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLimiteTime() {
        return limiteTime;
    }

    public void setLimiteTime(int limiteTime) {
        this.limiteTime = limiteTime;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }
}
