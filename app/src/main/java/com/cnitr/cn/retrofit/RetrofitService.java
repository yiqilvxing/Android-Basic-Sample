package com.cnitr.cn.retrofit;

import android.os.Environment;

import com.cnitr.cn.BuildConfig;
import com.cnitr.cn.Constant;
import com.cnitr.cn.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ZHRSRetrofitService
 * <p>
 * Created by yangcheng on 2018/6/21.
 */

public final class RetrofitService {

    private static final String TAG = "RetrofitService";

    // 超时时间10s
    private static final int TIME_OUT = 10;

    // 缓存有效期为7天
    private static final long CACHE_STALE = 60 * 60 * 24 * 7;

    // 缓存大小100M
    private static final int CACHE_SIZE = 1024 * 1024 * 100;

    // 查询缓存的Cache-Control设置（没有网络时）
    // 为only-if-cached时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE;

    // 查询网络的Cache-Control设置（网络正常时）
    // (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    private static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=60";

    // User-Agent
    private static final String HTTP_USER_AGENT = "User-Agent: Mozilla/5.0 (Linux; Android 6.0.1; MI 4LTE Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36";

    private RetrofitService() {
        throw new AssertionError();
    }

    public static IApiInterface create() {
        Retrofit retrofit = initRetrofit(null);
        return retrofit.create(IApiInterface.class);
    }

    public static IApiInterface create(String baseUrl) {
        Retrofit retrofit = initRetrofit(baseUrl);
        return retrofit.create(IApiInterface.class);
    }

    /**
     * 初始化网络通信服务
     */
    private static Retrofit initRetrofit(String baseUrl) {
        Retrofit retrofit;
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory(), "HttpCache"),
                CACHE_SIZE);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(interceptor);
        }
        okHttpClient.cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(mUserAgentInterceptor)
                .addInterceptor(mCacheControlInterceptor)
                .addNetworkInterceptor(mCacheControlInterceptor)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl == null ? Constant.HTTP_HOST : baseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isNetworkAvailable(MyApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtil.isNetworkAvailable(MyApplication.getContext())) {
                return originalResponse.newBuilder()
                        .header("Cache-Control", CACHE_CONTROL_NETWORK)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 用户代理拦截器
     */
    private static final Interceptor mUserAgentInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", HTTP_USER_AGENT)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    };


}
