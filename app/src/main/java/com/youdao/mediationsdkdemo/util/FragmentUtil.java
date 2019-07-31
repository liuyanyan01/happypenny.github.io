package com.youdao.mediationsdkdemo.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by lyy on 2019/3/18
 */
public class FragmentUtil {
    public static Fragment addFragment(FragmentActivity activity,
                                       @NonNull Class<? extends Fragment> fragmentClass,
                                       int containerId,
                                       Bundle bundle) {
        Fragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            operateFragment(activity, fragment, containerId, bundle);
        }
        return fragment;
    }

    public static boolean popFragment(@NonNull FragmentActivity activity) {
        return activity.getSupportFragmentManager().popBackStackImmediate();
    }

    private static void operateFragment(@NonNull FragmentActivity activity,
                                        @NonNull Fragment destFragment,
                                        int containerId,
                                        Bundle bundle) {
        String name = destFragment.getClass().getName();
        destFragment.setArguments(bundle);

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.add(containerId, destFragment);
        ft.addToBackStack(name);

        ft.commitAllowingStateLoss();
    }
}
