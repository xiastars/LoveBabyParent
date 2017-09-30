package com.summer.helper.utils;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

<<<<<<< HEAD
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
=======
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
>>>>>>> 7cc00bee69b40f9634b41ccdf27b439a3c4fd3e9

/**
 * Created by xiaqiliang on 2017/4/6.
 */
public class STextUtils {

    /**
     * 设置文本内的一段颜色
     *
     * @param content
     * @param tvContent
     * @param start
     * @param end
     * @param color
     */
    public static void setSpannableView(String content, TextView tvContent, int start, int end, int color) {
        SpannableString msp = getSpannableString(content, start, end, color);
        if (tvContent != null) {
            tvContent.setText(msp);
        }
    }

    public static SpannableString getSpannableString(String content, int start, int end, int color) {
        SpannableString msp = new SpannableString(content);
        msp.setSpan(new ForegroundColorSpan(color), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /**
     * 如果数字过10万，则以万为单位
     *
     * @param tvContent
     * @param value
     * @param pre
     * @param suf
     */
    public static void setThousants(TextView tvContent, long value, String pre, String suf) {
        if (value < 100000) {
            SUtils.setHtmlText(pre + value + suf, tvContent);
        } else {
            long mul = value / 10000;
            SUtils.setHtmlText(pre + mul + "万" + suf, tvContent);
        }
    }

    /**
     * 如果数字过10万，则以万为单位，返回格式化数据
     *
     * @param value
     * @return
     */
    public static String getThousants(int value) {
        if (value < 100000) {
            return value + "";
        } else {
            int mul = value / 10000;
            return mul + "万";
        }
    }

    /**
     * 检查EditText是不是为空
     *
     * @param edtTitle
     * @return
     */
    public static boolean checkEditViewEmpty(EditText edtTitle, int toastRes) {
        String betTitle = edtTitle.getText().toString();
        betTitle = betTitle.replaceAll(" ", "");
        if (TextUtils.isEmpty(betTitle)) {
            SUtils.makeToast(edtTitle.getContext(), toastRes);
            return true;
        }
        return false;
    }

    /**
     * 拼接字符串
     * @param args
     * @return
     */
    public static String spliceText(String... args){
        StringBuilder builder = new StringBuilder();
        for(String content : args){
            builder.append(content);
        }
        return builder.toString();
    }

    /**
     * 拼接字符串
     * @param args
     * @return
     */
    public static void setSpliceText(TextView tvConent,String... args){
        tvConent.setText(spliceText(args));
    }

    /**
     * 设置支持表情的Text
     *
     * @param name
     * @param showName
     */
    public static void setHtmlText( TextView showName,String name) {
        showName.setText(Html.fromHtml(name), null);
    }
<<<<<<< HEAD

    /**
     * md5加密
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }

    /**
     * 中文转unicode
     * @param cn
     * @return
     */
    public static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

    /**
     * 检查是否是汉字
     */
    public static boolean isChineseStr(String str){
        Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");


        Matcher m = p_str.matcher(str);
        if(m.find()&&m.group(0).equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 获取汉字字符串的汉语拼音，英文字符不变
     */
    public static String getPinYin(String chines) {
        StringBuffer sb = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(nameChar[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 设置不为空的TextView
     *
     * @param view
     * @param text
     */
    public static void setNotEmptText(TextView view, String text) {
        if(view == null){
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }

    /**
     * 设置不为空的TextView
     *
     * @param view
     * @param text
     */
    public static void setNotEmptText(TextView view, String text, String replace) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText(replace);
        }
    }

    /**
     * 设置不为空的TextView
     *
     * @param view
     * @param text
     */
    public static void setNotEmptText(EditText view, String text) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
            SUtils.setSelection(view);
        } else {
            view.setText("");
        }
    }
=======
>>>>>>> 7cc00bee69b40f9634b41ccdf27b439a3c4fd3e9
}
