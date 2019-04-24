package com.cnitr.cn.activity.tools;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.util.CommonUtil;

import butterknife.Bind;

/**
 * 信息显示页
 * Created by YangChen on 2018/11/1.
 */

public class MessageActivity extends BaseActivity {

    @Bind(R.id.tViewDate)
    AppCompatTextView tViewDate;

    @Bind(R.id.tViewMsg)
    AppCompatTextView tViewMsg;

    @Bind(R.id.btnOk)
    AppCompatButton btnOk;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_message;
    }

    @Override
    protected void init() {
        String message = getIntent().getStringExtra("message");
        String time = getIntent().getStringExtra("time");
        if (TextUtils.isEmpty(time)) {
            tViewDate.setText(CommonUtil.dateFormat(System.currentTimeMillis()));
        } else {
            tViewDate.setText(time);
        }
        if (message != null) {
            tViewMsg.setText(message);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });
    }


}
