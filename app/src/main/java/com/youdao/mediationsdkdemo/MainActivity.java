package com.youdao.mediationsdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.youdao.mediationsdkdemo.fragment.InterstitialAdSampleFragment;
import com.youdao.mediationsdkdemo.fragment.NativeAdSampleFragment;

/**
 * Created by lyy on 2019/3/18
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_btn_native).setOnClickListener(this);
        findViewById(R.id.load_btn_interstitial).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_btn_native:
                NativeAdSampleFragment.open(this, "f50ea4b479bc58dad3bd6c92bff72145");
                break;
            case R.id.load_btn_interstitial:
                InterstitialAdSampleFragment.open(this, "2bc9a354762340bae25a117a89c6ba88");
                break;
            default:
                break;
        }
    }
}
