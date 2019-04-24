package com.cnitr.cn.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cnitr.cn.Constant;
import com.cnitr.cn.R;

import butterknife.ButterKnife;

/**
 * BaseActivity
 * Created by YangChen on 2018/7/4.
 */

public abstract class BaseActivity extends SwipeBackBaseActivity {

    // 导航栏
    protected Toolbar toolbar;

    // 子类布局
    protected LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(Constant.SYSTEM_CODE, Context.MODE_PRIVATE);
        this.setContentView(R.layout.activity_base);

        // 初始化导航栏
        initActionBar();

        // 绑定视图组件
        ButterKnife.bind(this);

        // 初始化
        init();
    }

    /**
     * 初始化导航栏
     */
    protected void initActionBar() {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = getIntent().getStringExtra(Constant.INTENT_TITLE);
        if (TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            getSupportActionBar().setTitle(title);
        }

        layoutContent = (LinearLayout) this.findViewById(R.id.layoutContent);
        layoutContent.addView(LayoutInflater.from(this).inflate(getLayoutResID(), null));
    }

    /**
     * 布局文件ID
     */
    protected abstract int getLayoutResID();

    /**
     * 初始化
     */
    protected abstract void init();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
