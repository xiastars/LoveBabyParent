package com.summer.helper.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by dell on 2017/6/13.
 */

public class PermissionUtils {
    /**
     * 检查有没有读取权限
     *
     * @param context
     * @return
     */
    public static boolean checkReadPermission(
            final Context context) {
        return checkPermmision(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static boolean checkWritePermission(
            final Context context) {
        return checkPermmision(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 检查有没有联系人读取权限
     *
     * @param context
     * @return
     */
    public static boolean checkPhonePermission(
            final Context context) {
        return checkPermmision(context, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 检查有没有相机权限
     *
     * @param context
     * @return
     */
    public static boolean checkCameraPermission(
            final Context context) {
        return checkPermmision(context, Manifest.permission.CAMERA);
    }

    /**
     * 检查有没有联系人读取权限
     *
     * @param context
     * @return
     */
    public static boolean checkRecordPermission(
            final Context context) {
        return checkPermmision(context, Manifest.permission.RECORD_AUDIO);
    }

    private static boolean checkPermmision(final Context context, final String permission) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        permission)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("请开启以下权限");
                    //alertBuilder.setMessage("读写权限");
                    alertBuilder.setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 123);
                                }
                            });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    Logs.i("xia","请求权限");
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 123);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
