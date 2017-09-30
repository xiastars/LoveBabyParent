package com.summer.parent;

import com.summer.helper.view.SRecycleView;
import com.summer.parent.adapter.MovementAdapter;
import com.summer.parent.base.BaseActivity;
import com.summer.parent.bean.MovementInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    MovementAdapter movementAdapter;

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        switch (requestCode) {
            case 1:
                handleViewData(obj);
                break;
        }
    }

    @Override
    protected int setTitleId() {
        return R.string.home;
    }

    @Override
    protected int setContentView() {
        return R.layout.view_srecyleview;
    }

    @Override
    protected void initData() {
        SRecycleView svContainer = (SRecycleView) findViewById(R.id.sv_container);
        setSRecyleView(svContainer);
        movementAdapter = new MovementAdapter(context);
        svContainer.setAdapter(movementAdapter);
        loadData();
    }

    @Override
    protected void loadData() {
        BmobQuery<MovementInfo> findMovements = new BmobQuery<>();
        findMovements.findObjects(context, new FindListener<MovementInfo>() {
            @Override
            public void onSuccess(List<MovementInfo> list) {
                movementAdapter.notifyDataChanged(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }
}
