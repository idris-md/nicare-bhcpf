package com.nicare.ves.ui.auth.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.api.NiCareAPI;
import com.nicare.ves.ui.adapter.WardSpinnerAdapter;
//import com.nicare.ves.ui.enrol.vulnerable.EnrolVulnActivity;
import com.nicare.ves.ui.profile.ProfileActivity;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import dmax.dialog.SpotsDialog;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WardActivity extends DaggerAppCompatActivity {

    public static final String WARDS = "wards";
    @BindView(R.id.spWard)
    MaterialSpinner spWard;
    @BindView(R.id.txtPassword)
    TextInputEditText txtPassword;
    @BindView(R.id.button5)
    MaterialButton btnProceed;
    WardSpinnerAdapter mWardSpinnerAdapter;
    @Inject
    NiCareAPI api;
    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;
    WardViewModel mWardViewModel;
    private AlertDialog mAlertDialog;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private String mToken;
    private Ward selectedWard;
    List<Ward> wards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ward);
        ButterKnife.bind(this);
        mWardSpinnerAdapter = new WardSpinnerAdapter(this);
        spWard.setAdapter(mWardSpinnerAdapter);

        mWardViewModel = ViewModelProviders.of(this, mViewModelProviderFactory).get(WardViewModel.class);
//        mWardViewModel.observeWard().observe(this, new Observer<LogoutResource<BaseResponse>>() {
//            @Override
//            public void onChanged(LogoutResource<BaseResponse> baseResponseLogoutResource) {
//                switch (baseResponseLogoutResource.status) {
//                    case SUCCESS:
//                        getPins();
//                        break;
//
//                    default:
//                        mAlertDialog.dismiss();
//                        Util.showDialogueMessae(WardActivity.this, baseResponseLogoutResource.message, "Message");
//
//                }
//            }
//        });

        String lga = PrefUtils.getInstance(this).getLGA();
        mWardViewModel.getWardsByLG(lga).observe(WardActivity.this, new Observer<List<Ward>>() {
            @Override
            public void onChanged(List<Ward> wardList) {
                mWardSpinnerAdapter.notifyEntryChange(wardList);
//                spWard.setSelection(-1);
////                        spProvider.setSelection(-1);
//                //edtResidentAddress.getText().clear();
//                mSelectedWard = null;
////                      mSelectedFacility = null;
//                if (checkRes.isChecked()) {
//                    spWard.setSelection(mWardSpinnerAdapter.getItemPosition(new Ward(mEoWard)));
//                }
            }
        });


        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Please wait, signing in..")
                .build();
        //api = Ni  CareClient.NiCareClient(getApplicationContext()).create(NiCareAPI.class);

        spWard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                selectedWard = (Ward) materialSpinner.getAdapter().getItem(i);
                PrefUtils.getInstance(WardActivity.this).setWard(selectedWard.getWard_id());
                PrefUtils.getInstance(WardActivity.this).setAddress(selectedWard.getName());
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
//        readIntentState();

    }

//    private void readIntentState() {
//        wards = (List<Ward>) getIntent().getSerializableExtra(WARDS);
//        mWardSpinnerAdapter.notifyEntryChange(wards);
//    }


//    private void getPins() {
//        mAlertDialog.setMessage("Transferring data");
//        mToken = PrefUtils.getInstance(this).getToken();
//        mDisposable.add(api.getPins("Bearer " + mToken).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<PINResponse>() {
//                    @Override
//                    public void onSuccess(PINResponse pinResponse) {
//                        int statuscode = pinResponse.getStatus();
//                        switch (statuscode) {
//                            case 200:
//
//                                if (pinResponse.getPins().size() > 0) {
//                                    LocalPINDataSource localPinDataSource = new LocalPINDataSource(getApplicationContext());
//                                    for (Premium premium : pinResponse.getPins()) {
//                                        premium.setPin(Util.encryptThisString(premium.getPin()));
//                                        premium.setSerial_no(Util.encryptThisString(premium.getSerial_no()));
//                                    }
//
//                                    localPinDataSource.insertOrUpdate(pinResponse.getPins())
//                                            .subscribeOn(Schedulers.io())
//                                            .observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(new CompletableObserver() {
//                                                @Override
//                                                public void onSubscribe(Disposable d) {
//
//                                                }
//
//                                                @Override
//                                                public void onComplete() {
//                                                    getProvider();
//                                                }
//
//                                                @Override
//                                                public void onError(Throwable e) {
//
//                                                }
//                                            });
//                                }
//
//                                getProvider();
//
//                                break;
//
//                            default:
//                                Util.showDialogueMessae(WardActivity.this, pinResponse.getMessage(), "Error message");
//                                hideDialogue();
//                                break;
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        displayNetworkError();
//                    }
//                }));
//
//
//    }

//    private void getProvider() {
//
//        mDisposable.add(api.getProviders("Bearer " + mToken).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<ProviderResponse>() {
//                    @Override
//                    public void onSuccess(ProviderResponse providerResponse) {
//                        int statusCode = providerResponse.getStatus();
//                        switch (statusCode) {
//                            case 200:
//
//                                new LocalFacilityDatasource(getApplication()).insert(providerResponse.getProviders())
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new CompletableObserver() {
//                                            @Override
//                                            public void onSubscribe(Disposable d) {
//
//                                            }
//
//                                            @Override
//                                            public void onComplete() {
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable e) {
//                                                e.printStackTrace();
//                                                hideDialogue();
//                                            }
//                                        });
//                                break;
//                            default:
//                                Util.showDialogueMessae(WardActivity.this, providerResponse.getMessage(), "Error message");
//                                hideDialogue();
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        displayNetworkError();
//                    }
//                }));
//
//    }


    private void hideDialogue() {
        mAlertDialog.dismiss();
    }

//    void getLGA() {
//
//        mDisposable.add(api.lga().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<LGAResponse>() {
//                    @Override
//                    public void onSuccess(LGAResponse lgaResponse) {
//                        if (lgaResponse != null) {
//                            switch (lgaResponse.getStatus()) {
//
//                                case 200:
//
//                                    new LocalLGADataSource(getApplication()).insert(lgaResponse.getLga())
//                                            .subscribeOn(Schedulers.io())
//                                            .observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(new CompletableObserver() {
//                                                @Override
//                                                public void onSubscribe(Disposable d) {
//
//                                                }
//
//                                                @Override
//                                                public void onComplete() {
//                                                    getWard();
//
//                                                }
//
//                                                @Override
//                                                public void onError(Throwable e) {
//                                                    e.printStackTrace();
//                                                }
//                                            });
//
//                                    break;
//
//                                case 404:
//                                    Util.showDialogueMessae(getBaseContext(), "No response from server", "");
//                                    hideDialogue();
//                                    break;
//
//                                default:
//                                    Util.showDialogueMessae(WardActivity.this, lgaResponse.getMessage(), "Error message");
//                                    hideDialogue();
//                                    break;
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        hideDialogue();
//                    }
//                }));
//
//    }

//    void getWard() {
//        //  WardRequest request = new WardRequest("ward");
//        mDisposable.add(api.ward().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<WardResponse>() {
//                    @Override
//                    public void onSuccess(WardResponse wardResponse) {
//
//                        switch (wardResponse.getStatus()) {
//                            case 200:
//
//                                new LocalWardDataSource(getApplication()).insert(wardResponse.getWard())
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new CompletableObserver() {
//                                            @Override
//                                            public void onSubscribe(Disposable d) {
//
//                                            }
//
//                                            @Override
//                                            public void onComplete() {
//                                                PrefUtils.getInstance(WardActivity.this).setLogin(true);
//
//                                                Constraints workConstraints = new Constraints.Builder()
//                                                        .setRequiresBatteryNotLow(true)
//                                                        .setRequiresDeviceIdle(true)
//                                                        .build();
//
//                                                PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(SyncWorker.class, 1, TimeUnit.HOURS)
//                                                        .setConstraints(workConstraints)
//                                                        .build();
//
//                                                mWorkManager.enqueue(periodicWork);
//
//                                                mAlertDialog.dismiss();
//                                                startActivity(new Intent(WardActivity.this, SetPasswordActivity.class));
//                                                finish();
//                                            }
//
//                                            @Override
//                                            public void onError(Throwable e) {
//                                                e.printStackTrace();
//                                            }
//                                        });
//
//                                break;
//
//                            case 404:
//
//                                Util.showDialogueMessae(getBaseContext(), "No response from server", "");
//                                hideDialogue();
//                                break;
//
//                            default:
//                                Util.showDialogueMessae(WardActivity.this, wardResponse.getMessage(), "Error message");
//                                hideDialogue();
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//                }));
//    }

    private void displayNetworkError() {
        Util.showDialogueMessae(WardActivity.this, getString(R.string.server_error), getString(R.string.network_failure));
        hideDialogue();
    }

    @OnClick(R.id.button5)
    public void onViewClicked() {
//        Cryptography cryptography = new Cryptography(this);
//        try {
//            PrefUtils.getInstance(this).setXCVFRN(cryptography.encryptData("V293ISBob3cgY3VyaW91cyBlaD8="));
//        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | CertificateException | KeyStoreException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | IOException | NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
//
        if (selectedWard == null) {
            Util.showDialogueMessae(this, "Please select ward of enrolment", "No Ward Selected");
            return;
        }
//        mAlertDialog.show();
//        mWardViewModel.setWard(selectedWard);

        startActivity(new Intent(WardActivity.this, SetPasswordActivity.class));

    }
}
