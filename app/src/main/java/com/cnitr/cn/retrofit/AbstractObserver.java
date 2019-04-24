package com.cnitr.cn.retrofit;

import android.net.ParseException;
import android.widget.Toast;

import com.cnitr.cn.MyApplication;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * BaseObserver
 * <p>
 * Created by yangcheng on 2018/6/21.
 */

public abstract class AbstractObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T result) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        String errorMsg = "未知错误";
        if (e instanceof UnknownHostException) {
            errorMsg = "网络不可用";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "连接超时";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            if (httpException.code() >= 500 && httpException.code() < 600) {
                errorMsg = "网络连接失败，请检查网络";
            } else {
                errorMsg = "请求异常，请稍后重试";
            }
        } else if (e instanceof ParseException || e instanceof JSONException
                || e instanceof JSONException) {
            errorMsg = "数据解析错误";
        }
        Toast.makeText(MyApplication.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {

    }

}