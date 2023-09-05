package com.nicare.ves.ui.splash;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.AuthResponse;
import com.nicare.ves.persistence.remote.apimodels.DeviceInfo;
import com.nicare.ves.persistence.remote.api.AuthApi;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends ViewModel {
    private static final String TAG = "SplashViewModel";
    AuthApi mAuthApi;
    DeviceInfo mDeviceInfo;
    PrefUtils instance;

    MediatorLiveData<SplashDeviceAuthResource<AuthResponse>> authResponse = new MediatorLiveData<>();
    Application mApplication;
//    AuthRepository mRepository;
    MutableLiveData<SplashAuthResource> splashAuthResource = new MutableLiveData<>();

    @Inject
    public SplashViewModel(AuthApi authApi, DeviceInfo device_verify, Application application, PrefUtils prefUtils) {
        mAuthApi = authApi;
        mDeviceInfo = device_verify;
        mApplication = application;
//        mRepository = new AuthRepository(application);
        instance = prefUtils;
    }

    public void verifyDevice() {
        authResponse.setValue(SplashDeviceAuthResource.loading());
        final LiveData<SplashDeviceAuthResource<AuthResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.deviceVerification(mDeviceInfo)
                        .onErrorReturn(throwable -> {
                            AuthResponse response = new AuthResponse();
                            response.setStatus(-1);

                            return response;
                        })
                        .map((Function<AuthResponse, SplashDeviceAuthResource<AuthResponse>>) authRes -> {

                            switch (authRes.getStatus()) {
                                case 200:
                                    instance.setMinVersionCode(authRes.getMinVersionCode());
                                    instance.setLatestVersionUrl(authRes.getDownloadUrl());
                                    instance.setLatestVersionName(authRes.getVersionName());
                                    return SplashDeviceAuthResource.success(authRes);

                                //Device Blocked
                                case 212:
                                    return SplashDeviceAuthResource.blocked();

                                case 404:
                                    return SplashDeviceAuthResource.notRecognise();
                                case 113:

                                    instance.setMinVersionCode(authRes.getMinVersionCode());
                                    instance.setLatestVersionUrl(authRes.getDownloadUrl());
                                    instance.setLatestVersionName(authRes.getVersionName());

                                    return SplashDeviceAuthResource.update(authRes);

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

    public LiveData<SplashDeviceAuthResource<AuthResponse>> observeAuthentication() {
        return authResponse;
    }

    public boolean isPlayServiceVersionLatest(Activity activity) {

        final boolean[] isAvailable = {false};
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(activity)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "onComplete: Play Services OKAY");
                            isAvailable[0] = true;
                        }
                    }
                });
        return isAvailable[0];
    }

    public void checkAuthStatus() {

        splashAuthResource.setValue(SplashAuthResource.loading());
        boolean isLogin = instance.isLogin();
        boolean isActivated = instance.isActivated();
        boolean isAccountDisable = instance.isAccountDeactivated();
        boolean isDeviceBlocked = instance.isDeviceBlocked();

        boolean isDeviceSuspended = instance.isDeviceSuspended();
        boolean isAccountSuspended = instance.isAccountSuspended();

        if (isLogin) {

            if (isActivated) {

                if (isDeviceBlocked) {
                    splashAuthResource.setValue(SplashAuthResource.blocked());
                } else if (isAccountSuspended || isDeviceSuspended || isDeviceBlocked) {
                    splashAuthResource.setValue(SplashAuthResource.suspend());
                } else {
                    splashAuthResource.setValue(SplashAuthResource.success());
                }

            } else {
                splashAuthResource.setValue(SplashAuthResource.notSecured());
            }

        } else {

            //DELETE ME
//                intent = new Intent(SplashActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            // DELETE ME END HERE
            if (isAccountDisable || isDeviceBlocked) {
                splashAuthResource.setValue(SplashAuthResource.blocked());
            } else if (isAccountSuspended || isDeviceSuspended) {
                splashAuthResource.setValue(SplashAuthResource.suspend());
            } else {
                splashAuthResource.setValue(SplashAuthResource.notAuthenticated());
            }
        }

    }

    public LiveData<SplashAuthResource> observeAuthStatus() {
        return splashAuthResource;
    }



}
