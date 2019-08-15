package com.mvtrail.thirdparty.api;

import com.mvtrail.thirdparty.entity.Data;
import com.mvtrail.thirdparty.entity.ResponseResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ASTApi {


    @POST("/demo")
    @FormUrlEncoded
    Call<ResponseResult<String>> getDemo(@Field("demo") String demo);


    @POST("/api/v2.1/user/zfb_login")
    @FormUrlEncoded
    Call<ResponseResult<Data>> alipayLogin(@Field("auth_code") String auth_code);


    @POST("/api/v2.1/user/other_login")
    @FormUrlEncoded
    Call<ResponseResult<Data>> threeLogin(@Field("other_account") String open_id,
                                          @Field("user_img") String e_image,
                                          @Field("user_sex") String e_sex,
                                          @Field("user_name") String e_name,
                                          @Field("login_way") int type);
}
