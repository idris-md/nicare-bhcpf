package com.nicare.ves.ui.auth.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SetPasswordActivity extends DaggerAppCompatActivity {

    @BindView(R.id.txtPIN)
    TextInputEditText txtPIN;
    @BindView(R.id.txtQ)
    TextInputEditText txtQ;
    @BindView(R.id.txtAnswer)
    TextInputEditText txtAnswer;
    @BindView(R.id.txtConfirm)
    TextInputEditText txtConfirm;
    @BindView(R.id.btnOkay)
    MaterialButton btnOkay;

    EnrolAuthViewModel mEnrolAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        mEnrolAuthViewModel = ViewModelProviders.of(this).get(EnrolAuthViewModel.class);

        txtQ.requestFocus();
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btnOkay)
    void click() {
        if (txtPIN.getText() != null && txtConfirm.getText() != null && txtQ.getText() != null && txtAnswer.getText() != null) {

            if (txtQ.getText().toString().trim().length() < 5) {
                Util.showDialogueMessae(this, "Your question is too short", "Question too short");
                return;
            }
            if (txtQ.getText().toString().trim().length() < 1) {
                Util.showDialogueMessae(this, "Your question is too short", "Question too short");
                return;
            }

            if (txtPIN.getText().toString().length() >= 4 && txtConfirm.getText().toString().length() >= 4) {
                if (txtPIN.getText().toString().equals(txtConfirm.getText().toString())) {

                    EOAuthModel mEOAuthModel = new EOAuthModel();
                    mEOAuthModel.setQuestion(txtQ.getText().toString());
                    mEOAuthModel.setAnswer(Util.encryptThisString(txtAnswer.getText().toString()));
                    mEOAuthModel.setPassword(Util.encryptThisString(txtConfirm.getText().toString()));

                    mEnrolAuthViewModel.insert(mEOAuthModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new MaybeObserver<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Long aLong) {


                                    PrefUtils.getInstance(SetPasswordActivity.this).setActivated(true);
                                    PrefUtils.getInstance(SetPasswordActivity.this).setCap(true);
                                    PrefUtils.getInstance(SetPasswordActivity.this).setLogin(true);


                                    startActivity(new Intent(SetPasswordActivity.this, PasswordAuthActivity.class));
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


//                    Intent intent = new Intent(this, PasswordAuthActivity.class);
//                    intent.putExtra(PasswordAuthActivity.QUESTION, txtQ.getText().toString());
//                    intent.putExtra(EnrolAuthActivity.ANSWER, Util.encryptThisString(txtAnswer.getText().toString()));
//                    intent.putExtra(EnrolAuthActivity.PIN, Util.encryptThisString(txtConfirm.getText().toString()));
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();

                } else {
                    //////// Premium don't match
                    clearFields();
                    Util.showDialogueMessae(this, "Please enter same PIN on both fields to confirm", "PIN's not match");

                }
            } else {
                /////////////PIN too short
                clearFields();
                Util.showDialogueMessae(this, "PIN too short minimum PIN length is four", "PIN's too short");
            }


        } else {
            ////////// Please provide Premium
            clearFields();
            Util.showDialogueMessae(this, "Please provide your desired security PIN to proceed", "Invalid Input");

        }
    }

    private void clearFields() {
        txtConfirm.getText().clear();
        txtPIN.getText().clear();
    }
}
