package com.nicare.ves.ui.auth.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;
import com.nicare.ves.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PinRecoveryActivity extends AppCompatActivity {

    @BindView(R.id.txtQ)
    TextInputEditText txtQ;
    @BindView(R.id.txtAnswer)
    TextInputEditText txtAnswer;
    @BindView(R.id.txtPIN)
    TextInputEditText txtPIN;
    @BindView(R.id.txtConfirm)
    TextInputEditText txtConfirm;
    EOAuthModel mEOAuthModel;
    EnrolAuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_recovery);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(EnrolAuthViewModel.class);
        viewModel.getAuth().observe(this, new Observer<EOAuthModel>() {
            @Override
            public void onChanged(EOAuthModel eoAuthModel) {
                mEOAuthModel = eoAuthModel;
            }
        });
        txtQ.setText(PrefUtils.getInstance(this).getSQuestion());
        txtAnswer.requestFocus();
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btnOkay)
    void okay() {
        if (txtAnswer.getText() == null) {
            Util.showDialogueMessae(this, "Please enter a valid answer", "No answer");
            return;
        }

        if (mEOAuthModel.getAnswer().equals(Util.encryptThisString(txtAnswer.getText().toString()))) {
            if (txtPIN.getText().toString().length() >= 4 && txtConfirm.getText().toString().length() >= 4) {
                if (txtPIN.getText().toString().equals(txtConfirm.getText().toString())) {
                    //////////
                    mEOAuthModel.setPassword(Util.encryptThisString(txtConfirm.getText().toString()));
                    PrefUtils.getInstance(this).setActivated(true);

                    viewModel.insert(mEOAuthModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new MaybeObserver<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Long aLong) {
                                    Intent intent = new Intent(PinRecoveryActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
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
            }
        } else {
            Util.showDialogueMessae(this, "Incorrect answer", "No answer");
        }

    }
}