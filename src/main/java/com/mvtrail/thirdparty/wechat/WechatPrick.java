package com.mvtrail.thirdparty.wechat;

import com.mvtrail.thirdparty.entity.wx.WechatKey;


public class WechatPrick {

    public static WechatKey wechatKey;

    public static synchronized WechatKey getInstance(String wechatAppId, String wechatSecret) {
        if (wechatKey == null) {
            wechatKey = new WechatKey();
            wechatKey.setWECHAT_APPID(wechatAppId);
            wechatKey.setWECHAT_SECRET(wechatSecret);
        }
        return wechatKey;
    }

    public static synchronized WechatKey getInstance() {
        return wechatKey;
    }
}
