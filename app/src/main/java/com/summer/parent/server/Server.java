package com.summer.parent.server;

import com.summer.parent.MyApplication;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class Server {

    public static String SERVER = "";

    static {
        if (MyApplication.DEBUGMODE) {
            SERVER = "http://www.toutiao.com/api/";
        } else {
            SERVER = "http://www.toutiao.com/api/";
        }
    }

    /**
     * 首页
     */
    public static final String HOME_DATA = SERVER + "pc/feed/";


}

