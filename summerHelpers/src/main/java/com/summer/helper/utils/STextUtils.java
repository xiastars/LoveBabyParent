package com.summer.helper.utils;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public static void setThousants(TextView tvContent, int value, String pre, String suf) {
        if (value < 100000) {
            SUtils.setHtmlText(pre + value + suf, tvContent);
        } else {
            int mul = value / 10000;
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
}
