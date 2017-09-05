package com.summer.helper.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class SScrollView extends ScrollView {
    float downX;
    float downY;

    public SScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = ev.getX();
                float curY = ev.getY();
                if (Math.abs(curX - downX) > 1) {

                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
