package com.gmx.stepview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author :  MingXing
 * Email : gemingxing12345@163.com
 * Date : 16/7/3 21:24
 * Description :
 */
public class Orientation {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;


    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationChecker {

    }

}
