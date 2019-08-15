package com.mvtrail.thirdparty;

import com.mvtrail.thirdparty.api.ASTApi;
import com.mvtrail.thirdparty.entity.Data;
import com.mvtrail.thirdparty.entity.ResponseResult;
import com.mvtrail.thirdparty.utils.StringUtils;
import com.mvtrail.thirdparty.utils.TPLogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient implements ASTApi { private static final String TAG = "NetworkClient";
    private static String SERVICE_BASE_URL;
    private Retrofit retrofit;
    private ASTApi astAPI;

    private static NetworkClient sInstance;

    public static synchronized NetworkClient instance() {
        if (sInstance == null) {
            sInstance = new NetworkClient();
        }
        return sInstance;
    }
    public static synchronized NetworkClient instance(String ServiceBaseUrl) {
        SERVICE_BASE_URL =ServiceBaseUrl;
        if (sInstance == null) {
            sInstance = new NetworkClient();
        }
        return sInstance;
    }


    protected NetworkClient() {
        if (StringUtils.isEmpty(SERVICE_BASE_URL)){
            TPLogUtil.d("域名为空");
            return;
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        astAPI = retrofit.create(ASTApi.class);
    }

    @Override
    public Call<ResponseResult<String>> getDemo(String demo) {
        return astAPI.getDemo(demo);
    }

    @Override
    public Call<ResponseResult<Data>> alipayLogin(String auth_code) {
        return astAPI.alipayLogin(auth_code);
    }

    @Override
    public Call<ResponseResult<Data>> threeLogin(String open_id, String e_image, String e_sex, String e_name, int type) {
        return astAPI.threeLogin(open_id, e_image, e_sex, e_name, type);
    }
}
