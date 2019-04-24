package com.cnitr.cn.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnitr.cn.R;
import com.cnitr.cn.adapter.MenuAdapter;
import com.cnitr.cn.entity.MenuEntity;
import com.cnitr.cn.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 设置
 * Created by YangChen on 2018/7/23.
 */

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private MenuAdapter adapter;
    private List<MenuEntity> data;

    private static final int[] MENU_IMAGE = {R.mipmap.set_feedback, R.mipmap.set_about};

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_settings;
    }

    @Override
    protected void init() {
        final String[] MENU_NAME = getResources().getStringArray(R.array.arrays_mine_set);
        data = new ArrayList<MenuEntity>();
        for (int i = 0; i < MENU_NAME.length; i++) {
            MenuEntity entity = new MenuEntity();
            entity.setTitle(MENU_NAME[i]);
            entity.setIcon(MENU_IMAGE[i]);
            data.add(entity);
        }
        adapter = new MenuAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String title = MENU_NAME[position];
                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
                }
                if (position == 1) {
                    intent = new Intent(SettingsActivity.this, AboutActivity.class);
                }
                if (intent != null) {
                    intent.putExtra(Constant.INTENT_TITLE, title);
                    startActivity(intent);
                }
            }
        });

    }

}
