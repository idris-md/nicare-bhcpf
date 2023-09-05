package com.nicare.ves.ui.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.lifecycle.ViewModelProviders;

import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.ui.auth.device.DeviceBlockedActivity;
import com.nicare.ves.ui.auth.device.DeviceNotVerifyFailActivity;
import com.nicare.ves.ui.auth.user.AuthActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;


public class NoNetworkActivity extends DaggerAppCompatActivity {

    @BindView(R.id.btnTryAgain)
    Button btnTry;
    @Inject
    ViewModelProviderFactory mProviderFactory;
    NoNetworkViewModel mViewModel;
    private Intent intent;
    private ProgressDialog mDialog;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);

        ButterKnife.bind(this);
        mDialog = new ProgressDialog(this);
        mViewModel = ViewModelProviders.of(this, mProviderFactory).get(NoNetworkViewModel.class);
    }

    @OnClick(R.id.btnTryAgain)
    void retry() {
//         btnTry.setEnabled(false);
        verifyDevice();
    }

    private void verifyDevice() {

        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setTitle("Please wait...");
        mDialog.setMessage("Retrying Connection");
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        subscribeDeviceAuthObserver();
        mViewModel.verifyDevice();

//        mDisposable.add(authService.deviceVerification(new DeviceInfo("device_verify", mDeviceManufacture + " " + mDeviceModel, mDeviceId, mDeviceIMEI))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<AuthResponse>() {
//                    @Override
//                    public void onSuccess(AuthResponse authResponse) {
//                        switch (authResponse.getStatus()) {
//                            case 200:
//                                mDialog.dismiss();
//                                intent = new Intent(NoNetworkActivity.this, AuthActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//
//                            //Device Blocked
//                            case 212:
//                                intent = new Intent(NoNetworkActivity.this, DeviceBlockedActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                                break;
//
//                            case 404:
//                                mDialog.dismiss();
//                                intent = new Intent(NoNetworkActivity.this, DeviceNotVerifyFailActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//
//                            default:
//                                mDialog.dismiss();
//                                Util.showDialogueMessae(NoNetworkActivity.this, authResponse.getMessage(), "Message");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mDialog.dismiss();
//                        Toast.makeText(NoNetworkActivity.this, "Reconnection attempt failed, try again.", Toast.LENGTH_SHORT).show();
//                    }
//                }));
    }


    private void subscribeDeviceAuthObserver() {
        mViewModel.observeAuthentication().observe(NoNetworkActivity.this, authResponse -> {
            if (authResponse != null) {
                mDialog.dismiss();
                switch (authResponse.status) {
                    case LOADING:

                        break;
                    case BLOCKED:
                        gotoBlocked();
                        break;

                    case SUCCESS:
                        intent = new Intent(NoNetworkActivity.this, AuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case NOT_RECOGNISED:
                        gotoNotVerified();
                        break;

                    case ERROR:
                        Util.showDialogueMessae(NoNetworkActivity.this, authResponse.data.getMessage(), "Message");
                        break;

                }
            }
        });
    }

    private void gotoNotVerified() {
        intent = new Intent(NoNetworkActivity.this, DeviceNotVerifyFailActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoBlocked() {
        intent = new Intent(this, DeviceBlockedActivity.class);
        startActivity(intent);
        finish();
    }
}
