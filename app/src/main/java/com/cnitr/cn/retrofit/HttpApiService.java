package com.cnitr.cn.retrofit;

import com.cnitr.cn.Constant;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangChen on 2018/11/20.
 */

public final class HttpApiService {

    private static HttpApiService instance;

    public static HttpApiService getInstance() {
        if (instance == null) {
            instance = new HttpApiService();
        }
        return instance;
    }

    private HttpApiService() {

    }

    /**
     * 封装线程管理和订阅的过程
     */
    private void apiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    /**
     * 获取Banner
     *
     * @param observer
     */
    public void getBannerList(Observer observer) {
        apiSubscribe(RetrofitService.create().getBannerList(), observer);
    }

    /**
     * 获取News
     *
     * @param offset   页码
     * @param limit    大小
     * @param observer
     */
    public void getNewsList(int offset, int limit, Observer observer) {
        apiSubscribe(RetrofitService.create().getNewsList(offset, limit), observer);
    }

    /**
     * 获取SmartCustom
     *
     * @param observer
     */
    public void getSmartCustom(Observer observer) {
        apiSubscribe(RetrofitService.create().getSmartCustom(), observer);
    }

    /**
     * 快递查询
     *
     * @param nu       单号
     * @param observer
     */
    public void getExpress(String nu, Observer observer) {
        apiSubscribe(RetrofitService.create().getExpress(nu), observer);
    }

    /**
     * 我的小蜜
     *
     * @param page     页码
     * @param observer
     */
    public void getDiscoveryList(int page, Observer observer) {
        apiSubscribe(RetrofitService.create(Constant.HTTP_API_GANK).getDiscoveryList(page), observer);
    }

    /**
     * 新闻列表
     *
     * @param key      key
     * @param cid      ID
     * @param page     页码
     * @param size     大小
     * @param observer
     */
    public void getWechat(String key, int cid, int page, int size, Observer observer) {
        apiSubscribe(RetrofitService.create(Constant.HTTP_API_MOB).getWechat(key, cid, page, size), observer);
    }

    /**
     * 城市搜索
     *
     * @param key      key
     * @param location 名称
     * @param observer
     */
    public void getHeCity(String key, String location, Observer observer) {
        apiSubscribe(RetrofitService.create(Constant.HTTP_API_CITY).getHeCity(key, location), observer);
    }

    /**
     * 冷笑话
     *
     * @param key      key
     * @param page     页码
     * @param pagesize 大小
     * @param observer
     */
    public void getJokes(String key, int page, int pagesize, Observer observer) {
        apiSubscribe(RetrofitService.create(Constant.HTTP_API_JUHE).getJokes(key, page, pagesize), observer);
    }

    /**
     * 微信登录结果
     *
     * @param appid
     * @param secret
     * @param code
     * @param grant_type
     * @param observer
     */
    public void getWXLoginResult(String appid, String secret, String code, String grant_type, Observer observer) {
        apiSubscribe(RetrofitService.create(Constant.HTTP_API_WX).getWXLoginResult(appid, secret, code, grant_type), observer);
    }

    /**
     * 微信登录用户信息
     *
     * @param access_token
     * @param openid
     * @param observer
     */
    public void getWXUserInfo(String access_token, String openid, Observer observer) {
        apiSubscribe(RetrofitService.create(Constant.HTTP_API_WX).getWXUserInfo(access_token, openid), observer);
    }


}
