package com.nicare.ves.ui.main;

import android.app.Application;
import android.os.StrictMode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.nicare.ves.BuildConfig;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.local.localdatasources.LocalLGADataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalTransactionDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalWardDataSource;
import com.nicare.ves.persistence.remote.TokenReValidationWorker;
import com.nicare.ves.persistence.remote.apimodels.Address;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.persistence.remote.works.uploads.FetchRecaptureWorker;
import com.nicare.ves.persistence.remote.works.uploads.FetchReproductiveWorker;
import com.nicare.ves.persistence.remote.works.uploads.UploadRecaptureWorker;
import com.nicare.ves.persistence.remote.works.uploads.UploadVulnerablesWorker;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class MainActivityViewModel extends ViewModel {
    private LocalLGADataSource mLocalLGADataSource;
    private LocalWardDataSource mLocalWardDataSource;
    private LocalVulnerableDataSource mLocalPINDataSource;
    private LocalTransactionDataSource mLocalTransactionDataSource;

    private Application mApplication;

    @Inject
    public MainActivityViewModel(Application application) {
        mApplication = application;
        mLocalPINDataSource = new LocalVulnerableDataSource(mApplication);
        mLocalTransactionDataSource = new LocalTransactionDataSource(mApplication);
        mLocalWardDataSource = new LocalWardDataSource(mApplication);
        mLocalLGADataSource = new LocalLGADataSource(mApplication);

    }

    public LiveData<Transaction> getTransaction() {
        return mLocalTransactionDataSource.getLiveTodayLog();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return mLocalTransactionDataSource.getTransactions();
    }

    public LiveData<Integer> getPinsUnused() {
        return mLocalPINDataSource.allData();
    }


    public LiveData<List<Ward>> getWardsByLG(String lga) {
        return mLocalWardDataSource.getWardByLga(lga);
    }

    public Maybe<Ward> getWardById() {
        return mLocalWardDataSource.getWardById((PrefUtils.getInstance(mApplication)).getWard());
    }

    public Maybe<Address> getLGAById() {
        return mLocalLGADataSource.getAddress((PrefUtils.getInstance(mApplication)).getLGA(), (PrefUtils.getInstance(mApplication)).getWard());
    }

    public Maybe<Long> insertOrUpdateTransaction(Transaction transaction) {
        return mLocalTransactionDataSource.insertOrUpdate(transaction);
    }

    WorkManager mWorkManager = WorkManager.getInstance(mApplication);

    Constraints workConstraints = new Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();
    OneTimeWorkRequest periodicWork = new OneTimeWorkRequest.Builder(TokenReValidationWorker.class)
            .setConstraints(workConstraints)
            .build();

    OneTimeWorkRequest uploadWork = new OneTimeWorkRequest.Builder(UploadVulnerablesWorker.class)
            .setConstraints(workConstraints)
            .build();
    OneTimeWorkRequest uploadRecaptureWork = new OneTimeWorkRequest.Builder(UploadRecaptureWorker.class)
            .setConstraints(workConstraints)
            .build();
    OneTimeWorkRequest fetchRecaptureWorker = new OneTimeWorkRequest.Builder(FetchRecaptureWorker.class)
            .setConstraints(workConstraints)
            .build();
    OneTimeWorkRequest fetchReproductiveWorker = new OneTimeWorkRequest.Builder(FetchReproductiveWorker.class)
            .setConstraints(workConstraints)
            .build();

    void initPeriodicWorker() {
        mWorkManager.beginUniqueWork("token_refresh",
                        ExistingWorkPolicy.REPLACE,
                        periodicWork)
                .then(uploadWork)
                .then(uploadRecaptureWork)
                .then(fetchRecaptureWorker)
                .then(fetchReproductiveWorker)
                .enqueue();

    }

    LiveData<WorkInfo> observerAuthWork() {
        return mWorkManager.getWorkInfoByIdLiveData(periodicWork.getId());
    }

    LiveData<WorkInfo> observerSyncWork() {
        return mWorkManager.getWorkInfoByIdLiveData(uploadWork.getId());
    }

    LiveData<WorkInfo> observeSyncWo() {
        return mWorkManager.getWorkInfoByIdLiveData(uploadRecaptureWork.getId());
    }

    public void cancelAllWork() {
        mWorkManager.cancelAllWork();
    }

    public void enableStrictMode() {

        if (BuildConfig.DEBUG) {

            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();

            StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();

            StrictMode.setThreadPolicy(threadPolicy);
            StrictMode.setVmPolicy(vmPolicy);

        }
    }


}
