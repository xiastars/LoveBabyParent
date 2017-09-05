package com.summer.parent.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summer.helper.server.ServerFileUtils;
import com.summer.parent.MyApplication;
import com.summer.parent.R;
import com.summer.helper.db.CommonService;
import com.summer.helper.db.DBType;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.SRecycleMoreAdapter;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.server.RequestCallback;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.LoadingDialog;
import com.summer.helper.view.NRecycleView;

import java.util.List;

/**
 * @see BaseFragment
 * @see BaseRequestActivity
 * @see BaseFragmentActivity
 * Created by xiaqiliang on 2017/5/2.
 */

public class BaseHelper {
    Context context;
    boolean firstRequest;
    Handler myHandlder;
    LoadingDialog loadingDialog;
    boolean isRefresh;

    //请求数据失败
    public static final int MSG_ERRO = -1;
    //请求数据成功
    public static final int MSG_SUCCEED = -2;

    public static final int MSG_FINISHLOAD = -3;

    //从数据库读取目录成功
    public static final int MSG_CACHE = -4;

    //虚拟数据地址
    public static String VITURAL_DATA = "";

    boolean toastMessage;

    //当一个页面反复刷新时，只第一次插入
    boolean isFirstInsertDB;

    //分页数量
    int loadCount = 0;

    public BaseHelper(Context context, Handler myHandlder) {
        this.context = context;
        this.myHandlder = myHandlder;
    }

    /**
     * 请求数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     * @param post
     */
    public void requestData(final int requestCode, final Class className, SummerParameter params, final String url, boolean post) {
        requestData(requestCode, 0, className, params, url, post);
    }

    /**
     * 请求数据
     *
     * @param limitTime 数据重新请求限定时间
     * @param className 要注入的类
     * @param params
     * @param url       链接
     * @param post      是否是Post
     */
    public void requestData(final int requestCode, int limitTime, final Class className, SummerParameter params, final String url, boolean post) {
        if (params == null) {
            return;
        }
        long time = System.currentTimeMillis();
        //取得每页请求的数量，用来处理底部栏没有更多了
        if (params.containsKey("count")) {
            String count = (String) params.get("count");
            if (count != null) {
                loadCount = Integer.parseInt(count);
            }
        }
        final String saveurl = params.encodeLogoUrl(url);
        //当为开发者模式且SummperParameter设置了虚拟数据时才启用
        if (MyApplication.DEBUGMODE) {
            if (params.isVirtualData()) {
                String virtualCode = params.getVirtualCode();
                //virtualdata.txt
                String data = ServerFileUtils.readFileByLineOnAsset(virtualCode,VITURAL_DATA, context);
                Logs.i("data:::" + data);
                if (data != null) {
                    try {
                        BaseResp t = JSON.parseObject(data, BaseResp.class);
                        if (t != null) {
                            handleData(requestCode, t, saveurl, className);
                            isRefresh = false;
                            myHandlder.sendEmptyMessage(MSG_FINISHLOAD);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Logs.t(time, "");
        boolean readCache = params.isCacheSupport();
        Logs.i("readCache::::" + readCache);

        Logs.t(time, "");
        Logs.t(time, "");
        //频率限制，暂时不做限制
       /*if (isFrequentRequest(saveurl, 0)) {
            return;
        }*/
        //页面启动第一次时从缓存里获取数据
        if (!firstRequest && readCache) {
            firstRequest = true;
            Object hunkResp = new CommonService(context).getObjectData(DBType.COMMON_DATAS, saveurl);
            if (hunkResp != null) {
                myHandlder.obtainMessage(MSG_CACHE, hunkResp).sendToTarget();
                cancelLoading();
            }
            Logs.t(time, hunkResp + "");
        }

        //当第一次请求数据时显示加载框
        if (!isRefresh && loadingDialog == null) {
            loadingDialog = LoadingDialog.getInstance(context);
            loadingDialog.startLoading();
        }
        //默认所有className继承自BaseResp，当为List数据时，为了方便，直接用List里的单个对象
        Class injectClass = BaseResp.class;
        if (BaseResp.class.isAssignableFrom(className) && className != BaseResp.class) {
            injectClass = className;
        }
        if (post) {
            EasyHttp.post(context, url, injectClass, params, new RequestCallback<Object>() {
                @Override
                public void done(Object hunkResp) {
                    handleData(requestCode, hunkResp, saveurl, className);
                    isRefresh = false;
                }

                @Override
                public void onError(int errorCode, String errorStr) {
                    Logs.e("hxq","errorStr,"+errorStr);
                    myHandlder.obtainMessage(MSG_ERRO, requestCode, errorCode, errorStr).sendToTarget();
                }
            });
        } else {
            EasyHttp.get(context, url, injectClass, params, new RequestCallback<Object>() {
                @Override
                public void done(Object hunkResp) {
                    handleData(requestCode, hunkResp, saveurl, className);
                    isRefresh = false;
                }

                @Override
                public void onError(int errorCode, String errorStr) {
                    Logs.e("hxq","errorStr,"+errorStr);
                    myHandlder.obtainMessage(MSG_ERRO, requestCode, errorCode, errorStr).sendToTarget();
                }
            });
        }
    }

    /**
     * 频繁请求提示
     *
     * @param saveurl
     * @return
     */
    private boolean isFrequentRequest(String saveurl, int reqestTime) {
        final String saveLong = saveurl + "_long";
        final String saveInt = saveurl + "_int";
        long lastTime = SUtils.getLongData(context, saveLong);
        int requestIndex = SUtils.getIntegerData(context, saveInt);
        int divideTime = (int) (System.currentTimeMillis() - lastTime);
        if (divideTime < reqestTime) {
            return true;
        }
        if (divideTime < 300) {
            SUtils.saveLongData(context, saveLong, System.currentTimeMillis());
            SUtils.saveIntegerData(context, saveInt, requestIndex + 1);
        } else {
            SUtils.saveLongData(context, saveLong, System.currentTimeMillis());
            myHandlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SUtils.saveIntegerData(context, saveInt, 0);
                }
            }, 2000);
        }
        if (requestIndex > 1) {
            SUtils.makeToast(context, "操作频繁，请稍后重试");
            return true;
        }
        return false;
    }

    /**
     * 请求数据
     *
     * @param requestCode 请求类型
     * @param className   要注入的类
     * @param params
     * @param url         链接
     * @param post        是否是Post
     */
    public void requestData(final int requestCode, final Class datas, Class className, SummerParameter params, final String url, boolean post) {
        long time = System.currentTimeMillis();
        final String saveurl = params.encodeLogoUrl(url);
        Logs.t(time, "");
        Logs.i("saveUrl:++++++" + saveurl);
    /*    if (isFrequentRequest(saveurl,0)) {
            return;
        }*/
        if (!firstRequest) {
            firstRequest = true;
            Object hunkResp = new CommonService(context).getObjectData(DBType.COMMON_DATAS, saveurl);
            if (hunkResp != null) {
                myHandlder.obtainMessage(MSG_CACHE, hunkResp).sendToTarget();
                cancelLoading();
            }
        }
        Logs.t(time, "");
        if (!isRefresh && loadingDialog == null) {
            loadingDialog = LoadingDialog.getInstance(context);
            loadingDialog.startLoading();
        }
        Logs.t(time, "");
        if (post) {
            EasyHttp.post(context, url, className, params, new RequestCallback<Object>() {
                @Override
                public void done(Object hunkResp) {
                    handleData(requestCode, hunkResp, saveurl, datas);
                    isRefresh = false;
                }

                @Override
                public void onError(int errorCode, String errorStr) {

                }
            });
            Logs.t(time, "");
        } else {
            EasyHttp.get(context, url, className, params, new RequestCallback<Object>() {
                @Override
                public void done(Object hunkResp) {
                    handleData(requestCode, hunkResp, saveurl, datas);
                    isRefresh = false;
                }

                @Override
                public void onError(int errorCode, String errorStr) {

                }
            });
        }
    }

    /**
     * 处理返回数据
     *
     * @param hunkResp
     * @param url
     * @param classD
     */
    private void handleData(final int requestCode, final Object hunkResp, final String url, Class classD) {
        if (hunkResp != null) {
            final BaseResp resp = (BaseResp) hunkResp;
            if (resp.getData() != null) {
                Object content = resp.getData();
                Object datas = null;
                if (!BaseResp.class.isAssignableFrom(classD) && classD != BaseResp.class) {
                    if (content instanceof JSONArray) {
                        JSONArray arrayContent = (JSONArray) content;
                        datas = JSON.parseArray(arrayContent.toJSONString(), classD);
                    } else if (content instanceof JSONObject) {
                        JSONObject arrayContent = (JSONObject) content;
                        datas = JSON.parseObject(arrayContent.toJSONString(), classD);
                    }
                } else {
                    datas = resp;
                }
                if (datas != null) {
                    myHandlder.obtainMessage(BaseHelper.MSG_SUCCEED, requestCode, 0, datas).sendToTarget();
                    final Object finalDatas = datas;
                    if (!isFirstInsertDB) {
                        isFirstInsertDB = true;
                        SThread.getIntances().submit(new Runnable() {
                            @Override
                            public void run() {
                                long time = System.currentTimeMillis();
                                new CommonService(context).insert(DBType.COMMON_DATAS, url, finalDatas);
                                Logs.t(time, "url=" + url);
                            }
                        });
                    }
                } else {
                    myHandlder.obtainMessage(BaseHelper.MSG_SUCCEED, requestCode, -1, null).sendToTarget();
                }
            } else {
                final String code = resp.getCode();
                Logs.i("datas:" + code);
                if (BaseResp.class.isAssignableFrom(classD) || classD == BaseResp.class) {
                    myHandlder.obtainMessage(BaseHelper.MSG_SUCCEED, requestCode, 0, resp).sendToTarget();
                }
            }
        }
        myHandlder.sendEmptyMessage(MSG_FINISHLOAD);
    }

    public void cancelLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancelLoading();
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    /**
     * 设置沉浸式状态栏
     */
    public void setLayoutFullscreen(boolean fullscreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Activity activity = (Activity) context;
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (fullscreen) {
                    window.getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                } else {
                    window.getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                View view = activity.findViewById(R.id.title);
                if (view != null) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    params.height = SUtils.getDip(context, 45) + SUtils.getStatusBarHeight(activity);
                }

            } else {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    /**
     * 处理RecyleView的数据，支持上拉加载显示
     *
     * @param obj
     * @param sRecycleView
     * @param pageIndex
     */
    public void handleViewData(Object obj, MaterialRefreshLayout sRecycleView, int pageIndex) {
        if (sRecycleView == null) {
            return;
        }
        if (sRecycleView.getRefreshView() == null) {
            return;
        }
        if (obj != null) {
            List resp = (List) obj;
            SRecycleMoreAdapter adapter = (SRecycleMoreAdapter) sRecycleView.getRefreshView().getAdapter();
            int size = resp.size();
            if (size > 0) {
                if (pageIndex > 0 && adapter.items != null) {
                    adapter.items.addAll(resp);
                } else {
                    adapter.items = resp;
                }
                //如果当前加载的数据量小于设定的单页数量，则显示底部栏
                if (size < loadCount) {
                    sRecycleView.setLoadMore(false);
                    adapter.notifyDataChanged(adapter.items, false);
                } else {
                    sRecycleView.setLoadMore(true);
                    adapter.notifyDataChanged(adapter.items, true);
                }
                sRecycleView.hideEmptyView();
                //如果不是第一页，且没有更多数据了，显示底部栏
            } else if (pageIndex > 0) {
                sRecycleView.setLoadMore(false);
                adapter.setBottomViewVisible();
            } else {
                adapter.showEmptyView();
                sRecycleView.showEmptyView();
            }
        }
    }


    /**
     * 处理RecyleView的数据，支持上拉加载显示
     *
     * @param obj
     * @param sRecycleView
     * @param pageIndex
     */
    public void handleViewData(Object obj, NRecycleView sRecycleView, MaterialRefreshLayout topView, int pageIndex) {
        if (sRecycleView == null) {
            return;
        }
        if (obj != null) {
            List resp = (List) obj;
            SRecycleMoreAdapter adapter = (SRecycleMoreAdapter) sRecycleView.getAdapter();
            //是否显示空页面
            boolean isShowEmptyView = adapter.isShowEmptyView();
            if(isShowEmptyView){
                if (pageIndex == 0 && resp.size() == 0) {
                    adapter.notifyDataChanged(resp, true);
                    return;
                }
            }
            if (resp.size() > 0) {
                if (pageIndex > 0 && adapter.items != null) {
                    adapter.items.addAll(resp);
                } else {
                    adapter.items = resp;
                }
                if (resp.size() < loadCount) {
                    if (topView != null) {
                        topView.setLoadMore(false);
                    }
                    adapter.notifyDataChanged(adapter.items, false);
                } else {
                    adapter.notifyDataChanged(adapter.items, true);
                }

            } else if (pageIndex > 0) {
                if (topView != null) {
                    topView.setLoadMore(false);
                }
                adapter.setBottomViewVisible();
            }
        }
    }


    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}
