package com.mvtrail.thirdparty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mvtrail.thirdparty.alipay.AlipayModel;
import com.mvtrail.thirdparty.alipay.AlipayToken;
import com.mvtrail.thirdparty.business.Business;
import com.mvtrail.thirdparty.business.IBusiness;
import com.mvtrail.thirdparty.entity.Data;
import com.mvtrail.thirdparty.entity.ResponseResult;
import com.mvtrail.thirdparty.entity.qq.QQUserMessage;
import com.mvtrail.thirdparty.entity.wx.WeiXinInfo;
import com.mvtrail.thirdparty.qq.MIUiListener;
import com.mvtrail.thirdparty.qq.QQModel;
import com.mvtrail.thirdparty.utils.TPLogUtil;
import com.mvtrail.thirdparty.wechat.WechatGetBack;
import com.mvtrail.thirdparty.wechat.WechatModel;
import com.tencent.tauth.UiError;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ThirdPartyActivity extends Activity implements View.OnClickListener {


    private QQModel qqModel;
    private AlipayModel alipayModel;
    private WechatModel wechatModel;
    public String mqqAppid;
    public String wechatAppid;
    public String wechatSecret;
    public String alipayScheme;
    public String alipayAppId;
    public static IBusiness iBusiness;
    public String ServiceBaseUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party);
        getIntentNum();
        findViewById(R.id.wechat).setOnClickListener(this);
        findViewById(R.id.qq).setOnClickListener(this);
        findViewById(R.id.alipay).setOnClickListener(this);
        //初始化
        NetworkClient.instance(ServiceBaseUrl);
        Business.BusinessFactory factory = new Business.BusinessFactory();
        factory.createiBusiness(this);
        qqModel = new QQModel(mqqAppid,this);
        wechatModel = WechatModel.getInstance(ThirdPartyActivity.this,wechatAppid,wechatSecret);
        alipayModel = new AlipayModel();
        iBusiness = Business.getIBusiness();
        //end
    }

    private void getIntentNum() {
        Intent intent = getIntent();
        mqqAppid = intent.getStringExtra("mqqAppid");
        wechatAppid = intent.getStringExtra("wechatAppid");
        wechatSecret = intent.getStringExtra("wechatSecret");
        alipayScheme = intent.getStringExtra("alipayScheme");
        alipayAppId = intent.getStringExtra("alipayAppId");
        ServiceBaseUrl = intent.getStringExtra("ServiceBaseUrl");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wechat) {
            wechatModel.login(this, new WechatGetBack() {
                @Override
                public void onComplete(WeiXinInfo var1) {
                    Log.d("wechatlogin--onComplete",var1.toString());
                    iBusiness.threeLogin(var1.getOpenid(),
                            var1.getHeadimgurl(),
                            var1.getSex()+"",
                            var1.getNickname(),
                            1)
                            .subscribe(new Consumer<ResponseResult<Data>>() {
                                @Override
                                public void accept(ResponseResult<Data> data) throws Exception {
                                    TPLogUtil.d("微信登录成功返回用户信息:"+data.getData().getUser().toString());
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    TPLogUtil.d("qqlogin---accept:"+throwable.getMessage());
                                }
                            });
                }

                @Override
                public void onError(String var1) {
                    TPLogUtil.d("wechatlogin--onError:"+var1);
//                    Toast.makeText(ThirdPartyActivity.this, "微信异常:"+var1, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void noWechat() {
                    TPLogUtil.d("wechatlogin---noWechat");
                }
            });
        }else
        if (view.getId() == R.id.qq) {
            qqModel.login(this, new MIUiListener() {
                @Override
                public void onComplete(QQUserMessage var1) {
                    TPLogUtil.d("qqlogin---onComplete:"+var1.toString());
                    iBusiness.threeLogin(var1.getOpenid(),
                            var1.getFigureurl_qq_2(),
                            var1.getGender(),
                            var1.getNickname(),
                            0)
                            .subscribe(new Consumer<ResponseResult<Data>>() {
                                @Override
                                public void accept(ResponseResult<Data> data) throws Exception {
                                    TPLogUtil.d("QQ登录成功返回用户信息:"+data.getData().getUser().toString());
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    TPLogUtil.d("qqlogin---accept:"+throwable.getMessage());
                                }
                            });
                }
                @Override
                public void onError(String var1) {
                    TPLogUtil.d("qqlogin---onError:"+var1);
                }

                @Override
                public void onCancel() {
                    TPLogUtil.d("qqlogin---onCancel");
                }
            });
        }else
        if (view.getId() == R.id.alipay) {
            alipayModel.alipayLogin(this,alipayScheme,alipayAppId, new AlipayToken() {
                @Override
                public void onComplete(String var1) {
                    TPLogUtil.d("alipaylogin---onCancel:"+var1);
                    iBusiness.alipayLogin(var1)
                            .subscribe(new Consumer<ResponseResult<Data>>() {
                                @Override
                                public void accept(ResponseResult<Data> data) throws Exception {
                                    TPLogUtil.d("支付宝登录成功返回用户信息:"+data.getData().getUser().toString());
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    TPLogUtil.d("alipaylogin---accept:"+throwable.getMessage());
                                }
                            });
                }

                @Override
                public void onError(String var1) {
                    TPLogUtil.d("alipaylogin---onError:"+var1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        qqModel.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        wechatModel.onDestroy();
    }
}
