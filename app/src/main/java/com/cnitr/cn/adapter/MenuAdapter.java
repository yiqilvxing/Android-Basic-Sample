package com.cnitr.cn.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnitr.cn.R;
import com.cnitr.cn.entity.MenuEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class MenuAdapter extends BaseQuickAdapter<MenuEntity, BaseViewHolder> {

    public MenuAdapter(List<MenuEntity> data) {
        super(R.layout.item_menu, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuEntity item) {
        helper.setText(R.id.title, item.getTitle());
        helper.setImageResource(R.id.icon, item.getIcon());
    }

}
