package com.nicare.ves.ui.auth.user;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.BaseResponse;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.ForgotPasswordRequest;

import javax.inject.Inject;

import io.reactivex.functions.Function;

public class FirstFragmentViewModel extends ViewModel {

    MediatorLiveData<AuthResource<BaseResponse>> mAuthResource = new MediatorLiveData<>();
    Application mApplication;
    private AuthApi mAuthApi;

    @Inject
    public FirstFragmentViewModel(AuthApi authApi, Application application) {
        mAuthApi = authApi;
        mApplication = application;
    }

    public void sendMail(String id) {

        ForgotPasswordRequest request = new ForgotPasswordRequest(id);
        final LiveData<AuthResource<BaseResponse>> source = LiveDataReactiveStreams.fromPublisher(mAuthApi.forgotPassword(request)
                .onErrorReturn(new Function<Throwable, BaseResponse>() {
                    @Override
                    public BaseResponse apply(Throwable throwable) throws Exception {
                        BaseResponse response = new BaseResponse();
                        response.setMessage(throwable.getMessage());
                        response.setStatus(-1);

                        return response;
                    }
                }).map(new Function<BaseResponse, AuthResource<BaseResponse>>() {
                    @Override
                    public AuthResource<BaseResponse> apply(BaseResponse baseResponse) throws Exception {
                        int status = baseResponse.getStatus();
                        if (status == 200) {
                            return AuthResource.success(baseResponse);
                        } else   {
                            return AuthResource.suspend(baseResponse);
                        }
                    }
                }));

        mAuthResource.addSource(source, new Observer<AuthResource<BaseResponse>>() {
            @Override
            public void onChanged(AuthResource<BaseResponse> responseAuthResource) {
                mAuthResource.setValue(responseAuthResource);
                mAuthResource.removeSource(source);
            }
        });

    }
    public LiveData<AuthResource<BaseResponse>> observeSend() {
        return mAuthResource;
    }


}
