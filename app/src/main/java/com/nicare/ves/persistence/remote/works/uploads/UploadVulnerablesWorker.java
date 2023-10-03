package com.nicare.ves.persistence.remote.works.uploads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicare.ves.common.Constant;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalTransactionDataSource;
import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.RowID;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.UploadResponse;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.VulnerableRequest;
import com.nicare.ves.persistence.remote.remotedatasource.RemoteEnroleeDatasource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class UploadVulnerablesWorker extends Worker {
    Transaction mTransaction;

    LocalFingerprintDataSource mLocalFingerprintDataSource;
    LocalTransactionDataSource mLocalTransactionDataSource;
    LocalVulnerableDataSource repository;

    RemoteEnroleeDatasource mRemoteEnroleeDatasource;
    Context mContext;

    public UploadVulnerablesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
//        EnrolmentRequest request = null;
//        mLocalEnrolDataSource = new LocalEnrolDataSource(context);
//        mLocalChildDataSource = new LocalChildDataSource(context);
//        mLocalSpouseDataSource = new LocalSpouseDataSource(context);
        mLocalFingerprintDataSource = new LocalFingerprintDataSource(context);
        mRemoteEnroleeDatasource = new RemoteEnroleeDatasource(context);
//      mLocalUnCapturedBeneficiaryDataSource = new LocalUnCapturedBeneficiaryDataSource(getApplicationContext());
        mLocalTransactionDataSource = new LocalTransactionDataSource(context);
        repository = new LocalVulnerableDataSource(getApplicationContext());
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        mTransaction = mLocalTransactionDataSource.
                getTodayLog()
                .subscribeOn(Schedulers.newThread())
                .blockingGet();
        if (mTransaction == null) {
            mLocalTransactionDataSource.insertOrUpdate(new Transaction(Util.todayDate()))
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new MaybeObserver<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Long id) {
                            mTransaction = new Transaction(Util.todayDate());
                            mTransaction.setId(id.intValue());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
        uploadVulnerables();
        return Result.success();
    }

    private void uploadVulnerables() {
        //////////////////// Upload VULNERABLE [START HERE]
        VulnerableRequest vulnerables = getVulnerables();


        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setLenient()
                .create();
        String string = gson.toJson(vulnerables);
        Log.i("UPLOAD DATA-UP", string);

//        writeToFile(string, mContext);

        if (vulnerables.getVulnerables().size() > 0) {
            Call<UploadResponse> callVulnerable = mRemoteEnroleeDatasource.uploadVulnerables(vulnerables);
            Response<UploadResponse> response = null;
            try {
                response = callVulnerable.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        switch (response.body().getStatus()) {
                            case 200:
                                PrefUtils.getInstance(mContext).setLGAEnrolmentLimit(response.body().getLgaLimit());
                                int count = 0;
                                for (RowID id : response.body().getPin()) {
                                    if (id.getRid() != null) {
                                        count++;
                                        mLocalFingerprintDataSource.delete(id.getId(), Constant.BENEFICIARY_TYPE_VULNERABLE).subscribeOn(Schedulers.io())
                                                .subscribe();
                                        repository.delete(id.getId()).subscribeOn(Schedulers.io())
                                                .subscribe();

                                        Log.e("RID", count + " " + id.getRid());

                                    }
                                }

                                mTransaction.setSync(mTransaction.getSync() + count);
                                mLocalTransactionDataSource.insertOrUpdate(mTransaction).subscribeOn(Schedulers.io())
                                        .subscribe();
                                break;

                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //////////////////// Upload VULNERABLE [END HERE]
    }


    private VulnerableRequest getVulnerables() {
        VulnerableRequest vulnerable_upload = null;
        try {
            List<Vulnerable> vulnerables = repository.principalsList();
            for (Vulnerable vulnerable : vulnerables) {
                vulnerable.setBiometric(mLocalFingerprintDataSource.getFingerprint(vulnerable.getId(), Constant.BENEFICIARY_TYPE_VULNERABLE));
            }
            vulnerable_upload = new VulnerableRequest(vulnerables);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return vulnerable_upload;
    }

//    private void writeToFile(String data, Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("rahmat_data.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        } catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
}
