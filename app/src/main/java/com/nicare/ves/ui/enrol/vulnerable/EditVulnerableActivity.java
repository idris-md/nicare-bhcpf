package com.nicare.ves.ui.enrol.vulnerable;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nicare.ves.R;
import com.nicare.ves.common.Constant;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Facility;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.LGA;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.ui.adapter.FacilitySpinnerAdapter;
import com.nicare.ves.ui.adapter.LGASpinnerAdapter;
import com.nicare.ves.ui.adapter.StringSpinnerAdapter;
import com.nicare.ves.ui.adapter.WardSpinnerAdapter;
import com.tiper.MaterialSpinner;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditVulnerableActivity extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    public static final String ID = "id";
    public static final String CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS = "Female Reproductive (15-45 years)";
    public static final String CATEGORY_CHILDREN_UNDER_5_YRS = "Children under 5yrs";
    public static final String CATEGORY_AGED_85 = "Elderly (85 and above)";
    public static final String CATEGORY_OTHERS = "Others";

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
    @BindView(R.id.edtNin)
    TextInputEditText edtNin;
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
    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    @BindView(R.id.tv_premium)
    TextView tvPremium;

    @BindView(R.id.spWardFacility)
    MaterialSpinner spWardFacility;
    @BindView(R.id.spFacLGA)
    MaterialSpinner spFacLGA;

//    @BindView(R.id.spLGA)
//    MaterialSpinner spLGA;
//    @BindView(R.id.spWard)
//    MaterialSpinner spWard;


    String provider;
    String ward;
    String nkRelationship;
    String maritalStatus;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    FacilitySpinnerAdapter mFacilitySpinnerAdapter;
    StringSpinnerAdapter mOccupationSpinnerAdapter;
    StringSpinnerAdapter mMaritalSpinnerAdapter;
    StringSpinnerAdapter mNKSpinnerAdapter;
    private String mPIN;
    private String mPINType;
    private boolean isUnderAged;
    private boolean mIsDetailsConfirmed;
    private String dateOfBirth;
    private long id;
    private Vulnerable mVulnerable;
    private boolean mInit;
    private String mDateOfBirth;
    private String mOccupation;
    private EditPrincipalViewModel mViewModel;
    private Facility mFacility;
    private Facility mOldFacility;
    private List<String> mCategories;

    StringSpinnerAdapter mDisabilityAdapter;
    StringSpinnerAdapter mSettlementAdapter;

    LGASpinnerAdapter mLGASpinnerAdapter;
    WardSpinnerAdapter mWardSpinnerAdapter;
    WardSpinnerAdapter mFacWardSpinnerAdapter;
    LGASpinnerAdapter mFAcLGASpinnerAdapter;

    private LGA mSelectedLGA;
    private Ward mSelectedWard;
    private LGA mSelectedFacLGA;
    private Ward mSelectedFacWard;

    @BindView(R.id.spDisability)
    MaterialSpinner spDisability;
    @BindView(R.id.spSettlement)
    MaterialSpinner spSettlement;

    private int mAge;
    private String mSettlement;
    private String category;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        mAlertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Please wait, saving data..")
                .build();

        mLGASpinnerAdapter = new LGASpinnerAdapter(this);
        mFAcLGASpinnerAdapter = new LGASpinnerAdapter(this);
        mWardSpinnerAdapter = new WardSpinnerAdapter(this);
        mFacWardSpinnerAdapter = new WardSpinnerAdapter(this);
        mDisabilityAdapter = new StringSpinnerAdapter(this);
        mSettlementAdapter = new StringSpinnerAdapter(this);

        spWardFacility.setAdapter(mFacWardSpinnerAdapter);
//        spLGA.setAdapter(mLGASpinnerAdapter);
//        spWard.setAdapter(mWardSpinnerAdapter);
        spFacLGA.setAdapter(mFAcLGASpinnerAdapter);
        spDisability.setAdapter(mDisabilityAdapter);
        spSettlement.setAdapter(mSettlementAdapter);

        readIntentStateValues();


        mViewModel = ViewModelProviders.of(this).get(EditPrincipalViewModel.class);
        init();

        mViewModel.getPrincipal(id).observe(this, new Observer<Vulnerable>() {
            @Override
            public void onChanged(Vulnerable vulnerable) {
                mVulnerable = vulnerable;

                mViewModel.getFacilityByCode(mVulnerable.getProvider())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(fac -> {
                            mOldFacility = fac;
                            bindDataToView();
                        });
            }
        });


    }

    private void initCategories() {
        mCategories = new ArrayList<>();
        mCategories.add(CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS);
        mCategories.add(CATEGORY_CHILDREN_UNDER_5_YRS);
        mCategories.add(CATEGORY_AGED_85);
        mCategories.add(CATEGORY_OTHERS);
        mDisabilityAdapter.notifyEntryChange(mCategories);
    }

    private void init() {

        mNKSpinnerAdapter = new StringSpinnerAdapter(this);
        mOccupationSpinnerAdapter = new StringSpinnerAdapter(this);
        mMaritalSpinnerAdapter = new StringSpinnerAdapter(this);
        mFacilitySpinnerAdapter = new FacilitySpinnerAdapter(this);

        spOccupaion.setAdapter(mOccupationSpinnerAdapter);
        spMaritalStatus.setAdapter(mMaritalSpinnerAdapter);
        spProvider.setAdapter(mFacilitySpinnerAdapter);
        spNkRel.setAdapter(mNKSpinnerAdapter);

        initCategories();

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
//                initCategories();
//                isReprodcutive(mAge);
//                mDisabilityAdapter.notifyEntryChange(mCategories);
//
            }
        });

        spOccupaion.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                String occupation;
                if (i > 0) {
                    occupation = (String) materialSpinner.getAdapter().getItem(i);
                    if (occupation.equalsIgnoreCase("Other specify")) {
                        edtOccupation.setVisibility(View.VISIBLE);
                        edtOccupation.setEnabled(true);
                        edtOccupation.requestFocus();
                    } else {
                        mOccupation = occupation;
                        edtOccupation.setVisibility(View.GONE);
                    }
                }
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

//        spLGA.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//
//                mSelectedLGA = (LGA) materialSpinner.getAdapter().getItem(i);
//                mViewModel.getWardsByLG(mSelectedLGA.getLgaId()).observe(EditVulnerableActivity.this, new Observer<List<Ward>>() {
//                    @Override
//                    public void onChanged(List<Ward> wardList) {
//                        mWardSpinnerAdapter.notifyEntryChange(wardList);
//                        spWard.setSelection(-1);
////                        spProvider.setSelection(-1);
//                        mSelectedWard = null;
////                      mSelectedFacility = null;
//                        if (mInit) {
//                            spWard.setSelection(mWardSpinnerAdapter.getItemPosition(new Ward(mVulnerable.getWard())));
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//            }
//        });
        spFacLGA.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {

                mSelectedFacLGA = (LGA) materialSpinner.getAdapter().getItem(i);
                mViewModel.getWardsByLG(mSelectedFacLGA.getLgaId()).observe(EditVulnerableActivity.this, new Observer<List<Ward>>() {
                    @Override
                    public void onChanged(List<Ward> wardList) {
                        mFacWardSpinnerAdapter.notifyEntryChange(wardList);
                        spWardFacility.setSelection(-1);
                        spProvider.setSelection(-1);

                        if (mInit) {
                            spWardFacility.setSelection(mFacWardSpinnerAdapter.getItemPosition(new Ward(mOldFacility.getHcpward())));
                        }

                    }
                });
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        spWardFacility.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {

                mSelectedFacWard = (Ward) materialSpinner.getAdapter().getItem(i);

                mViewModel.getFacility(mSelectedFacLGA.getLgaId(), mSelectedFacWard.getWard_id()).observe(EditVulnerableActivity.this, new Observer<List<Facility>>() {
                    @Override
                    public void onChanged(List<Facility> facilities) {
                        mFacilitySpinnerAdapter.notifyEntryChange(facilities);
                        if (mInit && facilities.size() > 0) {
                            spProvider.setSelection(mFacilitySpinnerAdapter.getItemPosition(new Facility(mOldFacility.getHcpcode())));
                            mInit = false;
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

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
        spSettlement.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                mSettlement = (String) materialSpinner.getSelectedItem();

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        spDisability.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                category = (String) materialSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        List<String> occupations = new ArrayList<>();
        occupations.add("Farmer");
        occupations.add("Artisan");
        occupations.add("Student");
        occupations.add("Other specify");

        mOccupationSpinnerAdapter.notifyEntryChange(occupations);
        mNKSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.relationships)));
        mMaritalSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.marital_status)));
        mSettlementAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.settlement)));

    }

    private void readIntentStateValues() {

        Intent intent = getIntent();
        id = intent.getLongExtra(ID, 0);
        mInit = true;

    }


    private void bindDataToView() {

        imageViewPhoto.setImageBitmap(Util.decodeBase64ToBitmap(mVulnerable.getPhoto()));
        edtFirstName.setText(mVulnerable.getFirstName());
        edtOtherName.setText(mVulnerable.getOtherName());
        edtSurName.setText(mVulnerable.getSurName());
        edtNin.setText(mVulnerable.getNin());
        edtCommunity.setText(mVulnerable.getCommunity());
        //edtDOB.setText(mEnrollment.getDateOfBirthgetDateOfBirth());
        edtPhone.setText(mVulnerable.getPhone());
        edtResidentAddress.setText(mVulnerable.getAddress());
        edtEmail.setText(mVulnerable.getEmail());
        edtNkName.setText(mVulnerable.getNkName());
        edtNkPhone.setText(mVulnerable.getNkPhone());
        tvPremium.setText("Vulnerable");

        String[] string = mVulnerable.getDateOfBirth().split("-");

        calculateAge(Integer.parseInt(string[0]), Integer.parseInt(string[1]) - 1, Integer.parseInt(string[2]));

        mViewModel.getLGAs().observe(this, new Observer<List<LGA>>() {
            @Override
            public void onChanged(List<LGA> lgaList) {
                mLGASpinnerAdapter.notifyEntryChange(lgaList);
                mFAcLGASpinnerAdapter.notifyEntryChange(lgaList);
                if (mInit) {
//                    spLGA.setSelection(mLGASpinnerAdapter.getItemPosition(new LGA(mVulnerable.getLga())));
                    spFacLGA.setSelection(mFAcLGASpinnerAdapter.getItemPosition(new LGA(mOldFacility.getHcplga())));
                }

            }
        });

        if (mAge >= 5) {
            if (mVulnerable.getOccupation().equals("Farmer") || mVulnerable.getOccupation().equals("Artisan") || mVulnerable.getOccupation().equals("Student")) {
                spOccupaion.setSelection(mOccupationSpinnerAdapter.getItemPosition(mVulnerable.getOccupation()));
            } else {
                spOccupaion.setSelection(4);
                edtOccupation.setText(mVulnerable.getOccupation());
            }
        } else {
            spOccupaion.setVisibility(View.GONE);
        }


        spNkRel.setSelection(mNKSpinnerAdapter.getItemPosition(mVulnerable.getNkRelationship()));
        spMaritalStatus.setSelection(mMaritalSpinnerAdapter.getItemPosition(mVulnerable.getMaritalStatus()));
        spSettlement.setSelection(mSettlementAdapter.getItemPosition(mVulnerable.getSettlement()));

        rbtMale.setChecked(true);
        if (mVulnerable.getGender().equalsIgnoreCase("Female")) {
            rbtFemale.setChecked(true);
        }

        doCheck();
        spDisability.setSelection(mDisabilityAdapter.getItemPosition(mVulnerable.getCategory()));

        //   spMaritalStatus.setEnabled(true);
    }

    @OnClick(R.id.fab)
    void save() {

        String firstName = edtFirstName.getText().toString();
        String surName = edtSurName.getText().toString();
        String otherName = edtOtherName.getText().toString();
        String gender;

        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String address = edtResidentAddress.getText().toString();
        String nin = edtNin.getText().toString();
        String community = edtCommunity.getText().toString();

        String nkName = edtNkName.getText().toString();
        String nkPhone = edtNkPhone.getText().toString();

        if (rbtFemale.isChecked()) {
            gender = "Female";
        } else {
            gender = "Male";
        }

        if (edtOccupation.getVisibility() == View.VISIBLE) {
            mOccupation = edtOccupation.getText().toString();
        }
        if (TextUtils.isEmpty(nin) || nin.length() < 11) {
            Util.showDialogueMessae(this, "Provide a valid NIN", getString(R.string.error_msg));
            return;
        }
        if (TextUtils.isEmpty(surName)) {
            Util.showDialogueMessae(this, "Surname field cant blank", getString(R.string.error_msg));
            return;
        }
        if (TextUtils.isEmpty(firstName)) {
            Util.showDialogueMessae(this, "First name field cant blank", getString(R.string.error_msg));
            return;
        }
        if (TextUtils.isEmpty(gender)) {
            Util.showDialogueMessae(this, "Gender not selected", getString(R.string.error_msg));
            return;
        }
        if (TextUtils.isEmpty(dateOfBirth)) {
            Util.showDialogueMessae(this, "Date of birth field cant blank", getString(R.string.error_msg));
            return;
        }

//        if (isUnderAged) {
//            Util.showDialogueMessae(EditVulnerableActivity.this, "This member is under aged and can't be enroll to the scheme", "Under");
//            return;
//        }

        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            Util.showDialogueMessae(this, "Phone number may not be correct", getString(R.string.error_msg));
            return;
        }

        if (mAge >= 5) {
            if (TextUtils.isEmpty(mOccupation)) {
                Util.showDialogueMessae(this, "Occupation field can't be blank", getString(R.string.error_msg));
                return;
            }
        }


        if (TextUtils.isEmpty(category)) {
            Util.showDialogueMessae(this, "Category is required", getString(R.string.error_msg));
            return;
        }

        if (TextUtils.isEmpty(mSettlement)) {
            Util.showDialogueMessae(this, "Settlement is required", getString(R.string.error_msg));
            return;
        }

//        if (mSelectedLGA == null) {
//            Util.showDialogueMessae(this, "LGA of Residence is required", getString(R.string.error_msg));
//            return;
//        }
//        if (mSelectedWard == null) {
//            Util.showDialogueMessae(this, "Ward of Residence is required", getString(R.string.error_msg));
//            return;
//        }

        if (!TextUtils.isEmpty(nin)) {
            if (nin.length() < 11) {
                Util.showDialogueMessae(this, "NIN may not be valid", getString(R.string.error_msg));
                return;
            }
        }
        if (TextUtils.isEmpty(community)) {
            Util.showDialogueMessae(this, "Community field is required", getString(R.string.error_msg));
            return ;
        }

        if (TextUtils.isEmpty(address)) {
            Util.showDialogueMessae(this, "Address field can't be blank", getString(R.string.error_msg));
            return;
        }

        if (!TextUtils.isEmpty(email)) {
            if (!Util.isEmailValid(email)) {
                Util.showDialogueMessae(this, "Email address not valid", getString(R.string.error_msg));
                return;
            }
        }

        if (TextUtils.isEmpty(nkName)) {
            Util.showDialogueMessae(this, "NK Name is required please provide name to proceed", getString(R.string.error_msg));
            return;
        }

        if (TextUtils.isEmpty(nkPhone) || nkPhone.length() < 11) {
            Util.showDialogueMessae(this, "Next of Kin's phone number may not be correct", getString(R.string.error_msg));
            return;
        }

        /////////// confirm input
        if (!mIsDetailsConfirmed) {
            Util.showDialogueMessae(this, "Please confirm user's detail before saving", "Confirm detail");
            mIsDetailsConfirmed = true;
            return;
        }

        mVulnerable.setFirstName(firstName);
        mVulnerable.setSurName(surName);
        mVulnerable.setOtherName(otherName);
        mVulnerable.setDateOfBirth(dateOfBirth);
        mVulnerable.setMaritalStatus(maritalStatus);
        mVulnerable.setGender(gender);
        mVulnerable.setPhone(phone);
        mVulnerable.setOccupation(mOccupation);
        mVulnerable.setEmail(email);
        mVulnerable.setNin(nin);
        mVulnerable.setWard(PrefUtils.getInstance(this).getWard());
        mVulnerable.setLga(PrefUtils.getInstance(this).getLGA());
        mVulnerable.setProvider(mFacility.getHcpcode());
        mVulnerable.setCategory(category);
        mVulnerable.setAddress(address);
        mVulnerable.setCommunity(community);
        mVulnerable.setNkName(nkName);
        mVulnerable.setNkRelationship(nkRelationship);
        mVulnerable.setNkPhone(nkPhone);
        mVulnerable.setSettlement(mSettlement);

        btnAdd.setVisibility(View.GONE);
        mAlertDialog.show();

        mViewModel.updatePrincipal(mVulnerable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MaybeObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Long aLong) {
//                        logTransaction();
//                        Toast.makeText(EditDependentActivity.this, mChildDependent.getFirstName() + " record update successful", Toast.LENGTH_LONG).show();

                        mFacility.setHcpcount(mFacility.getHcpcount() + 1);
                        mOldFacility.setHcpcount(mOldFacility.getHcpcount() - 1);

                        mViewModel.updateCount(mOldFacility).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                        mViewModel.updateCount(mFacility).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                        mAlertDialog.dismiss();
                        Util.showToast("Changes to " + mVulnerable.getFirstName() + " detail save successfully", false, getBaseContext(), getLayoutInflater());
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


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

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calculateAge(year, monthOfYear, dayOfMonth);
        doCheck();
    }

    private void calculateAge(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

        Map map = EnrolVulnActivity.getDateDifferenceInDDMMYYYY(calendar.getTime());
        int age = (int) map.get("year");
        int mon = (int) map.get("month");
        int dy = (int) map.get("day");
        mAge = age;
        dateOfBirth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        edtDOB.setText(dateOfBirth + " [" + age + "yrs]");

        isUnderAged = age < Constant.PRINCIPAL_MIN_AGE;
    }

    private void doCheck() {

        // check if reproductive
        initCategories();
        if (!mInit) {
            spDisability.setSelection(-1);
            category = null;
        }

        isReprodcutive(mAge);

        // check if aged (elderly)
        if (mAge < 85) {
            mCategories.remove(CATEGORY_AGED_85);
        }

        // check if underaged
        if (mAge >= 5) {
            spOccupaion.setVisibility(View.VISIBLE);
            mCategories.remove(CATEGORY_CHILDREN_UNDER_5_YRS);

        } else {
            spOccupaion.setVisibility(View.GONE);
        }

        // check
//
//        mDisabilityAdapter.notifyEntryChange(mCategories);
//
//
//        isReprodcutive(mAge);
        mDisabilityAdapter.notifyEntryChange(mCategories);
    }

    private void isReprodcutive(int age) {
        if (age < 15 || age > 45 || rbtMale.isChecked()) {
            mCategories.remove(CATEGORY_FEMALE_REPRODUCTIVE_15_45_YEARS);
        }
    }
}
