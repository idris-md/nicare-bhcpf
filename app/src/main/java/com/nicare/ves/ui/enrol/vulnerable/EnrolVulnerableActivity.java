//package com.nicare.ees.ui.enrol.vulnerable;
//
//import android.app.DatePickerDialog;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.mlkit.common.sdkinternal.SharedPrefManager;
//import com.nicare.ees.R;
//import com.nicare.ees.common.PrefUtils;
//import com.nicare.ees.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
//import com.nicare.ees.persistence.local.databasemodels.enrolmentmodels.Vulnerable;
//import com.nicare.ees.persistence.local.databasemodels.utilmodels.Facility;
//import com.nicare.ees.ui.adapter.FacilitySpinnerAdapter;
//import com.nicare.ees.ui.adapter.StringSpinnerAdapter;
//import com.nicare.ees.common.Util;
//
//import com.nicare.ees.ui.enrol.camera.CameraKitActivity;
//import com.nicare.ees.ui.enrol.fingerprint.HuifanActivity;
//import com.nicare.ees.ui.enrol.RecordSavedActivity;
//import com.nicare.ees.ui.enrol.fingerprint.UareUMainActivity;
//import com.nicare.ees.ui.enrol.dependent.EnrollDependentActivity;
//import com.tiper.MaterialSpinner;
//import com.tsongkha.spinnerdatepicker.DatePicker;
//import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class EnrolVulnerableActivity extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
//    private static final int CAMERA_REQUEST_CODE = 100;
//    private static final int BIOMETRIC_REQUEST_CODE = 101;
//
//    public static final String PIN = "pin";
//    public static final String PIN_PHOTO = "photo";
//
//    public static final String PIN_BIOMETRIC = "biometric";
//
//    static final int VULNERABLE_COL__ID = 0;
//
////    private static final String[] WARD_COLUMNS = {DataContract.ProviderEntry.COLUMN_HCP_WARD};
//
//    static final int COL_PROVIDER_NAME = 0;
//    static final int COL_PROVIDER_CODE = 1;
//
////    private static final String[] PROVIDER_COLUMNS = {
////            DataContract.ProviderEntry.COLUMN_HCP_NAME,
////            DataContract.ProviderEntry.COLUMN_HCP_CODE
////    };
////    private static final String[] LOCAL_GOVT_COLUMNS = {DataContract.LGAEntry.COLUMN_LOCAL_GOVT_NAME,};
////    private static final String[] VULNERABLE_ID_COLUMNS = {DataContract.VulnerableEntry.COLUMN_ID};
//
//    String provider;
//    String lga;
//    String ward;
//    String nkRelationship;
//    String maritalStatus;
//
//    String stateOfOrigin;
//    String lgaOfOrigin;
//
//    DatePickerDialog.OnDateSetListener mDateSetListener;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.imageViewPhoto)
//    ImageView imageViewPhoto;
//    @BindView(R.id.tv_premium)
//    TextView tvPremium;
//    @BindView(R.id.tv_bio)
//    TextView tvBio;
//    @BindView(R.id.edtSurname)
//    TextInputEditText edtSurname;
//    @BindView(R.id.edtFirstName)
//    TextInputEditText edtFirstName;
//    @BindView(R.id.edtOtherName)
//    TextInputEditText edtOtherName;
//    @BindView(R.id.male)
//    RadioButton male;
//    @BindView(R.id.female)
//    RadioButton female;
//    @BindView(R.id.edtDOB)
//    TextInputEditText edtDOB;
//    @BindView(R.id.edtPhone)
//    TextInputEditText edtPhone;
//    @BindView(R.id.edtEmail)
//    TextInputEditText edtEmail;
//    @BindView(R.id.spOccupation)
//    MaterialSpinner spOccupation;
//    @BindView(R.id.edtOccupation)
//    TextInputEditText edtOccupation;
//    @BindView(R.id.layOccupation)
//    TextInputLayout layOccupation;
//    @BindView(R.id.spMaritalStatus)
//    MaterialSpinner spMaritalStatus;
//    //    @BindView(R.id.spStateOrigin)
////    Spinner spStateOrigin;
////    @BindView(R.id.spLGAOfOrigin)
////    Spinner spLGAOfOrigin;
////    @BindView(R.id.spState)
////    Spinner spState;
////    @BindView(R.id.spLGA)
////    Spinner spLGA;
//    @BindView(R.id.lvl)
//    FrameLayout lvl;
//    @BindView(R.id.edtAddress)
//    TextInputEditText edtAddress;
//    @BindView(R.id.spProvider)
//    MaterialSpinner spProvider;
//    @BindView(R.id.nkName)
//    TextInputEditText nkName;
//    @BindView(R.id.spNkRel)
//    MaterialSpinner spNkRel;
//    @BindView(R.id.edtNKPhone)
//    TextInputEditText edtNKPhone;
////    @BindView(R.id.register)
////    Button register;
//
//
//    @BindView(R.id.spDisability)
//    MaterialSpinner spDisability;
//    @BindView(R.id.nav_menu)
//    BottomNavigationView mNavigationView;
//
//
//
//    @BindView(R.id.fab)
//    FloatingActionButton fab;
//
//    FacilitySpinnerAdapter mFacilitySpinnerAdapter;
//    StringSpinnerAdapter mOccupationSpinnerAdapter;
//    StringSpinnerAdapter mMaritalSpinnerAdapter;
//    StringSpinnerAdapter mNKSpinnerAdapter;
//
//
//
//    private String mPIN;
//    private Fingerprint mBiometric;
//    private String mPhoto;
//    private boolean mIsDetailsConfirmed;
//    private String dateOfBirth;
//    private boolean isUnderAged;
//    private String mEoWard;
//    private String mEoLGA;
//    private String mOccupation;
//    private Facility mFacility;
//
//    private EnrolVulnerableViewModel mEnrolmentViewModel;
//    private Fingerprint mFingerprint;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_enrol_vulnerable);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ButterKnife.bind(this);
//        readIntentStateValues();
//
//        bindViewModel();
//
//        mNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
//
//            switch (menuItem.getItemId()) {
//                case R.id.nav_bio_data:
//                    //mNavigationView.setItemBackgroundResource(R.color.colorPrimary);
//                    return true;
//                case R.id.nav_photo:
//                    //mNavigationView.setItemBackgroundResource(R.color.colorPrimary);
//                    //createEnrollmentObject();
//                    facialCapture();
//                    return true;
//
//                case R.id.nav_fingerprint:
//                    //mNavigationView.setItemBackgroundResource(R.color.colorPrimary);
//                    bioCapture();
//                    return true;
//
//                default:
//                    return false;
//            }
//
//        });
//
//
//    }
//
//    private void readIntentStateValues() {
//
//        Intent intent = getIntent();
//        mPIN = intent.getStringExtra(PIN);
//
//    }
//    private void bindViewModel() {
//
//        mEnrolmentViewModel = ViewModelProviders.of(this).get(EnrolVulnerableViewModel.class);
//
//        mNKSpinnerAdapter = new StringSpinnerAdapter(this);
//        mOccupationSpinnerAdapter = new StringSpinnerAdapter(this);
//        mMaritalSpinnerAdapter = new StringSpinnerAdapter(this);
//        mFacilitySpinnerAdapter = new FacilitySpinnerAdapter(this);
//
//        spOccupation.setAdapter(mOccupationSpinnerAdapter);
//        spMaritalStatus.setAdapter(mMaritalSpinnerAdapter);
//        spProvider.setAdapter(mFacilitySpinnerAdapter);
//        spNkRel.setAdapter(mNKSpinnerAdapter);
//
//        spOccupation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//                String occupation;
//                if (i > 0) {
//                    occupation = (String) materialSpinner.getAdapter().getItem(i);
//                    if (occupation.equalsIgnoreCase("Other specify")) {
//                        layOccupation.setVisibility(View.VISIBLE);
//                        edtOccupation.setEnabled(true);
//                        edtOccupation.requestFocus();
//                    } else {
//                        mOccupation = occupation;
//                        layOccupation.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//
//            }
//        });
//        spMaritalStatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//                maritalStatus = (String) materialSpinner.getAdapter().getItem(i);
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//
//            }
//        });
//        spNkRel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//                nkRelationship = (String) materialSpinner.getAdapter().getItem(i);
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//
//            }
//        });
//        spProvider.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
//                mFacility = (Facility) materialSpinner.getSelectedItem();
//
//            }
//
//            @Override
//            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
//
//            }
//        });
//
//        //tvPremium.setText(mPINType);
//
//        List<String> occupations = new ArrayList<>();
//        occupations.add("Select Occupation");
//        occupations.add("Farmer");
//        occupations.add("Artisan");
//        occupations.add("Student's");
//        occupations.add("Other specify");
//
//        mOccupationSpinnerAdapter.notifyEntryChange(occupations);
//        mNKSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.relationships)));
//        mMaritalSpinnerAdapter.notifyEntryChange(Arrays.asList(getResources().getStringArray(R.array.marital_status)));
//
//        mEoWard = SharedPrefManager.getInstance(getApplicationContext()).getWard();
//        mEoLGA = SharedPrefManager.getInstance(getApplicationContext()).getLGA();
//
//        mEnrolmentViewModel.getFacility(mEoLGA, mEoWard).observe(this, new Observer<List<Facility>>() {
//            @Override
//            public void onChanged(List<Facility> facilities) {
//                mFacilitySpinnerAdapter.notifyEntryChange(facilities);
//            }
//        });
//
//    }
//
//    String[] getProviders(Cursor cursor) {
//
//        List<String> strings = new ArrayList<>();
//        strings.add("Select Health Care Provider");
//        while (cursor.moveToNext()) {
//
//            strings.add("["+cursor.getString(COL_PROVIDER_CODE)+"] "+ cursor.getString(COL_PROVIDER_NAME) );
//
//        }
//
//        return strings.toArray(new String[strings.size()]);
//    }
//
//
//
//    @OnClick(R.id.edtDOB)
//    void setDate() {
//
//        Calendar calendar = Calendar.getInstance();
//        int month = ((calendar.get(Calendar.MONTH) + 1));
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int year = calendar.get(Calendar.YEAR);
//
//        new SpinnerDatePickerDialogBuilder()
//                .context(this)
//                .callback(this)
//                .spinnerTheme(R.style.DatePickerSpinner)
//                .defaultDate(year, month, day)
//                .maxDate(year, month, day)
//                .build()
//                .show();
//
//    }
//
//    @OnClick(R.id.fab)
//    void save() {
//
//        String firstName = edtFirstName.getText().toString();
//        String surName = edtSurname.getText().toString();
//        String otherName = edtOtherName.getText().toString();
//        String dateOfBirth = edtDOB.getText().toString();
//        String gender;
//
//        String phone = edtPhone.getText().toString();
//       // String occupation = edtOccupation.getText().toString();
//        String email = edtEmail.getText().toString();
//        String address = edtAddress.getText().toString();
//        String disability = null;
//        //edtDisability.getText().toString();
//
//        String nkName = edtFirstName.getText().toString();
//        String nkPhone = edtNKPhone.getText().toString();
//
//
//        if (female.isChecked()) {
//            gender = "Female";
//        } else {
//            gender = "Male";
//        }
//
//        if (layOccupation.getVisibility() == View.VISIBLE) {
//            mOccupation = edtOccupation.getText().toString();
//        }
//
//        if (TextUtils.isEmpty(surName)) {
//            Util.showDialogueMessae(this, "Surname field cant blank", getString(R.string.error_msg));
//            return ;
//        }
//        if (TextUtils.isEmpty(firstName)) {
//            Util.showDialogueMessae(this, "First name field cant blank", getString(R.string.error_msg));
//            return ;
//        }
//        if (TextUtils.isEmpty(gender)) {
//            Util.showDialogueMessae(this, "Gender not selected", getString(R.string.error_msg));
//            return ;
//        }
//        if (TextUtils.isEmpty(dateOfBirth)) {
//            Util.showDialogueMessae(this, "Date of birth field cant blank", getString(R.string.error_msg));
//            return ;
//        }
//
//        if (isUnderAged) {
//            Util.showDialogueMessae(EnrolVulnerableActivity.this, "This member is under aged and can't be enroll to the scheme", "Under");
//            return ;
//        }
//
//        if (TextUtils.isEmpty(phone)|| phone.length() < 11){
//            Util.showDialogueMessae(this, "Phone number may not be correct", getString(R.string.error_msg));
//            return ;
//        }
//
//        if (TextUtils.isEmpty(mOccupation)) {
//            Util.showDialogueMessae(this, "Occupation field can't be blank", getString(R.string.error_msg));
//            return;
//        }
//
//        if (TextUtils.isEmpty(address)) {
//            Util.showDialogueMessae(this, "Address field can't be blank", getString(R.string.error_msg));
//            return ;
//        }
//
//        if (!TextUtils.isEmpty(email)) {
//            if (!Util.isEmailValid(email)) {
//                Util.showDialogueMessae(this, "Email address not valid", getString(R.string.error_msg));
//                return ;
//            }
//        }
//
//        if (spMaritalStatus.getSelection() == 0) {
//            Util.showDialogueMessae(this, "Marital status not selected", getString(R.string.error_msg));
//            return ;
//        }
////        if (spStateOrigin.getSelectedItemPosition() == 0) {
////            Util.showDialogueMessae(this, "State of origin not selected", getString(R.string.error_msg));
////            return ;
////        }
////        if (spLGAOrigin.getSelectedItemPosition() == 0) {
////            Util.showDialogueMessae(this, "Local Govt of origin not selected", getString(R.string.error_msg));
////            return null;
////        }
//        /////////////lg res
////        if (spLGA.getSelectedItemPosition() == 0) {
////            Util.showDialogueMessae(this, "Local Govt of residence not selected", getString(R.string.error_msg));
////            return ;
////        }
////        if (spWard.getSelectedItemPosition() == 0) {
////            Util.showDialogueMessae(this, "Ward of residence not selected", getString(R.string.error_msg));
////            return ;
////        }
//
//        if (spProvider.getSelection() == 0) {
//            Util.showDialogueMessae(this, "Health Care Provider not selected", getString(R.string.error_msg));
//            return ;
//        }
//
//        if (TextUtils.isEmpty(nkName)) {
//            Util.showDialogueMessae(this, "NK Name field can't be blank", getString(R.string.error_msg));
//            return ;
//        }
//
//        if (spNkRel.getSelection() == 0) {
//            Util.showDialogueMessae(this, "NK Relationship field can't be blank", getString(R.string.error_msg));
//            return ;
//        }
//
//        if (TextUtils.isEmpty(nkPhone) || nkPhone.length() < 11) {
//            Util.showDialogueMessae(this, "Next of Kin's pone number may not be correct", getString(R.string.error_msg));
//            return ;
//        }
//
//        /////////// confirm input
//        if (!mIsDetailsConfirmed) {
//            Util.showDialogueMessae(this, "Please confirm user's detail before saving", "Confirm detail");
//            mIsDetailsConfirmed = true;
//            return;
//        }
//
//
//        Vulnerable vulnerable = new Vulnerable();
//
//        vulnerable.setFirstName(firstName);
//        vulnerable.setSurName(surName);
//        vulnerable.setOtherName(otherName);
//        vulnerable.setDateOfBirth(dateOfBirth);
//        vulnerable.setMaritalStatus(maritalStatus);
//        vulnerable.setGender(gender);
//        vulnerable.setPhone(phone);
//        vulnerable.setOccupation(mOccupation);
//        vulnerable.setEmail(email);
//        vulnerable.setAddress(address);
//        vulnerable.setWard(mEoWard);
//        vulnerable.setLga(mEoLGA);
//        vulnerable.setProvider(provider.substring(1,8));
//        vulnerable.setNkName(nkName);
//        vulnerable.setNkRelationship(nkRelationship);
//        vulnerable.setNkPhone(nkPhone);
//        vulnerable.setDisability(disability);
//        vulnerable.setPin(mPIN);
//        //  vulnerable.setBiometric(mBiometric);
//        // vulnerable.setPhoto(mPhoto);
//        vulnerable.setRegDate(Util.todayDate());
////        vulnerable.setStateOfOrigin(stateOfOrigin);
////        vulnerable.setLgaOfOrigin(lgaOfOrigin);
//
//        getContentResolver().insert(DataContract.VulnerableEntry.CONTENT_URI, vulnerableToContentValues(vulnerable));
//        Cursor mCursor = getContentResolver().query(DataContract.VulnerableEntry.CONTENT_URI, VULNERABLE_ID_COLUMNS, null, null, DataContract.VulnerableEntry.COLUMN_ID + " DESC LIMIT 1");
//        if (mCursor.moveToNext()) {
//            vulnerable.setId(mCursor.getInt(VULNERABLE_COL__ID));
//        }
//
//        if (vulnerable.getBiometric() != null) {
//            getContentResolver().insert(DataContract.VulnerableBiometricEntry.CONTENT_URI, vulnerableBiometricToContentValues(mBiometric, vulnerable.getId()));
//        }
//
//        ContentValues values = new ContentValues();
//        values.put(DataContract.PinEntry.COLUMN_USED, 1);
//        getContentResolver().update(DataContract.PinEntry.CONTENT_URI, values, DataContract.PinEntry.COLUMN_PIN + " = ? ", new String[]{vulnerable.getPin()});
//        //SharedRecord.getInstance().setVulnerable(vulnerable);
//        Intent intent = new Intent(this, RecordSavedActivity.class);
//        startActivity(intent);
//    }
//
//    private void verifyEntries(List<String> strings) {
//        for (String string : strings) {
//            if (TextUtils.isEmpty(string)) {
//                Util.showDialogueMessae(getBaseContext(), "Please fill in all fields before saving record", getString(R.string.error_msg));
//                return;
//            }
//        }
//    }
//
//
////    private ContentValues vulnerableToContentValues(Vulnerable vulnerable) {
////        ContentValues values = new ContentValues();
////
////        values.put(DataContract.VulnerableEntry.COLUMN_PIN, vulnerable.getPin());
////        values.put(DataContract.VulnerableEntry.COLUMN_FIRST_NAME, vulnerable.getFirstName());
////        values.put(DataContract.VulnerableEntry.COLUMN_SURNAME, vulnerable.getSurName());
////        values.put(DataContract.VulnerableEntry.COLUMN_OTHERNAME, vulnerable.getOtherName());
////        values.put(DataContract.VulnerableEntry.COLUMN_DATE_OF_BIRTH, vulnerable.getDateOfBirth());
////        values.put(DataContract.VulnerableEntry.COLUMN_MARITAL_STATUS, vulnerable.getMaritalStatus());
////        values.put(DataContract.VulnerableEntry.COLUMN_GENDER, vulnerable.getGender());
////        values.put(DataContract.VulnerableEntry.COLUMN_PHONE, vulnerable.getPhone());
////        values.put(DataContract.VulnerableEntry.COLUMN_OCCUPATION, vulnerable.getOccupation());
////        values.put(DataContract.VulnerableEntry.COLUMN_EMAIL, vulnerable.getEmail());
////        values.put(DataContract.VulnerableEntry.COLUMN_RESIDENTIAL_ADDRESS, vulnerable.getAddress());
////        values.put(DataContract.VulnerableEntry.COLUMN_LGA, vulnerable.getWard());
////        values.put(DataContract.VulnerableEntry.COLUMN_WARD, vulnerable.getLga());
////        values.put(DataContract.VulnerableEntry.COLUMN_PROVIDER, vulnerable.getProvider());
////        values.put(DataContract.VulnerableEntry.COLUMN_PHOTO, mPhoto);
////
////        values.put(DataContract.VulnerableEntry.COLUMN_BIOMETRIC_ISSUE, "");
////
////        values.put(DataContract.VulnerableEntry.COLUMN_DISABLITY, vulnerable.getDisability());
////
////        values.put(DataContract.VulnerableEntry.COLUMN_NK_NAME, vulnerable.getNkName());
////        values.put(DataContract.VulnerableEntry.COLUMN_NK_RELATIONSHIP, vulnerable.getNkRelationship());
////        values.put(DataContract.VulnerableEntry.COLUMN_NK_PHONE, vulnerable.getNkPhone());
////        values.put(DataContract.VulnerableEntry.COLUMN_REG_DATE, vulnerable.getRegDate());
//////        values.put(DataContract.VulnerableEntry.COLUMN_STATE_OF_ORIGIN, vulnerable.getStateOfOrigin());
//////        values.put(DataContract.VulnerableEntry.COLUMN_LGA_ORIGIN, vulnerable.getLgaOfOrigin());
////
////        return values;
////    }
////
////    private ContentValues vulnerableBiometricToContentValues(Fingerprint biometric, int id) {
////
////        ContentValues values = new ContentValues();
////        values.put(DataContract.PrincipalBiometricEntry.COLUMN_USER_ID, id);
////        if (biometric.getLeftOne() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_ONE, biometric.getLeftOne());
////        }
////        if (biometric.getLeftTwo() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_TWO, biometric.getLeftTwo());
////        }
////        if (biometric.getLeftThree() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_THREE, biometric.getLeftThree());
////        }
////        if (biometric.getLeftFour() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_FOUR, biometric.getLeftFour());
////        }
////        if (biometric.getLeftFive() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_FIVE, biometric.getLeftFive());
////        }
////        if (biometric.getRightOne() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_SIX, biometric.getRightOne());
////        }
////        if (biometric.getRightTwo() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_SEVEN, biometric.getRightTwo());
////        }
////        if (biometric.getRightThree() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_EIGHT, biometric.getRightThree());
////        }
////        if (biometric.getRightFour() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_NINE, biometric.getRightFour());
////        }
////        if (biometric.getRightFive() != null) {
////            values.put(DataContract.VulnerableBiometricEntry.COLUMN_FINGER_TEN, biometric.getRightFive());
////        }
////
////
////        return values;
////
////    }
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
//        // edtDOB.setText(simpleDateFormat.format(calendar.getTime()));
//
////        month = month + 1;
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(year, month, day);
//            /*  Calendar calendar1 = Calendar.getInstance();
//            calendar1.set(2012,01,23);*/
//        // System.out.println(EnrollDependentActivity.getDateDifferenceInDDMMYYYY(calendar.getTime(),new Date()));
//        Map map = EnrollDependentActivity.getDateDifferenceInDDMMYYYY(calendar.getTime());
//        int age = (int) map.get("year");
//        int mon = (int) map.get("month");
//        int dy = (int) map.get("day");
//
//        if (age < 0 || mon < 0 || dy < 0) {
//            edtDOB.setText(null);
//            dateOfBirth = null;
//            return;
//        }
//
//        //edtDOB.setText(year + "-" + month + "-" + day+" Age: "+age+"Yrs "+" "+mon+" Month"+" "+" "+dy+" Day(s)");
//        dateOfBirth = year + "-" + monthOfYear + "-" + dayOfMonth;
//
//        if (age <= 0) {
//            edtDOB.setText(dateOfBirth + " [" + mon + "month(s)]");
//        } else {
//            edtDOB.setText(dateOfBirth + " [" + age + "yr(s)]");
//        }
//
//        if (age < 1 && mon >= 3) {
//            isUnderAged = true;
//        } else {
//            isUnderAged = false;
//        }
//
//    }
//
//    // @OnClick(R.id.btnFacial)
//    void facialCapture() {
//        Intent intent = new Intent(this, CameraKitActivity.class);
//        startActivityForResult(intent, CAMERA_REQUEST_CODE);
//    }
//
//    // @OnClick(R.id.btnBio)
//    void bioCapture() {
//
//        if (mPhoto == null) {
//            Toast.makeText(this, "Please take photo before capturing Fingerprint", Toast.LENGTH_LONG).show();
//            mNavigationView.setSelectedItemId(R.id.nav_bio_data);
//            return;
//        }
//
//        String device = PrefUtils.getInstance(getBaseContext()).getBiometric();
//        Intent intent;
//
//        switch (device) {
//
//            case "FP07 Fingerprint":
//                intent = new Intent(this, HuifanActivity.class);
//                this.startActivityForResult(intent, BIOMETRIC_REQUEST_CODE);
//                break;
//
//            case "USB Fingerprint":
//                intent = new Intent(this, UareUMainActivity.class);
//                this.startActivityForResult(intent, BIOMETRIC_REQUEST_CODE);
//                break;
//
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        if (data == null) {
//            return;
//        }
//
//        switch (requestCode) {
//
//            case CAMERA_REQUEST_CODE:
//
//                if (resultCode == RESULT_OK) {
//
//                    mPhoto = data.getStringExtra("photo");
//
//                    imageViewPhoto.setImageBitmap(Util.decodeBase64ToBitmap(mPhoto));
//                    // btnBio.setEnabled(true);
//
//                }
//
//                break;
//
//            case BIOMETRIC_REQUEST_CODE:
//
//                mFingerprint = data.getParcelableExtra("biometric");
//                if (mFingerprint != null) {
//                    tvBio.setText("Captured");
//                    tvBio.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    fab.setVisibility(View.VISIBLE);
//                }
//                //ivBio.setImageResource(Util.getIConID(this, "prr"));
//                break;
//
//        }
//    }
//
//}
