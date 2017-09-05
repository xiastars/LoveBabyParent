package com.summer.helper.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 处理数据相关
 *
 * @author xiaqiliang
 */
public class SFileUtils {
    /**
     * 根目录
     */
    public static String ROOT_PATH = SUtils.getSDPath() + "/";
    /**
     * 资源根目录
     */
    public static String SOURCE_PATH = ROOT_PATH + "huoxingquan/";
    /**
     * 我的录音根目录
     */
    public static String ROOT_AUDIO = SOURCE_PATH + ".audio/";
    /**
     * 背景图根目录
     */
    public static String ROOT_BACKGROUND = SOURCE_PATH + ".pics/";
    /**
     * 图书目录
     */
    public static String ROOT_BOOK = SOURCE_PATH + ".books/";

    /**
     * 图书目录
     */
    public static String ROOT_FILE = SOURCE_PATH + ".file/";

    /**
     * 预览图根目录
     */
    public static String ROOT_AVATAR = SOURCE_PATH + "avatar/";


    private static String INTERVAL_CACHE = "INTERVAL_CACHE";

    /**
     * 资源后缀
     */
    public class FileType {
        public static final String FILE_MP4 = ".mp4";
        public static final String FILE_PNG = ".png";
        public static final String FILE_APK = ".apk";
    }

    /**
     * 安全获取目录
     *
     * @param path
     * @return
     */
    public static String getDirectorySafe(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getBookDirectory() {
        String path = ROOT_BOOK;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getFileDirectory() {
        String path = ROOT_FILE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getAudioDirectory() {
        String path = ROOT_AUDIO;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getAvatarDirectory() {
        String path = ROOT_AVATAR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getVideoDirectory() {
        String path = SOURCE_PATH + ".video/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getImageViewDirectory() {
        String path = ROOT_BACKGROUND;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 根据名称获取该名称下的图片集合
     *
     * @param name
     * @return
     */
    public static String[] getResourceByName(String name) {
        File file = new File(name);
        if (file.exists() && file.isDirectory()) {
            String[] files = file.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    if (!filename.endsWith(".png") && !filename.endsWith(".jpg")) {
                        return false;
                    }
                    return true;
                }
            });
            for (int i = 0; i < files.length; i++) {
                files[i] = file.getAbsolutePath() + "/" + files[i];
            }
            return files;
        }
        return null;
    }

    /***
     * 定时清理,三天清理一次
     */
    public static void initCache(Context context) {
        long time = SUtils.getLongData(context, INTERVAL_CACHE);
        int day = 1000 * 60 * 60 * 24 * 3;
        if (time == 0) {
            SUtils.saveLongData(context, INTERVAL_CACHE, System.currentTimeMillis());
            return;
        }
        if (System.currentTimeMillis() - time < day) {
            Logs.i("xia", "未到清理时间");
            return;
        }
        SUtils.saveLongData(context, INTERVAL_CACHE, System.currentTimeMillis());
        File file = new File(SOURCE_PATH);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            deleteFile(file);
        }
    }

    public static void deleteFile(String currentInputVideoPath) {
        File file = new File(currentInputVideoPath);
        if (!file.exists()) {
            return;
        }
        file.delete();
    }

    private static void deleteFile(File file) {
        int day = 1000 * 60 * 60 * 24 * 7;
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                long createTime = f.lastModified();
                Logs.i("xia", SUtils.getTimeLimitM(createTime));
                if (System.currentTimeMillis() - createTime > day) {
                    f.delete();
                    Logs.i("xia", "删除文件" + f.exists());
                }
            }
        }
    }

    /**
     * 将Assets里的文件拷贝到SD卡
     * @param context
     * @param fileNameFromAssets
     * @param outputFileName
     * @return
     */
    public static boolean copyBinaryFromAssetsToData(Context context, String fileNameFromAssets, String outputFileName) {

        InputStream is;
        try {
            is = context.getAssets().open(fileNameFromAssets);
            // copy ffmpeg file from assets to files dir
            final FileOutputStream os = new FileOutputStream(new File(outputFileName));
            byte[] buffer = new byte[1024 * 4];

            int n;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
            }

            if (os != null) {
                os.close();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }

            return true;
        } catch (IOException e) {
            Logs.i("issue in coping binary from assets to data. ", e.toString());
        }
        return false;
    }

}
