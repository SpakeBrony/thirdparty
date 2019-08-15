package com.mvtrail.thirdparty.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ansen.http.entity.HttpConfig;
import com.ansen.http.net.HTTPCaller;
import com.ansen.http.net.RequestDataCallback;
import com.mvtrail.thirdparty.entity.wx.WechatKey;
import com.mvtrail.thirdparty.entity.wx.WeiXin;
import com.mvtrail.thirdparty.entity.wx.WeiXinInfo;
import com.mvtrail.thirdparty.entity.wx.WeiXinToken;
import com.mvtrail.thirdparty.utils.ShareListener;
import com.mvtrail.thirdparty.utils.StringUtils;
import com.mvtrail.thirdparty.utils.TPLogUtil;
import com.mvtrail.thirdparty.utils.WXUtilFile;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WechatModel {
    private static HttpConfig httpConfig;
    private static IWXAPI iwxapi;
    private static String WECHAT_APPID;
    private static String WECHAT_SECRET;
    private static Context mContetx;
    private static WechatGetBack mWechatGetBack;
    private static ShareListener mShareWechatGetBack;
    private WechatKey wechatKey;
    private static WechatModel wechatModel;


    public static synchronized WechatModel getInstance(Context context, String wechatAppId, String wechatSecret) {
        if (wechatModel == null)
            wechatModel = new WechatModel(context, wechatAppId, wechatSecret);
        return wechatModel;
    }

    public static synchronized WechatModel getInstance() {
        return wechatModel;
    }


    private WechatModel(Context context, String wechatAppId, String wechatSecret) {
        if (iwxapi == null) {
            wechatKey = WechatPrick.getInstance(wechatAppId, wechatSecret);
            this.WECHAT_APPID = wechatKey.getWECHAT_APPID();
            this.WECHAT_SECRET = wechatKey.getWECHAT_SECRET();
            this.mContetx = context;
            iwxapi = WXAPIFactory.createWXAPI(context, wechatAppId, true);
            iwxapi.registerApp(wechatAppId);
        }
    }

    public void login(Context context, WechatGetBack wechatGetBack) {
        mWechatGetBack = wechatGetBack;
        if (!isWeChatAppInstalled(context)) {
            TPLogUtil.d("wechatLogin:未安装微信");
            mWechatGetBack.noWechat();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = String.valueOf(System.currentTimeMillis());
        iwxapi.sendReq(req);
    }

    /**
     * 判断微信客户端是否存在
     *
     * @return true安装, false未安装
     */
    public static boolean isWeChatAppInstalled(Context context) {
        if (iwxapi.isWXAppInstalled()) {
            return true;
        } else {
            return false;
        }
    }


    private static void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + WECHAT_APPID + "&secret=" + WECHAT_SECRET +
                "&code=" + code + "&grant_type=authorization_code";
        HTTPCaller.getInstance().get(WeiXinToken.class, url, null, new RequestDataCallback<WeiXinToken>() {
            @Override
            public void dataCallback(WeiXinToken obj) {
                if (obj!=null&&obj.getErrcode() == 0) {//请求成功
                    getWeiXinUserInfo(obj);
                } else {
                    mWechatGetBack.onError(obj.getErrmsg());
                    //请求失败
                }
            }
        });
    }

    private static void getWeiXinUserInfo(final WeiXinToken weiXinToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                weiXinToken.getAccess_token() + "&openid=" + weiXinToken.getOpenid();
        HTTPCaller.getInstance().get(WeiXinInfo.class, url, null, new RequestDataCallback<WeiXinInfo>() {
            @Override
            public void dataCallback(WeiXinInfo obj) {
                weiXinToken.setOpenid(weiXinToken.getOpenid());
                int sex = -1;
                if (obj.getSex() == 1)
                    sex = 0;
                else if (obj.getSex() == 0)
                    sex = 1;
                obj.setSex(sex);
                mWechatGetBack.onComplete(obj);
            }
        });
    }

    public static void setEventBus(WeiXin weiXin) {
        if (httpConfig == null) {
            httpConfig = new HttpConfig();
            httpConfig.setAgent(true);//有代理的情况能不能访问
            httpConfig.setDebug(false);//是否debug模式 如果是debug模式打印log
            //可以添加一些公共字段 每个接口都会带上
            httpConfig.addCommonField("pf", "android");
            httpConfig.addCommonField("version_code", "1");
            //初始化HTTPCaller类
            HTTPCaller.getInstance().setHttpConfig(httpConfig);
        }
        if (weiXin.getType() == 1) {//登录
            getAccessToken(weiXin.getCode());
        } else if (weiXin.getType() == 2) {//分享
            mShareWechatGetBack.onComplete(null);
        }
    }


    /**
     * @param activity
     * @param path               文件路径
     * @param appName            app名称
     * @param appYingYongBaoUr   下载界面url
     * @param appDesc            描述
     * @param appIcon            图片
     * @param shareWechatGetBack //回调
     */
    public void stareWechat(final Activity activity,
                            String path,
                            String appName,
                            String appYingYongBaoUr,
                            String appDesc,
                            int appIcon,
                            ShareListener shareWechatGetBack) {
        mShareWechatGetBack = shareWechatGetBack;
        if (path != null) {
            WXFileObject textObject = new WXFileObject();
            textObject.filePath = path;
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = textObject;
            msg.title = appName + ".mp4";
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("appdata");
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.message = msg;
            iwxapi.sendReq(req);
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = appYingYongBaoUr;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = appName;
            msg.description = appDesc;
            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), appIcon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.thumbData = WXUtilFile.bmpToByteArray(thumbBmp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            iwxapi.sendReq(req);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * @param activity
     * @param path               文件路径
     * @param appName            app名称
     * @param appYingYongBaoUr   下载界面url
     * @param appDesc            描述
     * @param appIcon            图片
     * @param shareWechatGetBack //回调
     */
    public void StareWechatPyq(final Activity activity,
                               String path,
                               String appName,
                               String appYingYongBaoUr,
                               String appDesc,
                               int appIcon,
                               ShareListener shareWechatGetBack) {
        mShareWechatGetBack = shareWechatGetBack;
        if (!StringUtils.isEmpty(path)) {
            //目前不支持上传视频
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = appYingYongBaoUr;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = appName;
            msg.description = appDesc;
            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), appIcon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.thumbData = WXUtilFile.bmpToByteArray(thumbBmp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            iwxapi.sendReq(req);
        }
    }

}

