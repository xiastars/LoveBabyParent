package com.summer.helper.utils;

import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by xiastars on 2017/9/7.
 */

public class SViewUtils {

    /**
     * 获取一个View的左外边距
     *
     * @param view
     * @return
     */
    public static int getViewLeMargin(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            if (parent instanceof RelativeLayout) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    return params.leftMargin;
                }
            } else if (parent instanceof LinearLayout) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    return params.leftMargin;
                }
            } else if (parent instanceof FrameLayout) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    return params.leftMargin;
                }
            }
        }

        return 0;
    }

    /**
     * 设置View的外边距
     *
     * @param view
     * @return
     */
    public static void setViewMargin(View view, int leftMargin, SDirection direction) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            if (parent instanceof RelativeLayout) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    switch (direction) {
                        case LEFT:
                            params.leftMargin = leftMargin;
                            break;
                        case RIGHT:
                            params.rightMargin = leftMargin;
                            break;
                        case TOP:
                            params.topMargin = leftMargin;
                            break;
                        case BOTTOM:
                            params.bottomMargin = leftMargin;
                            break;
                    }

                }
            } else if (parent instanceof LinearLayout) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    switch (direction) {
                        case LEFT:
                            params.leftMargin = leftMargin;
                            break;
                        case RIGHT:
                            params.rightMargin = leftMargin;
                            break;
                        case TOP:
                            params.topMargin = leftMargin;
                            break;
                        case BOTTOM:
                            params.bottomMargin = leftMargin;
                            break;
                    }
                }
            } else if (parent instanceof FrameLayout) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (params != null) {
                    switch (direction) {
                        case LEFT:
                            params.leftMargin = leftMargin;
                            break;
                        case RIGHT:
                            params.rightMargin = leftMargin;
                            break;
                        case TOP:
                            params.topMargin = leftMargin;
                            break;
                        case BOTTOM:
                            params.bottomMargin = leftMargin;
                            break;
                    }
                }
            }
        }
    }


    /**
     * 设置View的高度
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            return;
        }
        params.height = SUtils.getDip(view.getContext(), height);
    }


    public enum SDirection {
        LEFT(0),
        RIGHT(1),
        BOTTOM(2),
        TOP(3);
        public int direction;

        SDirection(int value) {
            this.direction = value;
        }
    }
}
