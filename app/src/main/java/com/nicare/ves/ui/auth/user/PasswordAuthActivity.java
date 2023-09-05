package com.nicare.ves.ui.auth.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fgtit.data.ConversionsEx;
import com.fgtit.device.Constants;
import com.fgtit.device.FPModule;
import com.fgtit.fpcore.FPMatch;
import com.google.android.material.button.MaterialButton;
import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;
import com.nicare.ves.ui.customwidget.KeyboardView;
import com.nicare.ves.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class PasswordAuthActivity extends DaggerAppCompatActivity {

    @BindView(R.id.imageView5)
    ImageView ivFpImage;
    @BindView(R.id.tvStatus)
    TextView tvFpStatu;
    private FPModule fpm = new FPModule();

    private byte bmpdata[] = new byte[Constants.RESBMP_SIZE];
    private int bmpsize = 0;

    private byte refdata[] = new byte[Constants.TEMPLATESIZE * 2];
    private int refsize = 0;

    private byte matdata[] = new byte[Constants.TEMPLATESIZE * 2];
    private int matsize = 0;

    private String refstring = "";
    private String matstring = "";

    private int worktype = 0;

    @BindView(R.id.btnVerify)
    MaterialButton btnVerify;
    @BindView(R.id.kv)
    KeyboardView mKeyboardView;
    @BindView(R.id.tvForgotten)
    TextView tvForgotten;
    private EnrolAuthViewModel mEnrolAuthViewModel;
    EOAuthModel mEoAuthModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_auth);

        int i = fpm.InitMatch();

        mEnrolAuthViewModel = ViewModelProviders.of(this).get(EnrolAuthViewModel.class);
        mEnrolAuthViewModel.getAuth().observe(this, new Observer<EOAuthModel>() {
            @Override
            public void onChanged(EOAuthModel eoAuthModel) {
                mEoAuthModel = eoAuthModel;
            }
        });

        ButterKnife.bind(this);
    }



    @OnClick(R.id.btnVerify)
    void verify() {

        if (Util.encryptThisString(mKeyboardView.getInputText().toString()).equals(mEoAuthModel.getPassword())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Util.showDialogueMessae(this, "Incorrect PIN", "PIN verification failed");
        }

    }

    @OnClick(R.id.tvForgotten)
    void forgot() {
        startActivity(new Intent(this, PinRecoveryActivity.class));
    }





}
