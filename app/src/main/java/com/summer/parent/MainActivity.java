package com.summer.parent;

import com.summer.helper.utils.Logs;
import com.summer.parent.adapter.MovementAdapter;
import com.summer.parent.base.BaseActivity;
import com.summer.parent.bean.HomeBean;
import com.summer.parent.bean.MovementInfo;
import com.summer.parent.server.Const;
import com.summer.parent.server.Server;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.view.SRecycleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;

public class MainActivity extends BaseActivity implements ValueEventListener {
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
        BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(context, this);
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


    @Override
    public void onConnectCompleted() {
        Logs.i("连接成功:");
    }

    @Override
    public void onDataChange(JSONObject jsonObject) {
        Logs.i("(" + jsonObject.optString("action") + ")" + "数据：" + jsonObject);
    }
}
