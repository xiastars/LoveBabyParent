package com.summer.helper.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by xiaqiliang on 2017/5/25.
 */

public class DiagonalRotateAnimation extends Animation {

    private float mFromDegrees = 0f;
    private float mToDegrees = 90f;
    private int mCenterWidth, mCenterHeight;

    private Camera mCamera = new Camera();
    private Matrix matrix2 = new Matrix();
    private Matrix matrix3 = new Matrix();

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        // 设置默认时长
        setDuration(500);
        // 保持动画的结束状态
        setFillAfter(false);
        // 设置默认插值器
        setInterpolator(new LinearInterpolator());
        mCenterWidth = width / 2;
        mCenterHeight = height /2;

        matrix3.postRotate(-45);//顺时针旋转45度
    }

    public void setDegrees(float fromDegrees,float toDegrees){
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        Matrix matrix = t.getMatrix();
        float deg = mFromDegrees+interpolatedTime * mToDegrees;

        mCamera.save();
        mCamera.rotateX(deg);
        mCamera.getMatrix(matrix);
        mCamera.restore();

        matrix2.postRotate(45);//逆时针旋转45度
        matrix2.setConcat(matrix, matrix2);//图逆时针旋转45度和X轴旋转相加
        matrix2.setConcat(matrix3, matrix2);//在之前的基础上图和X轴都顺时针旋转45度

        matrix.set(matrix2);
        matrix2.reset();

        // 通过pre方法设置矩阵作用前的偏移量来改变旋转中心
        matrix.preTranslate(-mCenterWidth, -mCenterHeight);// 在旋转之前开始位移动画
        matrix.postTranslate(mCenterWidth, mCenterHeight);// 在旋转之后开始位移动画
    }
}
