package com.summer.parent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.helper.recycle.SRecycleMoreAdapter;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.STimeUtils;
import com.summer.helper.utils.SUtils;
import com.summer.parent.R;
import com.summer.parent.bean.MovementInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行程
 * Created by xiaqiliang on 2017/8/7.
 */
public class MovementAdapter extends SRecycleMoreAdapter {String todayFormat;
    boolean hideMore;
    String xUserId;

    public MovementAdapter(Context context) {
        super(context);
        todayFormat = STimeUtils.getDayWithFormat("MM/dd", System.currentTimeMillis());
    }

    public MovementAdapter(Context context, boolean hideMore, String xUserId) {
        super(context);
        this.hideMore = hideMore;
        todayFormat = STimeUtils.getDayWithFormat("MM/dd", System.currentTimeMillis());
        this.xUserId = xUserId;
    }

    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder hd = (ViewHolder) holder;
        final MovementInfo info = (MovementInfo) items.get(position);
        hd.tvTitle.setText(info.getContent());
        if (position == 0) {
            hd.viewTopLine.setVisibility(View.INVISIBLE);
            hd.viewCenter.setBackgroundResource(R.drawable.so_redd4_90);
        } else {
            //hd.viewCenter.setBackgroundResource(R.drawable.so_greyca_45);
            hd.viewTopLine.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_top_line)
        View viewTopLine;
        @BindView(R.id.view_center)
        View viewCenter;
        @BindView(R.id.view_bottom_line)
        View viewBottomLine;
        TextView tvType;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ll_right)
        LinearLayout llRight;
        TextView tvStarName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
