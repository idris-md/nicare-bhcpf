package com.nicare.ves.ui.enrol.fingerprint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fgtit.data.ConversionsEx;
import com.fgtit.device.Constants;
import com.fgtit.device.FPModule;
import com.fgtit.fpcore.FPMatch;
import com.google.android.material.button.MaterialButton;
import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.ui.enrol.camera.CameraKitActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HuifanActivityTwo extends AppCompatActivity {

    @BindView(R.id.pinkyLeft)
    ImageView pinkyLeft;
    @BindView(R.id.ringLeft)
    ImageView ringLeft;
    @BindView(R.id.middleLeft)
    ImageView middleLeft;
    @BindView(R.id.indexLeft)
    ImageView indexLeft;
    @BindView(R.id.thumbLeft)
    ImageView thumbLeft;
    @BindView(R.id.thumbRight)
    ImageView thumbRight;
    @BindView(R.id.indexRight)
    ImageView indexRight;
    @BindView(R.id.middleRight)
    ImageView middleRight;
    @BindView(R.id.ringRight)
    ImageView ringRight;
    @BindView(R.id.pinkyRight)
    ImageView pinkyRight;
    @BindView(R.id.imageViewRight)
    ConstraintLayout imageViewRight;
    @BindView(R.id.frReason)
    FrameLayout frReason;
    @BindView(R.id.btnPhotoLeft)
    MaterialButton btnPhotoLeft;
    @BindView(R.id.btnPhotoRight)
    MaterialButton btnPhotoRight;
    @BindView(R.id.photoLeft)
    ImageView photoLeft;
    @BindView(R.id.photoRight)
    ImageView photoRight;
    @BindView(R.id.finger)
    TextView fingerSelec;
    @BindView(R.id.tvInstruction)
    TextView tvFpStatu;
    @BindView(R.id.tvError)
    TextView tvFpError;
    @BindView(R.id.imageViewFP)
    ImageView ivFpImage = null;
    @BindView(R.id.btnReset)
    MaterialButton btnReset;
    @BindView(R.id.btnSkip)
    MaterialButton btnSkip;
    @BindView(R.id.spReason)
    Spinner spinner;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    boolean isRightFinger;
    Fingerprint biometric = null;
    private FPModule fpm = new FPModule();
    //    private StringBuilder matstring = new StringBuilder();
    private byte[] bmpdata = new byte[Constants.RESBMP_SIZE];

    //private TextView tvDevStatu;
//    private RadioGroup radioGroup;

    //    int fingerCountLeft = 0;
//    int fingerCountRight = 5;
    private int bmpsize = 0;
    private byte[] refdataOne = new byte[Constants.TEMPLATESIZE];
    private byte[] refdataTwo = new byte[Constants.TEMPLATESIZE];
    private byte[] refdata = new byte[Constants.TEMPLATESIZE];
    private int refsize = 0;
    private byte[] refdataEnrol = new byte[Constants.TEMPLATESIZE * 2];
    private int refsizeEnrol = 0;
    private StringBuilder refstring = new StringBuilder();
    private int worktype = 0;
    private String issues;
    private String mNID;
    private boolean mIsFormalSector;
    private List<byte[]> mRefEnrols = new ArrayList<>();
    private int mCountPosition;
    private int lastCount;
    private String mPhoto;
    private boolean isChild;
    private boolean leftSkiped;
    private boolean rightSkiped;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.FPM_DEVICE:
                    switch (msg.arg1) {
                        case Constants.DEV_OK:
                            tvFpStatu.setText("Scanner Ready");
                            break;
                        case Constants.DEV_FAIL:
                            tvFpStatu.setText("Scanner Fail");
                            break;
                        case Constants.DEV_ATTACHED:
                            tvFpStatu.setText("USB Device Attached");
                            break;
                        case Constants.DEV_DETACHED:
                            tvFpStatu.setText("USB Device Detached");
                            break;
                        case Constants.DEV_CLOSE:
                            tvFpStatu.setText("Device Close");
                            break;
                    }
                    break;
                case Constants.FPM_PLACE:
                    tvFpStatu.setText("Place Finger");
                    break;
                case Constants.FPM_LIFT:
                    tvFpStatu.setText("Lift Finger");
                    tvFpError.setText(" ");
                    break;
                case Constants.FPM_GENCHAR: {
                    if (msg.arg1 == 1) {
//                        if (worktype == 0) {
////                            tvFpStatu.setText("Generate Template OK");
////                            matsize = fpm.GetTemplateByGen(matdata);
////                            matstring.append(ConversionsEx.getInstance().ToAnsiIso(matdata, ConversionsEx.ISO_19794_2011, ConversionsEx.COORD_MIRRORV));
////                            int sc = MatchIsoTemplateStr(refstring.toString(), matstring.toString());
////                            tvFpStatu.setText("Match Result:" + String.valueOf(sc) + "/" + String.valueOf(FPMatch.getInstance().MatchTemplate(refdata, matdata)));
//
////                            switch (radioGroup.getCheckedRadioButtonId()) {
////                                case R.id.radio1:
////
////                                    //if(fpm.getDeviceType()==Constants.DEV_7_3G_SPI){
////                                    matstring = ConversionsEx.getInstance().ToAnsiIso(matdata, ConversionsEx.ANSI_378_2004, ConversionsEx.COORD_MIRRORV);
////                                    Log.d("test", "handleMessage: Test " + Base64.encodeToString(matdata, 0));
////                                    Log.d("test", "handleMessage: Test " + matstring);
////
////                                    break;
////                                case R.id.radio2:
////                                    //if(fpm.getDeviceType()==Constants.DEV_7_3G_SPI){
////                                    matstring = ConversionsEx.getInstance().ToAnsiIso(matdata, ConversionsEx.ISO_19794_2005, ConversionsEx.COORD_MIRRORV);
////                                    Log.d("test", "handleMessage: Test " + matstring);
////                                    //}else{
////                                    //	matstring=ConversionsEx.getInstance().ToAnsiIso(matdata, ConversionsEx.ISO_19794_2005, ConversionsEx.COORD_NOTCHANGE);
////                                    //}
////                                    break;
////                                case R.id.radio3:
////                                    refstring = ConversionsEx.getInstance().ToAnsiIso(refdata, ConversionsEx.ISO_19794_2009, ConversionsEx.COORD_MIRRORV);
////                                    break;
////
////                                case R.id.radio4:
////                                    refstring = ConversionsEx.getInstance().ToAnsiIso(refdata, ConversionsEx.ISO_19794_2011, ConversionsEx.COORD_MIRRORV);
//
////                                case R.id.radio5:
////                                    int rawImageSize = bmpsize - 1078;
////                                    byte[] rawImageData = new byte[rawImageSize];
////                                    Bitmap bm1 = BitmapFactory.decodeByteArray(bmpdata, 0, bmpsize);
////
////                                    System.arraycopy(bmpdata, 1078, rawImageData, 0, rawImageSize);
////
////                                    byte[] ansi381Data = ConversionsEx.getInstance().toANSI381u(rawImageData, rawImageSize, bm1.getWidth(), bm1.getHeight());
////                                    String ansi381String = Base64.encodeToString(ansi381Data, 0, ansi381Data.length, Base64.DEFAULT);
////
////                                    Log.d("TestANSI", "handleMessage: ANSI381 String " + ansi381String);
////                                    // tvFpData.setText(ansi381String);
////
////                            }
//
//                            //tvFpData.setText(matstring);
//
//                        } else
                        if (worktype == 1) {

                            tvFpStatu.setText("Place Finger");

                            refsize = fpm.GetTemplateByGen(refdataOne);
                            if (fpm.GenerateTemplate(1)) {
                                worktype = 2;
                            } else {
                                Toast.makeText(HuifanActivityTwo.this, "Busy", Toast.LENGTH_SHORT).show();
                            }
                        } else if (worktype == 2) {
                            refsize = fpm.GetTemplateByGen(refdataTwo);

                            if (FPMatch.getInstance().MatchTemplate(refdataOne, refdataTwo) > 15) {
                                tvFpStatu.setText("Enrol template ok");
                            } else {
//                                worktype = 1;
                                tvFpError.setText("Finger not match pls re-enrol");
                                startEnrollment();
                                return;
                            }

                            refdataEnrol = byteMerger(refdataOne, refdataTwo);
                            for (byte[] ref : mRefEnrols) {
                                if (FPMatch.getInstance().MatchTemplate(ref, refdataEnrol) > 30) {
                                    tvFpError.setText("Finger already captured");
                                    startEnrollment();
                                    return;
                                }
                            }

//                           mRefEnrols.add(refdataEnrol);
                            //if(fpm.getDeviceType()==Constants.DEV_7_3G_SPI){
                            refstring.append(ConversionsEx.getInstance().ToAnsiIso(refdataEnrol, ConversionsEx.ISO_19794_2011, ConversionsEx.COORD_MIRRORV));
                            updateView();

                        }
                    } else {
                        tvFpStatu.setText("Generate Template Fail");
                        startEnrollment();
                    }
                }
                break;
                case Constants.FPM_NEWIMAGE: {
                    bmpsize = fpm.GetBmpImage(bmpdata);
                    Bitmap bm1 = BitmapFactory.decodeByteArray(bmpdata, 0, bmpsize);
                    ivFpImage.setImageBitmap(bm1);
                    if (photoLeft.isShown()) {
                        photoLeft.setVisibility(View.INVISIBLE);
                    }
                }
                break;
                case Constants.FPM_TIMEOUT:
                    tvFpStatu.setText("Time Out");
                    break;
            }
        }
    };

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        ButterKnife.bind(this);
        readIntentStateValues();
        initView();
        popu();
        //tvDevStatu.setText(String.valueOf(fpm.getDeviceType()));

        fpm.InitMatch();
        fpm.SetContextHandler(this, mHandler);
        fpm.SetTimeOut(Constants.TIMEOUT_LONG);
        fpm.SetLastCheckLift(true);
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        fpm.SetImageOrientation(sp.getInt("ImageOrientation", 1));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void readIntentStateValues() {
        Intent intent = getIntent();
        mIsFormalSector = intent.getBooleanExtra("isFormalSector", false);
        isChild = intent.getBooleanExtra("isChild", false);
        mNID = intent.getStringExtra("rid");
    }

    private void updateView() {
//        if (isRightFinger) {
//            if (fingerCountRight < 5)
//                fingerCountRight = 5;
//            fingerCountRight += 1;
        countPrint(mCountPosition);
//            ivFpImage.setImageBitmap(null);
//
//        } else if (!isRightFinger) {
//
//            fingerCountLeft += 1;
//            countPrint(fingerCountLeft);
//            ivFpImage.setImageBitmap(null);
//        }
//        } else if (fingerCountLeft == 5 && fingerCountRight == 5) {
//            Intent intent = new Intent();
//            intent.putExtra("biometric", (Parcelable) biometric);
//            setResult(RESULT_OK, intent);
//            finish();
////            Intent intent = new Intent(getBaseContext(), ChooseEnrolmentTypeActivity.class);
////            switch (SharedRecord.getInstance().getEnrolleeType()) {
////
////                case Constant.ENROLMENT_TYPE_HOUSE_HOLD:
////                    SharedRecord.getInstance().getRecord().setBiometric(biometric);
////
////                    break;
////                case Constant.ENROLMENT_TYPE_INDIVIDUAL:
////                    SharedRecord.getInstance().getRecord().setBiometric(biometric);
////
////                    break;
////                case Constant.ENROLMENT_TYPE_VULNERABLE:
////                    SharedRecord.getInstance().getVulnerable().setBiometric(biometric);
////
////                    break;
////                case Constant.ENROLMENT_TYPE_CHILD:
////                    SharedRecord.getInstance().getDependent().setBiometric(biometric);
////
////                    break;
////                case Constant.ENROLMENT_TYPE_SPOUSE:
////                    SharedRecord.getInstance().getDependent().setBiometric(biometric);
////                    break;
////
////
////            }
////            intent.putExtra("from", "bio");
////            startActivity(intent);
//        }
    }

    public int MatchIsoTemplateByte(byte[] piFeatureA, byte[] piFeatureB) {
        byte[] adat = new byte[512];
        byte[] bdat = new byte[512];
        int sc = 0;
//        switch (radioGroup.getCheckedRadioButtonId()) {
//            case R.id.radio1:
//                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ANSI_378_2004);
//                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ANSI_378_2004);
//                return FPMatch.getInstance().MatchTemplate(adat, bdat);
//            case R.id.radio2:
        ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ISO_19794_2011);
        ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ISO_19794_2011);
        return FPMatch.getInstance().MatchTemplate(adat, bdat);
//            case R.id.radio3:
//                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ISO_19794_2009);
//                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ISO_19794_2009);
//                return FPMatch.getInstance().MatchTemplate(adat, bdat);
//            case R.id.radio4:
//                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ISO_19794_2011);
//                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ISO_19794_2011);
//                return FPMatch.getInstance().MatchTemplate(adat, bdat);
//        }
    }

    public int MatchIsoTemplateStr(String strFeatureA, String strFeatureB) {
        byte[] piFeatureA = Base64.decode(strFeatureA, Base64.DEFAULT);
        byte[] piFeatureB = Base64.decode(strFeatureB, Base64.DEFAULT);
        return MatchIsoTemplateByte(piFeatureA, piFeatureB);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btnSkip)
    void enrol() {
        continueToEnrolment();
    }

    private void continueToEnrolment() {

        if (biometric == null) {
            Util.showDialogueMessae(this, "Please capture enrolee fingerprint, or take photo in case of missing or unreadable finger ", "Fingerprint Not Captured");
            return;
        }

        biometric.setFailureReason(issues);
        boolean isRightWithMissing = checkRightMissing();
        boolean isLeftWithMissing = checkLeftMissing();

//        if (isRightWithMissing && biometric.getPhotoRight() == null) {
//            Util.showDialogueMessae(this, "Please capture missing finger from the right hand or take photo if it cannot be capture", "Missing Fingerprint(s)");
//            return;
//        }

        if ((isLeftWithMissing || isRightWithMissing) && biometric.getFingersPhoto() == null) {
            Util.showDialogueMessae(this, "Please take a photo of the Palm(s) with missing Finger(s)", "Missing Fingerprint(s)");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("biometric", biometric);
        setResult(RESULT_OK, intent);
        finish();

    }

    private boolean checkLeftMissing() {

        if (biometric.getLeftFive() == null) return true;
        if (biometric.getLeftFour() == null) return true;
        if (biometric.getLeftThree() == null) return true;
        if (biometric.getLeftTwo() == null) return true;
        return biometric.getLeftOne() == null;
    }

    private boolean checkRightMissing() {

        if (biometric.getRightFive() == null) return true;
        if (biometric.getRightFour() == null) return true;
        if (biometric.getRightThree() == null) return true;
        if (biometric.getRightTwo() == null) return true;
        return biometric.getRightOne() == null;
    }

    @OnClick(R.id.btnReset)
    void reset() {
        mCountPosition = 0;
        biometric = null;
//        fingerCountLeft = 0;
//        fingerCountRight = 0;
        ivFpImage.setImageResource(0);
        photoLeft.setImageResource(0);
        checkBox.setEnabled(true);
        mRefEnrols.clear();
        popu();
        refstring.setLength(0);

        pinkyLeft.setImageResource(0);
        ringLeft.setImageResource(0);
        middleLeft.setImageResource(0);
        indexLeft.setImageResource(0);
        thumbLeft.setImageResource(0);

        pinkyRight.setImageResource(0);
        ringRight.setImageResource(0);
        middleRight.setImageResource(0);
        indexRight.setImageResource(0);
        thumbRight.setImageResource(0);

    }

    @OnClick(R.id.checkBox)
    void check() {

        ivFpImage.setImageResource(0);
        boolean isChecked = checkBox.isChecked();
        if (isChecked) {
            spinner.setEnabled(true);
        } else {
            spinner.setEnabled(false);
            spinner.setSelection(0);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fpm.ResumeRegister();
        fpm.OpenDevice();
        //startEnrollment();

    }

    @Override
    protected void onStop() {
        super.onStop();
        fpm.PauseUnRegister();
        fpm.CloseDevice();
    }

    private void initView() {
        ButterKnife.bind(this);
        spinner.setEnabled(false);
        spinner.setAdapter(Util.getSpinnerAdapter(getCaptureFailure(), this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    issues = (String) adapterView.getItemAtPosition(i);
                    if (biometric == null) {
                        biometric = new Fingerprint();
                        biometric.setFailureReason(issues);

                        btnPhotoLeft.setEnabled(true);
                        btnPhotoRight.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void startEnrollment() {
        if (fpm.GenerateTemplate(1)) {
            worktype = 1;
        } else {
            Toast.makeText(HuifanActivityTwo.this, "Busy", Toast.LENGTH_SHORT).show();
        }
    }

    private void countPrint(int count) {

        lastCount = 0;

        mRefEnrols.add(count - 1, refdataEnrol);

        switch (count) {

            case 1:

                flagCaptured(thumbLeft);
                biometric.setLeftOne(refstring.toString());
//                startEnrollment();
                //checkBox.setEnabled(false);
//                flagClicked(indexLeft, count + 1);
                nextLeftFinger();
                break;

            case 2:
                flagCaptured(indexLeft);
                biometric.setLeftTwo(refstring.toString());
                flagClicked(middleLeft, count + 1);
//                startEnrollment();
                break;
            case 3:
                flagCaptured(middleLeft);
                biometric.setLeftThree(refstring.toString());

//                flagClicked(ringLeft, count + 1);
//                startEnrollment();
                nextLeftFinger();
                break;

            case 4:
                flagCaptured(ringLeft);
                biometric.setLeftFour(refstring.toString());
//                mRefEnrols.add(4, refdataEnrol);

//                flagClicked(pinkyLeft, count + 1);
//                startEnrollment();
                nextLeftFinger();
                break;

            case 5:
                if (null == biometric) {
                    biometric = new Fingerprint();
                }
                flagCaptured(pinkyLeft);
                biometric.setLeftFive(refstring.toString());

                if (checkLeftMissing() && !leftSkiped) {
                    nextLeftFinger();
                } else if (checkRightMissing()) {
                    nextRightFinger();
                } else {
                    tvFpStatu.setText(R.string.capture_completed);
                    removePhotos();
                }
//                count = -5;
//                if (count >= 5 && fingerCountRight != 10) {
//                    swictchRight();
//                } else if (count == 5 && fingerCountRight == 5) {
//                    tvFpStatu.setText("Capturing Complete");
//                }
//                startEnrollment();
                break;

            case 6:
                if (null == biometric) {
                    biometric = new Fingerprint();
                }
                flagCaptured(thumbRight);
                //ivFpRight.setImageResource(getIConID("rightone"));
                biometric.setRightOne(refstring.toString());

//                startEnrollment();
                //checkBox.setEnabled(false);
//                flagClicked(indexRight, count + 1);
                nextRightFinger();
                break;

            case 7:
                flagCaptured(indexRight);
                // ivFpRight.setImageResource(getIConID("righttwo"));
                biometric.setRightTwo(refstring.toString());
//                startEnrollment();
//                flagClicked(middleRight, count + 1);
                nextRightFinger();
                break;

            case 8:
                flagCaptured(middleRight);
                //ivFpRight.setImageResource(getIConID("rightthree"));
                biometric.setRightThree(refstring.toString());
//                flagClicked(ringRight, count + 1);
                nextRightFinger();
//                startEnrollment();
                break;

            case 9:
                flagCaptured(ringRight);
                // ivFpRight.setImageResource(getIConID("rightfour"));
                biometric.setRightFour(refstring.toString());
//                startEnrollment();
//                flagClicked(pinkyRight, count + 1);
                nextRightFinger();
                break;
            case 10:
                flagCaptured(pinkyRight);
                //ivFpRight.setImageResource(getIConID("rightfive"));
                biometric.setRightFive(refstring.toString());

                if (checkRightMissing()) {
                    nextRightFinger();
                } else if (checkLeftMissing()) {
                    nextLeftFinger();
                } else {
                    tvFpStatu.setText(R.string.capture_completed);
                    removePhotos();
                }
//                if (count != 5) {
//                    swictchLeft();
////                    startEnrollment();
//                } else {
//                    tvFpStatu.setText("Capturing Complete");
////                    startEnrollment();
//                }
                break;

        }


    }

    private void removePhotos() {
        photoLeft.setImageResource(0);
        photoRight.setImageResource(0);
        biometric.setFingersPhoto(null);
        biometric.setFailureReason(null);
        spinner.setSelection(0);
        checkBox.setChecked(false);
    }

    private void clearRefString() {
        refstring.setLength(0);
    }

    private int getIConID(String icon) {

        int courseIconID = getBaseContext().getResources().getIdentifier(icon, "drawable", "com.nicare.ees");
        return courseIconID;

    }

    //    @OnClick(R.id.imageViewLeft)
//    void btnLeft() {
//        swictchLeft();
//    }
//
    private void nextLeftFinger() {
        clearRefString();
        if (biometric.getLeftOne() == null) {
            flagClicked(thumbLeft, 1);
            return;
        }
        if (biometric.getLeftTwo() == null) {
            flagClicked(indexLeft, 2);
            return;
        }
        if (biometric.getLeftThree() == null) {
            flagClicked(middleLeft, 3);
            return;
        }
        if (biometric.getLeftFour() == null) {
            flagClicked(ringLeft, 4);
            return;
        }
        if (biometric.getLeftFive() == null) {
            flagClicked(pinkyLeft, 5);
        }
    }

    //
//    //
////    @OnClick(R.id.imageViewRight)
////    void btnRight() {
////        swictchRight();
////    }
////
    private void nextRightFinger() {
        clearRefString();
        if (biometric.getRightOne() == null) {
            flagClicked(thumbRight, 6);
            return;
        }
        if (biometric.getRightTwo() == null) {
            flagClicked(indexRight, 7);
            return;
        }
        if (biometric.getRightThree() == null) {
            flagClicked(middleRight, 8);
            return;
        }
        if (biometric.getRightFour() == null) {
            flagClicked(ringRight, 9);
            return;
        }
        if (biometric.getRightFive() == null) {
            flagClicked(pinkyRight, 10);
        }
    }

    String[] getCaptureFailure() {

        List<String> strings = new ArrayList<>();
        strings.add("Select Failure Reason");
        strings.add("Case of missing finger(s)");
        strings.add("Case of leprosy");
        if (isChild)
            strings.add("Case of infant");
        strings.add("Case of unreadable fingerprint(s)");

        return strings.toArray(new String[strings.size()]);

    }

    @OnClick({R.id.pinkyLeft, R.id.ringLeft, R.id.middleLeft, R.id.indexLeft, R.id.thumbLeft, R.id.thumbRight, R.id.indexRight, R.id.middleRight, R.id.ringRight, R.id.pinkyRight, R.id.imageViewRight, R.id.btnPhotoLeft, R.id.btnPhotoRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pinkyLeft:
                if (biometric != null)
                    biometric.setLeftFive(null);
                flagClicked(pinkyLeft, 5);
                removeEnrolledPrintFromArray(5);
                fingerSelec.setText("Left Pinky");
                break;
            case R.id.ringLeft:
                if (biometric != null)
                    biometric.setLeftFour(null);
                flagClicked(ringLeft, 4);
                removeEnrolledPrintFromArray(4);
                fingerSelec.setText("Left Ring");

                break;
            case R.id.middleLeft:
                if (biometric != null)
                    biometric.setLeftThree(null);
                flagClicked(middleLeft, 3);
                removeEnrolledPrintFromArray(3);
                fingerSelec.setText("Left Middle");

                break;
            case R.id.indexLeft:
                if (biometric != null)
                    biometric.setLeftTwo(null);
                flagClicked(indexLeft, 2);
                removeEnrolledPrintFromArray(2);
                fingerSelec.setText("Left Index");

                break;
            case R.id.thumbLeft:
                if (biometric != null)
                    biometric.setLeftOne(null);
                flagClicked(thumbLeft, 1);
                removeEnrolledPrintFromArray(1);
                fingerSelec.setText("Left Thumb");

                break;
            case R.id.thumbRight:
                if (biometric != null)
                    biometric.setRightOne(null);
                flagClicked(thumbRight, 6);
                removeEnrolledPrintFromArray(6);
                fingerSelec.setText("Right Thumb");

                break;
            case R.id.indexRight:
                if (biometric != null)
                    biometric.setRightTwo(null);
                flagClicked(indexRight, 7);
                removeEnrolledPrintFromArray(7);
                fingerSelec.setText("Right Index");

                break;
            case R.id.middleRight:
                if (biometric != null)
                    biometric.setRightThree(null);
                flagClicked(middleRight, 8);
                removeEnrolledPrintFromArray(8);
                fingerSelec.setText("Right Middle");

                break;
            case R.id.ringRight:
                if (biometric != null)
                    biometric.setRightFour(null);
                flagClicked(ringRight, 9);
                removeEnrolledPrintFromArray(9);
                fingerSelec.setText("Right Ring");

                break;
            case R.id.pinkyRight:
                if (biometric != null)
                    biometric.setRightFive(null);
                flagClicked(pinkyRight, 10);
                removeEnrolledPrintFromArray(10);
                fingerSelec.setText("Right Pinky");

                break;


            ////////////
            case R.id.btnPhotoLeft:
                startCamera();
                isRightFinger = false;
                break;

            case R.id.btnPhotoRight:
                startCamera();
                isRightFinger = true;
                break;
        }
    }

    private void removeEnrolledPrintFromArray(int index) {
        try {
            mRefEnrols.remove(index - 1);
        } catch (IndexOutOfBoundsException e) {
            // Error occured
        }
    }

    private void startCamera() {
        if (issues == null) {
            Util.showDialogueMessae(this, "Select reason for not capturing", "Alert");
            return;
        }

        Intent intent = new Intent(this, CameraKitActivity.class);
        intent.putExtra(CameraKitActivity.IS_FINGER, true);
        startActivityForResult(intent, 90);
    }


    private void flagClicked(ImageView imageView, int pos) {
        mCountPosition = pos;
        if (lastCount > 0)
            update();
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_selected));
        lastCount = mCountPosition;

        if (biometric == null)
            biometric = new Fingerprint();

        if (pos >= 6 && checkLeftMissing()) {
            leftSkiped = true;
        }
        startEnrollment();
        switch (imageView.getId()) {
            case R.id.pinkyLeft:

                fingerSelec.setText("Left Pinky");
                break;
            case R.id.ringLeft:

                fingerSelec.setText("Left Ring");

                break;
            case R.id.middleLeft:

                fingerSelec.setText("Left Middle");

                break;
            case R.id.indexLeft:

                fingerSelec.setText("Left Index");

                break;
            case R.id.thumbLeft:

                fingerSelec.setText("Left Thumb");

                break;
            case R.id.thumbRight:

                fingerSelec.setText("Right Thumb");

                break;
            case R.id.indexRight:

                fingerSelec.setText("Right Index");

                break;
            case R.id.middleRight:

                fingerSelec.setText("Right Middle");

                break;
            case R.id.ringRight:

                fingerSelec.setText("Right Ring");

                break;
            case R.id.pinkyRight:

                fingerSelec.setText("Right Pinky");

                break;
            default:
                fingerSelec.setText("No finger selected");
        }


    }

    private void update() {
        switch (lastCount) {
            case 1:
                thumbLeft.setImageBitmap(null);
                break;
            case 2:
                indexLeft.setImageBitmap(null);
                break;
            case 3:
                middleLeft.setImageBitmap(null);
                break;
            case 4:
                ringLeft.setImageBitmap(null);
                break;
            case 5:
                pinkyLeft.setImageBitmap(null);
                break;
            case 6:
                thumbRight.setImageBitmap(null);
                break;
            case 7:
                indexRight.setImageBitmap(null);
                break;
            case 8:
                middleRight.setImageBitmap(null);
                break;
            case 9:
                ringRight.setImageBitmap(null);
                break;
            case 10:
                pinkyRight.setImageBitmap(null);
                break;
        }

    }

    private void flagCaptured(ImageView imageView) {
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_captured));
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mPhoto = data.getStringExtra("photo");
            if (biometric == null)
                biometric = new Fingerprint();

            btnPhotoLeft.setText("CAPTURED");
            biometric.setFingersPhoto(mPhoto);
            photoLeft.setImageBitmap(Util.decodeBase64ToBitmap(mPhoto));

            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return Util.decodeBase64ToBitmap(mPhoto);
                }

                @SuppressLint("StaticFieldLeak")
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
//                    if (isRightFinger) {
//                        photoRight.setImageBitmap(bitmap);
//                    } else {
                    photoLeft.setImageBitmap(bitmap);
                    photoLeft.setVisibility(View.VISIBLE);
//                    }
                    mPhoto = null;
                }
            }.execute();
        }

    }

    private void popu() {
        for (int i = 0; i < 10; i++)
            mRefEnrols.add(new byte[0]);
    }



}
