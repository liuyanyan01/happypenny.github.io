package com.youdao.mediationsdkdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.youdao.admediationsdk.interstitialAd.YoudaoInterstitialAd;
import com.youdao.admediationsdk.interstitialAd.YoudaoInterstitialAdListener;
import com.youdao.admediationsdk.interstitialAd.YoudaoInterstitialParameter;
import com.youdao.admediationsdk.logging.YoudaoLog;
import com.youdao.mediationsdkdemo.util.CommonUtil;
import com.youdao.mediationsdkdemo.util.FragmentUtil;

/**
 * Created by lyy on 2019/7/18
 */
public class InterstitialAdSampleFragment extends BaseAdSampleFragment {
    private YoudaoInterstitialAd mInterstitialAd;

    @Override
    protected String getLoadTip() {
        return "聚合插屏广告加载示例";
    }

    @Override
    protected void loadAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }
        mInterstitialAd = new YoudaoInterstitialAd();
        mInterstitialAd.setInterstitialParameter(new YoudaoInterstitialParameter.Builder()
                .setZhixuanParameter(YoudaoInterstitialParameter.ZHIXUAN_NORMAL)
                .isBaiduAdFullScreen(true)//不设置默认为false。true表示加载百度插屏全屏广告；false表示加载百度插屏半屏广告
                .withAdLoadTimeout(10000)//设置超时时间，如果超时时间范围内没有广告返回，则回调失败给用户。默认超时时间为15s
                .build());
        mInterstitialAd.loadAds(
                getActivity(), mMediationPId, new

                        YoudaoInterstitialAdListener() {
                            @Override
                            public void onInterstitialLoaded() {
                                YoudaoLog.d(mMediationPId + " onInterstitialLoaded");
                                updateBtn(SHOW);
                                CommonUtil.showToast("加载成功");
                            }

                            @Override
                            public void onInterstitialFailed(int errorCode, String errorMessage) {
                                YoudaoLog.d(mMediationPId + " onInterstitialFailed errorCode = " + errorCode + ", errorMessage = " + errorMessage);
                                updateBtn(LOAD);
                                CommonUtil.showToast("加载失败");
                            }

                            @Override
                            public void onInterstitialShown() {
                                YoudaoLog.d(mMediationPId + " onInterstitialShown");
                                CommonUtil.showToast("已显示");
                            }

                            @Override
                            public void onInterstitialClicked() {
                                YoudaoLog.d(mMediationPId + " onInterstitialClicked");
                                CommonUtil.showToast("已点击");
                            }

                            @Override
                            public void onInterstitialDismissed() {
                                YoudaoLog.d(mMediationPId + " onInterstitialDismissed");
                                updateBtn(LOAD);
                                CommonUtil.showToast("已关闭");
                            }
                        });
    }

    @Override
    protected void showAd() {
        mInterstitialAd.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mInterstitialAd != null){
            mInterstitialAd.destroy();
            mInterstitialAd = null;
        }
    }

    public static void open(FragmentActivity activity, String mediationPid) {
        if (TextUtils.isEmpty(mediationPid)) {
            YoudaoLog.d("mediationPid is empty");
        }
        Bundle args = new Bundle();
        args.putString(BaseAdSampleFragment.ARGS_MEDIATION_PID, mediationPid);
        FragmentUtil.addFragment(activity, InterstitialAdSampleFragment.class, android.R.id.content, args);
    }
}
