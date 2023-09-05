package com.nicare.ves.ui.enrol.premium;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.ui.enrol.vulnerable.EnrolVulnActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PinVerificationActivity extends AppCompatActivity {
    public static final String DEPENDENT_TYPE = "dtype";
    public static final String PRINCIPAL_ID = "prId";
    public static final String IS_ACTIVE = "isActive";
    public static final String PRINCIPAL_ENROLMENT_NUMBER = "num";

    @BindView(R.id.edtPIN)
    EditText edtPIN;
    @BindView(R.id.edtNIN)
    EditText edtNIN;
    @BindView(R.id.btnVerify)
    Button btnVeriify;
    @BindView(R.id.tvError)
    TextView tvError;
    private Cursor mCursor;
    private PINActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled_dependent_pin_v);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(PINActivityViewModel.class);
    }

    @OnClick(R.id.btnVerify)
    void verify() {

        if (TextUtils.isEmpty(edtPIN.getText().toString().trim())) {
            edtPIN.requestFocus();
            Util.showDialogueMessae(this, "Invalid NiCare ID, please enter a valid NiCare ID.", getString(R.string.error_msg));
            return;
        }

        mViewModel.getReproductive(edtPIN.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Reproductive>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Reproductive pin) {
                        if (pin != null) {
                            onVerified(pin.getNicareId(), pin.getName(), edtNIN.getText().toString());
                        } else {
                            Util.showDialogueMessae(PinVerificationActivity.this, "No matched record was found.", getString(R.string.error_msg));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Util.showDialogueMessae(PinVerificationActivity.this, "No matched record was found.", getString(R.string.error_msg));
                    }

                });
    }

    protected void onVerified(String nicareId, String name, String nin) {
        Intent intent = new Intent(this, EnrolVulnActivity.class);
        intent.putExtra(EnrolVulnActivity.GUARDIAN_NIN, nin);
        intent.putExtra(EnrolVulnActivity.GUARDIAN_NAME, name);
        intent.putExtra(EnrolVulnActivity.GUARDIAN_NICARE_ID, nicareId);

        startActivity(intent);
    }

}
