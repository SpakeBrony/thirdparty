package com.mvtrail.thirdparty.business;


import androidx.annotation.Nullable;

import com.mvtrail.thirdparty.entity.Data;
import com.mvtrail.thirdparty.entity.ResponseResult;

import io.reactivex.Observable;

public interface IBusiness {
    @Nullable
    Observable<ResponseResult<String>> getDemo(String demo);


    @Nullable
    Observable<ResponseResult<Data>> alipayLogin(String token);


    @Nullable
    Observable<ResponseResult<Data>> threeLogin(String open_id,String e_image,String e_sex,String e_name,int type);

}
