package com.nicare.ves.ui.profile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.HUWEDatabase;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.apimodels.Address;
import com.nicare.ves.persistence.remote.apimodels.Report;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.ChangePasswordRequest;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.BaseResponse;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponse;
import com.nicare.ves.persistence.remote.apimodels.DeviceInfo;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.persistence.local.localdatasources.LocalLGADataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalPINDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalTransactionDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;
import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.ui.auth.user.AuthResource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {
    LocalLGADataSource mLocalLGADataSource;
    LocalWardDataSource mLocalWardDataSource;
    LocalPINDataSource mLocalPINDataSource;
    LocalTransactionDataSource mLocalTransactionDataSource;
    Application mApplication;
    AuthApi mAuthApi;
    MediatorLiveData<AuthResource<LoginResponse>> mAuthResource = new MediatorLiveData<>();
    MediatorLiveData<LogoutResource<BaseResponse>> mLogoutResource = new MediatorLiveData<>();
    MediatorLiveData<ChangePasswordResource<BaseResponse>> mChangePasswordResource = new MediatorLiveData<>();
    DeviceInfo mDeviceInfo;
    PrefUtils mPrefUtils;
    LoginRequest mLoginRequest;

    @Inject
    public ProfileViewModel(Application application, AuthApi authApi, DeviceInfo deviceInfo, PrefUtils prefUtils, LoginRequest loginRequest) {
        mDeviceInfo = deviceInfo;
        mAuthApi = authApi;
        mPrefUtils = prefUtils;
        mApplication = application;
        mLoginRequest = loginRequest;
        mLocalPINDataSource = new LocalPINDataSource(application);
        mLocalTransactionDataSource = new LocalTransactionDataSource(application);
        mLocalWardDataSource = new LocalWardDataSource(application);
        mLocalLGADataSource = new LocalLGADataSource(application);

    }

    public LiveData<Transaction> getTransaction() {
        return mLocalTransactionDataSource.getLiveTodayLog();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return mLocalTransactionDataSource.getTransactions();
    }

    public Maybe<Address> getLGAById() {
        return mLocalLGADataSource.getAddress((PrefUtils.getInstance(mApplication)).getLGA(), (PrefUtils.getInstance(mApplication)).getWard());
    }
    public LiveData<List<Ward>> getWardsByLG(String id) {
        return mLocalWardDataSource.getWardByLga(id);
    }
    public LiveData<Integer> allReport() {
        return mLocalTransactionDataSource.allReports();
    }

    public void authenticate() {
        final LiveData<AuthResource<LoginResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.login(mLoginRequest)
                        .onErrorReturn(new Function<Throwable, LoginResponse>() {
                            @Override
                            public LoginResponse apply(Throwable throwable) throws Exception {
                                LoginResponse response = new LoginResponse();
                                response.setStatus(-1);
                                return response;
                            }
                        })
                        .map(new Function<LoginResponse, AuthResource<LoginResponse>>() {
                            @Override
                            public AuthResource<LoginResponse> apply(LoginResponse loginResponse) throws Exception {
                                if (loginResponse != null) {
                                    switch (loginResponse.getStatus()) {

                                        case 200:
                                            mPrefUtils.setToken(loginResponse.getToken());
                                            return AuthResource.success(loginResponse);

                                        case 113:
                                            mPrefUtils.setToken(loginResponse.getToken());
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


                                return AuthResource.error(loginResponse, loginResponse.getMessage());
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );

        mAuthResource.addSource(source, new Observer<AuthResource<LoginResponse>>() {
            @Override
            public void onChanged(AuthResource<LoginResponse> loginResponseAuthResource) {
                mAuthResource.setValue(loginResponseAuthResource);
                mAuthResource.removeSource(source);
            }
        });
    }

    public void logOut() {
        String tok = mPrefUtils.getToken();
        final LiveData<LogoutResource<BaseResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.logout(mLoginRequest, "Bearer " + mPrefUtils.getToken())
                        .onErrorReturn(new Function<Throwable, BaseResponse>() {
                            @Override
                            public BaseResponse apply(Throwable throwable) throws Exception {
                                BaseResponse response = new BaseResponse();
                                response.setMessage(throwable.getMessage());
                                response.setStatus(-1);
                                throwable.printStackTrace();
                                return response;
                            }
                        })
                        .map(baseResponse -> {

                            switch (baseResponse.getStatus()) {
                                case 200:
                                    HUWEDatabase mDatabase = HUWEDatabase.getInstance(mApplication);
                                    mDatabase.clearAllTables();

                                    SharedPreferences preferences = mApplication.getSharedPreferences("NiCare", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear().commit();
                                    return LogoutResource.success(baseResponse);
                                default:
                                    return LogoutResource.error(baseResponse, baseResponse.getMessage());
                            }

                        })
                        .subscribeOn(Schedulers.io()));
        mLogoutResource.addSource(source, new Observer<LogoutResource<BaseResponse>>() {
            @Override
            public void onChanged(LogoutResource<BaseResponse> loginResponseAuthResource) {
                mLogoutResource.setValue(loginResponseAuthResource);
                mLogoutResource.removeSource(source);
            }
        });

//        AuthApi authService = NiCareClient.NiCareClient(mApplication).create(AuthApi.class);
//        authService.logout(new LogoutRequest("logout", PrefUtils.getInstance(mApplication).getUser(), PrefUtils.getInstance(mApplication).getPwd(), mDeviceManufacture + " " + mDeviceModel, mDeviceId, mDeviceIMEI), token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
//                    @Override
//                    public void onSuccess(BaseResponse logoutResponse) {
//                        switch (logoutResponse.getStatus()) {
//                            case 200:
//
//                                clearAppData();
//                                mAlertDialog.dismiss();
//
//                                break;
//                            default:
//                                Util.showDialogueMessae(ProfileActivity.this, "Logout request failed please try again later", "Server Error");
//                                dismissDialogs();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        dismissDialogs();
//                    }
//                });

    }

    public LiveData<LogoutResource<BaseResponse>> observeLogout() {
        return mLogoutResource;
    }

    public LiveData<AuthResource<LoginResponse>> observeAuthentication() {
        return mAuthResource;
    }

    public void changePassword(String newPassword) {
        final LiveData<ChangePasswordResource<BaseResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.changePassword(new ChangePasswordRequest(newPassword), "Bearer " + mPrefUtils.getToken())
                        .onErrorReturn(new Function<Throwable, BaseResponse>() {
                            @Override
                            public BaseResponse apply(Throwable throwable) throws Exception {
                                BaseResponse response = new BaseResponse();
                                response.setStatus(-1);
                                return response;
                            }
                        })
                        .map(baseResponse -> {

                            if (baseResponse != null) {
                                if (baseResponse.getStatus() == 200) {
                                    mPrefUtils.setPWD(newPassword);
                                    return ChangePasswordResource.success(baseResponse);
                                } else {
                                    return ChangePasswordResource.error(baseResponse, baseResponse.getMessage());
                                }
                            }

                            return ChangePasswordResource.error(baseResponse, baseResponse.getMessage());

                        })
                        .subscribeOn(Schedulers.io()));
        mChangePasswordResource.addSource(source, new Observer<ChangePasswordResource<BaseResponse>>() {
            @Override
            public void onChanged(ChangePasswordResource<BaseResponse> loginResponseAuthResource) {
                mChangePasswordResource.setValue(loginResponseAuthResource);
                mChangePasswordResource.removeSource(source);
            }
        });

    }

    public LiveData<ChangePasswordResource<BaseResponse>> observeChangePassword() {
        return mChangePasswordResource;
    }
}
