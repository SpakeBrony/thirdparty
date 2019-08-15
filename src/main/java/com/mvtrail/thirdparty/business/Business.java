package com.mvtrail.thirdparty.business;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.mvtrail.thirdparty.business.impl.IbusinessImpl;


public class Business {
    private static IBusiness iBusiness;

    @NonNull
    public static IBusiness getIBusiness() {
        return iBusiness;
    }

    public static class BusinessFactory {
        @UiThread
        public void createiBusiness(Context context) {
            iBusiness = new IbusinessImpl(context);
        }

    }

}
