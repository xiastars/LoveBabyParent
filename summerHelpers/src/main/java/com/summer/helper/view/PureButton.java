package com.summer.helper.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;

import com.summer.helper.utils.SUtils;

/**
 * Created by xiaqiliang on 2017/3/27.
 */
public class PureButton extends Button {

    int normalBg ;
    int pressedBg ;

    public PureButton(Context context) {
        super(context);
        this.setPadding(0,0,0,0);
    }

    public PureButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PureButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置宽高
     * @param width
     * @param height
     */
    public void setLayoutWH(int width,int height){
        this.setLayoutParams(new ViewGroup.LayoutParams(SUtils.getSWidth((Activity) getContext(),width), SUtils.getSHeight((Activity) getContext(),height)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(pressedBg != 0){
                    this.setBackgroundResource(pressedBg);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(normalBg != 0){
                    this.setBackgroundResource(normalBg);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(normalBg != 0){
                    this.setBackgroundResource(normalBg);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setNormalBg(int id){
        this.normalBg = id;
        this.setBackgroundResource(id);
    }

    public void setPressedBg(int id){
        this.pressedBg = id;
    }
}
