package com.cnitr.cn.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * BaseFragment
 * Created by YangChen on 2018/7/4.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(getLayoutViewId(), container, false);
        ButterKnife.bind(this, layoutView);

        // 初始化
        init();

        return layoutView;
    }

    /**
     * 布局文件
     */
    protected abstract int getLayoutViewId();

    /**
     * 初始化
     */
    protected abstract void init();


    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
