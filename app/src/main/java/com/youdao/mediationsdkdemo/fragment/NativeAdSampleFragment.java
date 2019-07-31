package com.youdao.mediationsdkdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.duapps.ad.DuNativeAd;
import com.facebook.ads.MediaView;
import com.inmobi.ads.InMobiNative;
import com.youdao.admediationsdk.base.adload.YoudaoAdLoader;
import com.youdao.admediationsdk.base.adload.YoudaoAdRequestParams;
import com.youdao.admediationsdk.base.listener.YoudaoAdClickEventListener;
import com.youdao.admediationsdk.base.listener.YoudaoAdImpressionListener;
import com.youdao.admediationsdk.base.listener.YoudaoAdLoadListener;
import com.youdao.admediationsdk.logging.YoudaoLog;
import com.youdao.admediationsdk.nativead.YoudaoNativeAd;
import com.youdao.admediationsdk.thirdsdk.admob.AdmobNativeAd;
import com.youdao.admediationsdk.thirdsdk.admob.AdmobNativeAdRender;
import com.youdao.admediationsdk.thirdsdk.admob.AdmobViewBinder;
import com.youdao.admediationsdk.thirdsdk.baidu.BaiduNativeAd;
import com.youdao.admediationsdk.thirdsdk.baidu.BaiduNativeAdRender;
import com.youdao.admediationsdk.thirdsdk.baidu.BaiduViewBinder;
import com.youdao.admediationsdk.thirdsdk.facebook.FacebookNativeAd;
import com.youdao.admediationsdk.thirdsdk.facebook.FacebookNativeAdRender;
import com.youdao.admediationsdk.thirdsdk.facebook.FacebookViewBinder;
import com.youdao.admediationsdk.thirdsdk.inmobi.InMobiNativeAd;
import com.youdao.admediationsdk.thirdsdk.inmobi.InMobiNativeAdRender;
import com.youdao.admediationsdk.thirdsdk.inmobi.InMobiViewBinder;
import com.youdao.admediationsdk.thirdsdk.zhixuan.ZhixuanNativeAd;
import com.youdao.admediationsdk.thirdsdk.zhixuan.ZhixuanNativeAdRender;
import com.youdao.mediationsdkdemo.R;
import com.youdao.mediationsdkdemo.util.CommonUtil;
import com.youdao.mediationsdkdemo.util.FragmentUtil;
import com.youdao.sdk.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2019/3/18
 */
public class NativeAdSampleFragment extends BaseAdSampleFragment {
    private YoudaoAdLoader mAdLoader;
    private YoudaoNativeAd mAd;

    @Override
    protected String getLoadTip() {
        return "聚合原生广告加载示例";
    }

    @Override
    protected void loadAd() {
        if(mAdLoader != null){
            mAdLoader.destroy();
        }
        mAdLoader = new YoudaoAdLoader.Builder(getActivity(), mMediationPId)
                .withAdLoadListener(new YoudaoAdLoadListener() {
                    @Override
                    public void onAdLoaded(YoudaoNativeAd ad) {
                        YoudaoLog.d(mMediationPId + " onAdLoaded");
                        updateBtn(SHOW);
                        CommonUtil.showToast("加载成功");
                        mAd = ad;
                    }

                    @Override
                    public void onAdLoadFailed(int errorCode, String errorMessage) {
                        YoudaoLog.d(mMediationPId + " onAdLoadFailed");
                        updateBtn(LOAD);
                        CommonUtil.showToast("加载失败");
                    }
                })
                .withAdClickEventListener(new YoudaoAdClickEventListener() {
                    @Override
                    public void onAdClicked() {
                        YoudaoLog.d(mMediationPId + " onAdClicked");
                        CommonUtil.showToast("已点击");
                    }
                })
                .withAdImpressListener(new YoudaoAdImpressionListener() {
                    @Override
                    public void onAdImpressed() {
                        YoudaoLog.d(mMediationPId + " onAdImpressed");
                        CommonUtil.showToast("已展示");
                    }
                })
                .build();

        mAdLoader.loadAd(new YoudaoAdRequestParams.Builder().build());
    }

    @Override
    protected void showAd() {
        if (mAd instanceof ZhixuanNativeAd) {
            showZhixuanAd((ZhixuanNativeAd) mAd);
        } else if (mAd instanceof AdmobNativeAd) {
            showAdmobAd((AdmobNativeAd) mAd);
        } else if (mAd instanceof BaiduNativeAd) {
            showBaiduAd((BaiduNativeAd) mAd);
        } else if (mAd instanceof FacebookNativeAd) {
            showFacebookAd((FacebookNativeAd) mAd);
        } else if(mAd instanceof InMobiNativeAd){
            showInMobiAd((InMobiNativeAd) mAd);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdLoader != null) {
            mAdLoader.destroy();
            mAdLoader = null;
        }
    }

    private void showZhixuanAd(ZhixuanNativeAd ad) {
        ZhixuanNativeAdRender adRender = new ZhixuanNativeAdRender(
                new ViewBinder.Builder(R.layout.zhixuan_native_ad)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image).build(), false);
        View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

        mNativeAdContainer.removeAllViews();
        mNativeAdContainer.addView(adView);

        adRender.renderAdView(adView, ad);

        ad.recordImpression(adView);

        ad.registerViewForInteraction(adView);
    }

    private void showAdmobAd(AdmobNativeAd ad) {
        AdmobNativeAdRender adRender = new AdmobNativeAdRender(
                new AdmobViewBinder.Builder(R.layout.admob_ad_unified)
                        .setMediaViewId(R.id.ad_media)
                        .setHeadlineViewId(R.id.ad_headline)
                        .setBodyViewId(R.id.ad_body)
                        .setCallToActionViewId(R.id.ad_call_to_action)
                        .setIconViewId(R.id.ad_app_icon)
                        .setPriceViewId(R.id.ad_price)
                        .setStarRatingViewId(R.id.ad_stars)
                        .setStoreViewId(R.id.ad_store)
                        .setAdvertiserViewId(R.id.ad_advertiser)
                        .build()
        );
        View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

        mNativeAdContainer.removeAllViews();
        mNativeAdContainer.addView(adView);

        adRender.renderAdView(adView, ad);
    }

    private void showBaiduAd(BaiduNativeAd ad) {
        DuNativeAd nativeAd = ad.getNativeAd();
        String url = nativeAd.getImageUrl();
        BaiduNativeAdRender adRender;
        if (TextUtils.isEmpty(url)) {
            adRender = new BaiduNativeAdRender(
                    new BaiduViewBinder.Builder(R.layout.baidu_small_ad_card)
                            .setTitleViewId(R.id.small_card_name)
                            .setIconViewId(R.id.small_card_icon)
                            .setStarRatingViewId(R.id.small_card_rating)
                            .setShortDescViewId(R.id.small_card__des)
                            .setCallToActionViewId(R.id.small_card_btn)
                            .build()
            );
        } else {
            adRender = new BaiduNativeAdRender(
                    new BaiduViewBinder.Builder(R.layout.baidu_big_ad_card)
                            .setTitleViewId(R.id.card_name)
                            .setIconViewId(R.id.card_icon)
                            .setStarRatingViewId(R.id.card_rating)
                            .setShortDescViewId(R.id.card__des)
                            .setCallToActionViewId(R.id.card_btn)
                            .setImageViewId(R.id.card_image)
                            .build()
            );
        }
        View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

        mNativeAdContainer.removeAllViews();
        mNativeAdContainer.addView(adView);

        adRender.renderAdView(adView, ad);
        ad.registerViewForInteraction(adView);

        ad.recordImpression(mMediationPId);
    }

    private void showFacebookAd(FacebookNativeAd ad) {
        FacebookNativeAdRender adRender = new FacebookNativeAdRender(
                new FacebookViewBinder.Builder(R.layout.facebook_native_ad_unit)
                        .setAdChoiceContainerId(R.id.ad_choices_container)
                        .setBodyViewId(R.id.native_ad_body)
                        .setCallToActionViewId(R.id.native_ad_call_to_action)
                        .setIconViewId(R.id.native_ad_icon)
                        .setMediaViewViewId(R.id.native_ad_media)
                        .setSocialContextViewId(R.id.native_ad_social_context)
                        .setTitleViewId(R.id.native_ad_title)
                        .setSponsoredLabelViewId(R.id.native_ad_sponsored_label)
                        .build()
        );
        View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

        mNativeAdContainer.removeAllViews();
        mNativeAdContainer.addView(adView);

        adRender.renderAdView(adView, ad);

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(adView.findViewById(R.id.native_ad_title));
        clickableViews.add(adView.findViewById(R.id.native_ad_call_to_action));

        // Register the Title and CTA button to listen for clicks.
        ad.registerViewForInteraction(
                adView,
                (MediaView) adView.findViewById(R.id.native_ad_media),
                (MediaView) adView.findViewById(R.id.native_ad_icon),
                clickableViews);
    }

    private void showInMobiAd(final InMobiNativeAd ad){
        InMobiNativeAdRender adRender = new InMobiNativeAdRender(
                new InMobiViewBinder.Builder(R.layout.inmobi_native_ad)
                        .setCallToActionViewId(R.id.adAction)
                        .setDescriptionViewId(R.id.adDescription)
                        .setIconViewId(R.id.adIcon)
                        .setRatingViewId(R.id.adRating)
                        .setTitleViewId(R.id.adTitle)
                        .setPrimaryViewId(R.id.adContent)
                        .build()
        );
        View adView = adRender.createAdView(getActivity(), mNativeAdContainer);

        mNativeAdContainer.removeAllViews();
        mNativeAdContainer.addView(adView);

        adRender.renderAdView(adView, ad);

        adView.findViewById(R.id.adAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InMobiNative inMobiNative = ad.getNativeAd();
                inMobiNative.reportAdClickAndOpenLandingPage();
            }
        });
    }

    public static void open(FragmentActivity activity, String mediationPid) {
        if (TextUtils.isEmpty(mediationPid)) {
            YoudaoLog.d("mediationPid is empty");
        }
        Bundle args = new Bundle();
        args.putString(BaseAdSampleFragment.ARGS_MEDIATION_PID, mediationPid);
        FragmentUtil.addFragment(activity, NativeAdSampleFragment.class, android.R.id.content, args);
    }
}
