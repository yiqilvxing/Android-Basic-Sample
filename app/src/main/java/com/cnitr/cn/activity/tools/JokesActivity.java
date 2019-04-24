package com.cnitr.cn.activity.tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cnitr.cn.Constant;
import com.cnitr.cn.MyApplication;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.adapter.JokesAdapter;
import com.cnitr.cn.entity.JokesEntity;
import com.cnitr.cn.retrofit.AbstractObserver;
import com.cnitr.cn.retrofit.HttpApiService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 冷笑话
 * Created by YangChen on 2018/11/1.
 */

public class JokesActivity extends BaseActivity {

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private JokesAdapter adapter;
    private List<JokesEntity.JokesBaseEntity.Jokes> data;
    private int currentPage = 1;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_jokes;
    }

    @Override
    protected void init() {
        initRecylerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecylerView() {
        data = new ArrayList<JokesEntity.JokesBaseEntity.Jokes>();
        adapter = new JokesAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadMore(true);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                currentPage = 1;
                requesOfficeList(currentPage);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                requesOfficeList(currentPage);
            }

        });

        // 请求第一页数据
        currentPage = 1;
        requesOfficeList(currentPage);
    }

    /**
     * 刷新数据
     *
     * @param page
     */
    private void requesOfficeList(int page) {

        HttpApiService.getInstance().getJokes(MyApplication.JUHE_APP_KEY, page, Constant.PAGE_SIZE, new AbstractObserver<JokesEntity>() {
            @Override
            public void onNext(JokesEntity result) {
                super.onNext(result);
                if (result != null) {
                    if (result.getResult() != null) {
                        List<JokesEntity.JokesBaseEntity.Jokes> jokesList = result.getResult().getData();
                        if (jokesList != null) {
                            if (data == null) {
                                data = new ArrayList<JokesEntity.JokesBaseEntity.Jokes>();
                            }
                            if (currentPage == 1) {
                                data.clear();
                            }
                            if (!jokesList.isEmpty()) {
                                data.addAll(jokesList);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (jokesList.size() >= Constant.PAGE_SIZE) {
                            refreshLayout.setEnableLoadMore(true);
                        } else {
                            refreshLayout.setEnableLoadMore(false);
                        }
                    }
                }
                if (currentPage == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }


}
