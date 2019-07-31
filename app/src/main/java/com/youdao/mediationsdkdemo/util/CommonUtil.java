package com.youdao.mediationsdkdemo.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.youdao.admediationsdk.YoudaoMediationSdk;

/**
 * Created by lyy on 2019/3/18
 */
public class CommonUtil {
    private static long sLastClickTime;
    private static final int INTERVAL = 300;//300毫秒

    public static boolean isFastDoubleClick() {
        if (System.currentTimeMillis() - sLastClickTime < INTERVAL) {
            return true;
        } else {
            sLastClickTime = System.currentTimeMillis();
            return false;
        }
    }

    public static void showToast(Context context, String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void showToast(String text) {
        showToast(YoudaoMediationSdk.getApplicationContext(), text);
    }
}
