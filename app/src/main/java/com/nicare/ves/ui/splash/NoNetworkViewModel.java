package com.nicare.ves.ui.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.nicare.ves.persistence.remote.apimodels.ApiResponse.AuthResponse;
import com.nicare.ves.persistence.remote.apimodels.DeviceInfo;
import com.nicare.ves.persistence.remote.api.AuthApi;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NoNetworkViewModel extends ViewModel {

    AuthApi mAuthApi;
    DeviceInfo mDeviceInfo;

    MediatorLiveData<SplashDeviceAuthResource<AuthResponse>> authResponse = new MediatorLiveData<>();

    @Inject
    public NoNetworkViewModel(AuthApi authApi, DeviceInfo device_verify) {
        mAuthApi = authApi;
        mDeviceInfo = device_verify;
    }

    public LiveData<SplashDeviceAuthResource<AuthResponse>> observeAuthentication() {
        return authResponse;
    }

    public void verifyDevice() {
        authResponse.setValue(SplashDeviceAuthResource.loading());
        final LiveData<SplashDeviceAuthResource<AuthResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.deviceVerification(mDeviceInfo)
                        .onErrorReturn(throwable -> {
                            AuthResponse response = new AuthResponse();
                            response.setMessage(throwable.getMessage());
                            response.setStatus(-1);
                            return response;
                        })
                        .map((Function<AuthResponse, SplashDeviceAuthResource<AuthResponse>>) authRes -> {

                            switch (authRes.getStatus()) {
                                case 200:
                                    return SplashDeviceAuthResource.success(authRes);

                                //Device Blocked
                                case 212:
                                    return SplashDeviceAuthResource.blocked();

                                case 404:
                                    return SplashDeviceAuthResource.notRecognise();

                                case -1:
                                    return SplashDeviceAuthResource.error(authRes);

                            }
                            return SplashDeviceAuthResource.error(authRes);
                        })
                        .subscribeOn(Schedulers.io())
        );

        authResponse.addSource(source,
                authRes -> {
                    authResponse.setValue(authRes);
                    authResponse.removeSource(source);
                });
    }

}
