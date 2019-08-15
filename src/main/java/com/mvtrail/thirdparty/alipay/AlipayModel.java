package com.mvtrail.thirdparty.alipay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.alipay.sdk.app.OpenAuthTask;
import com.mvtrail.thirdparty.business.Business;
import com.mvtrail.thirdparty.business.IBusiness;
import com.mvtrail.thirdparty.entity.ResponseResult;
import com.mvtrail.thirdparty.utils.TPLogUtil;
import com.mvtrail.thirdparty.wechat.WechatModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AlipayModel {

    private AlipayToken mAlipayToken;

    public void alipayLogin(Activity activity,String alipayScheme,String alipayAppId,AlipayToken alipayToken){
        mAlipayToken= alipayToken;
        try {
            final Map<String, String> bizParams = new HashMap<>();
            bizParams.put("url", "https://authweb.alipay.com/auth?auth_type=PURE_OAUTH_SDK&app_id="+alipayAppId+"&scope=auth_user&state=init");

            // 支付宝回跳到你的应用时使用的 Intent Scheme。请设置为不和其它应用冲突的值。
            // 如果不设置，将无法使用 H5 中间页的方法(OpenAuthTask.execute() 的最后一个参数)回跳至
            // 你的应用。
            //
            // 参见 AndroidManifest 中 <AlipayResultActivity> 的 android:scheme，此两处
            // 必须设置为相同的值。
            final String scheme = alipayScheme;

            // 唤起授权业务
            final OpenAuthTask task = new OpenAuthTask(activity);
            task.execute(
                    scheme,    // Intent Scheme
                    OpenAuthTask.BizType.AccountAuth, // 业务类型
                    bizParams, // 业务参数
                    openAuthCallback, // 业务结果回调。注意：此回调必须被你的应用保持强引用
                    false); // 是否需要在用户未安装支付宝 App 时，使用 H5 中间页中转
        }catch (Exception e){
            mAlipayToken.onError(e.getMessage());
        }

    }

    /**
     * 通用跳转授权业务的回调方法。
     * 此方法在支付宝 SDK 中为弱引用，故你的 App 需要以成员变量等方式保持对 Callback 的强引用，
     * 以免对象被回收。
     * 以局部变量保持对 Callback 的引用是不可行的。
     */
    final OpenAuthTask.Callback openAuthCallback = new OpenAuthTask.Callback() {
        @Override
        public void onResult(int i, String s, Bundle bundle) {
            final String token =bundleToString(bundle);
            TPLogUtil.d("支付宝授权成功");
            mAlipayToken.onComplete(token);
            //链接服务器
        }
    };

    private static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        String auth_code = null;
        for (String key : bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n");
            if (key.equals("auth_code"))
                auth_code = (String) bundle.get(key);
        }
        return auth_code;
    }
}
