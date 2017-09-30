package com.summer.parent;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.summer.helper.utils.Logs;
import com.summer.helper.utils.PostData;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class MyApplication extends Application {
    static Context context;
    public static final boolean DEBUGMODE = false;
    private static final String APP_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Logs.isDebug = true;
        SUtils.setContext(context);
        SFileUtils.initCache(context);
        Bmob.initialize(context, APP_KEY);
        BmobSMS.initialize(context, APP_KEY);
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                PostData.CHANNEL = appInfo.metaData.getString("JPUSH_CHANNEL");
                Logs.i("CHANNEL:" + PostData.CHANNEL);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Context getIntance() {
        return context;
    }

}
