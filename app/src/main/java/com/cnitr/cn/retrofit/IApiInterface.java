package com.cnitr.cn.retrofit;

import com.cnitr.cn.entity.BannerEntity;
import com.cnitr.cn.entity.DiscoveryEntity;
import com.cnitr.cn.entity.ExpressEntity;
import com.cnitr.cn.entity.HeCitySearchEntity;
import com.cnitr.cn.entity.JokesEntity;
import com.cnitr.cn.entity.NewsEntity;
import com.cnitr.cn.entity.WechatItem;
import com.cnitr.cn.wxapi.WXAccessTokenBean;
import com.cnitr.cn.wxapi.WXUserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * IApiInterface
 * <p>
 * Created by yangcheng on 2018/6/21.
 */

public interface IApiInterface {

    @GET("/app/index/banner/list")
    Observable<BaseEntity<List<BannerEntity>>> getBannerList();

    @GET("/app/index/news/pagelist")
    Observable<BaseEntity<List<NewsEntity>>> getNewsList(@Query("offset") int offset,
                                                        @Query("limit") int limit);

    @GET("/app/common/question/auto/reply")
    Observable<BaseEntity<String>> getSmartCustom();

    @GET("/app/local/express")
    Observable<BaseEntity<ExpressEntity>> getExpress(@Query("nu") String nu);

    @GET("/api/data/福利/10/{page}")
    Observable<DiscoveryEntity> getDiscoveryList(@Path("page") int page);

    @GET("/wx/article/search")
    Observable<WechatItem> getWechat(@Query("key") String key,
                                     @Query("cid") int cid,
                                     @Query("page") int page,
                                     @Query("size") int size);

    @GET("/find")
    Observable<HeCitySearchEntity> getHeCity(@Query("key") String key,
                                             @Query("location") String location);

    @GET("/joke/content/text.php")
    Observable<JokesEntity> getJokes(@Query("key") String key,
                                     @Query("page") int page,
                                     @Query("pagesize") int pagesize);

    @POST("/sns/oauth2/access_token")
    @FormUrlEncoded
    Observable<WXAccessTokenBean> getWXLoginResult(@Field("appid") String appid,
                                                   @Field("secret") String secret,
                                                   @Field("code") String code,
                                                   @Field("grant_type") String grant_type);

    @POST("/sns/userinfo")
    @FormUrlEncoded
    Observable<WXUserInfoBean> getWXUserInfo(@Field("access_token") String access_token,
                                             @Field("openid") String openid);


}
