package com.mvtrail.thirdparty.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mvtrail.thirdparty.entity.wx.WechatKey;
import com.mvtrail.thirdparty.entity.wx.WeiXin;
import com.mvtrail.thirdparty.utils.TPLogUtil;
import com.mvtrail.thirdparty.wechat.WechatModel;
import com.mvtrail.thirdparty.wechat.WechatPrick;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI wxAPI;
    private WechatKey wechatKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wechatKey= WechatPrick.getInstance();
        if (wechatKey !=null) {
            TPLogUtil.d("wechat---onCreate");
            wxAPI = WXAPIFactory.createWXAPI(this, wechatKey.getWECHAT_APPID(), true);
            wxAPI.registerApp(wechatKey.getWECHAT_APPID());
            wxAPI.handleIntent(getIntent(), this);
        }
//        WeiXin weiXin = new WeiXin(1, 0, "777777");
//        WechatModel.setEventBus(weiXin);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wxAPI.handleIntent(getIntent(), this);
        TPLogUtil.d("wechat---WXEntryActivity onNewIntent");
    }

    @Override
    public void onReq(BaseReq arg0) {
        TPLogUtil.d("wechat---WXEntryActivity onReq:" + arg0);
    }

    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享
                    TPLogUtil.d("wechat---微信分享操作.....");
                    WeiXin weiXin = new WeiXin(2, resp.errCode, "");
                    WechatModel.setEventBus(weiXin);
                } else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登陆
                    TPLogUtil.d("wechat---微信登录操作.....");
                    SendAuth.Resp authResp = (SendAuth.Resp) resp;
                    WeiXin weiXin = new WeiXin(1, resp.errCode, authResp.code);
                    WechatModel.setEventBus(weiXin);
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
        finish();
    }
}
