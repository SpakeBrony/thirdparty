package com.mvtrail.thirdparty.business.impl;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvtrail.thirdparty.NetworkClient;
import com.mvtrail.thirdparty.business.IBusiness;
import com.mvtrail.thirdparty.entity.Data;
import com.mvtrail.thirdparty.entity.ResponseResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class IbusinessImpl implements IBusiness {
    private Context context;

    public IbusinessImpl(Context context) {
        this.context = context;
    }


    @Nullable
    @Override
    public Observable<ResponseResult<String>> getDemo(final String demo) {
        final ArrayList<Call<ResponseResult<String>>> calls = new ArrayList<>(1);
        return Observable.create(new ObservableOnSubscribe<ResponseResult<String>>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseResult<String>> emitter) throws Exception {
                NetworkClient networkClient = NetworkClient.instance();
                Call<ResponseResult<String>> clientReq = networkClient.getDemo(demo);
                Response<ResponseResult<String>> response = clientReq.execute();
                ResponseResult<String> add = response.body();
                onError(add,emitter);
                emitter.onNext(add);
                emitter.onComplete();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                Call<ResponseResult<String>> req = calls.get(0);
                req.cancel();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public Observable<ResponseResult<Data>> alipayLogin(final String auth_code) {
        final ArrayList<Call<ResponseResult<Data>>> calls = new ArrayList<>(1);
        return Observable.create(new ObservableOnSubscribe<ResponseResult<Data>>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseResult<Data>> emitter) throws Exception {
                NetworkClient networkClient = NetworkClient.instance();
                Call<ResponseResult<Data>> clientReq = networkClient.alipayLogin(auth_code);
                Response<ResponseResult<Data>> response = clientReq.execute();
                ResponseResult<Data> add = response.body();
                onError(add,emitter);
                emitter.onNext(add);
                emitter.onComplete();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                Call<ResponseResult<Data>> req = calls.get(0);
                req.cancel();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }



    @Nullable
    @Override
    public Observable<ResponseResult<Data>> threeLogin(final String open_id, final String e_image, final String e_sex, final String e_name, final int type) {
        final ArrayList<Call<ResponseResult<Data>>> calls = new ArrayList<>(1);
        return Observable.create(new ObservableOnSubscribe<ResponseResult<Data>>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseResult<Data>> emitter) throws Exception {
                NetworkClient networkClient = NetworkClient.instance();
                Call<ResponseResult<Data>> clientReq = networkClient.threeLogin(open_id, e_image, e_sex, e_name, type);
                Response<ResponseResult<Data>> response = clientReq.execute();
                ResponseResult<Data> add = response.body();
                onError(add,emitter);
                emitter.onNext(add);
                emitter.onComplete();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                Call<ResponseResult<Data>> req = calls.get(0);
                req.cancel();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 报错判断
     * @param add
     * @param emitter
     */
    private void onError(ResponseResult add,ObservableEmitter emitter){
        if (add.getCode() == ResponseResult.ERROR_SERVERS) {
            emitter.tryOnError(new Throwable("ERROR_SERVERS"));
        }
    }
}
