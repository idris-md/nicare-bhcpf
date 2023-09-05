package com.nicare.ves.ui.auth.device;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.nicare.ves.common.Cryptography;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponse;
import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.ui.auth.user.AuthResource;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class SuspendViewModel extends ViewModel {


    MediatorLiveData<AuthResource<LoginResponse>> mAuthResource = new MediatorLiveData<>();

    AuthApi mApi;
    Application mApplication;
    PrefUtils instance;

    @Inject
    public SuspendViewModel(AuthApi authApi, Application application, PrefUtils prefUtils) {
        mApplication = application;
        instance = prefUtils;
        mApi = authApi;
    }

    public void authenticate(LoginRequest request) {
        final LiveData<AuthResource<LoginResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mApi.login(request)
                        .onErrorReturn(throwable -> {
                            LoginResponse response = new LoginResponse();
                            response.setStatus(-1);
                            return response;
                        })
                        .map(loginResponse -> {
                            if (loginResponse != null) {
                                switch (loginResponse.getStatus()) {

                                    case 200:

                                        Cryptography cryptography = new Cryptography(mApplication);
                                        try {
                                            saveToPref(loginResponse, cryptography, request);
                                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | UnrecoverableEntryException | CertificateException | KeyStoreException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | IOException e) {
                                            e.printStackTrace();
                                        }

                                        return AuthResource.success(loginResponse);

                                    case 108:

                                        break;
                                    //Suspend
                                    case 111:
                                        // Deactivated
                                    case 112:
                                        //////////////
                                        return AuthResource.suspend(loginResponse);

                                    //Suspend
                                    case 211:
                                        // Deactivated
                                        break;

                                    case 212:
                                        return AuthResource.blocked(loginResponse);

                                }

                                return AuthResource.error(loginResponse, loginResponse.getMessage());
                            }


                            return null;
                        })
                        .subscribeOn(Schedulers.io())
        );
    }

    public LiveData<AuthResource<LoginResponse>> observeAuthentication() {
        return mAuthResource;
    }

    private void saveToPref(LoginResponse loginResponse, Cryptography cryptography, LoginRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, UnrecoverableEntryException, CertificateException, KeyStoreException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchProviderException, BadPaddingException, IllegalBlockSizeException {
        cryptography.encryptData("V293ISBob3cgY3VyaW91cyBlaD8=");
        instance.setPWD(request.getPassword());
        instance.setUser(request.getUsername());

        instance.setDeviceId(loginResponse.getDeviceId());
        instance.setEoName(loginResponse.getName());
        instance.setEoPhone(loginResponse.getPhone());

        instance.setBiometric("FP07 Fingerprint");
        instance.setToken(loginResponse.getToken());
        instance.setLatestAppVersionCode(loginResponse.getVersionCode());
        instance.setLatestVersionUrl(loginResponse.getDownloadUrl());
        instance.setLatestVersionName(loginResponse.getVersionName());
    }
}
