package com.mvtrail.thirdparty.qq;


import com.mvtrail.thirdparty.entity.qq.QQUserMessage;

public interface MIUiListener {
    void onComplete(QQUserMessage var1);

    void onError(String var1);

    void onCancel();
}
