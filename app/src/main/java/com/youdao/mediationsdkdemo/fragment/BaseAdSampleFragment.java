package com.youdao.mediationsdkdemo.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.youdao.mediationsdkdemo.R;
import com.youdao.mediationsdkdemo.util.CommonUtil;

/**
 * Created by lyy on 2019/7/18
 * 该类是demo中AdSampleFragment基础类，提供demo页面UI。其他广告示例页面都继承于该类，复用该类UI，重写loadAd和showAd逻辑
 */
public abstract class BaseAdSampleFragment extends BaseFragment {
    public static final String ARGS_MEDIATION_PID = "m_pid";

    protected final int LOAD = 0;//0表示等待加载
    protected final int LOADING = 1;//1表示正在加载
    protected final int SHOW = 2;//2表示加载完成，可以显示

    private int state = LOAD;
    protected String mMediationPId;

    private TextView mTipTv;
    private TextView mMediationPidTv;
    private TextView mLoadAndShowBtn;
    protected FrameLayout mNativeAdContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_ad_sample;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mMediationPId = getArguments().getString(ARGS_MEDIATION_PID);
        }
        if (TextUtils.isEmpty(mMediationPId)) {
            finish();
        }

        mNativeAdContainer = mRootView.findViewById(R.id.native_ad_container);

        mTipTv = mRootView.findViewById(R.id.load_tip_view);
        mTipTv.setText(getLoadTip());

        mMediationPidTv = mRootView.findViewById(R.id.mediation_pid_view);
        mMediationPidTv.setText("聚合广告位ID：" + mMediationPId);

        mLoadAndShowBtn = mRootView.findViewById(R.id.load_show_btn);
        mLoadAndShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isFastDoubleClick()) {//防止快速点击
                    return;
                }
                if (state == LOAD) {
                    updateBtn(LOADING);
                    loadAd();
                } else if (state == SHOW) {
                    showAd();
                    updateBtn(LOAD);
                } else {
                    CommonUtil.showToast(getActivity(), "加载中");
                }
            }
        });
    }

    protected void updateBtn(int newState) {
        state = newState;
        if (state == LOAD) {
            mLoadAndShowBtn.setText("加载广告");
        } else if (state == LOADING) {
            mLoadAndShowBtn.setText("加载广告中");
        } else {
            mLoadAndShowBtn.setText("显示广告");
        }
    }

    protected abstract String getLoadTip();

    protected abstract void loadAd();

    protected abstract void showAd();
}
