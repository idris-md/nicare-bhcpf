package com.nicare.ves.ui.enrol.recapture;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.common.Constant;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.remote.apimodels.ApiResponse.ReCapture;
import com.nicare.ves.ui.enrol.camera.CameraKitActivity;
import com.nicare.ves.ui.enrol.fingerprint.HuifanActivity;
import com.nicare.ves.ui.enrol.fingerprint.SecuGenActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReCaptureActivity extends AppCompatActivity {

    public static final String ID = "ID";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int RECAPTURE = 100;
    private static final int PHOTO_ONLY = 1;
    private static final int FINGERPRINT_ONLY = 2;
    private static final int FINGERPRINT_AND_PHOTO = 3;
    private static final int BIOMETRIC_REQUEST_CODE = 101;
    private Fingerprint mFingerprint;
    private String mPhoto;

    @BindView(R.id.txtId)
    TextInputEditText txtId;

    @BindView(R.id.tvFullName)
    TextView tvFullName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvProvider)
    TextView tvProvider;

    @BindView(R.id.tv_fp)
    TextView tvBio;

    @BindView(R.id.tv_photo)
    TextView tvPhoto;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;

    @BindView(R.id.imageViewFP)
    ImageView imageViewFP;

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.btnFacial)
    LinearLayout btnFacial;
    @BindView(R.id.btnBio)
    LinearLayout btnBio;
//    @BindView(R.id.profile)
//    MaterialCardView profile;
//    @BindView(R.id.actions)
//    MaterialCardView actions;

    ReCapture mReCapture;
    ReCaptureViewModel viewModel;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_capture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        fab.setVisibility(View.GONE);

        viewModel = ViewModelProviders.of(this).get(ReCaptureViewModel.class);

        String id = getIntent().getStringExtra(ID);
        viewModel.getUser(id).observe(this, new Observer<ReCapture>() {
            @Override
            public void onChanged(ReCapture reCapture) {

                mReCapture = reCapture;
                if (mReCapture.getIssue_code() == PHOTO_ONLY) {
                    btnBio.setVisibility(View.GONE);

                    if (mReCapture.isRecaptured()) {
                        tvPhoto.setText("Re-Captured");
                        tvBio.setText("Re-Captured");
                    }

                } else if (mReCapture.getIssue_code() == FINGERPRINT_ONLY) {
                    btnFacial.setVisibility(View.GONE);
                }

                String name = reCapture.getLastName() + " " + reCapture.getFirstName();
                if (reCapture.getOtherName() != null) {
                    name = reCapture.getLastName() + " " + reCapture.getFirstName() + " " + reCapture.getOtherName().trim();
                }

                tvFullName.setText(name);
                tvProvider.setText(reCapture.getWard());
                tvPhone.setText(reCapture.getPhone());
                if (reCapture.getPhoto() != null) {
                    ivPhoto.setImageBitmap(Util.decodeBase64ToBitmap(reCapture.getPhoto()));
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    mPhoto = data.getStringExtra("photo");
                    ivPhoto.setImageBitmap(Util.decodeBase64ToBitmap(mPhoto));
                    tvPhoto.setText("Re-Captured");
                    tvPhoto.setTextColor(getResources().getColor(R.color.iconColor));
                    ImageViewCompat.setImageTintList(imageViewPhoto, ColorStateList.valueOf(getResources().getColor(R.color.iconColor)));
                    if (mReCapture.getIssue_code() == PHOTO_ONLY) {
                        fab.setVisibility(View.VISIBLE);
                    } else if (mReCapture.getIssue_code() == FINGERPRINT_AND_PHOTO) {
                        if (mPhoto != null && mFingerprint != null) {
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
                }

                break;

            case BIOMETRIC_REQUEST_CODE:
                mFingerprint = data.getParcelableExtra("biometric");
                if (mFingerprint != null) {
                    tvBio.setText("Re-Captured");
                    tvBio.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mReCapture.getIssue_code() == FINGERPRINT_ONLY) {
                        fab.setVisibility(View.VISIBLE);
                    } else if (mReCapture.getIssue_code() == FINGERPRINT_AND_PHOTO) {
                        if (mPhoto != null && mFingerprint != null) {
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
                }
                //ivBio.setImageResource(Util.getIConID(this, "prr"));
                break;

        }
    }

    @OnClick(R.id.fab)
    void save() {

        fab.setEnabled(false);
        fab.setVisibility(View.GONE);

        mReCapture.setRecaptured(true);

        if (mPhoto != null) {
            mReCapture.setPhoto(mPhoto);
        }

        if (mFingerprint != null) {
            mFingerprint.setBeneficiary_id(mReCapture.getId());
            mFingerprint.setBeneficiary_type(Constant.BENEFICIARY_TYPE_RECAPTURE);
            viewModel.saveFingerprint(mFingerprint).subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }

        viewModel.save(mReCapture).subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> mCompositeDisposable.add(disposable)).observeOn(AndroidSchedulers.mainThread()).subscribe();

        Util.showToast("Success", false, this, this.getLayoutInflater());
        this.finish();
    }

    @OnClick(R.id.imageViewPhoto)
    void takePhoto() {
        startActivityForResult(new Intent(this, CameraKitActivity.class), CAMERA_REQUEST_CODE);
    }

    @OnClick(R.id.imageViewFP)
    void captureFingerprints() {
        Intent intent = new Intent(this, SecuGenActivity.class);
        if (Objects.equals(Build.MODEL, "FP07"))
            intent = new Intent(this, HuifanActivity.class);
        this.startActivityForResult(intent, BIOMETRIC_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}