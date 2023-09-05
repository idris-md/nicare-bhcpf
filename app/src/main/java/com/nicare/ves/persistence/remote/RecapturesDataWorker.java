//package com.nicare.ves.persistence.remote;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.provider.Settings;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.work.RxWorker;
//import androidx.work.WorkerParameters;
//
//import com.nicare.ves.BuildConfig;
//import com.nicare.ves.common.BroadcastHelper;
//import com.nicare.ves.common.Constant;
//import com.nicare.ves.common.PrefUtils;
//import com.nicare.ves.persistence.HUWEDatabase;
//import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
//import com.nicare.ves.persistence.local.localdatasources.LocalFacilityDatasource;
//import com.nicare.ves.persistence.local.localdatasources.LocalFingerprintDataSource;
//import com.nicare.ves.persistence.local.localdatasources.LocalTransactionDataSource;
//import com.nicare.ves.persistence.local.localdatasources.LocalVulnerableDataSource;
//import com.nicare.ves.persistence.remote.api.AuthApi;
//import com.nicare.ves.persistence.remote.api.NiCareAPI;
//import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponseSingle;
//import com.nicare.ves.persistence.remote.apimodels.ApiResponse.RowID;
//import com.nicare.ves.persistence.remote.apimodels.ApiResponse.UploadResponse;
//import com.nicare.ves.persistence.remote.apimodels.Transaction;
//import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;
//import com.nicare.ves.persistence.remote.apimodels.requestmodel.VulnerableRequest;
//import com.nicare.ves.persistence.remote.remotedatasource.RemoteEnroleeDatasource;
//
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//import io.reactivex.Single;
//import io.reactivex.schedulers.Schedulers;
//import retrofit2.Call;
//import retrofit2.Response;
//
//@SuppressLint("CheckResult")
//public class RecapturesDataWorker extends RxWorker {
//
//    PrefUtils mManager;
//    private String mDeviceModel;
//    private String mDeviceManufacture;
//    private String mDeviceId;
//    private String mDeviceIMEI;
//    private String token;
//
//    private RemoteEnroleeDatasource mRemoteEnroleeDatasource;
//    private NiCareAPI api;
//
//    Transaction mTransaction;
//
//    LocalFingerprintDataSource mLocalFingerprintDataSource;
//    LocalTransactionDataSource mLocalTransactionDataSource;
//    LocalVulnerableDataSource repository;
//
//    Context mContext;
//
//    public RecapturesDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//        mContext = context;
//        api = NiCareClient.NiCareClient(getApplicationContext()).create(NiCareAPI.class);
//        mRemoteEnroleeDatasource = new RemoteEnroleeDatasource(mContext);
//
//        mContext = context;
////        EnrolmentRequest request = null;
////        mLocalEnrolDataSource = new LocalEnrolDataSource(context);
////        mLocalChildDataSource = new LocalChildDataSource(context);
////        mLocalSpouseDataSource = new LocalSpouseDataSource(context);
//        mLocalFingerprintDataSource = new LocalFingerprintDataSource(context);
//        mRemoteEnroleeDatasource = new RemoteEnroleeDatasource(context);
////      mLocalUnCapturedBeneficiaryDataSource = new LocalUnCapturedBeneficiaryDataSource(getApplicationContext());
//        mLocalTransactionDataSource = new LocalTransactionDataSource(context);
//        repository = new LocalVulnerableDataSource(getApplicationContext());
//
//    }
//
//    @NonNull
//    @Override
//    public Single<Result> createWork() {
//        Log.i("RID", "initialized worker");
//
//        mManager = PrefUtils.getInstance(getApplicationContext());
//        TelephonyManager manage = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//
//
//        //////////////////////////////////// UPLOADS START HERE //////////////////////////////////////////
//        String pwd = mManager.getPwd();
//        String user = mManager.getUser();
//        mDeviceIMEI = manage.getDeviceId();
//
//        mDeviceModel = Build.MODEL;
//        mDeviceManufacture = Build.MANUFACTURER;
//        mDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        AuthApi loginService = NiCareClient.NiCareClient(mContext).create(AuthApi.class);
//        return loginService.loginSingle(new LoginRequest(user, pwd, mDeviceManufacture + " " + mDeviceModel, mDeviceId, mDeviceIMEI, BuildConfig.VERSION_CODE))
//                .doOnSuccess(loginResponseSingle -> {
//
//                })
//                .map(
//                        loginResponse -> {
//                            int status = loginResponse.getStatus();
////                          Log.i("RID", "worker login response");
//
//                            switch (status) {
//
//                                case 200:
////                                    Log.e("RID", "Logged in");
//                                    processLoginResponse(loginResponse);
//
//                                    LocalFacilityDatasource repository = new LocalFacilityDatasource(getApplicationContext());
//                                    if (loginResponse.getFacilities().size() > 0) {
//                                        repository.insert(loginResponse.getFacilities())
//                                                .doOnComplete(() -> {
//                                                })
//
//                                                .subscribe();
//                                    }
//
//                                    LocalVulnerableDataSource dataSource = new LocalVulnerableDataSource(getApplicationContext());
//                                    if (loginResponse.getReCaptureList().size() > 0) {
//                                        dataSource.insertRecaptures(loginResponse.getReCaptureList()).doOnComplete(() -> {
//                                        }).subscribe();
//                                    }
//
//                                    return Result.success();
//
//                                case 300:
//                                    return Result.failure();
//
//                                //Suspend EO
//                                case 111:
//
//                                    //////////////
//                                    mManager.setAccountSuspended(true);
//                                    BroadcastHelper.sendUserStatusBroadcast(mContext, status);
//                                    return Result.failure();
//
//                                // Deactivated EO
//                                case 112:
//
//                                    clearDB(true);
//                                    mManager.setAccountDeactivated(true);
//                                    BroadcastHelper.sendUserStatusBroadcast(mContext, status);
//                                    return Result.retry();
//
//                                //Suspend DEVICE
//                                case 211:
//
//                                    //////////////
//                                    mManager.setDeviceSuspend(true);
//                                    BroadcastHelper.sendUserStatusBroadcast(mContext, status);
//                                    return Result.retry();
//
//                                // Blocked DEVICE
//                                case 212:
//                                    clearDB(false);
//                                    mManager.setDeviceBlocked(true);
//                                    BroadcastHelper.sendUserStatusBroadcast(mContext, status);
//                                    return Result.failure();
//                                case 113:
//                                    mManager.setMinVersionCode(loginResponse.getMinVersionCode());
//                                    mManager.setLatestVersionUrl(loginResponse.getDownloadUrl());
//                                    mManager.setLatestVersionName(loginResponse.getVersionName());
//                                    BroadcastHelper.sendUserStatusBroadcast(mContext, status);
//                                    return Result.failure();
//
//
//                                default:
//                                    return Result.failure();
//                            }
//
//
//                        }
//                )
//
//                .onErrorReturn(throwable -> {
////                    Toast.makeText(mContext, "Failed poooooooo", Toast.LENGTH_LONG).show();
//                    Log.e("RID exception", throwable.getMessage());
//
//                    throwable.printStackTrace();
//                    return Result.failure();
//                });
//
//    }
//
//    private void processLoginResponse(LoginResponseSingle loginResponse) {
//        if (mManager.isAccountDeactivated()) {
//            mManager.setLogin(false);
//            mManager.setActivated(false);
//            mManager.setAccountDeactivated(false);
//            BroadcastHelper.sendUserStatusBroadcast(mContext, 123);
//
//        } else {
//
//            token = loginResponse.getToken();
//            mManager.setToken(loginResponse.getToken());
//            mManager.setLGA(loginResponse.getLga());
//            mManager.setAccountDeactivated(false);
//            mManager.setMinVersionCode(loginResponse.getMinVersionCode());
//            mManager.setLatestAppVersionCode(loginResponse.getVersionCode());
//            mManager.setLatestVersionUrl(loginResponse.getDownloadUrl());
//            mManager.setLatestVersionName(loginResponse.getVersionName());
//            mManager.setAccountSuspended(false);
//            mManager.setDeviceSuspend(false);
//
//            // Create a Constraints object that defines when the task should run
//
//
////            Constraints constraints = new Constraints.Builder()
////                    //.setRequiresDeviceIdle(true)
////                    .setRequiredNetworkType(NetworkType.CONNECTED)
////                    .build();
////
////            OneTimeWorkRequest uploadsWorker = new OneTimeWorkRequest.Builder(Uploads.class)
////                    .setConstraints(constraints)
////                    .build();
////
////            WorkManager.getInstance(mContext).enqueueUniqueWork(
////                    "sync",
////                    ExistingWorkPolicy.KEEP,
////                    uploadsWorker
////            );
//        }
//    }
//
//    private void clearDB(boolean isEO) {
//        HUWEDatabase mDatabase = HUWEDatabase.getInstance(getApplicationContext());
//        mDatabase.clearAllTables();
//
//        if (!isEO) {
//            SharedPreferences preferences = mContext.getSharedPreferences("NiCare", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear().apply();
//        }
//
//    }
//
//    private void uploadVulnerables() {
//        //////////////////// Upload VULNERABLE [START HERE]
//        VulnerableRequest vulnerables = getVulnerables();
//
////        Gson gson = new GsonBuilder()
////                .serializeNulls()
////                .setLenient()
////                .create();
////        String string = gson.toJson(vulnerables);
////        Log.i("RID DATA-UP", string);
////
////        writeToFile(string, mContext);
//
//        if (vulnerables.getVulnerables().size() > 0) {
//            Call<UploadResponse> callVulnerable = mRemoteEnroleeDatasource.uploadVulnerables(vulnerables);
//            Response<UploadResponse> response = null;
//            try {
//                response = callVulnerable.execute();
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        switch (response.body().getStatus()) {
//                            case 200:
//
//                                int count = 0;
//                                for (RowID id : response.body().getPin()) {
//                                    if (id.getRid() != null) {
//                                        count++;
//                                        mLocalFingerprintDataSource.delete(id.getId(), Constant.BENEFICIARY_TYPE_VULNERABLE).subscribeOn(Schedulers.io())
//                                                .subscribe();
//                                        repository.delete(id.getId()).subscribeOn(Schedulers.io())
//                                                .subscribe();
//
//                                        Log.e("RID", count + " " + id.getRid());
//
//                                    }
//                                }
//
//                                BroadcastHelper.sendSyncStatusBroadcast(mContext, "Synchronization Complete");
//
//                                mTransaction.setSync(mTransaction.getSync() + count);
//                                mLocalTransactionDataSource.insertOrUpdate(mTransaction).subscribeOn(Schedulers.io())
//                                        .subscribe();
//                                break;
//
//                        }
//                    }
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                callVulnerable.cancel();
//            }
//        }
//        //////////////////// Upload VULNERABLE [END HERE]
//    }
//
//    private VulnerableRequest getVulnerables() {
//        VulnerableRequest vulnerable_upload = null;
//        try {
//            List<Vulnerable> vulnerables = repository.principalsList();
//            for (Vulnerable vulnerable : vulnerables) {
//                vulnerable.setBiometric(mLocalFingerprintDataSource.getFingerprint(vulnerable.getId(), Constant.BENEFICIARY_TYPE_VULNERABLE));
//            }
//
//            vulnerable_upload = new VulnerableRequest(vulnerables);
//
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return vulnerable_upload;
//    }
//
//    @Override
//    public void onStopped() {
//        super.onStopped();
//
//
//    }
//}
