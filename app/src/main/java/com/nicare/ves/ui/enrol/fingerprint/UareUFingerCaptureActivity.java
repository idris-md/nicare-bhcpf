//package com.nicare.ees.ui.enrol.fingerprint;
//
//import android.app.Activity;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Parcelable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.digitalpersona.uareu.Engine;
//import com.digitalpersona.uareu.Fid;
//import com.digitalpersona.uareu.Fmd;
//import com.digitalpersona.uareu.Reader;
//import com.digitalpersona.uareu.ReaderCollection;
//import com.digitalpersona.uareu.UareUGlobal;
//import com.nicare.ees.R;
//import com.nicare.ees.common.Globals;
//import com.nicare.ees.common.Util;
//import com.nicare.ees.model.beneficiaries.Fingerprint;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class UareUFingerCaptureActivity extends AppCompatActivity {
//
//    @BindView(R.id.tvInstruction)
//    TextView tvFpStatu;
//    @BindView(R.id.tvDevice)
//    TextView tvDevice;
//    @BindView(R.id.imageViewFP)
//    ImageView ivFpImage = null;
//    @BindView(R.id.imageViewLeft)
//    ImageView ivFpLeft = null;
//    @BindView(R.id.imageViewRight)
//    ImageView ivFpRight = null;
//    @BindView(R.id.btnReset)
//    Button btnReset;
//    @BindView(R.id.btnSkip)
//    Button btnSkip;
//
//    @BindView(R.id.spReason)
//    Spinner spinner;
//
//    @BindView(R.id.checkBox)
//    CheckBox checkBox;
//
//    int fingerCountLeft = 0;
//    int fingerCountRight = 5;
//
//    boolean isRightFinger;
//
//    Fingerprint biometric = new Fingerprint();
//    EnrollmentCallback enrollThread = null;
//    private String issues;
//
//    //===================================
//    private String refstring = "";
//    private ReaderCollection readers;
//    private String m_deviceName = "";
//    private String m_enginError;
//    private Reader m_reader = null;
//    private int m_DPI = 0;
//    private Bitmap m_bitmap = null;
//    private boolean m_reset = false;
//    private String m_textString;
//    private String m_text_conclusionString;
//    private Engine m_engine = null;
//    private int m_current_fmds_count = 0;
//    private boolean m_first = true;
//    private boolean m_success = false;
//    private Fmd m_enrollment_fmd = null;
//    private int m_templateSize = 0;
//    private Reader.CaptureResult cap_result = null;
//    private boolean mIsFormalSector;
//    private String mNID;
//
//    private void initializeActivity() {
//        m_enginError = "";
//
//        m_deviceName = getIntent().getExtras().getString("device_name");
//        tvDevice.setText("Device: " + m_deviceName);
//
//        m_bitmap = Globals.GetLastBitmap();
//        if (m_bitmap == null)
//            m_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black);
//        ivFpImage.setImageBitmap(m_bitmap);
//        readIntentStateValues();
//        UpdateGUI();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_uare_ufinger_print);
//        ButterKnife.bind(this);
//        m_textString = "Place any finger on the reader";
//        initializeActivity();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        // initiliaze dp sdk
//        try {
//            Context applContext = getApplicationContext();
//            m_reader = Globals.getInstance().getReader(m_deviceName, applContext);
//            m_reader.Open(Reader.Priority.EXCLUSIVE);
//            m_DPI = Globals.GetFirstDPI(m_reader);
//            m_engine = UareUGlobal.GetEngine();
//        } catch (Exception e) {
//            Log.w("UareUSampleJava", "error during init of reader");
//            m_deviceName = "";
//            onBackPressed();
//            return;
//        }
//
//        // loop capture on a separate thread to avoid freezing the UI
//        new Thread(() -> {
//            try {
//                m_current_fmds_count = 0;
//                m_reset = false;
//                enrollThread = new EnrollmentCallback(m_reader, m_engine);
//                while (!m_reset) {
//                    try {
//                        m_enrollment_fmd = m_engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378_2004, enrollThread);
//                        if (m_success = (m_enrollment_fmd != null)) {
//                            m_templateSize = m_enrollment_fmd.getData().length;
//                            m_current_fmds_count = 0;    // reset count on success
//
//                        }
//                    } catch (Exception e) {
//                        // template creation failed, reset count
//                        m_current_fmds_count = 0;
//                    }
//                }
//            } catch (Exception e) {
//                if (!m_reset) {
//                    Log.w("UareUSampleJava", "error during capture");
//                    m_deviceName = "";
//                    onBackPressed();
//                }
//            }
//        }).start();
//    }
//
//    private void readIntentStateValues() {
//        Intent intent = getIntent();
//        mIsFormalSector = intent.getBooleanExtra("isFormalSector", false);
//        mNID = intent.getStringExtra("rid");
//    }
//
//    public void UpdateGUI() {
//        ivFpImage.setImageBitmap(m_bitmap);
//        ivFpImage.invalidate();
//        tvFpStatu.setText(m_text_conclusionString);
//
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        try {
//            m_reset = true;
//            try {
//                m_reader.CancelCapture();
//            } catch (Exception e) {
//            }
//            m_reader.Close();
//        } catch (Exception e) {
//            Log.w("UareUSampleJava", "error during reader shutdown");
//        }
//        Intent i = new Intent();
//        i.putExtra("device_name", m_deviceName);
//        setResult(Activity.RESULT_OK, i);
//        finish();
//    }
//
//    @OnClick(R.id.btnSkip)
//    void enrol() {
//        continueToEnrolment();
////        String enrolType = SharedRecord.getInstance().getEnrolleeType();
////        Intent intent = new Intent(getBaseContext(), BiometricActivity.class);
////
////        switch (enrolType) {
////
////            case Constant.ENROLMENT_TYPE_DEPENDENT_UPDATE:
////
////                UpdateDependent updateDependent = SharedRecord.getInstance().getUpdateDependent();
////
////                if (biometric != null && !checkBox.isChecked()) {
////                    updateDependent.setBiometric(biometric);
////                } else {
////                    if (TextUtils.isEmpty(issues)) {
////                        Util.showDialogueMessae(this, getString(R.string.biometric_error), getString(R.string.error_msg));
////                        return;
////                    }
////                    updateDependent.setBiometricIssue(issues);
////                }
////                SharedRecord.getInstance().setUpdateDependent(updateDependent);
////
////                break;
////
////            case Constant.ENROLMENT_TYPE_HOUSE_HOLD:
////
////                PrincipalEnrolled enrollment = SharedRecord.getInstance().getRecord();
////
////                if (enrollment == null) {
////                    enrollment = new PrincipalEnrolled();
////                    enrollment.setPinType(SharedRecord.getInstance().getPinType());
////                    enrollment.setPinType(SharedRecord.getInstance().getPinType());
////                }
////
////                if (biometric != null && !checkBox.isChecked()) {
////                    enrollment.setBiometric(biometric);
////                } else {
////                    if (TextUtils.isEmpty(issues)) {
////                        Util.showDialogueMessae(this, getString(R.string.biometric_error), getString(R.string.error_msg));
////                        enrollment = null;
////                        return;
////                    }
////                    enrollment.setBiometricIssue(issues);
////                }
////                SharedRecord.getInstance().setRecord(enrollment);
////
////
////                break;
////
////            case Constant.ENROLMENT_TYPE_INDIVIDUAL:
////                enrollment = SharedRecord.getInstance().getRecord();
////
////                if (enrollment == null) {
////                    enrollment = new PrincipalEnrolled();
////                    enrollment.setPinType(SharedRecord.getInstance().getPinType());
////                    enrollment.setPinType(SharedRecord.getInstance().getPinType());
////                }
////
////                if (biometric != null && !checkBox.isChecked()) {
////                    enrollment.setBiometric(biometric);
////                } else {
////                    if (TextUtils.isEmpty(issues)) {
////                        Util.showDialogueMessae(this, getString(R.string.biometric_error), getString(R.string.error_msg));
////                        enrollment = null;
////                        return;
////                    }
////                    enrollment.setBiometricIssue(issues);
////                }
////                SharedRecord.getInstance().setRecord(enrollment);
////                break;
////
////            case Constant.ENROLMENT_TYPE_VULNERABLE:
////                Vulnerable vulnerable = SharedRecord.getInstance().getVulnerable();
////                if (vulnerable == null) {
////                    vulnerable = new Vulnerable();
////                }
////
////                if (biometric != null && !checkBox.isChecked()) {
////                    vulnerable.setBiometric(biometric);
////                } else {
////                    if (TextUtils.isEmpty(issues)) {
////                        Util.showDialogueMessae(this, getString(R.string.biometric_error), getString(R.string.error_msg));
////                        vulnerable = null;
////                        return;
////                    }
////                    vulnerable.setBiometricIssue(issues);
////                }
////
////                SharedRecord.getInstance().setVulnerable(vulnerable);
////
////                break;
////
////
////            case Constant.ENROLMENT_TYPE_FORMAL_SECTOR:
////                UnCapturedBeneficiarydsds formalSector = SharedRecord.getInstance().getFormalSector();
////                if (formalSector == null) {
////                    formalSector = new UnCapturedBeneficiarydsds();
////                }
////                if (biometric != null && !checkBox.isChecked()) {
////                    formalSector.setBiometric(biometric);
////                } else {
////                    if (TextUtils.isEmpty(issues)) {
////                        Util.showDialogueMessae(this, getString(R.string.biometric_error), getString(R.string.error_msg));
////                        formalSector = null;
////                        return;
////                    }
////                    formalSector.setBiometricIssue(issues);
////                }
////                SharedRecord.getInstance().setFormalSector(formalSector);
////                intent = new Intent(getBaseContext(), RecordSavedActivity.class);
////
////                break;
////
////            default:
////
////                SpouseDependent dependent = SharedRecord.getInstance().getDependent();
////                if (dependent == null) {
////                    dependent = new SpouseDependent();
////                }
////                if (biometric != null && !checkBox.isChecked()) {
////                    dependent.setBiometric(biometric);
////                } else {
////                    if (TextUtils.isEmpty(issues)) {
////                        Util.showDialogueMessae(this, getString(R.string.biometric_error), getString(R.string.error_msg));
////                        enrollment = null;
////                        return;
////                    }
////                    dependent.setBiometricIssue(issues);
////                }
////                SharedRecord.getInstance().setDependent(dependent);
////
////                break;
////        }
////        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        startActivity(intent);
//    }
//
//    private void continueToEnrolment() {
//
//            Intent intent = new Intent();
//            intent.putExtra("biometric", (Parcelable) biometric);
//            setResult(RESULT_OK, intent);
//            finish();
//
//
//    }
//
//    @OnClick(R.id.btnReset)
//    void reset() {
//        biometric = null;
//        fingerCountLeft = 0;
//        fingerCountRight = 0;
//        ivFpImage.setImageBitmap(null);
//        ivFpLeft.setImageResource(getIConID("left"));
//        ivFpRight.setImageResource(getIConID("right"));
//    }
//
//    @OnClick(R.id.checkBox)
//    void check() {
//
//        boolean isChecked = checkBox.isChecked();
//
//        if (isChecked) {
//            spinner.setEnabled(true);
//        } else {
//            spinner.setEnabled(false);
//        }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    private void initView() {
//        spinner.setEnabled(false);
//        spinner.setAdapter(Util.getSpinnerAdapter(getProviders(), this));
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                issues = (String) adapterView.getItemAtPosition(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
//
//    private void countPrint(int count) {
//
//        switch (count) {
//
//            case 1:
//                ivFpLeft.setImageResource(getIConID("leftone"));
//                biometric.setLeftOne(refstring);
//                break;
//            case 2:
//                ivFpLeft.setImageResource(getIConID("lefttwo"));
//                biometric.setLeftTwo(refstring);
//
//                break;
//            case 3:
//                ivFpLeft.setImageResource(getIConID("leftthree"));
//                biometric.setLeftThree(refstring);
//                break;
//            case 4:
//                ivFpLeft.setImageResource(getIConID("leftfour"));
//                biometric.setLeftFour(refstring);
//                break;
//            case 5:
//                ivFpLeft.setImageResource(getIConID("leftfive"));
//                biometric.setLeftFive(refstring);
//
//                if (fingerCountLeft >= 5 && fingerCountRight != 10) {
//                    swictchRight();
//                } else if (fingerCountLeft == 5 && fingerCountRight == 5) {
//                    tvFpStatu.setText("Capturing Complete");
//                }
//
//
//                break;
//            case 6:
//                ivFpRight.setImageResource(getIConID("rightone"));
//                biometric.setRightOne(refstring);
//
//                break;
//            case 7:
//                ivFpRight.setImageResource(getIConID("righttwo"));
//                biometric.setRightTwo(refstring);
//                break;
//            case 8:
//                ivFpRight.setImageResource(getIConID("rightthree"));
//                biometric.setRightThree(refstring);
//                break;
//            case 9:
//                ivFpRight.setImageResource(getIConID("rightfour"));
//                biometric.setRightFour(refstring);
//                break;
//            case 10:
//                ivFpRight.setImageResource(getIConID("rightfive"));
//                biometric.setRightFive(refstring);
//                if (fingerCountLeft != 5) {
//                    swictchLeft();
//                } else {
//                    tvFpStatu.setText("Capturing Complete");
//                    continueToEnrolment();
//                }
//
//                break;
//
//        }
//
//
//    }
//
//    private int getIConID(String icon) {
//
//        int courseIconID = getBaseContext().getResources().getIdentifier(icon, "drawable", "com.nicare");
//
//        return courseIconID;
//
//    }
//
//    @OnClick(R.id.imageViewLeft)
//    void btnLeft() {
//        swictchLeft();
//    }
//
//    private void swictchLeft() {
//        ivFpLeft.setBackgroundResource(R.drawable.iv_border);
//        ivFpRight.setBackgroundResource(0);
//
//        isRightFinger = false;
//    }
//
//    @OnClick(R.id.imageViewRight)
//    void btnRight() {
//        swictchRight();
//    }
//
//    private void swictchRight() {
//        ivFpRight.setBackgroundResource(R.drawable.iv_border);
//        ivFpLeft.setBackgroundResource(0);
//
//        isRightFinger = true;
//    }
//
//    String[] getProviders() {
//
//        List<String> strings = new ArrayList<>();
//        strings.add("Select Health Care Provider");
//        strings.add("Under Aged");
//        strings.add("Leprosy");
//
//
//        return strings.toArray(new String[strings.size()]);
//    }
//
//    private ContentValues formalSectorBiometricToContentValues(Fingerprint biometric, String id) {
//
//
//        ContentValues values = new ContentValues();
//        values.put(DataContract.FormalSectorBiometricEntry.COLUMN_USER_ID, id);
//        if (biometric.getLeftOne() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_ONE, biometric.getLeftOne());
//        }
//
//        if (biometric.getLeftTwo() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_TWO, biometric.getLeftTwo());
//            // values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_TWO_SIZE, biometric.getLeftTwo().getRefsize());
//        }
//        if (biometric.getLeftThree() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_THREE, biometric.getLeftThree());
//            // values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_THREE_SIZE, biometric.getLeftThree().getRefsize());
//        }
//        if (biometric.getLeftFour() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_FOUR, biometric.getLeftFour());
//            //values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_FOUR_SIZE, biometric.getLeftFour().getRefsize());
//        }
//        if (biometric.getLeftFive() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_FIVE, biometric.getLeftFive());
//            //values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_FIVE_SIZE, biometric.getLeftFive().getRefsize());
//        }
//        if (biometric.getRightOne() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_SIX, biometric.getRightOne());
//            //values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_SIX_SIZE, biometric.getRightOne().getRefsize());
//        }
//        if (biometric.getRightTwo() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_SEVEN, biometric.getRightTwo());
//            //values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_SEVEN_SIZE, biometric.getRightTwo().getRefsize());
//        }
//        if (biometric.getRightThree() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_EIGHT, biometric.getRightThree());
//            // values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_EIGHT_SIZE, biometric.getRightThree().getRefsize());
//        }
//        if (biometric.getRightFour() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_NINE, biometric.getRightFour());
//            // values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_SIX_SIZE, biometric.getRightFour().getRefsize());
//        }
//        if (biometric.getRightFive() != null) {
//            values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_TEN, biometric.getRightFive());
//            // values.put(DataContract.FormalSectorBiometricEntry.COLUMN_FINGER_TEN_SIZE, biometric.getRightFive().getRefsize());
//        }
//
//
//        return values;
//
//    }
//
//    public class EnrollmentCallback
//            extends Thread
//            implements Engine.EnrollmentCallback {
//        public int m_current_index = 0;
//
//        private Reader m_reader = null;
//        private Engine m_engine = null;
//
//        public EnrollmentCallback(Reader reader, Engine engine) {
//            m_reader = reader;
//            m_engine = engine;
//        }
//
//        // callback function is called by dp sdk to retrieve fmds until a null is returned
//        @Override
//        public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
//            Engine.PreEnrollmentFmd result = null;
//            while (!m_reset) {
//                try {
//                    cap_result = m_reader.Capture(Fid.Format.ANSI_381_2004, Globals.DefaultImageProcessing, m_DPI, -1);
//                } catch (Exception e) {
//                    Log.w("UareUSampleJava", "error during capture: " + e.toString());
//                    m_deviceName = "";
//                    onBackPressed();
//                }
//
//                // an error occurred
//                if (cap_result == null || cap_result.image == null) continue;
//
//                try {
//                    m_enginError = "";
//                    // save bitmap image locally
//                    m_bitmap = Globals.GetBitmapFromRaw(cap_result.image.getViews()[0].getImageData(), cap_result.image.getViews()[0].getWidth(), cap_result.image.getViews()[0].getHeight());
//                    Engine.PreEnrollmentFmd prefmd = new Engine.PreEnrollmentFmd();
//                    prefmd.fmd = m_engine.CreateFmd(cap_result.image, Fmd.Format.ANSI_378_2004);
//                    prefmd.view_index = 0;
//                    m_current_fmds_count++;
//
//                    result = prefmd;
//                    break;
//                } catch (Exception e) {
//                    m_enginError = e.toString();
//                    Log.w("UareUSampleJava", "Engine error: " + e.toString());
//                }
//            }
//
//            m_text_conclusionString = Globals.QualityToString(cap_result);
//
//            if (!m_enginError.isEmpty()) {
//                m_text_conclusionString = "Engine: " + m_enginError;
//            }
//
//            if (m_enrollment_fmd != null || m_current_fmds_count == 0) {
//                if (!m_first) {
//                    if (m_text_conclusionString.length() == 0) {
//                        m_text_conclusionString = m_success ? "Template created place next Finger" : "PrincipalEnrolled template failed. Please try again";
//                    }
//                }
//                refstring = Base64.encodeToString(m_enrollment_fmd.getData(), 0, m_enrollment_fmd.getData().length, Base64.DEFAULT);
//
//                m_textString = "Place next finger on the reader";
//                m_enrollment_fmd = null;
//
//            } else {
//                m_first = false;
//                m_success = false;
//                m_textString = "Place the same finger on the reader";
//            }
//
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (m_success) {
//                        if (isRightFinger) {
//
//                            fingerCountRight += 1;
//                            countPrint(fingerCountRight);
//                            ivFpImage.setImageBitmap(null);
//
//                        } else {
//                            fingerCountLeft += 1;
//                            countPrint(fingerCountLeft);
//                            ivFpImage.setImageBitmap(null);
//
//                        }
//                    }
//
//                    UpdateGUI();
//                }
//            });
//
//            return result;
//        }
//    }
//
//}