package com.summer.helper.recycle;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.summer.helper.R;
import com.summer.helper.listener.OnAnimEndListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SAnimUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.CustomScrollView;
import com.summer.helper.view.NRecycleView;

/**
 * @author https://github.com/android-cjj/Android-MaterialRefreshLayout
 */
public class ScollViewRefreshLayout extends MaterialRefreshLayout {

    private CustomScrollView refreshView;

    View eggView;
    RelativeLayout rlContentView;
    View viewContent;
    ImageView ivIcon;
    TextView tvContent;

    int preHeight = 0;
    int maxEggHeight;
    Rect childViewRect;
    //显示底部彩蛋
    boolean isShowEgg;
    float mTouchY;
    boolean isOnShowEgg;//正在显示彩蛋

    public ScollViewRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public ScollViewRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScollViewRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        refreshView = new CustomScrollView(context);
        this.removeAllViews();
        this.addView(refreshView);
        rlContentView = new RelativeLayout(context);
        rlContentView.setBackgroundColor(context.getResources().getColor(R.color.grey_eee));
        refreshView.addView(rlContentView);
        addEggView();

        refreshView.getParent().requestDisallowInterceptTouchEvent(true);
        refreshView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mTouchY == 0) {
                            mTouchY = event.getY();
                        }
                        float currentY = event.getY();
                        float dy = currentY - mTouchY;
                        Logs.i("xia", ViewCompat.canScrollVertically(refreshView, 1) + ",,,," + dy + ",,," + currentY + ",,," + mTouchY);
                        if (!ViewCompat.canScrollVertically(refreshView, 1)) {
                            setAutoLoadmore();
                            if (isShowEgg && dy < 0 && dy > -maxEggHeight && !isLoadMore) {
                                isOnShowEgg = true;
                                preHeight = (int) (dy * 0.7f);
                                viewContent.layout(childViewRect.left, (int) (childViewRect.top + dy * 0.7f), childViewRect.right, (int) (childViewRect.bottom + dy * 0.7f));
                            }
                        }
                        break;
                    default:
                        mTouchY = 0;
                        if (isShowEgg && isOnShowEgg && !isLoadMore) {
                            SAnimUtils.showPropertyAnim(false, viewContent, View.VISIBLE, "translationY", 0, -preHeight, -preHeight, 400, new OnAnimEndListener() {
                                @Override
                                public void onEnd() {
                                    viewContent.setTranslationY(0);
                                    viewContent.layout(childViewRect.left, childViewRect.top, childViewRect.right, childViewRect.bottom);
                                }
                            });
                        }
                        isOnShowEgg = false;
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化彩蛋视图
     */
    private void addEggView() {
        maxEggHeight = SUtils.getDip(getContext(), 200);
        eggView = LayoutInflater.from(getContext()).inflate(R.layout.include_bottom_view, null);
        ivIcon = (ImageView) eggView.findViewById(R.id.iv_bottom_icon);
        tvContent = (TextView) eggView.findViewById(R.id.tv_bottom_content);
        rlContentView.addView(eggView);
        RelativeLayout.LayoutParams eggParams = (RelativeLayout.LayoutParams) eggView.getLayoutParams();
        eggParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        eggParams.width = SUtils.screenWidth;
        eggParams.height = SUtils.getDip(getContext(), 130);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        childViewRect = new Rect(viewContent.getLeft(), viewContent.getTop(), viewContent.getRight(), viewContent.getBottom());
    }

    /**
     * 设置上拉加载
     */
    public void setLoadMore() {
        this.setLoadMore(true);
    }

    public void disableLoadMore() {
        this.setLoadMore(false);
    }

    /**
     * 设置圈圈在View上面
     */
    public void setOverLay() {
        setIsOverLay(true);
    }

    public void addRefreshView(View view) {
        viewContent = view;
        if (view.getParent() == null) {
            rlContentView.addView(view);
        }
        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
        view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.invalidate();
        view.requestLayout();
    }

    public void setTopView(View view) {
        // refreshView.setTopView(view);
    }

    /**
     * 显示最底下的彩蛋
     *
     * @param resString
     * @param resDrawable
     */
    public void setShowEggView(int resString, int resDrawable) {
        isShowEgg = true;
        ivIcon.setBackgroundResource(resDrawable);
        tvContent.setText(resString);
    }

    public CustomScrollView getScrollView() {
        return refreshView;
    }


    /**
     * 设置关联的RecycleView，用来自加载
     *
     * @param recyleView
     */
    NRecycleView recyleView;

    public void setRecyleView(NRecycleView recyleView) {
        Logs.i("xia", canChildScrollDown() + ",,,");
        this.recyleView = recyleView;
    }

}
