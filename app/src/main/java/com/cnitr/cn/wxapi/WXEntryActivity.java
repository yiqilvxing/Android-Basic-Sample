package com.cnitr.cn.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by YangChen on 2018/7/11.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1; // 登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; // 分享

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXShareManager.getInstance().getApi().handleIntent(getIntent(), this);
    }

    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) resp).code;

                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                    WXLoginManager.getInstance().getWXLoginResult(code);
                }
                if (type == RETURN_MSG_TYPE_SHARE) {

                }
                break;
        }
        WXEntryActivity.this.finish();
    }

    @Override
    public void onReq(BaseReq req) {
        WXEntryActivity.this.finish();
    }

}
