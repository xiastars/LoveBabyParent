package com.summer.parent.server;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.summer.parent.MyApplication;
import com.summer.helper.server.ILGChannel;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.PostData;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class Const {
    // 手机品牌
    public static String MANUFACTURER = android.os.Build.MANUFACTURER;
    // 手机型号
    public static String MODEL = android.os.Build.MODEL;
    // 操作系统
    public static String OS = "Android";
    // 操作系统版本号
    public static String VERSION_OS = android.os.Build.VERSION.SDK;
    // 手机唯一标识码
    public static String IMEI = "";
    // UUID标识码
    public static String UUID = "";
    // 项目版本号：
    public static String VERSION_PRO = null;
    // 标识是哪个应用市场
    public static String CHANNEL = ILGChannel.SELF;
    // 上下文
    public static Context context;
    // 返回当前手机语言 en or cn
    public static String language = null;
    //获取状态栏Height
    public static int statusBarHeight = 0;
    //每页显示的数据条数
    public static final int pageRecords = 20;
    //第一次登陆的常量
    public static int isFirstPlay = 0;
    //服务唯一码
    public static String uniqueID = IMEI;
    //用户Agent
    public static String userAgent;
    //应用版本号
    public static int VERSIONCODE;
    //用户Agent
    public static boolean userCanSim;
    //接口版本号
    public static int web_version = 1;
    //国家码
    public static String countryCode = "CN";
    //请求Token
    public static String TOKEN = "";
    //用户id
    public static long USER_ID = 0;
    //用户名称
    public static String USER_NAME = null;
    //用户头像
    public static String USER_ICON = "";
    //用户积分
    public static int USER_INTEGRAL = -1;
    //用户星币
    public static int USER_COINS = 0;
    //MAC地址
    public static String MAC_INFO = null;

    // 获取必须的基本参数
    public static SummerParameter getBasicParameters() {
        if (VERSION_PRO == null) {
            getVersionInfo();
        }
        SummerParameter params = new SummerParameter();
        String uuid = java.util.UUID.randomUUID().toString();
        params.put("t", uuid);
        return params;
    }

    //登录时传的参数
    public static SummerParameter getLoginParameters() {
        if (VERSION_PRO == null) {
            getVersionInfo();
        }
        SummerParameter params = new SummerParameter();
        params.put("app_version", VERSION_PRO);
        params.put("manufacturer", MANUFACTURER);
        params.put("model", MODEL);
        params.put("app_package", MyApplication.getIntance().getPackageName());
        params.put("os", OS + "_" + VERSION_OS);
        params.put("language", "cn");
        params.put("channel", CHANNEL);
        if (!TextUtils.isEmpty(PostData.TOKEN)) {
            params.put("TOKEN", PostData.TOKEN);
        }
        if (MAC_INFO == null) {
            MAC_INFO = getMac();
        }
        if (MAC_INFO != null) {
            params.put("mac", getMac());
            params.put("uniquecode", getMac());
        }
        params.put("idfa", "");

        return params;
    }

    private static void getVersionInfo() {
        PackageManager manager;
        PackageInfo info = null;
        manager = MyApplication.getIntance().getPackageManager();
        try {
            info = manager.getPackageInfo(MyApplication.getIntance().getPackageName(), 0);
            VERSIONCODE = info.versionCode;
            VERSION_PRO = info.versionName;
            Logs.i("xia", "VERSION_INFO:" + VERSIONCODE + "," + VERSION_PRO);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getMac() {
        String mac = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    mac = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return mac;
    }

    public static void emptyUserInfo() {
        Const.USER_COINS = 0;
        Const.USER_ID = 0;
        Const.USER_ICON = null;
        Const.USER_NAME = null;
        Const.USER_INTEGRAL = 0;
    }

    // 获取必须的基本参数
    public static SummerParameter getPostParameters() {
        SummerParameter params = new SummerParameter();
        String uuid = java.util.UUID.randomUUID().toString();
        params.put("t", uuid);
        return params;
    }

    /**
     * 获取手机厂商唯一识别号(IMEI)
     *
     * @return
     */
    public static String getIMIE() {
        if (TextUtils.isEmpty(Const.IMEI)) {
            try {
                TelephonyManager telManager = (TelephonyManager) MyApplication.getIntance().getSystemService(Context.TELEPHONY_SERVICE);
                Const.IMEI = telManager.getDeviceId();
                Logs.i("IMEI" + Const.IMEI);
            } catch (Exception e) {

                Logs.i("IMEI" + e.toString());
            }

        }
        return Const.IMEI;
    }

}

