package com.cnitr.cn.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.AppCompatTextView;

import com.cnitr.cn.R;

import butterknife.Bind;

/**
 * 关于
 * Created by YangChen on 2018/10/27.
 */

public class AboutActivity extends BaseActivity {


    @Bind(R.id.version)
    AppCompatTextView version;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
