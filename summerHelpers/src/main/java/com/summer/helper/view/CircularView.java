package com.summer.helper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.summer.helper.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

public class CircularView extends View {

    private Paint mPaint;
    private RectF mRect;

    Context context;

    private int audioValue;
    int padding = 0;
    boolean onDrawing;

    public CircularView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        padding = SUtils.getDip(context, 5);
    }

    public CircularView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRect = new RectF(padding, +padding, getWidth() - padding, getHeight() - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawing = true;
        Logs.i("audioValue"+audioValue);
        if (mRect != null) {
            try {
                canvas.restore();
            }catch (Exception e){
                e.printStackTrace();
            }
            mPaint.setColor(context.getResources().getColor(R.color.half_white));
            canvas.drawArc(mRect, 120, 300, false, mPaint);

            if (audioValue != 0) {
                mPaint.setColor(context.getResources().getColor(R.color.red_d4));
                canvas.drawArc(mRect, 120, audioValue, false, mPaint);
            }
            canvas.save();
        }
        onDrawing = false;

    }

    public void setAudioValue(int audioValue) {
        this.audioValue = audioValue;
        requestLayout();
        invalidate();
    }

}
