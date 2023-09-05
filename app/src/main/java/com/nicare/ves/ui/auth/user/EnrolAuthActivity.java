package com.nicare.ves.ui.auth.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.fgtit.data.ConversionsEx;
import com.fgtit.device.Constants;
import com.fgtit.device.FPModule;
import com.fgtit.fpcore.FPMatch;
import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.EOAuthModel;
import com.nicare.ves.ui.enrol.vulnerable.EnrolVulnActivity;
import com.nicare.ves.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EnrolAuthActivity extends AppCompatActivity {

    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String PIN = "pin";
    private FPModule fpm = new FPModule();

    private byte bmpdata[] = new byte[Constants.RESBMP_SIZE];
    private int bmpsize = 0;

    private byte refdata[] = new byte[Constants.TEMPLATESIZE * 2];
    private int refsize = 0;

    private byte[] refdataOne = new byte[Constants.TEMPLATESIZE];
    private byte[] refdataTwo = new byte[Constants.TEMPLATESIZE];
    private byte[] refdataEnrol = new byte[Constants.TEMPLATESIZE * 2];
    private List<byte[]> mRefEnrols = new ArrayList<>();

    private byte matdata[] = new byte[Constants.TEMPLATESIZE * 2];
    private int matsize = 0;


    private StringBuilder refstring = new StringBuilder();
    private String matstring = "";

    private int worktype = 0;
    private boolean isLeft = true;
    private TextView tvFpError, tvFpStatu;
    private ImageView ivFpImage = null;
    private RadioGroup radioGroup;

    private EOAuthModel mEOAuthModel;

    EnrolAuthViewModel mEnrolAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrol_auth);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        mEnrolAuthViewModel = ViewModelProviders.of(this).get(EnrolAuthViewModel.class);
        initView();
//        tvDevStatu.setText(String.valueOf(fpm.getDeviceType()));

        mEOAuthModel = new EOAuthModel();
        mEOAuthModel.setQuestion(getIntent().getStringExtra(QUESTION));
        mEOAuthModel.setAnswer(getIntent().getStringExtra(ANSWER));
        mEOAuthModel.setPassword(getIntent().getStringExtra(PIN));

        int i = fpm.InitMatch();
        Log.d("MainActivity", "i:" + i);

        fpm.SetContextHandler(this, mHandler);
        fpm.SetTimeOut(Constants.TIMEOUT_LONG);
        fpm.SetLastCheckLift(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("CheckResult")
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
                    placeFinger();
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

                            placeFinger();

                            refsize = fpm.GetTemplateByGen(refdataOne);
                            if (fpm.GenerateTemplate(1)) {
                                worktype = 2;
                            } else {
                                Toast.makeText(EnrolAuthActivity.this, "Busy", Toast.LENGTH_SHORT).show();
                            }
                        } else if (worktype == 2) {
                            refsize = fpm.GetTemplateByGen(refdataTwo);

                            if (FPMatch.getInstance().MatchTemplate(refdataOne, refdataTwo) > 20) {
                                tvFpStatu.setText("Enrol template ok");
                            } else {
//                                worktype = 1;
                                tvFpError.setText("Finger not match pls re-enrol");
                                startEnrollment();
                                return;
                            }

                            refdataEnrol = byteMerger(refdataOne, refdataTwo);
                            for (byte[] ref : mRefEnrols) {
                                if (FPMatch.getInstance().MatchTemplate(ref, refdataEnrol) > 20) {
                                    tvFpError.setText("Finger already captured");
                                    startEnrollment();
                                    return;
                                }
                            }

                            mRefEnrols.add(refdataEnrol);

                            //if(fpm.getDeviceType()==Constants.DEV_7_3G_SPI){
                            refstring.append(ConversionsEx.getInstance().ToAnsiIso(refdataEnrol, ConversionsEx.ISO_19794_2011, ConversionsEx.COORD_MIRRORV));

                            if (isLeft) {
                                mEOAuthModel.setLeftThumb(refstring.toString());
                                isLeft = false;
                                startEnrollment();
                            } else {

                                mEOAuthModel.setRightThumb(refstring.toString());

                                // to be remove
//                                if (mEOAuthModel.getPassword() == null) {
//
////                                    mEOAuthModel.setPassword(Util.encryptThisString(PrefUtils.getInstance(EnrolAuthActivity.this).getPin()));
////                                    mEOAuthModel.setAnswer(PrefUtils.getInstance(EnrolAuthActivity.this).getSAnswer());
////                                    mEOAuthModel.setQuestion(PrefUtils.getInstance(EnrolAuthActivity.this).getSQuestion());
//
//                                    PrefUtils.getInstance(EnrolAuthActivity.this).setPin(null);
//                                    PrefUtils.getInstance(EnrolAuthActivity.this).setSAnswer(null);
//                                    PrefUtils.getInstance(EnrolAuthActivity.this).setSQuestion(null);
//
//                                }

                                mEnrolAuthViewModel.insert(mEOAuthModel)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(new MaybeObserver<Long>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Long aLong) {
                                                PrefUtils.getInstance(EnrolAuthActivity.this).setActivated(true);
                                                PrefUtils.getInstance(EnrolAuthActivity.this).setCap(true);
                                                PrefUtils.getInstance(EnrolAuthActivity.this).setLogin(true);
                                                startActivity(new Intent(EnrolAuthActivity.this, PasswordAuthActivity.class));
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
                        tvFpStatu.setText("Generate Template Fail");
                        startEnrollment();
                    }
                }
                break;
                case Constants.FPM_NEWIMAGE: {
                    bmpsize = fpm.GetBmpImage(bmpdata);
                    Bitmap bm1 = BitmapFactory.decodeByteArray(bmpdata, 0, bmpsize);
                    ivFpImage.setImageBitmap(bm1);
                }
                break;
                case Constants.FPM_TIMEOUT:
                    tvFpStatu.setText("Time Out");
                    break;
            }
        }
    };

    private void placeFinger() {
        if (isLeft) {
            tvFpStatu.setText("Place Left Index Finger");
        } else {
            tvFpStatu.setText("Place RIght Index Finger");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fpm.ResumeRegister();
        fpm.OpenDevice();

        //auth
//        if(fpm.GenerateTemplate(1)){
//            worktype=0;
//        }else{
//            Toast.makeText(EnrolAuthActivity.this, "Busy", Toast.LENGTH_SHORT).show();
//        }

        // enrol
//        if (fpm.GenerateTemplate(2)) {
//            worktype = 1;
//        } else {
//            Toast.makeText(EnrolAuthActivity.this, "Busy", Toast.LENGTH_SHORT).show();
//        }
        startEnrollment();
    }

    /*
	@Override
	protected void onPause() {
		super.onPause();
		fpm.PauseUnRegister();
		fpm.CloseDevice();
	}
	*/

    @Override
    protected void onStop() {
        super.onStop();
        fpm.PauseUnRegister();
        fpm.CloseDevice();
    }

    private void initView() {

//        tvDevStatu=(TextView)findViewById(R.id.textView1);
        tvFpStatu = (TextView) findViewById(R.id.tvInstruction);
        tvFpError = (TextView) findViewById(R.id.tvStatus);
//        tvFpData = (TextView) findViewById(R.id.textView3);
        ivFpImage = (ImageView) findViewById(R.id.ivFP);
//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

//        final Button btn_enrol=(Button)findViewById(R.id.button1);
//        final Button btn_capture=(Button)findViewById(R.id.button2);

//        btn_enrol.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(fpm.GenerateTemplate(2)){
//                    worktype=1;
//                }else{
//                    Toast.makeText(EnrolAuthActivity.this, "Busy", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        btn_capture.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(fpm.GenerateTemplate(1)){
//                    worktype=0;
//                }else{
//                    Toast.makeText(EnrolAuthActivity.this, "Busy", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        //checked radio1
//        radioGroup.check(R.id.radio1);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void startEnrollment() {
        if (fpm.GenerateTemplate(1)) {
            worktype = 1;
        } else {
            Toast.makeText(EnrolAuthActivity.this, "Busy", Toast.LENGTH_SHORT).show();
        }
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
