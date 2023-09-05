package com.nicare.ves.ui.auth.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.nicare.ves.BuildConfig;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponse;
import com.nicare.ves.ui.auth.OutDatedActivity;
import com.nicare.ves.ui.auth.device.DeviceBlockedActivity;
import com.nicare.ves.ui.auth.device.SuspendActivity;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class AuthActivity extends DaggerAppCompatActivity {

    @BindView(R.id.tvDeviceId)
    TextView tvDeviceId;

    @BindView(R.id.txtId)
    TextInputEditText edtEmail;
    @BindView(R.id.txtPassword)
    TextInputEditText edtPassword;
    @BindView(R.id.btnLogin)
    MaterialButton btnLogin;
    @BindView(R.id.tvForgot)
    TextView tvForgot;
    CompositeDisposable mDisposable = new CompositeDisposable();
    @Inject
    ViewModelProviderFactory mProviderFactory;
    AuthViewModel mAuthViewModel;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String id = getIntent().getStringExtra("deviceID");
        ButterKnife.bind(this);
        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Please wait, signing in..")
                .build();

        tvDeviceId.setText(id);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // Check for READ_PHONE_STATE permission before accessing sensitive information
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                // Get the device ID
//                String deviceId = telephonyManager.getDeviceId();
//                // Get the IMEI
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    String imei = telephonyManager.getImei();
//                    tvDeviceId.setText(imei);
//                }
//                // Use the device ID and IMEI as needed
//            } else {
//                // Request the permission from the user
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
//                }
//            }
//        }

        mAuthViewModel = ViewModelProviders.of(this, mProviderFactory).get(AuthViewModel.class);
        mAuthViewModel.observeAuthentication().observe(this, new Observer<AuthResource<LoginResponse>>() {
            @Override
            public void onChanged(AuthResource<LoginResponse> loginResponseAuthResource) {
                hideDialogue();
                if (loginResponseAuthResource != null) {
                    switch (loginResponseAuthResource.status) {
                        case ERROR:
                            hideDialogue();
                            Util.showDialogueMessae(AuthActivity.this, loginResponseAuthResource.message, "Server Error");
                            break;
                        case SUSPENDED:
                            startActivity(new Intent(AuthActivity.this, SuspendActivity.class));
                            finish();
                            break;
                        case SUCCESS:
                            proceed(loginResponseAuthResource.data);
                            break;
                        case NOT_AUTHENTICATED:
                            Util.showDialogueMessae(AuthActivity.this, loginResponseAuthResource.message, "Server Message");
                            break;
                        case LOADING:
                            mAlertDialog.show();
                            break;
                        case BLOCKED:
                            startActivity(new Intent(AuthActivity.this, DeviceBlockedActivity.class));
                            finish();
                            break;

                        case UPDATE:
                            startActivity(new Intent(AuthActivity.this, OutDatedActivity.class));
                            finish();
                            break;


                    }
                }
            }
        });

    }

    @OnClick(R.id.btnLogin)
    void login() {

        btnLogin.setEnabled(false);
        mAlertDialog.show();

        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {

            hideDialogue();
            Util.showDialogueMessae(AuthActivity.this, "Incorrect login credential please provide valid  and password", "Invalid Login");
            edtEmail.requestFocus();

        } else {
            mAuthViewModel.authenticate(edtEmail.getText().toString(), edtPassword.getText().toString());
        }

    }

    private void proceed(LoginResponse response) {
        mAlertDialog.dismiss();
        PrefUtils.getInstance(this).setMinVersionCode(response.getMinVersionCode());
        if (response.getMinVersionCode() > BuildConfig.VERSION_CODE) {
            startActivity(new Intent(this, OutDatedActivity.class));
        } else {
            Intent intent = new Intent(this, WardActivity.class);
            intent.putExtra(WardActivity.WARDS, (Serializable) response.getWard());
            startActivity(intent);
            finish();
        }
    }

    private void hideDialogue() {
        mAlertDialog.dismiss();
        btnLogin.setEnabled(true);
    }

    @OnClick(R.id.tvForgot)
    void close() {
        startActivity(new Intent(this, PasswordRecoveryActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlertDialog.dismiss();
        mDisposable.clear();
    }
}