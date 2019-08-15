package com.mvtrail.thirdparty.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.mvtrail.thirdparty.R;
import com.mvtrail.thirdparty.entity.qq.QQUser;
import com.mvtrail.thirdparty.entity.qq.QQUserMessage;
import com.mvtrail.thirdparty.utils.ShareListener;
import com.mvtrail.thirdparty.utils.StringUtils;
import com.mvtrail.thirdparty.utils.TPLogUtil;
import com.mvtrail.thirdparty.utils.ThreadManager;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

public class QQModel {

    private static String mAppid;
    private static Tencent mTencent;
    private static Context mContext;
    private QQUserMessage qQUserMessage;
    private UserInfo mInfo;
    private QQUser qqUser;
    private MIUiListener mIUiListener;
    private ShareListener mShareIUiListener;

    public QQModel(String appid, Context context) {
        mAppid = appid;
        mContext = context;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppid, mContext);
        }
    }

    public void login(Activity activity,MIUiListener iUiListener){
        if (mTencent!=null){
            mTencent.logout(activity);
            if (!mTencent.isSessionValid()) {
                mIUiListener =iUiListener;
                mTencent.login(activity, "all", loginListener);
            }
        }
    }

    /**
     * 继承的到BaseUiListener得到doComplete()的方法信息
     */
    private IUiListener  loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {//得到用户的ID  和签名等信息  用来得到用户信息
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };



    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                TPLogUtil.d("QQ登录失败response为null");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                TPLogUtil.d("QQ登录失败jsonResponseJSON格式不正确");
                return;
            }
            Gson gson = new Gson();
            qqUser = gson.fromJson(jsonResponse.toString(), QQUser.class);
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            //登录错误
            TPLogUtil.d("QQ登录失败 onError:"+e.errorMessage);
            mIUiListener.onError(e.errorMessage);
        }

        @Override
        public void onCancel() {
            TPLogUtil.d("qq_login:onCancel");
            mIUiListener.onCancel();
            // 运行完成
        }
    }


    /**
     * 获取登录QQ腾讯平台的权限信息(用于访问QQ用户信息)
     *
     * @param jsonObject
     */
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            Log.d("QQ登录异常Exception",e.getMessage());
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(Object response) {
                    TPLogUtil.d("QQ登录成功");
                    Gson gson = new Gson();
                    qQUserMessage = gson.fromJson(response.toString(), QQUserMessage.class);
                    qQUserMessage.setOpenid(qqUser.getOpenid());
                    String sex = "-1";
                    if (qQUserMessage.getGender().equals("男"))
                        sex = "0";
                    else if (qQUserMessage.getGender().equals("女"))
                        sex = "1";
                    qQUserMessage.setGender(sex);
                    mIUiListener.onComplete(qQUserMessage);
                }

                @Override
                public void onCancel() {
                }
            };
            mInfo = new UserInfo(mContext, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }

    public IUiListener getLoginListener(){
        return loginListener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

    public void shareOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
        }
        if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
        }
    }


    /**
     * 分享到qq
     * @param path 文件路径,为图片路径
     * @param activity
     * @param content  内容地址
     * @param summary 摘要
     * @param iconUrl 图标地址
     * @param headline 标题
     * @param shareIUiListener 回调
     */
    public void share(final String path,
                      final Activity activity ,
                      final String content ,
                      final String summary ,
                      final String iconUrl ,
                      final String headline,
                      final ShareListener shareIUiListener) {
        if (null != mTencent) {
            mShareIUiListener = shareIUiListener;
            final Bundle params = new Bundle();
            //如果路径为null,则代表分享app
            if (StringUtils.isEmpty(path)) {
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, headline);// 标题
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, content);// 内容地址
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, iconUrl);//图标路径
            } else {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);//文件路径
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, headline);
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
            }
            ThreadManager.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    mTencent.shareToQQ(activity, params, qqShareListener);
                }
            });
        }
    }

    private IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            mShareIUiListener.onCancel();
        }

        @Override
        public void onComplete(Object response) {
            mShareIUiListener.onComplete(null);
            // 分享成功
        }

        @Override
        public void onError(UiError e) {
            mShareIUiListener.onError(e.errorMessage);
        }
    };



    /**
     * 分享到qq空间
     * @param path 文件路径,为图片路径
     * @param activity
     * @param content  内容地址
     * @param summary 摘要
     * @param iconUrl 图标地址
     * @param headline 标题
     * @param shareIUiListener 回调
     */
    public  void shareZone(final String path,
                           final Activity activity ,
                           final String content ,
                           final String summary ,
                           final String iconUrl ,
                           final String headline,
                           final ShareListener shareIUiListener) {
        if (null != mTencent) {
            mShareIUiListener = shareIUiListener;
            final Bundle params = new Bundle();
            //如果路径为null,则代表分享app
            if (!StringUtils.isEmpty(path)) {
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO);
                params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, summary);
                params.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, path);
                ThreadManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mTencent.publishToQzone(activity, params, qqShareListener);
                    }
                });
            } else {
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE,headline);// 标题
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);// 摘要
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, content);// 内容地址
                ArrayList<String> imgUrlList = new ArrayList<>();
                imgUrlList.add(iconUrl);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);// 图片地址
                // 分享操作要在主线程中完成
                ThreadManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mTencent.shareToQzone(activity, params, qqShareListener);
                    }
                });
            }
        }
    }

}
