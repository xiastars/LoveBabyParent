package com.summer.parent.common;

import android.content.Context;

import com.summer.parent.bean.UserInfo;

/**
 * 用户信息
 * Created by xiastars on 2017/8/22.
 */

public class ParentUser {

    /**
     * 用户手机
     */
    public static String USER_PHONE = null;

    //用户id
    public static String USER_ID;
    //用户名称
    public static String USER_NAME = null;
    //用户头像
    public static String USER_ICON = "";
    //用户积分
    public static int USER_INTEGRAL = -1;
    //用户星币
    public static int USER_COINS = 0;

    /**
     * 初始化用户数据
     *
     * @param info
     */
    public static void initUserInfo(UserInfo info, Context context) {
        if (info == null) {
            return;
        }
        USER_ID = info.getObjectId();
        USER_PHONE = info.getPhone();
    }

    /**
     * 清空用户数据
     */
    public static void emptyUserInfo() {
        USER_ICON = null;
        USER_NAME = null;
        USER_INTEGRAL = 0;
        USER_PHONE = null;
    }
}
