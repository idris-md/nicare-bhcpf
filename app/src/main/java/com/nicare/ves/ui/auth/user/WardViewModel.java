package com.nicare.ves.ui.auth.user;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.WardSeletionRequest;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.BaseResponse;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.ui.profile.LogoutResource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WardViewModel extends ViewModel {
    AuthApi mAuthApi;
    PrefUtils mPrefUtils;
    LocalWardDataSource mLocalWardDataSource;

    MediatorLiveData<LogoutResource<BaseResponse>> wardResource = new MediatorLiveData<>();

    @Inject
    public WardViewModel(Application application, AuthApi authApi, PrefUtils prefUtils) {
        mAuthApi = authApi;
        mPrefUtils = prefUtils;
        mLocalWardDataSource = new LocalWardDataSource(application);

    }

    public void setWard(Ward ward) {
        WardSeletionRequest request=  new WardSeletionRequest(ward.getWard_id());
        final LiveData<LogoutResource<BaseResponse>> source = LiveDataReactiveStreams.fromPublisher(
                mAuthApi.loginWard(request, "Bearer "+mPrefUtils.getToken())
                        .onErrorReturn(new Function<Throwable, BaseResponse>() {
                            @Override
                            public BaseResponse apply(Throwable throwable) throws Exception {
                                BaseResponse response = new BaseResponse();
                                response.setStatus(-1);
                                return response;
                            }
                        })
                        .map(new Function<BaseResponse, LogoutResource<BaseResponse>>() {
                            @Override
                            public LogoutResource<BaseResponse> apply(BaseResponse authResponse) throws Exception {
                                if (authResponse.getStatus() == 200) {
                                    mPrefUtils.setWard(ward.getWard_id());
                                    mPrefUtils.setLGA(ward.getLga_id());
                                    return LogoutResource.success(authResponse);
                                }

                                return LogoutResource.error(authResponse, authResponse.getMessage());
                            }
                        }).subscribeOn(Schedulers.io())
        );


        wardResource.addSource(source, new Observer<LogoutResource<BaseResponse>>() {
            @Override
            public void onChanged(LogoutResource<BaseResponse> baseResponseLogoutResource) {
                wardResource.setValue(baseResponseLogoutResource);
                wardResource.removeSource(source);
            }
        });

    }

    public LiveData<LogoutResource<BaseResponse>> observeWard() {
        return wardResource;
    }

    public LiveData<List<Ward>> getWardsByLG(String lgaId) {
        return mLocalWardDataSource.getWardByLga(lgaId);
    }
}
