package com.cnitr.cn.activity.tools.city;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnitr.cn.MyApplication;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.entity.HeCitySearchEntity;
import com.cnitr.cn.retrofit.AbstractObserver;
import com.cnitr.cn.retrofit.HttpApiService;
import com.cnitr.cn.util.KeyboardUtils;
import com.cnitr.cn.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by YangChen on 2018/11/1.
 */

public class CitySearchActivity extends BaseActivity {

    @Bind(R.id.keywords)
    ClearEditText keywords;

    @Bind(R.id.search)
    AppCompatImageButton search;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<HeCitySearchEntity.HeCityBase.Basic> data;
    private CityAdapter adapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_city_search;
    }

    @Override
    protected void init() {
        data = new ArrayList<>();
        adapter = new CityAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRequest();
            }
        });

        keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchRequest();
                    return true;
                }
                return false;
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                HeCitySearchEntity.HeCityBase.Basic basic = data.get(position);
                if (basic != null) {
                    String city = basic.getLocation();
                    Intent intent = new Intent();
                    intent.putExtra("city", city);
                    setResult(RESULT_OK, intent);
                    CitySearchActivity.this.finish();
                }
            }
        });

    }

    /**
     * 查询城市
     */
    private void searchRequest() {
        String keyword = keywords.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            KeyboardUtils.hideSoftInput(keywords);
        }
        HttpApiService.getInstance().getHeCity(MyApplication.HEFENG_WEATHER_KEY, keyword, new AbstractObserver<HeCitySearchEntity>() {
            @Override
            public void onNext(HeCitySearchEntity result) {
                super.onNext(result);
                if (data == null) {
                    data = new ArrayList<>();
                }
                try {
                    if (result != null) {
                        List<HeCitySearchEntity.HeCityBase> heCityBaseList = result.getHeWeather6();
                        if (heCityBaseList != null && !heCityBaseList.isEmpty()) {
                            HeCitySearchEntity.HeCityBase heCityBase = heCityBaseList.get(0);
                            if (heCityBase != null) {
                                List<HeCitySearchEntity.HeCityBase.Basic> basicList = heCityBase.getBasic();
                                data.clear();
                                data.addAll(basicList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
