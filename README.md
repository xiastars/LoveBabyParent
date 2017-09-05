# SummerDemo

## 了解MaterialRefreshLayout
一般的RecycleView:com.summer.helper.view.NRecycleView

 xml:
```xml
 <com.summer.helper.view.NRecycleView
        android:id="@+id/nv_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```
java:
```java
 mRecycleView.setList();//设置为ListView样式
 mRecycleView.setListInScrollView();//设置为ListView样式，当此RecycleView嵌套在ScrollView内
 mRecycleView.setGridView(int num);设置为GridView样式，传入展现列数
 mRecycleView.setHorizontalList();设置为横向滚动的ListView样式
```
> 可以刷新，上拉加载的RecycleView:com.summer.helper.view.SRecycleView

SRecycleView包含NRecycleView的一切用法，除此之外有以下用法
```java
    mRecycleView.setLoadMore();//支持上拉加载
    mRecycleView.setOverLay();//加载样式悬浮在View上，如果不设置，则View整体跟着下拉
	//添加刷新Listener
    mRecycleView.setMaterialRefreshListener(new MaterialRefreshListener() {
                @Override
                public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                    loadData();
                }
    
                @Override
                public void onfinish() {
                }
    
                @Override
                }
            });
    mRecycleView.finishRefresh();//完成刷新 
    mRecycleView.finishRefreshLoadMore();//完成上拉加载
```

> 可以刷新，上拉加载的ScrollVIew:com.summer.helper.recycle.ScollViewRefreshLayout：用法与SRecycleView一致

注:父类MaterialRefreshLayout里封装了空页面的展示,当调用到finishRefresh时,会调用到showEmptyView方法,该方法通过判断RecycleView的ItemCount个数来决定是否展现空页面

## SRecycleMoreAdapter使用
核心类：SRecycleAdapter，SRecycleMoreAdapter（加载更多时完成时，下面显示没有更多内容了）
示例：
```java
//创建一个类继承自SRecycleMoreAdapter
public class AreaMarAdapter extends SRecycleMoreAdapter {
    String today;

    public AreaMarAdapter(Context context) {
        super(context);
    }

    //传入HeaderView,HeaderView相关数据处理放到Activity里，相当于ScrollView嵌套RecyleView的方式
    public AreaMarAdapter(Context context, ViewGroup headerView) {
        super(context, headerView);
    }

    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resId, parent, false);
        return new TabViewHolder(view);
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, int position) {
        TabViewHolder hd = (TabViewHolder) holder;
 
    }

    private class TabViewHolder extends RecyclerView.ViewHolder {

        public TabViewHolder(View itemView) {
            super(itemView);
        }
    }

}
```


## 缓存处理
本缓存处理没有采取第三方框架，包含两种方式，一种为原始数据一个个插入的方式，另外一种为存储序列化后的对象

该框架下数据缓存已封闭在数据请求里，开发人员不必手动写缓存(后面再讲)，这里只使用第二种处理方式

核心类：com.summer.helper.db.CommonService

```java
获取CommonService对象:CommonService mService = new CommonService(Context context)
插入一个单个对象或List<?>:mService.insert(int type,Object data);//该对象及所属类必须实现Serializable,type类型必需,区分数据
根据ID插入:mService.insert(int type,String key,Object data);有时同类数据也要区别,或者需要一个个对象插入,则插入KEY

获取单个对象:mService.getObjectData(int type)
获取List<?>:mService.getListData(int type)
删除数据:mService.commonDeleteData(int type)
```
## 数据请求与数据下载
核心类:com.summer.helper.server.EasyHttp

数据请求示例-GET:

```java
 SummerParameter params = Const.getBasicParameters();//传入固定的一些参数
 params.put("id",122);//传入后台需要的参数
 params.putLog("我的圈子");//最好传这个进去,打印的时候方便看 
 //Context传入,用来做网络判断,当无网络时,直接Toast提示
 //第二个参数是请求链接 
 //第三个参数是要被注入的类
 EasyHttp.get(context, Server.HOME_HUNK, GroupResp.class, params, new  RequestCallback<GroupResp>() {
            @Override
            public void done(GroupResp hunkResp) {
                //请求错误返回NULL,这里需要判断
                if (hunkResp != null) {

                }
            }
        });
```
数据请求示例-POST:

```java
  EasyHttp.post(context, url, className, params, new RequestCallback<Object>() {
                @Override
                public void done(Object hunkResp) {

                }
            });
```
数据请求的链接与数据都在Logs里打印了,TAG为默认的,见Logs章

数据下载示例:

 ```java
 //第二个参数:下载链接 
  //第三个参数:保存文件的位置
  //第三个参数:保存文件的名称
  EasyHttp.download(getContext(), downloadUrl, path, fileName, new DownloadTaskListener() {
            @Override
            public void onDownloading(DownloadTask downloadTask) {
			   //返回下载数据的百分比,用于ProgressBar显示
                float progress = downloadTask.getPercent();
				//下载完毕
                if(downloadTask.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_COMPLETED){

             }
            }

            @Override
            public void onPause(DownloadTask downloadTask) {

            }

            @Override
            public void onError(DownloadTask downloadTask, int errorCode) {

            }
        });
```
## 综合使用,创建一个支持刷新与上拉加载的列表页面
//创建一个Activity继承自BaseActivity
```java

public class SubjectActivity extends BaseActivity {
    SubjectAdapter subjectAdapter;
    SRecycleView svContainer;

    @Override
    protected int setTitleId() {
        return R.string.subject;
    }

    @Override
    protected int setContentView() {
        return R.layout.view_srecyleview;
    }

    @Override
    protected void initData() {
        svContainer = (SRecycleView) findViewById(R.id.sv_container);
        setSRecyleView(svContainer);
        subjectAdapter = new SubjectAdapter(context);
        svContainer.setAdapter(subjectAdapter);
        loadData();
    }

    /**
     * 处理数据
     *
     * @param requestCode 请求码，一个页面可能有多个请求，用此参数来分别
     * @param obj
     */
    @Override
    protected void dealDatas(int requestCode, Object obj) {
        switch (requestCode) {
            case 0:
                handleViewData(obj);
                break;
        }
    }

    /**
     * 有没有单独对错误进行处理的时候，重写此方法。
     * @param requstCode 请求数据标识码
     * @param requestType 返回错误码，根据requestCode判断是返回的Code还是@ErroCode
     * @param errString 错误信息
     * @param requestCode 如果是返回的Code则为true
     */
    @Override
    protected void dealErrors(int requstCode, String requestType, String errString, boolean requestCode) {
        super.dealErrors(requstCode, requestType, errString, requestCode);
    }

    /**
     * 支持下一页加载的页面必须继承此方法,在这里做数据请求
     * count字段必须一致，如果项目中使用的字段不一致，请到@see BaseHelper里修改
     */
    @Override
    public void loadData() {
        final SummerParameter params = Const.getPostParameters();
        params.put("page", pageIndex);
        params.put("count", 10);
        //第二个参数只有当为datas字段下为集合数据时，才使用，一般写一个类继承自BaseResp
        requestData(0, SubjectInfo.class, params, Server.SUBJECT_LIST, true);
    }
}
```
BaseActivity里主要写了SRecycleView的刷新Listener和刷新完后的处理,它的父类BaseRequestActivity才是关键
### 解析BaseRequestActivity

此类与相关类做了以下工作:

A.添加了默认的头部栏,如果需要自定义,在这里设置

B.当第一次请求数据时,显示加载条;

> 注:了解LoadingDialog

C.当网络断开时,显示网络连接异常界面

D.请求数据,包括缓存
```java

 //由BaseHelper调用此方法
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
        //取得每页请求的数量，用来处理底部栏没有更多了
        if (params.containsKey("count")) {
            String count = (String) params.get("count");
            if (count != null) {
                loadCount = Integer.parseInt(count);
            }
        }
        long time = System.currentTimeMillis();
        final String saveurl = params.encodeLogoUrl(url);
        //频率限制，暂时不做限制
       /*if (isFrequentRequest(saveurl, 0)) {
            return;
        }*/
       //页面启动第一次时从缓存里获取数据
        if (!firstRequest) {
            firstRequest = true;
            Object hunkResp = new CommonService(context).getObjectData(DBType.COMMON_DATAS, saveurl);
            if (hunkResp != null) {
                myHandlder.obtainMessage(2, hunkResp).sendToTarget();
                cancelLoading();
            }
            Logs.i("xia", "处理缓存数据时间:" + (System.currentTimeMillis() - time));
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
                    myHandlder.obtainMessage(-1, requestCode, errorCode, errorStr).sendToTarget();
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
                    myHandlder.obtainMessage(-1, requestCode, errorCode, errorStr).sendToTarget();
                }
            });
        }
    }
```
E.设置沉浸式状态栏:setLayoutFullscreen

F.请求数据的Code统一处理
 ````java
BaseResp resp = (BaseResp) object;
 new CodeRespondUtils(activity.context, resp.getCode());
````

在BaseHelper里对数据做了处理
```java
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
            Logs.i("resp.size():" + resp.size() + "," + loadCount + "," + pageIndex);
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
```

H.一步实现Broadcast消息通知
子类调用initBroadcast（String... args）便可注册广播了，然后重写onMsgReceiver（int position),这个position是依据args添加的顺序

## 5.可追踪的Log打印
核心类:com.summer.helper.utils.Logs

当使用Logs.i("msg),使用的是默认的TAG,数据请求的信息都在此TAG下,建议开发人员在使用时,不要使用默认TAG,以使数据这些的信息纯粹

当软件要发布时,请在Application下,设置Logs.isDebug = false;//避免数据泄露

示例:
```java

04-27 16:08:39.076 30733-30733/? I/hxq: at EasyHttp.java(84) onResponse: 请求结果:我的圈子{"code":"0","message":"request success","handleTime":1493280519555}

```
## 6. com.summer.helper.utils.SUtils
SUtils类是本框架非常重要的类,熟悉它后会有效地提高开发人员的效率,以下是基本方法描述:

1.屏幕尺寸获取

```java
initScreenDisplayMetrics(Activity context);//在首个Activity里调用此代码,初始化数据
SUtils.screenWidth;//屏幕宽度
SUtils.screenHeight;//屏幕高度
```
2.自定义样式的Toast,不会重复的Toast
```java

makeToast(Context context, String text)
```
3.SharedPreferences的简单使用

```java
saveIntegerData(Context mContext, String type, int selected);插入int类型数据
getIntegerData(Context mContext, String type);获取int数据
支持各种基本类型数据的插入与获取
```
4.getDip(Context context,int value);转换为DIP值

5.检查网络是否可用

```java
isNetworkAvailable(Context context)
```
6.点击时,让View展现缩放动画

```java
clickTransColor(final TextView view)
```
7.时间距 ,一般评论时需要显示特定的时间,比如刚刚,一个小时前

```java
getRecentlyTime(long dtTime, long serverTime, Context mContext)
```
8.获取手机默认文件夹
```java
getSDPath()
```
9.获取状态栏高度
```java
getStatusBarHeight(Activity activity)
```
10.对Bitmap高斯模糊处理

```java
fastBlur(Bitmap sbitmap, float radiusf)
```
11.获取网络状态,有时需要判断是不是WIFI
```java
getNetWorkType(Context context)
```
12.将文件转为byte[]

```java
readFileAsBytes(String filename)
```
13.将文件写到Bitmap
```java
createScaleBitmap(String path, int width, int height)
```
14.加载图片,这里使用的Glide框架
 ```java
setPic(ImageView view, String img, boolean download)
```
> APP的图片存储链接前缀有可能变更(http/https),所以网络图片必须使用setPic,这里已做封装

15.使用静态资源内存管理
安卓本身对setBackgroundResource没有做好内存优化，导致在一些低端机子上加载本地图片容易报内存溢出错误，凡加载一些比较大的本地图片请使用BitmapUtils的setPic方法，示例如下
> RecycleView里不要使用，可能使用了回收的Bitmap造成闪退

```java
 BitmapUtils.getInstance().setPic(ImageView  view,int res,getClass().getSimpleName());
```
在Activity的onStop方法里回收
```java
 BitmapUtils.getInstance().clearBitmaps(getClass().getSimpleName());
```


### Libraries

Android Support Libraries
FastJson
Glide
OkHttp
MaterialRefreshLayout : https://github.com/android-cjj/Android-MaterialRefreshLayout
还有很多东摘西取的东西

> 其实代码有许多变动，文档还没改，改日完善
