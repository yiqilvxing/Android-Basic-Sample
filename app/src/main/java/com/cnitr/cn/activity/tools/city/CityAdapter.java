package com.cnitr.cn.activity.tools.city;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnitr.cn.R;
import com.cnitr.cn.entity.HeCitySearchEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/7/4.
 */

public class CityAdapter extends BaseQuickAdapter<HeCitySearchEntity.HeCityBase.Basic, BaseViewHolder> {

    public CityAdapter(List<HeCitySearchEntity.HeCityBase.Basic> data) {
        super(R.layout.item_city, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HeCitySearchEntity.HeCityBase.Basic item) {
        helper.setText(R.id.title, item.getLocation());
    }

}
