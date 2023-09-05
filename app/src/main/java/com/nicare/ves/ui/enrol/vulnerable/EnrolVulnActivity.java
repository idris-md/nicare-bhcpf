package com.nicare.ves.ui.enrol.vulnerable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nicare.ves.R;
import com.nicare.ves.common.Constant;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.Benefactor;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Reproductive;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.ui.adapter.BenefactorSpinnerAdapter;
import com.nicare.ves.ui.adapter.FacilitySpinnerAdapter;
import com.nicare.ves.ui.adapter.FacilityWardSpinnerAdapter;
import com.nicare.ves.ui.adapter.LGASpinnerAdapter;
import com.nicare.ves.ui.adapter.StringSpinnerAdapter;
import com.nicare.ves.ui.adapter.WardSpinnerAdapter;
import com.nicare.ves.ui.enrol.camera.CameraKitActivity;
import com.nicare.ves.ui.enrol.fingerprint.HuifanActivity;
import com.nicare.ves.ui.enrol.fingerprint.SecuGenActivity;
import com.tiper.MaterialSpinner;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EnrolVulnActivity extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    public static final String GUARDIAN_NIN = "gNIN";
    public static final String GUARDIAN_NICARE_ID = "gNicareId";
    public static final String GUARDIAN_NAME = "gName";

    public static final String PIN_TYPE = "pinType";
    public static final String PIN = "pin";
    public static final String PIN_SALES_ID = "pinSalesID";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int BIOMETRIC_REQUEST_CODE = 101;
    public static final String CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS = "Female Reproductive (15-45 years)";
    public static final String CATEGORY_CHILDREN_UNDER_5_YRS = "Children under 5yrs";
    public static final String CATEGORY_AGED_85 = "Elderly (85 and above)";
    //    public static final String CATEGORY_PHYSICALLY_DISABLE = "Physicaly Disabled";
    public static final String CATEGORY_OTHERS = "Others";

    SimpleDateFormat simpleDateFormat;
    @BindView(R.id.imageViewPhoto)
    ImageView imageViewPhoto;
    @BindView(R.id.edtFirstName)
    TextInputEditText edtFirstName;
    @BindView(R.id.edtSurname)
    TextInputEditText edtSurName;
    @BindView(R.id.edtOtherName)
    TextInputEditText edtOtherName;
    @BindView(R.id.edtDOB)
    TextInputEditText edtDOB;
    @BindView(R.id.edtPhone)
    TextInputEditText edtPhone;
    @BindView(R.id.edtOccupation)
    TextInputEditText edtOccupation;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtAddress)
    TextInputEditText edtResidentAddress;
    @BindView(R.id.edtCommunity)
    TextInputEditText edtCommunity;
    @BindView(R.id.male)
    RadioButton rbtMale;
    @BindView(R.id.female)
    RadioButton rbtFemale;
    @BindView(R.id.spMaritalStatus)
    MaterialSpinner spMaritalStatus;
    @BindView(R.id.spProvider)
    MaterialSpinner spProvider;
    @BindView(R.id.spOccupation)
    MaterialSpinner spOccupaion;
    @BindView(R.id.nkName)
    TextInputEditText edtNkName;
    @BindView(R.id.spNkRel)
    MaterialSpinner spNkRel;
    @BindView(R.id.edtNKPhone)
    TextInputEditText edtNkPhone;


    @BindView(R.id.spWardFacility)
    MaterialSpinner spWardFacility;
    @BindView(R.id.spFacLGA)
    MaterialSpinner spFacLGA;

    @BindView(R.id.spPhysicalDisability)
    MaterialSpinner spPhysicalDisability;
    //
//    @BindView(R.id.spLGA)

    //    MaterialSpinner spLGA;
//    @BindView(R.id.spWard)
//    MaterialSpinner spWard;
    List<Facility> allFacilities;
    @BindView(R.id.tv_bio)
    TextView tvBio;

    @BindView(R.id.layOccupation)
    TextInputLayout layOccupation;

    @BindView(R.id.layNicare)
    TextInputLayout layNicare;

    @BindView(R.id.layNin)
    TextInputLayout layNin;

    @BindView(R.id.nav_menu)
    BottomNavigationView mNavigationView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.checkRes)
    CheckBox checkRes;

    @BindView(R.id.checkFac)
    CheckBox checkFac;

    Facility mFacility;
    String lg;
    String ward;
    String nkRelationship;
    String maritalStatus;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    FacilitySpinnerAdapter mFacilitySpinnerAdapter;
    StringSpinnerAdapter mOccupationSpinnerAdapter;
    StringSpinnerAdapter mMaritalSpinnerAdapter;
    StringSpinnerAdapter mPhysicalSpinnerAdapter;
    StringSpinnerAdapter mNKSpinnerAdapter;
    StringSpinnerAdapter mDisabilityAdapter;
    StringSpinnerAdapter mSettlementAdapter;
    StringSpinnerAdapter mPregnantAdapter;

    @BindView(R.id.spCategory)
    MaterialSpinner spCategory;
    @BindView(R.id.spPregnant)
    MaterialSpinner spPregnant;
    @BindView(R.id.spSettlement)
    MaterialSpinner spSettlement;
    @BindView(R.id.edtNin)
    TextInputEditText edtNin;

    @BindView(R.id.edtNiCareID)
    TextInputEditText edtNiCareID;

    @BindView(R.id.edtMotherNIN)
    TextInputEditText edtMotherNIN;

    @BindView(R.id.spBenefactor)
    MaterialSpinner spBenefactor;

    private String mGuardianName;
    private String mGuardianNIN;
    private String mGuardianNiCare;

    private String mPIN;
    private String mPINSalesId;
    private String mPINType;

    private boolean isUnderAged;
    private boolean mIsDetailsConfirmed;
    private String dateOfBirth;
    // private String stateOfOrigin;
    // private String lgaOfOrigin;
    private String mOccupation;
    private String mPhoto;
    private Vulnerable mEnrollment;
    private List<String> mCode;
    private String mEoWard;
    private String mEoLGA;
    private String category;
    private EnrolVulnerableViewModel mEnrolmentViewModel;
    private Fingerprint mFingerprint;
    private int mAge;
    private List<String> mCategories;
    private String mSettlement;

    LGASpinnerAdapter mLGASpinnerAdapter;
    WardSpinnerAdapter mWardSpinnerAdapter;
    FacilityWardSpinnerAdapter mFacWardSpinnerAdapter;
    LGASpinnerAdapter mFAcLGASpinnerAdapter;
    BenefactorSpinnerAdapter mBenefactorSpinnerAdapter;

    private LGA mSelectedLGA;
    private Ward mSelectedWard;
    private LGA mSelectedFacLGA;
    private Ward mSelectedFacWard;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    AlertDialog mAlertDialog;
    private Transaction mTransaction;
    private int mInstantId;
    private List<Ward> allWard;
    private boolean isPregnant;
    //    private boolean isNINUsed = false;
    private String disability;
    private Reproductive mExistingNIN;
    private Reproductive mExistingMotherNIN;
    private Benefactor mBenefactor;
    private Dialog mPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrol_vulnerable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mInstantId = PrefUtils.getInstance(this).getInstantId() + 1;
        spCategory.setEnabled(false);
        mEoWard = PrefUtils.getInstance(getApplicationContext()).getWard();
        mEoLGA = PrefUtils.getInstance(getApplicationContext()).getLGA();

        readIntentStateValues();
        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Please wait, saving data..")
                .build();

        mLGASpinnerAdapter = new LGASpinnerAdapter(this);
        mFAcLGASpinnerAdapter = new LGASpinnerAdapter(this);
        mWardSpinnerAdapter = new WardSpinnerAdapter(this);
        mFacWardSpinnerAdapter = new FacilityWardSpinnerAdapter(this);
        mBenefactorSpinnerAdapter = new BenefactorSpinnerAdapter(this);

//        List<Benefactor> benefactors = new ArrayList<>();

//        benefactors.add(new Benefactor("BHCPF"));
//        benefactors.add(new Benefactor("Nigeria for Women Project"));

        spWardFacility.setAdapter(mFacWardSpinnerAdapter);
//        spLGA.setAdapter(mLGASpinnerAdapter);
//        spWard.setAdapter(mWardSpinnerAdapter);
        spFacLGA.setAdapter(mFAcLGASpinnerAdapter);
        spBenefactor.setAdapter(mBenefactorSpinnerAdapter);

        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        fab.setOnClickListener(view -> save());

        edtNiCareID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    if (edtNin)
                    mEnrolmentViewModel.getVulnerableBYNIN(edtNiCareID.getText().toString()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).doOnSuccess(data -> {
                                        mExistingMotherNIN = data;
                                        edtNiCareID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_done_24, 0);
//                                        if (mExistingNIN != null) {
//                                            Util.showDialogueMessae(EnrolVulnActivity.this, "NIN already used by another beneficiary", getString(R.string.error_msg));
//                                        }
                                    }
                            ).doOnComplete(() -> {
                                edtNiCareID.setError("No Beneficiary found with NiCare ID");
                                mExistingMotherNIN = null;
                            })
                            .subscribe();
                }
//                else {
//                    edtNiCareID.setError("No Beneficiary found with NiCare ID");
//                    mExistingMotherNIN = null;
//                }
            }
        });
        edtMotherNIN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    if (edtNin)
                    mEnrolmentViewModel.getVulnerableBYNIN(edtMotherNIN.getText().toString()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).doOnSuccess(data -> {
                                        mExistingMotherNIN = data;
                                        edtMotherNIN.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_done_24, 0);
                                        if ((mExistingMotherNIN.getNicareId() == null || mExistingMotherNIN.getNicareId().isEmpty()) && !mExistingMotherNIN.isNow()) {
                                            layNicare.setVisibility(View.VISIBLE);
                                        } else {
                                            layNicare.setVisibility(View.GONE);
                                        }
                                        //                                        if (mExistingNIN != null) {
//                                            Util.showDialogueMessae(EnrolVulnActivity.this, "NIN already used by another beneficiary", getString(R.string.error_msg));
//                                        }
                                    }
                            ).doOnComplete(() -> {
                                layNicare.setVisibility(View.VISIBLE);
                                edtMotherNIN.setError("No Beneficiary found with NIN");
                                mExistingMotherNIN = null;
                            })
                            .subscribe();
                }

            }
        });

        edtNin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
//                    if (edtNin)
                    mEnrolmentViewModel.getVulnerableBYNIN(edtNin.getText().toString()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).doOnSuccess(data -> {
                                        mExistingNIN = data;
                                        edtNin.setError("Beneficiary already enrolled with NIN");
//                                        if (mExistingNIN != null) {
//                                            Util.showDialogueMessae(EnrolVulnActivity.this, "NIN already used by another beneficiary", getString(R.string.error_msg));
//                                        }
                                    }
                            ).doOnComplete(() -> {
                                edtNin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_done_24, 0);
                                mExistingNIN = null;
                            })
                            .subscribe();
                }
            }
        });

        bindViewModel();

        mNavigationView.setOnNavigationItemSelectedListener(menuItem ->

        {
            switch (menuItem.getItemId()) {

                case R.id.nav_bio_data:
                    //mNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                    return true;

                case R.id.nav_photo:
                    //mNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                    if (createEnrollmentObject()) {
                        facialCapture();
                    } else {
                        mNavigationView.setSelectedItemId(R.id.nav_bio_data);
                    }
                    return true;

                case R.id.nav_fingerprint:
                    bioCapture();
                    return true;

                default:
                    return false;

            }

        });

    }

    private void bindViewModel() {

        mEnrolmentViewModel = ViewModelProviders.of(this).get(EnrolVulnerableViewModel.class);

        mEnrolmentViewModel.getAllWards().observe(EnrolVulnActivity.this, new Observer<List<Ward>>() {
            @Override
            public void onChanged(List<Ward> wardList) {
                allWard = wardList;
            }
        });


        mEnrolmentViewModel.getAllFacilities().observe(EnrolVulnActivity.this, new Observer<List<Facility>>() {
            @Override
            public void onChanged(List<Facility> facilities) {
                allFacilities = facilities;
            }
        });

        observeTransacetion();

        mNKSpinnerAdapter = new StringSpinnerAdapter(this);
        mOccupationSpinnerAdapter = new StringSpinnerAdapter(this);
        mMaritalSpinnerAdapter = new StringSpinnerAdapter(this);
        mPhysicalSpinnerAdapter = new StringSpinnerAdapter(this);
        mFacilitySpinnerAdapter = new FacilitySpinnerAdapter(this);
        mDisabilityAdapter = new StringSpinnerAdapter(this);
        mSettlementAdapter = new StringSpinnerAdapter(this);
        mPregnantAdapter = new StringSpinnerAdapter(this);

        spOccupaion.setAdapter(mOccupationSpinnerAdapter);
        spMaritalStatus.setAdapter(mMaritalSpinnerAdapter);
        spProvider.setAdapter(mFacilitySpinnerAdapter);
        spNkRel.setAdapter(mNKSpinnerAdapter);
        spCategory.setAdapter(mDisabilityAdapter);
        spSettlement.setAdapter(mSettlementAdapter);
        spPhysicalDisability.setAdapter(mPhysicalSpinnerAdapter);
        spPregnant.setAdapter(mPregnantAdapter);

        rbtFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                initCategories();
//                mDisabilityAdapter.notifyEntryChange(mCategories);
                doCheck();
            }
        });
        rbtMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCheck();
            }
        });


        spBenefactor.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                Benefactor benefactor = (Benefactor) materialSpinner.getSelectedItem();
                mBenefactor = benefactor;
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        spSettlement.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                mSettlement = (String) materialSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        spPhysicalDisability.setOnItemClickListener(new MaterialSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                disability = (String) materialSpinner.getAdapter().getItem(i);
            }
        });

        spOccupaion.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                String occupation;
//                if (i > 0) {
                occupation = (String) materialSpinner.getAdapter().getItem(i);
                if (occupation.equalsIgnoreCase("Other specify")) {
                    layOccupation.setVisibility(View.VISIBLE);
                    edtOccupation.setEnabled(true);
                    edtOccupation.requestFocus();
                } else {
                    mOccupation = occupation;
                    layOccupation.setVisibility(View.GONE);
//                    }
                }
            }


            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }

        });
        spCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {

                category = (String) materialSpinner.getAdapter().getItem(i);
//                if (category.equalsIgnoreCase(CATEGORY_PHYSICALLY_DISABLE)) {
//                    spPhysical.setVisibility(View.VISIBLE);
//                    category = null;
//                } else {
//                    spPhysical.setVisibility(View.GONE);
//                }

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
            }
        });
        spNkRel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                nkRelationship = (String) materialSpinner.getAdapter().getItem(i);
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
            }
        });
        spProvider.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                mFacility = (Facility) materialSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
            }
        });
        spMaritalStatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                maritalStatus = (String) materialSpinner.getAdapter().getItem(i);
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        spPregnant.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                isPregnant = i == 0;
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

//        spLGA.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//
//                mSelectedLGA = (LGA) materialSpinner.getAdapter().getItem(i);
//
//                mEnrolmentViewModel.getWardsByLG(mSelectedLGA.getLgaId()).observe(EnrolVulnActivity.this, new Observer<List<Ward>>() {
//                    @Override
//                    public void onChanged(List<Ward> wardList) {
//                        mWardSpinnerAdapter.notifyEntryChange(wardList);
//                        spWard.setSelection(-1);
////                        spProvider.setSelection(-1);
//                        //edtResidentAddress.getText().clear();
//                        mSelectedWard = null;
////                      mSelectedFacility = null;
//                        if (checkRes.isChecked()) {
//                            spWard.setSelection(mWardSpinnerAdapter.getItemPosition(new Ward(mEoWard)));
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//            }
//        });
//        spWard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//                mSelectedWard = (Ward) materialSpinner.getAdapter().getItem(i);
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//
//            }
//        });

        spFacLGA.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {

                mSelectedFacLGA = (LGA) materialSpinner.getAdapter().getItem(i);

                mFacWardSpinnerAdapter.notifyEntryChange(filterAvailableFacWardByLGA(mSelectedFacLGA));
                spWardFacility.setSelection(-1);
                spProvider.setSelection(-1);

                if (checkFac.isChecked()) {
                    spWardFacility.setSelection(mFacWardSpinnerAdapter.getItemPosition(new Ward(mEoWard)));
                }

//                mEnrolmentViewModel.getWardsByLG(mSelectedFacLGA.getLgaId()).observe(EnrolVulnActivity.this, new Observer<List<Ward>>() {
//                    @Override
//                    public void onChanged(List<Ward> wardList) {
//                        mFacWardSpinnerAdapter.notifyEntryChange(wardList);
//                        spWardFacility.setSelection(-1);
//                        spProvider.setSelection(-1);
//
//                        if (checkFac.isChecked()) {
//                            spWardFacility.setSelection(mFacWardSpinnerAdapter.getItemPosition(new Ward(mEoWard)));
//                        }
//
//                    }
//                });

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        spWardFacility.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {

                mSelectedFacWard = (Ward) materialSpinner.getAdapter().getItem(i);
//                if (inInitState) {
//                    spWardFacility.setSelection(mWardSpinnerAdapter.getItemPosition(new Ward(PrefUtils.getInstance(EnrolVulnActivity.this).getWard())));
//                }
                List<Facility> facilities = filterFacilityByWard(mSelectedFacWard);
                mFacilitySpinnerAdapter.notifyEntryChange(facilities);
//                if (checkFac.isChecked() && facilities.size() > 0) {
//                    spWardFacility.setSelection(mFacWardSpinnerAdapter.getItemPosition(new Ward(mEoWard)));
////
//                }


//                mEnrolmentViewModel.getFacility(mSelectedFacLGA.getLgaId(), mSelectedFacWard.getWard_id()).observe(EnrolVulnActivity.this, new Observer<List<Facility>>() {
//                    @Override
//                    public void onChanged(List<Facility> facilities) {
//                        mFacilitySpinnerAdapter.notifyEntryChange(facilities);
//                        if (checkFac.isChecked() && facilities.size() > 0) {
//                            spWardFacility.setSelection(mFacWardSpinnerAdapter.getItemPosition(new Ward(mEoWard)));
////
//                        }
//                    }
//                });
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        List<String> occupations = new ArrayList<>();
//        occupations.add("Select Occupation");
        occupations.add("Farmer");
        occupations.add("Artisan");
        occupations.add("Student");
        occupations.add("Petty Trader");
        occupations.add("Other specify");

        List<String> pregnantStatus = new ArrayList<>();
//        occupations.add("Select Occupation");
        pregnantStatus.add("Not pregnant");
        pregnantStatus.add("Currently pregnant");

        initCategories();

        mSettlementAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.settlement)));
        mOccupationSpinnerAdapter.notifyEntryChange(occupations);
        mPregnantAdapter.notifyEntryChange(pregnantStatus);
        mNKSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.relationships)));
        mMaritalSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.marital_status)));
        mPhysicalSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.physical)));

        //        mDisabilityAdapter.notifyEntryChange(mCategories);
        mEnrolmentViewModel.getLGAs().observe(this, new Observer<List<LGA>>() {
            @Override
            public void onChanged(List<LGA> lgaList) {
                mLGASpinnerAdapter.notifyEntryChange(lgaList);
                mFAcLGASpinnerAdapter.notifyEntryChange(lgaList);
            }
        });

        mEnrolmentViewModel.getBenefactor().observe(this, new Observer<List<Benefactor>>() {
            @Override
            public void onChanged(List<Benefactor> benefactors) {
                mBenefactorSpinnerAdapter.notifyEntryChange(benefactors);
            }
        });

//        mEnrolmentViewModel.getWardsByLG(PrefUtils.getInstance(EnrolVulnActivity.this).getLGA()).observe(EnrolVulnActivity.this, new Observer<List<Ward>>() {
//            @Override
//            public void onChanged(List<Ward> wardList) {
//                mFacWardSpinnerAdapter.notifyEntryChange(wardList);
////                spWard.setSelection(-1);
////                spProvider.setSelection(-1);
//
//            }
//        });
        initCategories();
    }

    private List<Facility> filterFacilityByWard(Ward ward) {
        List<Facility> result = new ArrayList<>();
        for (Facility facility : allFacilities) {
            if (facility.getHcpward().equalsIgnoreCase(ward.getWard_id()))
                result.add(facility);
        }
        return result;
    }

    private List<Ward> filterWardByLGA(LGA selectedFacLGA) {
        List<Ward> result = new ArrayList<>();
        for (Ward ward : allWard) {
            if (ward.getLga_id().equalsIgnoreCase(selectedFacLGA.getLgaId()))
                result.add(ward);
        }
        return result;
    }

    private List<Ward> filterAvailableFacWardByLGA(LGA selectedFacLGA) {
        List<Ward> result = new ArrayList<>();
        for (Ward ward : allWard) {
            if (ward.getLga_id().equalsIgnoreCase(selectedFacLGA.getLgaId()) && ward.getTotalEnrolled() != ward.getEnrolmentCap())
                result.add(ward);
        }
        return result;
    }

    private void observeTransacetion() {
        mEnrolmentViewModel.getTransaction().observe(this, new Observer<Transaction>() {
            @Override
            public void onChanged(Transaction transaction) {
                if (transaction != null) {
                    mTransaction = transaction;
                } else {
                    transaction = new Transaction(Util.todayDate());
                    mEnrolmentViewModel.insertOrUpdateTransaction(transaction)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();

                    observeTransacetion();

                }
            }
        });
    }

    private void initCategories() {
        mCategories = new ArrayList<>();
        mCategories.add(CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS);
        mCategories.add(CATEGORY_CHILDREN_UNDER_5_YRS);
        mCategories.add(CATEGORY_AGED_85);
//        mCategories.add(CATEGORY_PHYSICALLY_DISABLE);
        mCategories.add(CATEGORY_OTHERS);
        mDisabilityAdapter.notifyEntryChange(mCategories);
    }

    private void readIntentStateValues() {
        Intent intent = getIntent();
//        mGuardianNiCare = intent.getStringExtra(GUARDIAN_NICARE_ID);
//        mGuardianName = intent.getStringExtra(GUARDIAN_NAME);
//        mGuardianNIN = intent.getStringExtra(GUARDIAN_NIN);
    }

    @OnClick(R.id.edtDOB)
    void setDate() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        SpinnerDatePickerDialogBuilder spinnerDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        spinnerDatePickerDialogBuilder
                .context(this)
                .callback(this)
                .spinnerTheme(R.style.DatePickerSpinner)
                .maxDate(year, month, day)
        ;
        if (dateOfBirth != null) {
            String[] arr = dateOfBirth.split("-");
            spinnerDatePickerDialogBuilder.defaultDate(Integer.valueOf(arr[0]),
                    Integer.valueOf(arr[1]) - 1,
                    Integer.valueOf(arr[2]));

        } else {
            spinnerDatePickerDialogBuilder.defaultDate(year, month, day);
        }

        spinnerDatePickerDialogBuilder.build().show();
    }

    @SuppressLint("CheckResult")
    void save() {

        if (!createEnrollmentObject()) {
            if (mEnrollment == null) return;
            if (mEnrollment.getPhoto() == null || mEnrollment.getPhoto().isEmpty()) {
                Util.showToast("Please retake enrolee photo", true, this, this.getLayoutInflater());
            }
        }

        if (!mIsDetailsConfirmed) {
            Util.showDialogueMessae(this, "Please confirm user's detail before saving", "Confirm detail");
            mIsDetailsConfirmed = true;
            return;
        }

        mAlertDialog.show();
        fab.setVisibility(View.GONE);

        mEnrollment.setInstantId(Util.formatStat(mInstantId));
        mEnrolmentViewModel.savePrincipal(mEnrollment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                .map(id -> {

                    PrefUtils.getInstance(EnrolVulnActivity.this).setInstantId(mInstantId);

                    mFingerprint.setBeneficiary_type(Constant.BENEFICIARY_TYPE_VULNERABLE);
                    mFingerprint.setBeneficiary_id(id);

                    PrefUtils.getInstance(this).setInstantId(mInstantId);

                    mEnrolmentViewModel.saveFingerprint(mFingerprint)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();

                    mEnrolmentViewModel.updateWardCount(mSelectedFacWard.getId()).subscribeOn(Schedulers.io())
                            .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();

                    return id;

                }).subscribe(id -> {

//            mEnrolmentViewModel.todayTransaction()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
//                    .subscribe(transaction -> {
                    mTransaction.setRegistration(mTransaction.getRegistration() + 1);
                    updateTransaction();

                    mFacility.setHcpcount(mFacility.getHcpcount() + 1);
                    if (mExistingNIN == null) {
                        Reproductive reproductive = new Reproductive();
                        reproductive.setNin(mEnrollment.getNin());
                        reproductive.setNow(true);
                        mEnrolmentViewModel.saveNIN(reproductive)
                                .subscribeOn(Schedulers.io())
                                .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                    mEnrolmentViewModel.updateCount(mFacility)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                    ;

                    mAlertDialog.dismiss();
                    Util.showToast("Enrolment Successful", false, this, getLayoutInflater());
                    finish();

//                    });
                });

    }

    private void updateTransaction() {
        mEnrolmentViewModel.updateTransaction(mTransaction)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> mCompositeDisposable.add(disposable))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean createEnrollmentObject() {
        /////////// Remove Below /////////////

        String firstName = edtFirstName.getText().toString();
        String surName = edtSurName.getText().toString();
        String otherName = edtOtherName.getText().toString();

        String phone = edtPhone.getText().toString();

        String email = edtEmail.getText().toString();
        String address = edtResidentAddress.getText().toString();
        String community = edtCommunity.getText().toString();

        String nkName = edtNkName.getText().toString();
        String nkPhone = edtNkPhone.getText().toString();

        String nin = edtNin.getText().toString();
        String gender = "";
        mGuardianNiCare = edtNiCareID.getText().toString();


        if (mBenefactor == null) {
            Util.showDialogueMessae(this, "Benefactor is required", getString(R.string.error_msg));
            return false;
        }

        if (mBenefactor.getName().equalsIgnoreCase("Nigeria For Women") && (gender.equalsIgnoreCase("male") && !isUnderAged)) {
            Util.showDialogueMessae(this, "Only women and children are eligible for selected benefactor", getString(R.string.error_msg));
            return false;
        }

        if (layNin.getVisibility() == View.VISIBLE) {
            mGuardianNIN = edtMotherNIN.getText().toString();
        } else if (mExistingMotherNIN != null) {
            mGuardianNIN = mExistingMotherNIN.getNin();
        }

        if (rbtFemale.isChecked()) {
            gender = "Female";
        } else {
            gender = "Male";
        }

        if (layOccupation.getVisibility() == View.VISIBLE) {
            mOccupation = edtOccupation.getText().toString();
        }


        //                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<Vulnerable>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Vulnerable vulnerable) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

//        if (mAge >= 5){
        if (TextUtils.isEmpty(nin) || nin.length() < 11) {
            Util.showDialogueMessae(this, "Provide a valid NIN", getString(R.string.error_msg));
            return false;
        }

//        Reproductive ex!istingNIN = mEnrolmentViewModel.getVulnerableBYNIN(edtNin.getText().toString()).blockingGet();
        if (mExistingNIN != null && !(mAge < 5)) {
            Util.showDialogueMessae(this, "NIN already use by another beneficiary", getString(R.string.error_msg));
            return false;
        }

        if (mAge < 5 && mExistingNIN != null && !nin.equals(mGuardianNIN)) {
            Util.showDialogueMessae(this, "Invalid NIN provided for under 5, use mother's NIN for enrolee in a case under dont have NIN", getString(R.string.error_msg));
            return false;
        }

        if (mExistingMotherNIN == null && mAge < 5) {
            Util.showDialogueMessae(this, "No beneficiary found with Mother NiCare ID and NIN", getString(R.string.error_msg));
            return false;
        }
//
        if (TextUtils.isEmpty(surName)) {
            Util.showDialogueMessae(this, "Surname field cant blank", getString(R.string.error_msg));
            return false;
        }
        if (TextUtils.isEmpty(firstName)) {
            Util.showDialogueMessae(this, "First name is required", getString(R.string.error_msg));
            return false;
        }

        if (!rbtMale.isChecked() && !rbtFemale.isChecked()) {
            Util.showDialogueMessae(this, "Please select gender", getString(R.string.error_msg));
            return false;
        }

        if (TextUtils.isEmpty(dateOfBirth)) {
            Util.showDialogueMessae(this, "Date of birth field cant blank", getString(R.string.error_msg));
            return false;
        }

        if (layNin.getVisibility() == View.VISIBLE && (mGuardianNIN.length() < 11)) {
//            Util.showDialogueMessae(this, "Date of birth field cant blank", getString(R.string.error_msg));
            Util.showDialogueMessae(this, "Invalid Mother's NIN provided", getString(R.string.error_msg));
            return false;
        }

        if (layNicare.getVisibility() == View.VISIBLE && (mGuardianNiCare.length() < 12)) {
//            Util.showDialogueMessae(this, "Date of birth field cant blank", getString(R.string.error_msg));
            Util.showDialogueMessae(this, "Invalid Mother's NiCare ID provided", getString(R.string.error_msg));
            return false;
        }

//        if (!TextUtils.isEmpty(mGuardianNiCare)) {
//            if (mAge >= 5) {
//                Util.showDialogueMessae(this, "Beneficiary must be under 5years ", getString(R.string.error_msg));
//                return false;
//            }
//        }

//        if (isUnderAged) {
//            Util.showDialogueMessae(EnrolVulnActivity.this, "This member is under aged and can't be enroll to the scheme", "Under Aged");
//            return false;
//        }

        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            Util.showDialogueMessae(this, "Phone number may not be correct", getString(R.string.error_msg));
            return false;
        }

        if (mAge >= 5) {
            if (TextUtils.isEmpty(mOccupation)) {
                Util.showDialogueMessae(this, "Occupation field can't be blank", getString(R.string.error_msg));
                return false;
            }
        }

        if (TextUtils.isEmpty(category)) {
            Util.showDialogueMessae(this, "Category is required", getString(R.string.error_msg));
            return false;
        }

        if (spPregnant.getVisibility() == View.VISIBLE && spPregnant.getSelection() == -1) {
            Util.showDialogueMessae(this, "Pregnancy status is required", getString(R.string.error_msg));
            return false;
        }

        if (disability == null) {
            Util.showDialogueMessae(this, "Disability status is required", getString(R.string.error_msg));
            return false;
        }

        if (TextUtils.isEmpty(mSettlement)) {
            Util.showDialogueMessae(this, "Settlement is required", getString(R.string.error_msg));
            return false;
        }

//        if (mSelectedLGA == null) {
//            Util.showDialogueMessae(this, "LGA of Residence is required", getString(R.string.error_msg));
//            return false;
//        }
//        if (mSelectedWard == null) {
//            Util.showDialogueMessae(this, "Ward of Residence is required", getString(R.string.error_msg));
//            return false;
//        }
//        }

        if (TextUtils.isEmpty(community)) {
            Util.showDialogueMessae(this, "Community field is required", getString(R.string.error_msg));
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            Util.showDialogueMessae(this, "Address field is required", getString(R.string.error_msg));
            return false;
        }

        if (!TextUtils.isEmpty(email)) {
            if (!Util.isEmailValid(email)) {
                Util.showDialogueMessae(this, "Email address not valid", getString(R.string.error_msg));
                return false;
            }
        }

        if (maritalStatus == null) {
            Util.showDialogueMessae(this, "Marital status not selected", getString(R.string.error_msg));
            return false;
        }


        if (mFacility == null) {
            Util.showDialogueMessae(this, "Health Care Provider not selected", getString(R.string.error_msg));
            return false;
        }

        if (TextUtils.isEmpty(nkName)) {
            Util.showDialogueMessae(this, "Next of Kin Name field can't be blank", getString(R.string.error_msg));
            return false;
        }

        if (nkRelationship == null) {
            Util.showDialogueMessae(this, "Next of Kin Relationship is required", getString(R.string.error_msg));
            return false;
        }

        if (TextUtils.isEmpty(nkPhone) || nkPhone.length() < 11) {
            Util.showDialogueMessae(this, "Next of Kin's phone number may not be correct", getString(R.string.error_msg));
            return false;
        }

        /////////// confirm input

        mEnrollment = new Vulnerable();
        mEnrollment.setBenefactor(String.valueOf(mBenefactor.getId()));
        mEnrollment.setFirstName(firstName);
        mEnrollment.setSurName(surName);
        mEnrollment.setOtherName(otherName);
        mEnrollment.setDateOfBirth(dateOfBirth);
        mEnrollment.setMaritalStatus(maritalStatus);
        mEnrollment.setGender(gender);
        mEnrollment.setPhone(phone);
        mEnrollment.setOccupation(mOccupation);
        mEnrollment.setEmail(email);
        mEnrollment.setAddress(address);
        mEnrollment.setCommunity(community);
        mEnrollment.setWard(mEoWard);
        mEnrollment.setPregnant(isPregnant);
        mEnrollment.setLga(mEoLGA);
        mEnrollment.setProvider(mFacility.getHcpcode());
        mEnrollment.setNkName(nkName);
        mEnrollment.setNkRelationship(nkRelationship);
        mEnrollment.setNkPhone(nkPhone);
        mEnrollment.setCategory(category);
        mEnrollment.setNin(nin);
        mEnrollment.setSettlement(mSettlement);
        mEnrollment.setRegDate(Util.dateTimeString());
        mEnrollment.setPhoto(mPhoto);
        mEnrollment.setDisability(disability);

        mEnrollment.setGuardianNIN(mGuardianNIN);
        mEnrollment.setGuardianID(mGuardianNiCare);

        return true;
    }

    // @OnClick(R.id.btnFacial)
    void facialCapture() {
        Intent intent = new Intent(this, CameraKitActivity.class);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    // @OnClick(R.id.btnBio)
    void bioCapture() {

        if (mPhoto == null) {
            Util.showToast("Please take photo before capturing Fingerprint", true, this, getLayoutInflater());

            mNavigationView.setSelectedItemId(R.id.nav_bio_data);
            return;
        }

////        String device = PrefUtils.getInstance(getBaseContext()).getBiometric();
//        Intent intent;
////        switch (device) {
////
////            case "FP07 Fingerprint":
//        intent = new Intent(this, SecuGenActivity.class);
//        if (Objects.equals(Build.MODEL, "FP07"))
//            intent = new Intent(this, HuifanActivity.class);
//        this.startActivityForResult(intent, BIOMETRIC_REQUEST_CODE);
//
////            case "USB Fingerprint":
////                intent = new Intent(this, UareUMainActivity.class);
////                this.startActivityForResult(intent, BIOMETRIC_REQUEST_CODE);
////                break;
////        }


        Intent intent;

//        intent = new Intent(this, SecuGenActivity.class);
//        if (Objects.equals(Build.MODEL, "FP07")) {
//            intent = new Intent(this, HuifanActivity.class);
//        }
//        this.startActivityForResult(intent, BIOMETRIC_REQUEST_CODE);

        mPasswordDialog = new Dialog(this);
        mPasswordDialog.setContentView(R.layout.select_scanner_layout);

        MaterialButton btnFP07 = mPasswordDialog.findViewById(R.id.btnFP07);
        MaterialButton btnSecugen = mPasswordDialog.findViewById(R.id.btnSecugen);

        btnFP07.setOnClickListener(v -> {
            this.startActivityForResult(new Intent(this, HuifanActivity.class), BIOMETRIC_REQUEST_CODE);

        });

        btnSecugen.setOnClickListener(v -> {
            this.startActivityForResult(new Intent(this, SecuGenActivity.class), BIOMETRIC_REQUEST_CODE);
        });

        mPasswordDialog.setCancelable(true);
        mPasswordDialog.setCanceledOnTouchOutside(true);
        mPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPasswordDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        switch (requestCode) {

            case CAMERA_REQUEST_CODE:

                if (resultCode == RESULT_OK) {

                    mPhoto = data.getStringExtra("photo");
                    imageViewPhoto.setImageBitmap(Util.decodeBase64ToBitmap(mPhoto));

                    // btnBio.setEnabled(true);
                }

                break;

            case BIOMETRIC_REQUEST_CODE:

                mFingerprint = data.getParcelableExtra("biometric");
                if (mFingerprint != null) {
                    tvBio.setText("Captured");
                    tvBio.setTextColor(getResources().getColor(R.color.colorPrimary));
                    fab.setVisibility(View.VISIBLE);
                }
                //ivBio.setImageResource(Util.getIConID(this, "prr"));
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        Map map = getDateDifferenceInDDMMYYYY(calendar.getTime());

        int age = (int) map.get("year");
        int mon = (int) map.get("month");
        int dy = (int) map.get("day");
        mAge = age;
        if (age < 0 || mon < 0 || dy < 0) {
            edtDOB.setText(null);
            dateOfBirth = null;
            return;
        }

        //edtDOB.setText(year + "-" + month + "-" + day+" Age: "+age+"Yrs "+" "+mon+" Month"+" "+" "+dy+" Day(s)");
        dateOfBirth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        edtDOB.setText(dateOfBirth + " [" + age + "yrs]");


        doCheck();


        //  isUnderAged = age < Constant.PRINCIPAL_MIN_AGE;

    }

    private void doCheck() {
        // check if reproductive
//        spDisability.setSelection(-1);
        category = null;
        initCategories();

//        isReprodcutive(mAge);

        // check if aged (elderly)
        spCategory.setEnabled(false);
        spPregnant.setVisibility(View.GONE);
        isPregnant = false;
//        disability = null;
        spPregnant.setSelection(-1);
//        spDisability.setSelection(-1);
        layNicare.setVisibility(View.GONE);
        if (mAge < 5) {
            spCategory.setSelection(1);
            layNin.setVisibility(View.VISIBLE);

        } else if (mAge >= 85) {
            spCategory.setSelection(2);
            layNin.setVisibility(View.GONE);
        } else if (mAge >= 15 && mAge <= 45 && rbtFemale.isChecked()) {
            spCategory.setSelection(0);
            spPregnant.setVisibility(View.VISIBLE);
            layNin.setVisibility(View.GONE);
        } else {
//            mCategories.remove(CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS);
//            mCategories.remove(CATEGORY_CHILDREN_UNDER_5_YRS);
//            mCategories.remove(CATEGORY_AGED_85);
//
            spCategory.setSelection(3);
//            spDisability.setSelection(3);
            spCategory.setEnabled(false);
            layNin.setVisibility(View.GONE);

        }

        if (mAge < 10) {
            spMaritalStatus.setSelection(0);
            spMaritalStatus.setEnabled(false);
        } else {
            spMaritalStatus.setEnabled(true);
            spMaritalStatus.setSelection(-1);
        }

//        if (mAge >= 85) {
//            mCategories.add(CATEGORY_AGED_85);
//        }else {
//            mCategories.remove(CATEGORY_AGED_85);
//        }

        // check if underaged
//        if (mAge >= 5) {
//            spOccupaion.setVisibility(View.VISIBLE);
//            mCategories.remove(CATEGORY_CHILDREN_UNDER_5_YRS);
//        } else {
//            spOccupaion.setVisibility(View.GONE);
//        }

        // check

//        mDisabilityAdapter.notifyEntryChange(mCategories);


//        isReprodcutive(mAge);


//        mDisabilityAdapter.notifyEntryChange(mCategories);

    }

//    private void isReprodcutive(int age) {
//        if (age < 15 || age > 45 || rbtMale.isChecked()) {
////            mCategories.remove(CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS);
//      spDisability.se
//        }

//        if (age < 15 || age > 45 || rbtMale.isChecked()) {
//            mCategories.remove(CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS);
//        } else {
//            mCategories.remove(CATEGORY_OTHERS);
//        }
//    }

    public static Map<String, Integer> getDateDifferenceInDDMMYYYY(Date from) {

        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();

        fromDate.setTime(from);
        toDate.setTime(new Date());
        int increment = 0;
        int year, month, day;

        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        System.out.println("increment" + increment);
        // DAY CALCULATION
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }
        // MONTH CALCULATION
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }
        // YEAR CALCULATION
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        Map<String, Integer> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);

        return map;
    }

    @OnClick(R.id.checkFac)
    void checkFac() {

        boolean isChecked = checkFac.isChecked();

        if (isChecked) {
            spFacLGA.setSelection(mFAcLGASpinnerAdapter.getItemPosition(new LGA(mEoLGA)));
            spWardFacility.setEnabled(false);
            spFacLGA.setEnabled(false);
//            spLGA.setEnabled(true);
//            spWard.setEnabled(true);
//            spProvider.setEnabled(true);
//            edtResidentAddress.setEnabled(true);
//            spLGA.setSelection(0);
        } else {
            spWardFacility.setEnabled(true);
            spFacLGA.setEnabled(true);
            spFacLGA.setSelection(-1);
            spWardFacility.setSelection(-1);
            mFacWardSpinnerAdapter.notifyEntryChange(new ArrayList<>());
            mFacilitySpinnerAdapter.notifyEntryChange(new ArrayList<>());
//            spProvider.setEnabled(false);
//            spLGA.setEnabled(false);
//            spWard.setEnabled(false);
//            edtResidentAddress.setEnabled(false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
//