package com.cnitr.cn.wxapi;

import android.graphics.Bitmap;

import com.cnitr.cn.MyApplication;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by YangChen on 2018/7/12.
 */

public class WXShareManager {

    public static final int WX_SHARE_SESSION = 1;// 会话聊天
    public static final int WX_SHARE_TIMELINE = 2;// 朋友圈

    private static WXShareManager instance;
    private IWXAPI api;

    private WXShareManager() {
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

    public static WXShareManager getInstance() {
        synchronized (WXShareManager.class) {
            if (instance == null) {
                instance = new WXShareManager();
            }
        }
        return instance;
    }


    /**
     * 发送文本微信分享
     *
     * @param scene WX_SHARE_SESSION >> 会话聊天 ; WX_SHARE_TIMELINE >> 朋友圈
     * @param text  文本内容
     */
    public void sendTextObjectWXShare(int scene, String text) {

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage(textObj);
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;

        req.scene = (scene == WX_SHARE_SESSION) ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        getApi().sendReq(req);
    }

    /**
     * 发送图片微信分享
     *
     * @param scene  WX_SHARE_SESSION >> 会话聊天 ; WX_SHARE_TIMELINE >> 朋友圈
     * @param bitmap
     */
    public void sendImageObjectWXShare(int scene, Bitmap bitmap) {

        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage(imgObj);
        msg.mediaObject = imgObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;

        req.scene = (scene == WX_SHARE_SESSION) ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        getApi().sendReq(req);
    }

    /**
     * 发送网页微信分享
     *
     * @param scene       WX_SHARE_SESSION >> 会话聊天 ; WX_SHARE_TIMELINE >> 朋友圈
     * @param url         网页地址
     * @param title       网页标题
     * @param description 网页描述
     * @param imgBitmap   网页图标
     */
    public void sendWebPageObjectWXShare(int scene, String url, String title, String description, Bitmap imgBitmap) {

        WXWebpageObject webObj = new WXWebpageObject();
        webObj.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webObj);
        msg.title = title;
        msg.description = description;
        msg.thumbData = bitmap2Bytes(imgBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;

        req.scene = (scene == WX_SHARE_SESSION) ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        getApi().sendReq(req);
    }


    /**
     * 构建一个唯一标志
     *
     * @param type 分享的类型分字符串
     * @return 返回唯一字符串
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * Bitmap to byte[]
     *
     * @param bitmap
     * @return
     */
    private byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                return baos.toByteArray();
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }


}
