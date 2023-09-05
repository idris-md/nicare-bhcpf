package com.nicare.ves.persistence.remote.works;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.nicare.ves.common.Constant;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class DataSyncWorker extends RxWorker {

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */

    Context mContext;

    public DataSyncWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        mContext = appContext;
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        return Observable.range(0, 1)
                .flatMapSingle(new Function<Integer, SingleSource<?>>() {
                    @Override
                    public SingleSource<List<Vulnerable>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        return getAllDataFromDB();
                    }
                }).map(datas -> {
                    return addFingerprintsToData((List<Vulnerable>) datas);
                })
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        return true;
                    }
                })
                .flatMapSingle(new Function<Object, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        return null;
                    }
                }).toList()
                .map(new Function<List<Object>, Result>() {
                    @Override
                    public Result apply(@io.reactivex.annotations.NonNull List<Object> objects) throws Exception {
                        return Result.success();
                    }
                }).onErrorReturn(new Function<Throwable, Result>() {
                    @Override
                    public Result apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return Result.failure();
                    }
                });
    }


    private Single<List<Vulnerable>> getAllDataFromDB() {
        LocalVulnerableDataSource
                repository = new LocalVulnerableDataSource(getApplicationContext());

        return repository.getAllVulnerableData();
    }

    private List<Vulnerable> addFingerprintsToData(List<Vulnerable> vulnerables) {
        LocalFingerprintDataSource mLocalFingerprintDataSource = new LocalFingerprintDataSource(mContext);

        try {
            for (Vulnerable vulnerable : vulnerables) {

                vulnerable.setBiometric(mLocalFingerprintDataSource.getFingerprint(vulnerable.getId(), Constant.BENEFICIARY_TYPE_VULNERABLE));

            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return vulnerables;
    }
}
