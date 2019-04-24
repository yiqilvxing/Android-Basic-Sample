package com.cnitr.cn.wxapi;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.UserEntity;
import com.cnitr.cn.retrofit.AbstractObserver;
import com.cnitr.cn.retrofit.HttpApiService;
import com.cnitr.cn.service.UserDao;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by YangChen on 2018/7/12.
 */

public class WXLoginManager {

    private static WXLoginManager instance;
    private IWXAPI api;

    private WXLoginManager() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(MyApplication.getContext(), MyApplication.WX_APP_ID);
            api.registerApp(MyApplication.WX_APP_ID);
        }
    }

    public IWXAPI getApi() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(MyApplication.getContext(), MyApplication.WX_APP_ID);
            api.registerApp(MyApplication.WX_APP_ID);
        }
        return api;
    }

    public static WXLoginManager getInstance() {
        synchronized (WXLoginManager.class) {
            if (instance == null) {
                instance = new WXLoginManager();
            }
        }
        return instance;
    }

    /**
     * 微信登录
     */
    public void wxLogin() {
        if (isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "xiaomi_wx_login";

            // 向微信发送请求
            getApi().sendReq(req);
        }
    }

    /**
     * 判断是否安装微信
     */
    private boolean isWXAppInstalled() {
        boolean resutl = false;
        final PackageManager packageManager = MyApplication.getContext().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    resutl = true;
                }
            }
        }
        return resutl;
    }

    /**
     * 获取微信登录信息
     */
    public void getWXLoginResult(String code) {

        String appid = MyApplication.WX_APP_ID;
        String secret = MyApplication.WX_APP_SECRET;
        String grant_type = "authorization_code";

        HttpApiService.getInstance().getWXLoginResult(appid, secret, code, grant_type, new AbstractObserver<WXAccessTokenBean>() {
            @Override
            public void onNext(WXAccessTokenBean result) {
                super.onNext(result);
                if (result != null) {
                    String access_token = result.getAccess_token(); //接口调用凭证
                    String openid = result.getOpenid(); //授权用户唯一标识

                    //当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
                    String unionid = result.getUnionid();

                    if (access_token != null && openid != null) {
                        getWXUserInfo(access_token, openid);
                    }
                }
            }
        });

    }

    /**
     * 获取微信用户信息
     */
    private void getWXUserInfo(String access_token, String openid) {

        HttpApiService.getInstance().getWXUserInfo(access_token, openid, new AbstractObserver<WXUserInfoBean>() {
            @Override
            public void onNext(WXUserInfoBean result) {
                super.onNext(result);
                if (result != null) {
                    String country = result.getCountry(); //国家
                    String province = result.getProvince(); //省
                    String city = result.getCity(); //市
                    String nickname = result.getNickname(); //用户名
                    int sex = result.getSex(); //性别
                    String headimgurl = result.getHeadimgurl(); //头像url

                    UserEntity entity = UserDao.getInstance().getUser();
                    entity.setUsername(nickname);
                    entity.setHeadimgurl(headimgurl);
                    entity.setAddress(province + " " + city);
                    UserDao.getInstance().saveUser(entity);
                }
            }
        });
    }

}
