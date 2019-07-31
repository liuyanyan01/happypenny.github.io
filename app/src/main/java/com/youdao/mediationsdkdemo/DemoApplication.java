package com.youdao.mediationsdkdemo;

import android.app.Application;

import com.youdao.admediationsdk.YoudaoMediationSdk;
import com.youdao.admediationsdk.config.manager.ConfigHelper;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YouDaoOptions;

/**
 * Created by lyy on 2019/1/23
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        YouDaoAd.getYouDaoOptions().setYoudaoAdServer(YouDaoOptions.YoudaoAdServer.SERVER_TEST1);
        String defaultConfig = ConfigHelper.getConfigJsonStr(this, "default_config_test_a.json");
        YoudaoMediationSdk.setLaunchChannel("TEST");
        YoudaoMediationSdk.initialize(this, defaultConfig);
    }
}
