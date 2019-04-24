package com.cnitr.cn.activity.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cnitr.cn.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 新闻首页
 * Created by YangChen on 2018/10/22.
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.tab_layout)
    SmartTabLayout tab_layout;

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> data = new ArrayList<Fragment>();
    private String[] PAGE_TITLE;
    private int[] PAGE_TYPE;

    @Override
    protected int getLayoutViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {

        PAGE_TITLE = getResources().getStringArray(R.array.news_title);
        PAGE_TYPE = getResources().getIntArray(R.array.news_type);

        for (int i = 0; i < PAGE_TITLE.length; i++) {
            NewsFragment fragment = NewsFragment.newInstance(PAGE_TYPE[i]);
            data.add(fragment);
        }

        TabFragmentPagerAdapter fragmentPagerAdapter = new TabFragmentPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(fragmentPagerAdapter);
        tab_layout.setViewPager(viewpager);
        fragmentPagerAdapter.notifyDataSetChanged();
    }

    // FragmentPagerAdapter
    private class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLE[position];
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {

            return data.get(position);
        }

    }

}
