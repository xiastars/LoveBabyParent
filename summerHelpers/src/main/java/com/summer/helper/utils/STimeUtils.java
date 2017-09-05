package com.summer.helper.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 有关时间的方法
 * Created by xiastars on 2017/8/10.
 */

public class STimeUtils {

    /**
     * 获取前后六个月
     *
     * @return
     */
    public static String[] getMonthAboutSix() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        String[] titles = new String[12];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        for (int i = 5; i > -1; i--) {
            calendar.add(Calendar.MONTH, -1);
            titles[i] = format.format(calendar.getTimeInMillis());
        }
        calendar = Calendar.getInstance();
        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.MONTH, 1);
            titles[i + 6] = format.format(calendar.getTimeInMillis());
        }
        return titles;
    }

    /**
     * 转换时间，格式--
     *
     * @return
     */
    public static String getDayWithFormat(String formatContent) {
        SimpleDateFormat format = new SimpleDateFormat(formatContent, Locale.CHINA);
        String s = format.format(new Date());
        return s;
    }

    /**
     * 转换时间，格式--
     *
     * @return
     */
    public static String getDayWithFormat(String formatContent, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatContent, Locale.CHINA);
        String s = format.format(date);
        return s;
    }

    /**
     * 转换时间，格式--
     *
     * @return
     */
    public static String getDayWithFormat(String formatContent, long date) {
        return getDayWithFormat(formatContent, new Date(date));
    }

    /**
     * 获取当天星期
     *
     * @return
     */
    public static String getWeekDayXQ(long time) {
        String mWeekDay = "";
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(new Date(time));
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        switch (index) {
            case 1:
                mWeekDay = "星期日";
                break;
            case 2:
                mWeekDay = "星期一";
                break;
            case 3:
                mWeekDay = "星期二";
                break;
            case 4:
                mWeekDay = "星期三";
                break;
            case 5:
                mWeekDay = "星期四";
                break;
            case 6:
                mWeekDay = "星期五";
                break;
            case 7:
                mWeekDay = "星期六";
                break;
            default:
                mWeekDay = "星期日";
                break;
        }
        calendar.clear();
        return mWeekDay;
    }

    public static String getWeekDayZhou(long time) {
        String mWeekDay;
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(new Date(time));
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        switch (index) {
            case 1:
                mWeekDay = "周日";
                break;
            case 2:
                mWeekDay = "周一";
                break;
            case 3:
                mWeekDay = "周二";
                break;
            case 4:
                mWeekDay = "周三";
                break;
            case 5:
                mWeekDay = "周四";
                break;
            case 6:
                mWeekDay = "周五";
                break;
            case 7:
                mWeekDay = "周六";
                break;
            default:
                mWeekDay = "周日";
                break;
        }
        calendar.clear();
        return mWeekDay;
    }

}
