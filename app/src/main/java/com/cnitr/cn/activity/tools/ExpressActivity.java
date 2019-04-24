package com.cnitr.cn.activity.tools;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.adapter.ExpressAdapter;
import com.cnitr.cn.entity.ExpressEntity;
import com.cnitr.cn.retrofit.BaseObserver;
import com.cnitr.cn.retrofit.HttpApiService;
import com.cnitr.cn.util.KeyboardUtils;
import com.cnitr.cn.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 快递查询
 * Created by YangChen on 2018/10/25.
 */

public class ExpressActivity extends BaseActivity {

    @Bind(R.id.keywords)
    ClearEditText keywords;

    @Bind(R.id.search)
    AppCompatImageButton search;

    @Bind(R.id.imageLogo)
    AppCompatImageView imageLogo;

    @Bind(R.id.layoutExpress)
    LinearLayout layoutExpress;

    @Bind(R.id.listview)
    ListView listview;

    @Bind(R.id.layoutExpressCompany)
    LinearLayout layoutExpressCompany;

    @Bind(R.id.tViewExpressCompany)
    AppCompatTextView tViewExpressCompany;

    @Bind(R.id.layoutEmpty)
    RelativeLayout layoutEmpty;

    private List<ExpressEntity.Express> data;
    private ExpressAdapter adapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                startImageLogoAnimation();
            } else if (msg.what == 2) {
                endImageLogoAnimation();
                layoutEmpty.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_express;
    }


    @Override
    protected void init() {

        data = new ArrayList<ExpressEntity.Express>();
        adapter = new ExpressAdapter(this, data);
        listview.setAdapter(adapter);

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

        keywords.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = keywords.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    if (imageLogo.getVisibility() == View.GONE) {
                        mHandler.sendEmptyMessage(2);
                    }
                    data.clear();
                    adapter.notifyDataSetChanged();
                    layoutExpressCompany.setVisibility(View.GONE);
                    tViewExpressCompany.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 开始动画效果
     */
    private void startImageLogoAnimation() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageLogo.setVisibility(View.GONE);
            }
        }, 50);

        ScaleAnimation imageAnimation = new ScaleAnimation(1, 0, 1, 0);
        ScaleAnimation layoutAnimation = new ScaleAnimation(0, 1, 0, 1);

        imageAnimation.setDuration(500);
        layoutAnimation.setDuration(500);

        imageLogo.startAnimation(imageAnimation);
        layoutExpress.startAnimation(layoutAnimation);
    }

    /**
     * 结束动画效果
     */
    private void endImageLogoAnimation() {

        ScaleAnimation imageAnimation = new ScaleAnimation(0, 1, 0, 1);
        ScaleAnimation layoutAnimation = new ScaleAnimation(0, 1, 0, 1);

        imageAnimation.setDuration(500);
        layoutAnimation.setDuration(500);

        imageLogo.startAnimation(imageAnimation);
        layoutExpress.startAnimation(layoutAnimation);

        imageLogo.setVisibility(View.VISIBLE);
    }

    /**
     * 搜索请求
     */
    private void searchRequest() {
        String keyword = keywords.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            if (imageLogo.getVisibility() == View.VISIBLE) {
                mHandler.sendEmptyMessage(1);
            }
            KeyboardUtils.hideSoftInput(this);

            HttpApiService.getInstance().getExpress(keyword,new BaseObserver<ExpressEntity>() {
                @Override
                protected void onSuccess(ExpressEntity result) {
                    mHandler.sendEmptyMessage(3);
                    data.clear();
                    if (result != null) {
                        String expressCompany = result.getExpTextName();
                        tViewExpressCompany.setText(expressCompany == null ? "" : expressCompany);
                        List<ExpressEntity.Express> list = result.getData();
                        if (list != null && !list.isEmpty()) {
                            layoutEmpty.setVisibility(View.GONE);
                            layoutExpressCompany.setVisibility(View.VISIBLE);
                            data.addAll(list);
                        } else {
                            layoutEmpty.setVisibility(View.VISIBLE);
                            layoutExpressCompany.setVisibility(View.GONE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                protected void onFail(int errorCode, String errorMsg) {
                    super.onFail(errorCode, errorMsg);
                    mHandler.sendEmptyMessage(3);
                    data.clear();
                    adapter.notifyDataSetChanged();
                    layoutEmpty.setVisibility(View.VISIBLE);
                    layoutExpressCompany.setVisibility(View.GONE);
                }
            });
        }
    }


}
