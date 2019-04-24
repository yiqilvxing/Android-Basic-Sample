package com.cnitr.cn.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cnitr.cn.Constant;
import com.cnitr.cn.MyApplication;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.JPushMessageActivity;
import com.cnitr.cn.activity.MainActivity;
import com.cnitr.cn.activity.WebViewActivity;
import com.cnitr.cn.adapter.GlideImageLoader;
import com.cnitr.cn.adapter.NewsAdapter;
import com.cnitr.cn.entity.BannerEntity;
import com.cnitr.cn.entity.WechatItem;
import com.cnitr.cn.retrofit.AbstractObserver;
import com.cnitr.cn.retrofit.HttpApiService;
import com.cnitr.cn.service.BannerDao;
import com.cnitr.cn.widget.MarqueenTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 新闻
 * Created by YangChen on 2018/7/4.
 */

public class NewsFragment extends BaseFragment {

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.banner)
    Banner banner;

    @Bind(R.id.appbar)
    AppBarLayout appbar;

    @Bind(R.id.tViewNotice)
    MarqueenTextView tViewNotice;

    private NewsAdapter adapter;
    private List<WechatItem.ResultBean.ListBean> data;
    private int currentPage = 1;
    private int type;

    public static NewsFragment newInstance(int type) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void init() {

        type = getArguments().getInt("type");

        // 初始化Banner
        if (type == 2) {
            initBanner();
        }

        // 初始化RecyclerView
        initRecylerView();

        tViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JPushMessageActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, getString(R.string.menu_message));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null && type == 2) {
            banner.startAutoPlay();
        }
    }

    /**
     * 初始化Banner
     */
    private void initBanner() {
        List<BannerEntity> bannerEntityList = BannerDao.getInstance().getAll();
        initBanner(bannerEntityList);

//        HttpApiService.getInstance().getBannerList(new BaseObserver<List<BannerEntity>>() {
//            @Override
//            protected void onSuccess(List<BannerEntity> result) {
//                if (result != null) {
//                    initBanner(result);
//                }
//            }
//        });
    }

    /**
     * 初始化Banner
     */
    private void initBanner(List<BannerEntity> bannerEntityList) {
        List<String> images = new ArrayList<String>();
        final List<String> titles = new ArrayList<String>();

        if (bannerEntityList != null && !bannerEntityList.isEmpty()) {
            for (BannerEntity entity : bannerEntityList) {
                images.add(entity.getCover());
                titles.add(entity.getUrl());
            }
        } else {
            // 默认Banner
            String[] imageArray = {"file:///android_asset/banner_1.png", "file:///android_asset/banner_2.jpg", "file:///android_asset/banner_3.jpg"};
            String[] titleArray = {"http://simplelife.streetvoice.cn", "https://www.qunar.com", "http://yuehui.163.com"};

            for (int i = 0; i < imageArray.length; i++) {
                images.add(imageArray[i]);
                titles.add(titleArray[i]);
            }
        }

        appbar.setVisibility(View.VISIBLE);

        banner.setVisibility(View.VISIBLE);

        // 设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        // 设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        // 设置图片集合
        banner.setImages(images);

        // 设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);

        // 设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);

        // 设置自动轮播，默认为true
        banner.isAutoPlay(true);

        // 设置轮播时间
        banner.setDelayTime(5000);

        // 设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);

        // banner设置方法全部调用完毕时最后调用
        banner.start();

        // 设置监听器
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String url = titles.get(position);
                if (url != null) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * 初始化RecyclerView
     */
    private void initRecylerView() {
        data = new ArrayList<WechatItem.ResultBean.ListBean>();
        adapter = new NewsAdapter(getActivity(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadMore(true);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                currentPage = 1;
                requesOfficeList(currentPage);

                // 初始化Banner
                if (type == 2) {
                    initBanner();
                }
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

        HttpApiService.getInstance().getWechat(MyApplication.MOB_APP_KEY, type, page, Constant.PAGE_SIZE, new AbstractObserver<WechatItem>() {
            @Override
            public void onNext(WechatItem result) {
                super.onNext(result);
                if (result != null) {
                    if (result.getResult() != null) {
                        List<WechatItem.ResultBean.ListBean> wechatItem = result.getResult().getList();
                        if (result != null) {
                            if (data == null) {
                                data = new ArrayList<WechatItem.ResultBean.ListBean>();
                            }
                            if (currentPage == 1) {
                                data.clear();
                            }
                            if (!wechatItem.isEmpty()) {
                                data.addAll(wechatItem);
                            }
                            if (type == 2 && currentPage == 1) {
                                MainActivity.hotNewsList = wechatItem;
                                String noticeTitle = "";
                                for (WechatItem.ResultBean.ListBean bean : data) {
                                    noticeTitle += bean.getTitle() + "。\t\t";
                                }
                                tViewNotice.setText(noticeTitle);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (wechatItem.size() >= Constant.PAGE_SIZE) {
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

    @Override
    public void onStop() {
        super.onStop();
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

}