package com.summer.helper.view.resize;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.summer.helper.utils.SUtils;
import com.summer.helper.view.RoundAngleImageView;

/**
 * Created by xiaqiliang on 2017/3/28.
 */
public class RRoundImage extends RoundAngleImageView {

    public RRoundImage(Context context) {
        super(context);
    }

    public RRoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RRoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void reLayout(int width, int height, int marginLeft, int marginTop) {
        reLayout(width,height,marginLeft,marginTop,false);
    }

    public void reLayout(int width, int marginLeft, int marginTop) {
        reLayout(width,width,marginLeft,marginTop,true);
    }

    public void reLayout(int width, int height, int marginLeft, int marginTop,boolean rectangle) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (width != 0) {
            params.width = SUtils.getSWidth((Activity) getContext(),width);
        }
        if (height != 0) {
            if(rectangle){
                params.height = SUtils.getSWidth((Activity) getContext(),width);
            }else{
                params.height = SUtils.getSHeight((Activity) getContext(),height);
            }
        }
        if (params instanceof RelativeLayout.LayoutParams) {
            if (marginLeft != 0) {
                ((RelativeLayout.LayoutParams) params).leftMargin = SUtils.getSWidth((Activity) getContext(),marginLeft);
            }
            if (marginTop != 0) {
                ((RelativeLayout.LayoutParams) params).topMargin =  SUtils.getSHeight((Activity) getContext(),marginTop);
            }
        } else if (params instanceof LinearLayout.LayoutParams) {
            if (marginLeft != 0) {
                ((LinearLayout.LayoutParams) params).leftMargin = SUtils.getSWidth((Activity) getContext(),marginLeft);
            }
            if (marginTop != 0) {
                ((LinearLayout.LayoutParams) params).topMargin = SUtils.getSHeight((Activity) getContext(),marginTop);
            }
        } else if (params instanceof FrameLayout.LayoutParams) {
            if (marginLeft != 0) {
                ((FrameLayout.LayoutParams) params).leftMargin =  SUtils.getSWidth((Activity) getContext(),marginLeft);
            }
            if (marginTop != 0) {
                ((FrameLayout.LayoutParams) params).topMargin = SUtils.getSHeight((Activity) getContext(),marginTop);
            }
        }
    }
}
