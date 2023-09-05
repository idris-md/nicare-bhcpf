package com.nicare.ves.ui.auth.device;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProviders;

import com.nicare.ves.R;
import com.nicare.ves.common.Cryptography;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.LoginRequest;
import com.nicare.ves.ui.auth.user.AuthActivity;
import com.nicare.ves.ui.auth.user.PasswordAuthActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public class SuspendActivity extends DaggerAppCompatActivity {
    @BindView(R.id.imageView2)
    ImageView imageView2;

    @BindView(R.id.btnRetry)
    Button btnRetry;
    Intent intent;

    SuspendViewModel mSuspendViewModel;
    @Inject
    ViewModelProviderFactory mProviderFactory;
    LoginRequest mLoginRequest;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suspend);
        ButterKnife.bind(this);
        mSuspendViewModel = ViewModelProviders.of(this, mProviderFactory).get(SuspendViewModel.class);
        mSuspendViewModel.observeAuthentication().observe(this, loginResponseAuthResource -> {

            if (loginResponseAuthResource != null) {
                switch (loginResponseAuthResource.status) {
                    case ERROR:
                        btnRetry.setText("RETRY");
                        btnRetry.setEnabled(true);
                        break;

                    case SUCCESS:
                        Cryptography cryptography = new Cryptography(SuspendActivity.this);
                        try {
                            cryptography.encryptData("V293ISBob3cgY3VyaW91cyBlaD8=");

                            PrefUtils instance = PrefUtils.getInstance(getBaseContext());
                            instance.setDeviceId(loginResponseAuthResource.data.getDeviceId());
                            instance.setEoName(loginResponseAuthResource.data.getName());
                            instance.setEoPhone(loginResponseAuthResource.data.getPhone());

                            instance.setToken(loginResponseAuthResource.data.getToken());
                            instance.setLatestAppVersionCode(loginResponseAuthResource.data.getVersionCode());
                            instance.setLatestVersionUrl(loginResponseAuthResource.data.getDownloadUrl());
                            instance.setLatestVersionName(loginResponseAuthResource.data.getVersionName());

                            instance.setAccountSuspended(false);
                            instance.setDeviceSuspend(false);

                            startActivity(intent);
                            finish();

                        } catch (NoSuchPaddingException | NoSuchAlgorithmException | UnrecoverableEntryException | CertificateException | KeyStoreException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | IOException e) {
                            e.printStackTrace();
                        }
                        break;


                }
            }
        });

        intent = new Intent(SuspendActivity.this, PasswordAuthActivity.class);
        if (PrefUtils.getInstance(this).isAccountDeactivated()) {
            imageView2.setImageDrawable(getResources().getDrawable(R.drawable.deactivated));
            intent = new Intent(SuspendActivity.this, AuthActivity.class);
        }

    }

    @OnClick(R.id.btnRetry)
    public void onViewClicked() {
        btnRetry.setText("Please wait");
        btnRetry.setEnabled(false);
        mSuspendViewModel.authenticate(mLoginRequest);
    }
}
