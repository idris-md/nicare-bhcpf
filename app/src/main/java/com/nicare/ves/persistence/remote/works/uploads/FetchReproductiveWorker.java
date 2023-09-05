package com.nicare.ves.persistence.remote.works.uploads;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.local.localdatasources.LocalPINDataSource;
import com.nicare.ves.persistence.remote.NiCareClient;
import com.nicare.ves.persistence.remote.api.NiCareAPI;
import com.nicare.ves.persistence.remote.remotedatasource.RemoteEnroleeDatasource;

import io.reactivex.Single;

@SuppressLint("CheckResult")
public class FetchReproductiveWorker extends RxWorker {

    private RemoteEnroleeDatasource mRemoteEnroleeDatasource;
    private NiCareAPI api;

    Context mContext;

    public FetchReproductiveWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        api = NiCareClient.NiCareClient(getApplicationContext()).create(NiCareAPI.class);
        mRemoteEnroleeDatasource = new RemoteEnroleeDatasource(mContext);
        mContext = context;
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        NiCareAPI loginService = NiCareClient.NiCareClient(mContext).create(NiCareAPI.class);
        return loginService.fetchReproductive("Bearer " + PrefUtils.getInstance(mContext).getToken())
                .doOnSuccess(loginResponseSingle -> {
                })
                .map(
                        loginResponse -> {
                            int status = loginResponse.getStatus();
                            switch (status) {
                                case 200:
                                    LocalPINDataSource dataSource = new LocalPINDataSource(getApplicationContext());
                                    if (loginResponse.getEnrollees().size() > 0) {
                                        dataSource.insertOrUpdate(loginResponse.getEnrollees())
                                                .doOnComplete(() -> {
                                                })
                                                .subscribe();
                                    }
                                    return Result.success();
                                default:
                                    return Result.failure();
                            }
                        }
                )

                .onErrorReturn(throwable -> {
//                    Toast.makeText(mContext, "Failed poooooooo", Toast.LENGTH_LONG).show();
//                    Log.e("RID exception", throwable.getMessage());
                    throwable.printStackTrace();
                    return Result.failure();
                });

    }


}
