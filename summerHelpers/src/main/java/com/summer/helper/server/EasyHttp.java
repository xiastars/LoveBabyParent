package com.summer.helper.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.summer.helper.downloader.DownloadManager;
import com.summer.helper.downloader.DownloadStatus;
import com.summer.helper.downloader.DownloadTask;
import com.summer.helper.downloader.DownloadTaskListener;
import com.summer.helper.listener.OnResponseListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.PostData;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class EasyHttp {

    public static final String METHOD = "POST";
    public static final String METHOD_GET = "GET";
    static OkHttpClient okHttpClient;
    static OkHttpClient.Builder mBuilder;

    public static void init(Context context) {
        if (okHttpClient == null) {
            mBuilder = new OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000L, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
            okHttpClient = mBuilder.build();
            //设置最大并发量
            okHttpClient.dispatcher().setMaxRequestsPerHost(10);
            //其他配置
            OkHttpUtils.getInstance(okHttpClient);
        }
    }

    /**
     * POST请求，数据注入到类里
     *
     * @param context
     * @param url        请求链接
     * @param clazz      注入的类
     * @param parameters 参数对
     * @param callBack
     * @param <T>
     */
    public static <T> void post(Context context, String url, final Class<T> clazz,
                                final SummerParameter parameters, final RequestCallback<T> callBack) {
        if (!SUtils.isNetworkAvailable(context)) {
            if (callBack != null) {
                callBack.onError(ErrorCode.ERR_CONENCCT, "网络未连接,请连接后重试!");
            }
            return;
        }
        requestPost(context, url, clazz, parameters, callBack, METHOD);
    }

    /**
     * POST请求，数据注入到类里
     *
     * @param context
     * @param url        请求链接
     * @param clazz      注入的类
     * @param parameters 参数对
     * @param callBack
     * @param <T>
     */
    public static <T> void get(Context context, String url, final Class<T> clazz,
                               final SummerParameter parameters, final RequestCallback<T> callBack) {
        if (!SUtils.isNetworkAvailable(context)) {
            callBack.onError(ErrorCode.ERR_CONENCCT, "网络未连接,请连接后重试!");
            return;
        }
        requestGet(context, url, clazz, parameters, callBack, METHOD_GET);
    }

    /**
     * 不处理数据
     *
     * @param context
     * @param url        请求链接
     * @param parameters
     * @param listener
     */
    public static void get(Context context, String url, final SummerParameter parameters, RequestListener listener) {
        if (!SUtils.isNetworkAvailable(context)) {
            listener.onErrorException(new SummerException());
            return;
        }
        request(url, parameters, listener);
    }

    public static <T> void get(Context context, String action, String url, final Class<T> clazz,
                               final SummerParameter parameters, final RequestCallback<T> callBack) {
        if (null != parameters) parameters.putLog(action);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        requestGet(context, url, clazz, parameters, callBack, METHOD_GET);
    }

    private static <T> void requestPost(final Context context, String url, final Class<T> clazz, final SummerParameter parameters, final RequestCallback<T> callBack, String methodGet) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<String> params = parameters.keySet();
        String logInfo = "";
        Request.Builder formBody = new Request.Builder().url(url);
        for (String key : params) {
            if (key.equals("login")) {
                SummerParameter pa = PostData.getLoginParameters(context);
                Set<String> postdate = pa.keySet();
                for (String postKey : postdate) {

                    if (!TextUtils.isEmpty(postKey)) {
                        String value =  pa.get(postKey)+"";
                        if(STextUtils.isChineseStr(value)){
                            value = STextUtils.getPinYin(value);
                        }
                        formBody.addHeader(postKey,value + "");
                    }
                }
            } else {
                if (key.equals("requestType")) {
                    logInfo = (String) parameters.get(key);
                } else {
                    Object value = parameters.get(key);
                    if (value != null) {
                        requestBody.addFormDataPart(key, value + "");
                    }
                }
            }
        }
        if (Logs.isDebug) {
            parameters.encodeUrlAndLog(url);
        }
        formBody.post(requestBody.build()).header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", PostData.getUserAgent());
        final String finalLogInfo = logInfo;
        try {
            okHttpClient.newCall(formBody.build()).enqueue(new Callback() {

                @Override
                public void onResponse(Call arg0, Response arg1) throws IOException {
                    ResponseBody body = arg1.body();

                    if (body == null) {
                        return;
                    }
                    final String response = body.string();

                    SThread.getIntances().runOnUIThreadIfNeed(context, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (clazz == String.class) {
                                    @SuppressWarnings("unchecked")
                                    T t = (T) response;
                                    if (callBack != null) {
                                        callBack.done(t);
                                    }
                                    return;
                                }
                                T t = JSON.parseObject(response, clazz);
                                if (callBack != null) {
                                    callBack.done(t);
                                }
                            } catch (Exception e) {
                                Logs.i("e:::"+e.toString());
                                if (callBack != null) {
                                    callBack.onError(ErrorCode.INVALID_JSON, "无效的数据格式");
                                }
                                e.printStackTrace();
                            }
                        }
                    });
                    Logs.i("请求结果:" + finalLogInfo + response);
                }

                @Override
                public void onFailure(final Call arg0, final IOException arg1) {
                    SThread.getIntances().runOnUIThreadIfNeed(context, new Runnable() {
                        @Override
                        public void run() {
                            if (arg1 instanceof SocketTimeoutException) {
                                if (callBack != null) {
                                    callBack.onError(ErrorCode.ERR_TIMEOUT, "请求超时");
                                }
                            } else {
                                if (callBack != null) {
                                    callBack.onError(ErrorCode.ERR_OTHER, "其它错误" + arg1.getMessage() + ",,," + arg0.toString());
                                }
                                if (Logs.isDebug) {
                                    //SUtils.makeToast(context, "请求失败,请稍后重试!");
                                }
                            }
                        }
                    });
                }
            });
        } catch (OutOfMemoryError e) {
            callBack.onError(ErrorCode.ERR_LOWMEMORY, "内存不足");
            e.printStackTrace();
        }
    }

    /**
     * 处理GET
     *
     * @param context
     * @param url
     * @param clazz
     * @param parameters
     * @param callBack
     * @param methodGet
     * @param <T>
     */
    private static <T> void requestGet(final Context context, String url, final Class<T> clazz, final SummerParameter parameters, final RequestCallback<T> callBack, String methodGet) {
        GetBuilder utils = OkHttpUtils.get().url(url);
        Set<String> params = parameters.keySet();
        String logInfo = "";
        for (String key : params) {
            if (!key.equals("requestType")) {
                Object value = parameters.get(key);
                if (value != null) {
                    utils.addParams(key, value + "");
                }
            } else {
                logInfo = (String) parameters.get(key);
            }
        }
        if (Logs.isDebug) {
            parameters.encodeUrlAndLog(url);
        }
        final String finalLogInfo = logInfo;
        utils.addHeader("User-Agent", PostData.getUserAgent());
        utils.build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                Logs.i("hxq", "请求结果:" + finalLogInfo + response);
                if (clazz == String.class) {
                    @SuppressWarnings("unchecked")
                    T t = (T) response;
                    callBack.done(t);
                    return;
                }
                try {
                    T t = JSON.parseObject(response, clazz);
                    callBack.done(t);
                } catch (Exception e) {
                    callBack.onError(ErrorCode.INVALID_JSON, "无效的数据格式");
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1) {
                if (arg1 instanceof SocketTimeoutException) {
                    if (Logs.isDebug) {
                        // SUtils.makeToast(context, "请求超时");
                    }
                    callBack.onError(ErrorCode.ERR_TIMEOUT, "请求超时");
                } else {
                    callBack.onError(ErrorCode.ERR_OTHER, "请求错误" + arg1);
                    if (Logs.isDebug) {
                        //SUtils.makeToast(context, "请求失败,请稍后重试!");
                    }
                }
            }
        });
    }

    /**
     * 上传文件
     *
     * @param context
     * @param fileDirectory 上传到阿里云对应的文件位置与名称
     * @param fileType      文件类型
     * @param filePath      文件本地路径
     * @param listener
     */
    public static void upLoadFile(Context context, String fileDirectory, String fileType, String filePath, final OnResponseListener listener) {
        try {
            //setCoockies(context);
            MultipartBody.Builder builder = new MultipartBody.Builder();
            final String key = fileDirectory + "/" + System.nanoTime() + fileType;
            builder.addFormDataPart("key", key);
            builder.addFormDataPart("signature", PostData.ALI_SIGNATURE);
            builder.addFormDataPart("policy", PostData.ALI_POLICY);
            builder.addFormDataPart("OSSAccessKeyId", PostData.ALI_KEY);
            builder.addFormDataPart("success_action_status", "200");
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            File file = new File(filePath);
            byte[] sendDatas = null;
            if (fileType.endsWith(SFileUtils.FileType.FILE_MP4)) {
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
            } else {
                Bitmap lastBitmap = SUtils.createScaleBitmap(file.getPath(), 600, 600);
                sendDatas = SUtils.getBitmapArrays(lastBitmap);
            }
            if (sendDatas != null) {
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), sendDatas));
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(PostData.ALI_URL).post(body).build();

            //单独设置参数 比如读取超时时间
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            KeyStore s = null;
            trustManagerFactory.init(s);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            final Call call = mBuilder.sslSocketFactory(new TLSSocketFactory(), trustManager)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Logs.i("文件上传失败:" + e + "," + call);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        int code = response.code();
                        if (code == 200) {
                            listener.succeed(PostData.ALI_PRE + key);
                            return;
                        }
                    }
                    listener.failure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.failure();
            Logs.i("文件上传失败:" + e);
        }
    }

    private static void request(String url, final SummerParameter parameters, final RequestListener listener) {
        GetBuilder utils = OkHttpUtils.get().url(url);
        Set<String> params = parameters.keySet();
        for (String key : params) {
            utils.addParams(key, parameters.get(key) + "");
        }
        parameters.encodeUrl(url);
        utils.build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(Call arg0, Exception arg1) {
            }
        });
    }

    public static <T> void post(String url, final Class<T> clazz,
                                final SummerParameter parameters, final RequestCallback<T> callBack, String mothod) {
    }

    /**
     * 下载文件,使用URL作为ID
     *
     * @param url
     * @param path
     * @param fileName
     */
    public static void download(Context context, String url, String path, String fileName, DownloadTaskListener callBack) {
        DownloadManager manager = DownloadManager.getInstance(context);
        DownloadTask task = new DownloadTask(url, path, fileName);
        manager.addDownloadTask(task, callBack);
    }

    /**
     * 下载文件,使用给定ID
     *
     * @param url
     * @param path
     * @param fileName
     */
    public static void download(Context context, String id, String url, String path, String fileName, DownloadTaskListener callBack) {
        DownloadManager manager = DownloadManager.getInstance(context);
        DownloadTask task = new DownloadTask(id, url, path, fileName);
        manager.addDownloadTask(task, callBack);
    }

    /**
     * 检查是否在下载
     *
     * @param context
     * @param url
     */
    public static boolean existDownload(Context context, String url) {
        DownloadManager manager = DownloadManager.getInstance(context);
        if (manager.getCurrentTaskById(url) != null) {
            return true;
        }
        return false;
    }

    /**
     * 删除下载
     *
     * @param context
     * @param url
     */
    public static void deleteDownload(Context context, String url) {
        DownloadManager manager = DownloadManager.getInstance(context);
        if (manager.getCurrentTaskById(url) != null) {
            manager.cancel(url);
        }
    }

    /**
     * 暂停下载
     *
     * @param context
     * @param url
     */
    public static void pauseDownload(Context context, String url) {
        DownloadManager manager = DownloadManager.getInstance(context);
        if (manager.getCurrentTaskById(url) != null) {
            manager.pause(url);
        }
    }

    /**
     * 继续下载
     *
     * @param context
     * @param url
     */
    public static void resumeDownload(Context context, String url) {
        DownloadManager manager = DownloadManager.getInstance(context);
        if (manager.getCurrentTaskById(url) != null) {
            manager.resume(url);
        }
    }

    /**
     * 检查是否暂停,返回下载进度值
     */
    public static float checkPaused(Context context, String url) {
        DownloadManager manager = DownloadManager.getInstance(context);
        DownloadTask task = manager.getCurrentTaskById(url);
        if (task != null) {
            if (task.getPercent() != 0 && task.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_PAUSE) {
                return task.getPercent();
            }
        }
        return 0;
    }

}
