package com.summer.helper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.summer.helper.R;

/**
 * Created by xiaqiliang on 2017/4/12.
 */

public class CountView extends View {

    private Paint circlePaint;

    int curIndex = 0;
    int count = 0;

    public CountView(Context context) {
        super(context);
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (circlePaint == null) {
            circlePaint = new Paint();
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setStrokeWidth(2);
            circlePaint.setAntiAlias(true);
        }
        int height = getBottom() - getTop();
        int width = getRight() - getLeft();
        circlePaint.setColor(getContext().getResources().getColor(R.color.red_d4));
        canvas.drawOval(0, 0, width, height, circlePaint);
        circlePaint.setColor(getContext().getResources().getColor(R.color.white));
        canvas.drawLine(width * 0.28f, width * 0.8f, width * 0.72f, width * 0.2f, circlePaint);
        float textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, getContext().getResources().getDisplayMetrics());
        circlePaint.setTextSize(textSize);
        canvas.drawText(curIndex + "", width * 0.25f, width * 0.4f, circlePaint);
        canvas.drawText(count + "", width * 0.55f, width * 0.75f, circlePaint);
        canvas.save();
    }

    public void setCurIndex(int index,int count){
        this.curIndex = index;
        this.count = count;
    }

}
