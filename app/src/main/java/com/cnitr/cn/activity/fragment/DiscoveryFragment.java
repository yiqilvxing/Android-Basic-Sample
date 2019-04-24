package com.cnitr.cn.activity.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;

import com.cnitr.cn.R;
import com.cnitr.cn.adapter.DiscoveryAdapter;
import com.cnitr.cn.entity.DiscoveryEntity;
import com.cnitr.cn.retrofit.AbstractObserver;
import com.cnitr.cn.retrofit.HttpApiService;
import com.cnitr.cn.util.CommonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import io.reactivex.annotations.NonNull;

/**
 * 小蜜
 * Created by YangChen on 2018/7/5.
 */

public class DiscoveryFragment extends BaseFragment {

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private DiscoveryAdapter adapter;
    private List<DiscoveryEntity> data;
    private int currentPage = 1;

    private int imageWidth;

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void init() {
        data = new ArrayList<DiscoveryEntity>();

        // 图片宽度
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        imageWidth = (dm.widthPixels - CommonUtil.dp2px(getActivity(), 6)) / 2;

        // 两列瀑布流效果
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter=new DiscoveryAdapter(getActivity(),data);
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
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                currentPage++;
                requesOfficeList(currentPage);
            }
        });

        // 开始刷新第一页数据
        refreshLayout.autoRefresh();
    }

    /**
     * 刷新数据
     *
     * @param page
     */
    private void requesOfficeList(int page) {
        currentPage = page;
        HttpApiService.getInstance().getDiscoveryList(currentPage, new AbstractObserver<DiscoveryEntity>() {
            @Override
            public void onNext(DiscoveryEntity result) {
                super.onNext(result);
                if (data == null) {
                    data = new ArrayList<DiscoveryEntity>();
                }
                if (result != null) {
                    List<DiscoveryEntity> discoveryEntityList = result.getResults();
                    if (discoveryEntityList != null) {
                        if (currentPage == 1) {
                            data.clear();
                        }
                        data.addAll(setImageWidthHeight(discoveryEntityList));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onComplete() {
                if (currentPage == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    // 随机设置图片高度，宽度（根据屏幕大小而定）
    private List<DiscoveryEntity> setImageWidthHeight(List<DiscoveryEntity> data) {
        List<DiscoveryEntity> result = new ArrayList<>();
        for (DiscoveryEntity entity : data) {
            int height = new Random().nextInt(CommonUtil.dp2px(getActivity(), 50)) + CommonUtil.dp2px(getActivity(), 200);
            entity.setWidth(imageWidth);
//            entity.setHeight(height);
            entity.setHeight(imageWidth * 9 / 6);
            result.add(entity);
        }
        return result;
    }


}
