package com.cnitr.cn.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.cnitr.cn.R;
import com.cnitr.cn.widget.QHQCommentDialog;

import butterknife.Bind;

/**
 * 关于
 * Created by YangChen on 2018/10/27.
 */

public class AboutActivity extends BaseActivity {


    @Bind(R.id.version)
    AppCompatTextView version;

    private QHQCommentDialog mCommentDialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {

        try {
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;

            version.setText(versionName == null ? "" : getString(R.string.app_name) + "（" + versionName + ")");

            version.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 评论框Demo
                    // showCommentDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showCommentDialog() {
        if (mCommentDialog == null) {
            mCommentDialog = new QHQCommentDialog("请输入内容", new QHQCommentDialog.SendListener() {
                @Override
                public void sendComment(final String inputText) {

                }
            });
        }
        mCommentDialog.show(getSupportFragmentManager(), "comment");
    }

}
