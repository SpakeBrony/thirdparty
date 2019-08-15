package com.mvtrail.thirdparty.wechat;


import com.mvtrail.thirdparty.entity.wx.WeiXinInfo;

public interface WechatGetBack {

    void onComplete(WeiXinInfo var1);

    void onError(String var1);

    void noWechat();
}
