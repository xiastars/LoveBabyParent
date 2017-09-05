package com.summer.parent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.parent.bean.HomeBean;
import com.summer.helper.recycle.SRecycleMoreAdapter;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SUtils;
import com.summer.parent.bean.MovementInfo;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class HomeAdapter extends SRecycleMoreAdapter {

    public HomeAdapter(Context context) {
        super(context);
    }

    public HomeAdapter(Context context, ViewGroup headerView) {
        super(context, headerView);
    }
    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new TabViewHolder(view);
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, int position) {
        TabViewHolder hd = (TabViewHolder) holder;
        final MovementInfo info = (MovementInfo) items.get(position);

        SUtils.setNotEmptText(hd.tvTitle, info.getContent());
        hd.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JumpTo.getInstance().commonJump(context, NewActivity.class);
            }
        });
    }

    private class TabViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIcon;
        private TextView tvTitle, tvJoin, tvStatues;
        private LinearLayout rlParent;

        public TabViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_nav);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvJoin = (TextView) itemView.findViewById(R.id.tv_join);
            tvStatues = (TextView) itemView.findViewById(R.id.tv_status);
            rlParent = (LinearLayout) itemView.findViewById(R.id.rl_parent);
        }
    }

}
