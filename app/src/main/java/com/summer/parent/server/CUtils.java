package com.summer.parent.server;

import com.summer.helper.utils.SThread;

import java.util.HashMap;

/**
 * Created by xiaqiliang on 2017/5/15.
 */

public class CUtils {

    public static HashMap<String, String> getMapWithId(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        return map;
    }

    public static HashMap<String, String> getMapWithId(String id,String userID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("userId",userID);
        return map;
    }

    /**
     * 数据埋点，TAG+ID
     *
     * @param tag
     */
    public static void onClick(final String tag) {
        onClick(tag, -0);
    }

    /**
     * 数据埋点，TAG+ID
     *
     * @param tag
     * @param id
     */
    public static void onClick(final String tag, final long id) {
        onClick(tag, id + "");
    }

    public static void onClick(final String tag, final String id) {
        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {
                //MobclickAgent.onEvent(MyApplication.getIntance(), tag, CUtils.getMapWithId(id + "",Const.USER_ID+""));
            }
        });
    }
}
