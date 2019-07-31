package com.youdao.mediationsdkdemo.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.youdao.mediationsdkdemo.util.FragmentUtil;

/**
 * Created by lyy on 2019/3/18
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initView();
        return mRootView;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void finish(){
        if(getActivity() != null){
            FragmentUtil.popFragment(getActivity());
        }
    }
}
