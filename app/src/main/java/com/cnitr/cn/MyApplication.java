//
//            Copyright © 2018 YangCheng.All Rights Reserved.
//
//                .-~~~~~~~~~-._       _.-~~~~~~~~~-.
//            __.'              ~.   .~              `.__
//          .'//                  \./                  \\`.
//        .'//                     |                     \\`.
//      .'// .-~"""""""~~~~-._     |     _,-~~~~"""""""~-. \\`.
//    .'//.-"                 `-.  |  .-'                 "-.\\`.
//  .'//______.============-..   \ | /   ..-============.______\\`.
//.'______________________________\|/______________________________`.
//
package com.cnitr.cn;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.cnitr.cn.greendao.dao.DaoMaster;
import com.cnitr.cn.greendao.dao.DaoSession;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;

import org.xutils.DbManager;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;

/**
 * Created by YangChen on 2018/7/5.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private static Context context;
    private static DaoSession daoSession;
    private static final String DB_NAME = "xapp";
    private static final String DB_GREEN_NAME = "GApp";
    private static final int DB_VERSION = 1;
    private static DbManager db;
    private static final String BUGLY_APP_ID = "8cafc7dd25";
    public static final String WX_APP_ID = "wx48e3043a5caa333a";
    public static final String WX_APP_SECRET = "b28164b870ec50a0cdf90fa6ce3898c6";
    public static final String HEFENG_USER_ID = "HE1810311540371985";
    public static final String HEFENG_WEATHER_KEY = "9613b23c4c824debbae67dcf8f54c875";
    public static final String MOB_APP_KEY = "2887ff488f000";
    public static final String JUHE_APP_KEY = "d5c756b3192afcfc18115ee7966f92ae";

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
        if (context == null) {
            context = this.getApplicationContext();
        }

//        if (!LeakCanary.isInAnalyzerProcess(this) && BuildConfig.DEBUG) {
//            LeakCanary.install(this);
//        }

        // 初始化上下拉刷新控件
        initSmartRefreshLayout(R.color.colorPrimaryDark);

        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_APP_ID, false);

        // 初始化xutils
        x.Ext.init(this);
        initDatabase();

        // GreenDao配置数据库
        setupDatabase();

        // 初始化和风天气
        HeConfig.init(HEFENG_USER_ID, HEFENG_WEATHER_KEY);
        HeConfig.switchToFreeServerNode();

        // 初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // 初始化Thinker
        initThinker();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public static DbManager getDb() {
        if (db == null) {
            db = initDatabase();
        }
        return db;
    }

    /**
     * GreenDao配置数据库
     */
    private static DaoSession setupDatabase() {

        // 创建数据库shop.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_GREEN_NAME);

        // 获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();

        // 获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);

        // 获取dao对象管理者
        daoSession = daoMaster.newSession();

        return daoSession;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = setupDatabase();
        }
        return daoSession;
    }

    /**
     * 初始化上下拉刷新控件
     */
    public void initSmartRefreshLayout(final int color) {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(color, R.color.white);
                return new ClassicsHeader(context).setTextSizeTitle(12f).setEnableLastTime(true).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    /**
     * 初始化数据库
     */
    private static DbManager initDatabase() {
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName(DB_NAME);
        config.setDbVersion(DB_VERSION);
        config.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {

            }
        });
        config.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                // TODO: ...
                // db.addColumn(...);
                // db.dropTable(...);
                // ...
                // or
                // db.dropDb();
            }
        });
        db = x.getDb(config);
        return db;
    }

    /**
     * 初始化Thinker
     */
    private void initThinker() {
        if (BuildConfig.TINKER_ENABLE) {
            // 初始化TinkerPatch SDK
            TinkerPatch.init(TinkerPatchApplicationLike.getTinkerPatchApplicationLike())
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true)
                    .setFetchPatchIntervalByHours(1);
            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
        }
    }


}
