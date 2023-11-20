package com.nicare.ves.ui.splash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nicare.ves.BuildConfig;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.ui.auth.OutDatedActivity;
import com.nicare.ves.ui.auth.device.DeviceBlockedActivity;
import com.nicare.ves.ui.auth.device.DeviceNotVerifyFailActivity;
import com.nicare.ves.ui.auth.device.SuspendActivity;
import com.nicare.ves.ui.auth.user.AuthActivity;
import com.nicare.ves.ui.auth.user.EnrolAuthActivity;
import com.nicare.ves.ui.auth.user.PasswordAuthActivity;
import com.nicare.ves.ui.auth.user.SetPasswordActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

//import com.nicare.ees.vision.facedetection.LivePreviewActivity;

public class SplashActivity extends DaggerAppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    Intent intent;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.title)
    TextView title;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;
    SplashViewModel mSplashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashViewModel = ViewModelProviders.of(this, mViewModelProviderFactory).get(SplashViewModel.class);
        ButterKnife.bind(this);

//        try {
//            if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
//                // Enabled
////                Toast.makeText(this, "Auto time enabled", Toast.LENGTH_SHORT).show();
//            } else {
//                // Disabled
////                Toast.makeText(this, "Auto time not enabled", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }

        initHandler();
//        startActivity(new Intent(this, JSGDActivity.class));
//        startActivity(new Intent(this, HuifanActivity.class));
    }

    private void initHandler() {
        new Handler().postDelayed(() -> {

            mSplashViewModel.checkAuthStatus();
            mSplashViewModel.observeAuthStatus().observe(this, splashAuthResource -> {
                if (splashAuthResource != null) {
                    switch (splashAuthResource.status) {
                        case BLOCKED:
                            launchActivity(DeviceBlockedActivity.class);
                            break;
                        case SUSPEND:
                            launchActivity(SuspendActivity.class);
                            break;
                        case SUCCESS:
                            if (PrefUtils.getInstance(this).getMinVersionCode() > BuildConfig.VERSION_CODE) {
                                launchActivity(OutDatedActivity.class);
                            } else {
                                if (PrefUtils.getInstance(this).getCap()) {
                                    launchActivity(PasswordAuthActivity.class);
                                } else {
                                    launchActivity(EnrolAuthActivity.class);
                                }
                            }
                            break;
                        case NOT_SECURED:
                            launchActivity(SetPasswordActivity.class);
                            break;
                        case NOT_AUTHENTICATED:
                            mSplashViewModel.verifyDevice();
                            subscribeDeviceAuthObserver();
                            break;
                        case LOADING:
                            break;
                    }
                }
            });

        }, 300);
    }

    private void subscribeDeviceAuthObserver() {
        mSplashViewModel.observeAuthentication().observe(SplashActivity.this, authResponse -> {
            if (authResponse != null) {
                switch (authResponse.status) {
                    case BLOCKED:
                        launchActivity(DeviceBlockedActivity.class);
                        break;

                    case SUCCESS:

                        if (authResponse.data.getMinVersionCode() > BuildConfig.VERSION_CODE) {
                            launchActivity(OutDatedActivity.class);
                        } else {
                            if (PrefUtils.getInstance(this).getCap()) {
                                launchActivity(PasswordAuthActivity.class);
                            } else {
//                                launchActivity(AuthActivity.class);
                                intent = new Intent(getBaseContext(), AuthActivity.class);
                                intent.putExtra("deviceId", authResponse.data.getMessage());
                                startActivity(intent);
                                finish();
                            }
                        }
                        break;

                    case NOT_RECOGNISED:
                        launchActivity(DeviceNotVerifyFailActivity.class);
                        break;

                    case ERROR:
                        launchActivity(NoNetworkActivity.class);
                        break;
                    case UPDATE:
                        launchActivity(OutDatedActivity.class);
                        break;
                }
            }
        });
    }

    private void launchActivity(Class<?> cls) {
        intent = new Intent(getBaseContext(), cls);
        startActivity(intent);
        finish();
    }

    private boolean isGooglePlayServicesWithError() {
        final int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, GoogleApiAvailability.getInstance().getErrorString(status));

            // ask user to update google play services.
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, 1, status);
            dialog.show();
            return false;
        } else {
            Log.i(TAG, GoogleApiAvailability.getInstance().getErrorString(status));
            return true;
        }
    }

}
