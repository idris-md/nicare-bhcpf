package com.nicare.ves.ui.auth.user;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.nicare.ves.BuildConfig;
import com.nicare.ves.common.Cryptography;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.local.localdatasources.LocalBenefactorDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalLGADataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;
import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponse;
import com.nicare.ves.persistence.remote.apimodels.DeviceInfo;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;

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

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    MediatorLiveData<AuthResource<LoginResponse>> mAuthResource = new MediatorLiveData<>();

    AuthApi mApi;
    Application mApplication;
    PrefUtils instance;
    DeviceInfo mDeviceInfo;
    LocalWardDataSource mLocalWardDataSource;
    LocalLGADataSource mLocalLGADataSource;
    LocalBenefactorDataSource mLobeLocalBenefactorDataSource;

    @Inject
    public AuthViewModel(AuthApi authApi, Application application, PrefUtils prefUtils, DeviceInfo deviceInfo) {
        mApplication = application;
        instance = prefUtils;
        mDeviceInfo = deviceInfo;
        mApi = authApi;
        mLocalWardDataSource = new LocalWardDataSource(application);
        mLocalLGADataSource = new LocalLGADataSource(application);
        mLobeLocalBenefactorDataSource = new LocalBenefactorDataSource(application);

    }

    public void authenticate(String username, String password) {
        LoginRequest request = new LoginRequest(username, password, mDeviceInfo.getDeviceModel(), mDeviceInfo.getDeviceId(), mDeviceInfo.getDeviceIMEI(), BuildConfig.VERSION_CODE);
        final LiveData<AuthResource<LoginResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mApi.login(request)
                        .onErrorReturn(new Function<Throwable, LoginResponse>() {
                            @Override
                            public LoginResponse apply(Throwable throwable) throws Exception {
                                LoginResponse response = new LoginResponse();
                                response.setMessage(throwable.getMessage());
                                response.setStatus(-1);

                                return response;
                            }
                        })
                        .map(new Function<LoginResponse, AuthResource<LoginResponse>>() {
                            @Override
                            public AuthResource<LoginResponse> apply(LoginResponse loginResponse) throws Exception {
                                int status = loginResponse.getStatus();
                                if (status == 200) {
                                    Cryptography cryptography = new Cryptography(mApplication);
                                    try {

                                        mLocalWardDataSource.insert(loginResponse.getWard()).subscribeOn(Schedulers.io())
                                                .subscribe();
                                        ;
                                        mLocalLGADataSource.insert(loginResponse.getLga_list()).subscribeOn(Schedulers.io())
                                                .subscribe();

                                        mLobeLocalBenefactorDataSource.insert(loginResponse.getBenefactors()).subscribeOn(Schedulers.io())
                                                .subscribe();


                                        saveToPref(loginResponse, cryptography, request);

                                    } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                                             UnrecoverableEntryException | CertificateException |
                                             KeyStoreException |
                                             InvalidAlgorithmParameterException |
                                             InvalidKeyException | NoSuchProviderException |
                                             BadPaddingException | IllegalBlockSizeException |
                                             IOException e) {
                                        e.printStackTrace();
                                    }
                                    return AuthResource.success(loginResponse);
                                } else if (status == 111 || status == 112) {
                                    return AuthResource.suspend(loginResponse);
                                } else if (status == 211 || status == 212) {
                                    return AuthResource.blocked(loginResponse);
                                } else if (status == -1) {
                                    return AuthResource.error(loginResponse, loginResponse.getMessage());
                                } else if (status == 113) {
                                    return AuthResource.update(loginResponse);
                                } else {
                                    instance.setMinVersionCode(loginResponse.getMinVersionCode());
                                    instance.setLatestVersionUrl(loginResponse.getDownloadUrl());
                                    instance.setLatestVersionName(loginResponse.getVersionName());
                                    return AuthResource.error(loginResponse, loginResponse.getMessage());
                                }

                            }

                        }).subscribeOn(Schedulers.io())
        );

        mAuthResource.addSource(source, new Observer<AuthResource<LoginResponse>>() {
            @Override
            public void onChanged(AuthResource<LoginResponse> loginResponseAuthResource) {
                mAuthResource.setValue(loginResponseAuthResource);
                mAuthResource.removeSource(source);
            }
        });
    }

    public LiveData<AuthResource<LoginResponse>> observeAuthentication() {
        return mAuthResource;
    }

    private void saveToPref(LoginResponse loginResponse, Cryptography cryptography, LoginRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, UnrecoverableEntryException, CertificateException, KeyStoreException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchProviderException, BadPaddingException, IllegalBlockSizeException {
        loginResponse.setXcvg("V293ISBob3cgY3VyaW91cyBlaD8="); // Remove to use key from server

        PrefUtils.getInstance(mApplication).setXCVFRN(cryptography.encryptData(loginResponse.getXcvg()));
        instance.setLGA(loginResponse.getLga());
        instance.setAddress(loginResponse.getLga_name());
        instance.setPWD(request.getPassword());
        instance.setUser(request.getUsername());
        instance.setEnrolmentLimit(loginResponse.getEnrolmentLimit());
        instance.setLGAEnrolmentLimit(loginResponse.getLgaLimit());

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
