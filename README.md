# 第三方登录Model工程

## QQ登录

AndroidManifest.xml中xxxxxxxx是你的appid,前面必须有tencent;
```.xml
<activity
            android:name="com.tencent.connect.common.AssistActivity"
            android:configChanges="orientation|keyboardHidden"
            android:screenOrientation="behind"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
			
<activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true" >
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="tencentxxxxxxxx" />
                <!--应用的AppId要相同-->
            </intent-filter>
        </activity>
```

```java
//创建QQModel类
 QQModel qqModel = new QQModel(Constant.QQ_APP_ID,context);
 
 
 
 qqModel.login(this, new MIUiListener() {
                    @Override
                    public void onComplete(com.mvtrail.thirdparty.entity.qq.QQUserMessage var1) {
//返回用户信息
                        LogUtil.d("qqlogin---onComplete:"+var1.toString());
                    @Override
                    public void onError(String var1) {
//qq登录错误
                        TPLogUtil.d("qqlogin---onError:"+var1);
                    }

                    @Override
                    public void onCancel() {
//结束
                        TPLogUtil.d("qqlogin---onCancel");
                    }
                });
                
     
    //onActivityResult中需要添加下面的代码            
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        qqModel.onActivityResult(requestCode, resultCode, data);
    }
```


## 支付宝登录

```.xml
<activity
            android:name="com.alipay.sdk.app.AlipayResultActivity"
            tools:node="merge">
            <intent-filter tools:node="replace">
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="__xxxxxxx__" />
            </intent-filter>
        </activity>
```



```java

 	//alipay
	//必须与xml中的scheme相同
    public static String alipayScheme = "__xxxxxxx__";
    public static String alipayAppId = "xxxxxxxxxx";

//创建AlipayModel类
AlipayModel alipayModel = new AlipayModel();

  alipayModel.alipayLogin(this,alipayScheme,alipayAppId, new AlipayToken() {
                    @Override
                    public void onComplete(String var1) {
                        LogUtil.d("alipaylogin---onCancel:"+var1);
                       //返回token值
                    }
                    @Override
                    public void onError(String var1) {
					//返回错误
                        TPLogUtil.d("alipaylogin---onError:"+var1);
                    }
                });
```

## 微信登录

微信中必须导入
```.java
implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
```
因为微信必须在项目下创建.wxapi.WXEntryActivity

```.java
//继承model包中的activity文件
public class WXEntryActivity extends com.mvtrail.thirdparty.wxapi.WXEntryActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```


exported 必须为 true ,否则不反悔
```.xml
<activity
            android:exported="true"
            android:name=".wxapi.WXEntryActivity" />
```

```java
//创建WechatModel类
WechatModel wechatModel = WechatModel.getInstance( ElectronicMusicPadsApp.getContext(),Constant.WECHAT_APPID,Constant.WECHAT_SECRET);


wechatModel.login(this, new WechatGetBack() {
                    @Override
                    public void onComplete(com.mvtrail.thirdparty.entity.wx.WeiXinInfo var1) {
                        Log.d("wechatlogin--onComplete",var1.toString());
						//返回用户信息
                    }

                    @Override
                    public void onError(String var1) {
                        TPLogUtil.d("wechatlogin--onError:"+var1);
						//返回出错
                    }

                    @Override
                    public void noWechat() {
                        TPLogUtil.d("wechatlogin---noWechat");
                    }
                });

```
## 代码混淆


```java

 #微信分享登录SDK
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
 #QQ分享登录SDK
-dontwarn com.tencent.**

-keep class com.tencent.**{
    *;
}

-keep  class com.mvtrail.thirdparty.entity.*{*;}
-keep  class com.mvtrail.thirdparty.entity.qq.*{*;}
-keep  class com.mvtrail.thirdparty.entity.wx.*{*;}


```


## 网络请求

```.java
    //初始化
    //ServiceBaseUrl 为你的api域名
    NetworkClient.instance(ServiceBaseUrl);
    Business.BusinessFactory factory = new Business.BusinessFactory();
    factory.createiBusiness(this);
    iBusiness = Business.getIBusiness();
    
    
    
    
    
    //微信访问网络请求
    iBusiness.threeLogin(WeiXinInfo.getOpenid(),
                            WeiXinInfo.getHeadimgurl(),
                            WeiXinInfo.getSex()+"",
                            WeiXinInfo.getNickname(),
                            1)
    .subscribe(new Consumer<ResponseResult<Data>>() {
        @Override
        public void accept(ResponseResult<Data> data) throws Exception {
            //TPLogUtil.d("微信登录成功返回用户信息:"+data.getData().getUser().toString());
        }
    }, new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) {
            TPLogUtil.d("wechatlogin---accept:"+throwable.getMessage());
        }
    });
    
    
    //QQ访问网络请求
    iBusiness.threeLogin(QQUserMessage.getOpenid(),
                            QQUserMessage.getFigureurl_qq_2(),
                            QQUserMessage.getGender(),
                            QQUserMessage.getNickname(),
                            0)
                            .subscribe(new Consumer<ResponseResult<Data>>() {
                                @Override
                                public void accept(ResponseResult<Data> data) throws Exception {
                                    <!--TPLogUtil.d("QQ登录成功返回用户信息:"+data.getData().getUser().toString());-->
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    TPLogUtil.d("qqlogin---accept:"+throwable.getMessage());
                                }
                            });
                            
    
    //支付宝访问网络请求
    iBusiness.alipayLogin(token)
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
```



