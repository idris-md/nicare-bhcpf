package com.nicare.ves.ui.auth.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.persistence.remote.NiCareClient;
import com.nicare.ves.persistence.remote.api.AuthApi;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.ActivationRequest;
import com.nicare.ves.persistence.remote.apimodels.requestmodel.ActivationResponse;
import com.nicare.ves.ui.auth.user.AuthActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceNotVerifyFailActivity extends DaggerAppCompatActivity {

    @BindView(R.id.tvError)
    TextView tvError;

    @BindView(R.id.groupError)
    Group groupError;

    @BindView(R.id.btn_activate)
    Button btnActivate;

    @BindView(R.id.btn_clear)
    Button btnClear;

    @BindView(R.id.txtAuth)
    TextInputEditText txtAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activator_fragment);
        ButterKnife.bind(this);
        tvError.setText("");


    }


    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_activate)
    void activate() {
        tvError.setText("");
        btnActivate.setClickable(false);
        btnClear.setClickable(false);
        txtAuth.setEnabled(false);

        String mDeviceModel;
        String mDeviceManufacture;
        String mDeviceId;
        String mDeviceIMEI;

//      TelephonyManager manager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        mDeviceModel = Build.MODEL;
        mDeviceManufacture = Build.MANUFACTURER;
        mDeviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
//      mDeviceIMEI = "manager.getDeviceId()";

        ActivationRequest deviceInfo = new ActivationRequest();
        deviceInfo.setDeviceId(mDeviceId);
        deviceInfo.setDeviceModel(mDeviceManufacture + " " + mDeviceModel);
        deviceInfo.setAuthorizationCode(txtAuth.getText().toString());

        AuthApi loginService = NiCareClient.NiCareClient(this).create(AuthApi.class);
        loginService.sendStatusRequestAsync(deviceInfo).enqueue(new Callback<ActivationResponse>() {
            @Override
            public void onResponse(Call<ActivationResponse> call, Response<ActivationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                        intent.putExtra("deviceId", response.body().getCode());
                        startActivity(intent);
                    } else {

                        tvError.setText(response.body().getMessage());
                        btnActivate.setClickable(true);
                        btnClear.setClickable(true);
                        txtAuth.setEnabled(true);
//                        tvError.setText(response.body().getMessage());
//                        Util.showDialogueMessae(getApplicationContext(), response.message(), "Activation Error");
                    }
                } else {
                    groupError.setVisibility(View.VISIBLE);
                    tvError.setText("Activation request failed");
                    btnActivate.setClickable(true);
                    btnClear.setClickable(true);
                    txtAuth.setEnabled(true);
//                    tvError.setText(response.body().getMessage());
//                    Util.showDialogueMessae(getApplicationContext(), response.message(), "Activation Error");
                }
            }

            @Override
            public void onFailure(Call<ActivationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
