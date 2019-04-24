package com.cnitr.cn.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cnitr.cn.activity.MainActivity;
import com.cnitr.cn.entity.BannerEntity;
import com.cnitr.cn.entity.BannerPushEntity;
import com.cnitr.cn.entity.MessageEntity;
import com.cnitr.cn.service.BannerDao;
import com.cnitr.cn.service.MessageDao;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by YangChen on 2018/11/2.
 */

public class MyJPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyJPushReceiver";

    // 推送Banner
    private static final String PUSH_BANNER = "@Banner:";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                Log.d(TAG, "JPush 用户注册成功");
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的自定义消息");
                String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                savePushMessage(msg);
                NotificationHelper.show(context, "收到一条新消息", msg);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的通知");
                String msg = bundle.getString(JPushInterface.EXTRA_ALERT);
                savePushMessage(msg);
                NotificationHelper.show(context, "收到一条新消息", msg);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "用户点击打开了通知");
                openNotification(context, bundle);
            } else {
                Log.d(TAG, "Unhandled intent - " + intent.getAction());
            }
        }
    }

    /**
     * 保存推送消息
     *
     * @param msg
     */
    private void savePushMessage(String msg) {

        if (msg != null) {
            if (msg.startsWith(PUSH_BANNER)) {
                try {
                    String json = msg.substring(PUSH_BANNER.length(), msg.length());
                    BannerPushEntity pushEntity = JSON.parseObject(json, BannerPushEntity.class);
                    if (pushEntity != null) {
                        List<BannerEntity> list = pushEntity.getBannerList();
                        if (list != null) {
                            BannerDao.getInstance().deleteAll();
                            for (BannerEntity entity : list) {
                                BannerDao.getInstance().save(entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                MessageEntity entity = new MessageEntity();
                entity.setMessage(msg);
                entity.setTime(System.currentTimeMillis());
                MessageDao.getInstance().add(entity);
            }
        }

    }


    /**
     * 用户打开了通知
     *
     * @param context
     * @param bundle
     */
    private void openNotification(Context context, Bundle bundle) {
        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.putExtra("SYSTEM_MESSAGE", true);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }

}