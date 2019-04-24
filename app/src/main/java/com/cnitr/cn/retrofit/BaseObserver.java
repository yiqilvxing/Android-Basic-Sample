package com.cnitr.cn.retrofit;

import android.text.TextUtils;
import android.widget.Toast;

import com.cnitr.cn.Constant;
import com.cnitr.cn.MyApplication;

/**
 * BaseObserver
 * <p>
 * Created by yangcheng on 2018/6/21.
 */

public abstract class BaseObserver<T> extends AbstractObserver<BaseEntity<T>> {

    protected abstract void onSuccess(T result);

    protected void onFail(int errorCode, String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            Toast.makeText(MyApplication.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNext(BaseEntity<T> result) {
        super.onNext(result);
        try {
            if (result != null) {
                if (result.getCode() == Constant.HTTP_SUCCESS) {
                    T data = result.getData();
                    if (data != null) {
                        onSuccess(data);
                    } else {
                        onSuccess((T) result);
                    }
                } else {
                    onFail(result.getCode(), result.getMsg());
                }
            } else {
                Toast.makeText(MyApplication.getContext(), "请求结果为空！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MyApplication.getContext(), "请求数据异常！", Toast.LENGTH_SHORT).show();
        }
    }

}