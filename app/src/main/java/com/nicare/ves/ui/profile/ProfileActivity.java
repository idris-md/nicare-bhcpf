package com.nicare.ves.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.BaseResponse;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.LoginResponse;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.ui.adapter.WardSpinnerAdapter;
import com.nicare.ves.ui.auth.user.AuthActivity;
import com.nicare.ves.ui.auth.user.AuthResource;
import com.nicare.ves.ui.preference.SettingActivity;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import dmax.dialog.SpotsDialog;

@SuppressLint("CheckResult")
public class ProfileActivity extends DaggerAppCompatActivity {

    private static final String TAG = "ProfileActivity";

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLG)
    TextView tvLG;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.spWard)
    MaterialSpinner spWard;
    //    @BindView(R.id.tvTotal)
//    TextView tvTotal;
    @BindView(R.id.btnDevicePassword)
    Button btnDevicePassword;
    @BindView(R.id.btnAccountPassword)
    MaterialButton btnAccountPassword;
    @BindView(R.id.btnLogout)
    MaterialButton btnLogout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    List<Transaction> transactions;
    TransactionRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;
    ProfileViewModel mViewModel;

    Dialog mPasswordDialog;
    private AlertDialog mAlertDialog;
    WardSpinnerAdapter mWardSpinnerAdapter;

    private boolean allowLogoutAndSwith;
    private boolean isChangePassword;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mWardSpinnerAdapter = new WardSpinnerAdapter(this);
        spWard.setAdapter(mWardSpinnerAdapter);

        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait...")
                .setCancelable(false)
                .build();

        mAlertDialog.setTitle("Please wait...");

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }

        mViewModel = ViewModelProviders.of(this, mViewModelProviderFactory).get(ProfileViewModel.class);
        mViewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.submitList(transactions);
            }
        });
        String lga = PrefUtils.getInstance(this).getLGA();
        Log.v("TTT", lga);

        mViewModel.getWardsByLG(lga).observe(this, new Observer<List<Ward>>() {
            @Override
            public void onChanged(List<Ward> wards) {
                Log.v("TTT", ""+wards.size());
                mWardSpinnerAdapter.notifyEntryChange(wards);
                String ward = PrefUtils.getInstance(ProfileActivity.this).getWard();
                if (ward != null) {
                    spWard.setSelection(wards.indexOf(new Ward(ward)));
                }
            }
        });

        mViewModel.allReport().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer report) {
                if (report < 1)
                    allowLogoutAndSwith = true;
            }
        });

        mViewModel.observeAuthentication().observe(this, new Observer<AuthResource<LoginResponse>>() {
            @Override
            public void onChanged(AuthResource<LoginResponse> loginResponseAuthResource) {
                if (loginResponseAuthResource != null) {
                    switch (loginResponseAuthResource.status) {

                        case SUCCESS:

                            if (isChangePassword) {
                                mViewModel.changePassword(password);
                                mViewModel.observeChangePassword().observe(ProfileActivity.this, new Observer<ChangePasswordResource<BaseResponse>>() {
                                    @Override
                                    public void onChanged(ChangePasswordResource<BaseResponse> baseResponseChangePasswordResource) {
                                        if (baseResponseChangePasswordResource != null) {
                                            switch (baseResponseChangePasswordResource.status) {

                                                case SUCCESS:
                                                    dismissDialogs();
                                                    Util.showDialogueMessae(ProfileActivity.this, baseResponseChangePasswordResource.data.getMessage(), "Password changed successfully");
                                                    break;

                                                default:
                                                    dismissDialogs();
                                                    Util.showDialogueMessae(ProfileActivity.this, baseResponseChangePasswordResource.data.getMessage(), "Operation Failed");
                                                    break;

                                            }
                                        }
                                    }
                                });
                            } else {
                                logOut();
                            }

                            break;

                        case ERROR:
                            dismissDialogs();
                            Util.showDialogueMessae(ProfileActivity.this, loginResponseAuthResource.data.getMessage(), "Password changed successfully");

                            break;
//
                    }
                }
            }
        });

        loadUI();

    }

    private void loadUI() {

        PrefUtils prefManager = PrefUtils.getInstance(this);

        transactions = new ArrayList<>();
        adapter = new TransactionRecyclerAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);
//        mViewModel.getLGAById()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(dd -> {
//                    tvLG.setText("EO ID: " + Util.toTitleCase(dd.getWard()) + " Ward, " + Util.toTitleCase(dd.getLga()));
//                });
        tvLG.setText("E.O ID: " + prefManager.getUser().toUpperCase());

//        tvLastSync.setText("Last sync: " + prefManager.getLastSync());
        tvName.setText("Full Name: " + Util.toTitleCase(prefManager.getEoName()));
        tvPhone.setText("Phone Number: " + prefManager.getEoPhone());

        adapter.notifyDataSetChanged();
        spWard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                Ward selectedWard = (Ward) materialSpinner.getAdapter().getItem(i);
                PrefUtils.getInstance(ProfileActivity.this).setWard(selectedWard.getWard_id());
                PrefUtils.getInstance(ProfileActivity.this).setAddress(selectedWard.getName());
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
    }

    private void logOut() {
        mAlertDialog.setMessage("Logging you out");
        mViewModel.logOut();
        mViewModel.observeLogout().observe(ProfileActivity.this, new Observer<LogoutResource<BaseResponse>>() {
            @Override
            public void onChanged(LogoutResource<BaseResponse> baseResponseLogoutResource) {
                if (baseResponseLogoutResource != null) {
                    switch (baseResponseLogoutResource.status) {

                        case SUCCESS:
                            clearAppData();
                            mAlertDialog.dismiss();
                            break;

                        case ERROR:
                            dismissDialogs();
                            Util.showDialogueMessae(ProfileActivity.this, baseResponseLogoutResource.data.getMessage(), "Server Error");
                            dismissDialogs();
                            break;

                    }
                }
            }
        });

//        AuthApi authService = NiCareClient.NiCareClient(getApplicationContext()).create(AuthApi.class);
//        authService.logout(new LogoutRequest("logout", PrefUtils.getInstance(this).getUser(), PrefUtils.getInstance(this).getPwd(), mDeviceManufacture + " " + mDeviceModel, mDeviceId, mDeviceIMEI), token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
//                    @Override
//                    public void onSuccess(BaseResponse logoutResponse) {
//                        switch (logoutResponse.getStatus()) {
//                            case 200:
//
//                                clearAppData();
//                                mAlertDialog.dismiss();
//
//                                break;
//                            default:
//                                Util.showDialogueMessae(ProfileActivity.this, "Logout request failed please try again later", "Server Error");
//                                dismissDialogs();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        dismissDialogs();
//                    }
//                });

    }

    @SuppressLint("StaticFieldLeak")
    private void clearAppData() {

        mAlertDialog.dismiss();
        Intent intent = new Intent(getBaseContext(), AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {

            if (allowLogoutAndSwith) {
                login();
                isChangePassword = false;
                password = null;
            } else {
                // Syncing Data first
                Util.showDialogueMessae(this, "There are record(s) waiting to be synchronize, please synchronize existing record and try again", "Logout failed");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @OnClick(R.id.btnAccountPassword)
    void showChangePasswordDialogue() {

        mPasswordDialog = new Dialog(this);
        mPasswordDialog.setContentView(R.layout.change_password_layout);

        TextInputEditText txtOldPassword = mPasswordDialog.findViewById(R.id.txtOldPassword);
        TextInputEditText txtNewPassword = mPasswordDialog.findViewById(R.id.txtPassword);
        TextInputEditText txtConfirmNewPassword = mPasswordDialog.findViewById(R.id.txtConfirmPassword);

        MaterialButton btnChange = mPasswordDialog.findViewById(R.id.btnChange);

        btnChange.setOnClickListener(v -> {
            String pwd = PrefUtils.getInstance(ProfileActivity.this).getPwd();
            if (txtNewPassword.getText().toString().equals(txtConfirmNewPassword.getText().toString()) && pwd.equals(txtOldPassword.getText().toString())) {
                mPasswordDialog.hide();
                login();
                isChangePassword = true;
                txtNewPassword.getText().toString();
            } else {
                Util.showDialogueMessae(ProfileActivity.this, "Passwords do not match", "Password Error");
                txtOldPassword.getText().clear();
                txtNewPassword.getText().clear();
                txtConfirmNewPassword.getText().clear();

            }

        });

        mPasswordDialog.setCancelable(true);
        mPasswordDialog.setCanceledOnTouchOutside(true);
        mPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPasswordDialog.show();

    }

    @OnClick(R.id.btnDevicePassword)
    void showChangePINDialogue() {

        mPasswordDialog = new Dialog(this);
        mPasswordDialog.setContentView(R.layout.change_pin_layout);

        EditText txtOldPassword = mPasswordDialog.findViewById(R.id.txtOldPassword);
        EditText txtNewPassword = mPasswordDialog.findViewById(R.id.txtPassword);
        EditText txtConfirmNewPassword = mPasswordDialog.findViewById(R.id.txtConfirmPassword);
        MaterialButton btnChange = mPasswordDialog.findViewById(R.id.btnChange);

        btnChange.setOnClickListener(v -> {

            String pin = PrefUtils.getInstance(ProfileActivity.this).getPin();
            if (txtNewPassword.getText().toString().equals(txtConfirmNewPassword.getText().toString()) && txtOldPassword.getText().toString().equals(pin)) {
                PrefUtils.getInstance(ProfileActivity.this).setPin(txtConfirmNewPassword.getText().toString());
                Util.showDialogueMessae(ProfileActivity.this, "Your device level PIN update was successfully, please use the new PIN for subsequent login to the system", "PIN changed successfully");
                mPasswordDialog.dismiss();

            } else {
                Util.showDialogueMessae(ProfileActivity.this, "Passwords do not match", "Password Error");
                txtOldPassword.getText().clear();
                txtNewPassword.getText().clear();
                txtConfirmNewPassword.getText().clear();
            }

        });

        mPasswordDialog.setCancelable(true);
        mPasswordDialog.setCanceledOnTouchOutside(true);
        mPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPasswordDialog.show();

    }

    private void login() {
        mAlertDialog.show();

        mViewModel.authenticate();

    }

    private void dismissDialogs() {
        mAlertDialog.dismiss();
        if (mPasswordDialog != null) {
            if (mPasswordDialog.isShowing()) {
                mPasswordDialog.dismiss();
            }
        }
    }

}
