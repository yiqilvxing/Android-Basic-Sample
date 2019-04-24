package com.cnitr.cn.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cnitr.cn.R;
import com.cnitr.cn.adapter.MyBookmarksAdapter;
import com.cnitr.cn.entity.CommonWebEntity;
import com.cnitr.cn.service.BookmarkDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的书签
 * Created by YangChen on 2018/7/10.
 */

public class MyBookmarksActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.no_data)
    View no_data;

    private MyBookmarksAdapter adapter;
    private List<CommonWebEntity> data;

    public static final String ACTION_MYBOOKMARKSACTIVITY = "MyBookmarksActivity";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_MYBOOKMARKSACTIVITY)) {
                refreshNoDataView();
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void init() {
        data = new ArrayList<CommonWebEntity>();
        List<CommonWebEntity> bookmarkEntityList = BookmarkDao.getInstance().getBookmarkList();
        if (bookmarkEntityList != null) {
            data.addAll(bookmarkEntityList);
        }
        adapter = new MyBookmarksAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshNoDataView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_MYBOOKMARKSACTIVITY);
        registerReceiver(mBroadcastReceiver, filter);
    }

    // refreshNoDataView
    private void refreshNoDataView() {
        if (data.isEmpty()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

}
