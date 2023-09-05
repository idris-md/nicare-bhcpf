package com.nicare.ves.persistence.remote.remotedatasource;

import android.app.Application;
import android.content.Context;

import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.AuthResponse;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponse;
import com.nicare.ves.persistence.remote.apimodels.DeviceInfo;
import com.nicare.ves.persistence.remote.NiCareClient;
import com.nicare.ves.persistence.remote.api.AuthApi;

import io.reactivex.Flowable;


public class RemoteAuthDatasource {

    private Context mContext;
    public RemoteAuthDatasource(Application application) {
        mContext = application;
    }

    public Flowable<LoginResponse> login(LoginRequest login) {
        AuthApi loginService = NiCareClient.NiCareClient(mContext).create(AuthApi.class);
        return loginService.login(login);
    }

    public Flowable<AuthResponse> verifyDevice(DeviceInfo deviceInfo) {
        AuthApi authService = NiCareClient.NiCareClient(mContext).create(AuthApi.class);
        return authService.deviceVerification(deviceInfo);
    }

}
