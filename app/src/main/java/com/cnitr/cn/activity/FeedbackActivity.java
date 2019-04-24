package com.cnitr.cn.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cnitr.cn.R;
import com.cnitr.cn.util.CommonUtil;

import butterknife.Bind;

/**
 * 意见反馈
 * Created by YangChen on 2018/10/27.
 */

public class FeedbackActivity extends BaseActivity {


    @Bind(R.id.editText)
    AppCompatEditText editText;

    @Bind(R.id.btnOk)
    AppCompatButton btnOk;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {

        // 设置自动换行
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setGravity(Gravity.TOP);
        editText.setSingleLine(false);
        editText.setHorizontallyScrolling(false);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String descs = editText.getText().toString().trim();
                if (TextUtils.isEmpty(descs)) {
                    CommonUtil.showActionMessage("请输入您的意见或反馈", v);
                    return;
                }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FeedbackActivity.this);
                mBuilder.setTitle("是否确认提交？");
                mBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(FeedbackActivity.this, "感谢您意见反馈", Toast.LENGTH_SHORT).show();
                        FeedbackActivity.this.finish();
                    }
                });
                mBuilder.setNegativeButton(android.R.string.no, null);
                mBuilder.show();
            }
        });

    }

}
