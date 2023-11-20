package com.nicare.ves.ui.enrol.fingerprint;/*
 * Copyright (C) 2016 SecuGen Corporation
 *
 */
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;
import com.nicare.ves.ui.enrol.camera.CameraKitActivity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxConstant;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGFingerPresentEvent;
import SecuGen.FDxSDKPro.SGImpressionType;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecuGenActivity extends AppCompatActivity
        implements SGFingerPresentEvent {

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

    @BindView(R.id.photoLeft)
    ImageView photoLeft;
    @BindView(R.id.photoRight)
    ImageView photoRight;
    @BindView(R.id.finger)
    TextView tvFingerSelec;
    @BindView(R.id.tvInstruction)
    TextView tvFpStatu;
    @BindView(R.id.tvError)
    TextView tvFpError;
    @BindView(R.id.imageViewFP)
    ImageView ivFpImage = null;
    @BindView(R.id.btnReset)
    MaterialButton btnReset;
    @BindView(R.id.btnSave)
    MaterialButton btnSave;
    @BindView(R.id.spReason)
    Spinner spinner;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    boolean isRightFinger;
    Fingerprint biometric = null;

    private List<byte[]> mRefEnrols = new ArrayList<>();

    private static final String TAG = "SecuGen USB";
    private static final int IMAGE_CAPTURE_TIMEOUT_MS = 10000;
    private static final int IMAGE_CAPTURE_QUALITY = 50;

    private PendingIntent mPermissionIntent;

    private byte[] mRegisterImage;
    private byte[] mVerifyImage;
    private byte[] mRegisterTemplate;
    private byte[] mVerifyTemplate;
    private int[] mMaxTemplateSize;
    private int mImageWidth;
    private int mImageHeight;
    private int mImageDPI;
    private int[] grayBuffer;
    private Bitmap grayBitmap;
    private IntentFilter filter; //2014-04-11
    private SGAutoOnEventNotifier autoOn;
    private boolean mAutoOnEnabled;
    private int nCaptureModeN;
    //    private Button mButtonReadSN;
    private boolean bSecuGenDeviceOpened;
    private JSGFPLib sgfplib;
    private boolean usbPermissionRequested;

    private int[] mNumFakeThresholds;
    private int[] mDefaultFakeThreshold;
    private boolean[] mFakeEngineReady;
    private boolean bRegisterAutoOnMode;
    private boolean bVerifyAutoOnMode;
    private boolean bFingerprintRegistered;
    private int mFakeDetectionLevel = 1;


    private int mCountPosition;
    private int lastCount;

    private int capturedThreshold = 2;
    private int leftCount;
    private int rightCount;

    private boolean leftSkiped;
    private boolean rightSkiped;
    private String mPhoto;

    private boolean isChild;
    private String issues;

    private String fingerprintTemplateString;

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    private void debugMessage(String message) {
        Log.v("SECUGEN", message);
//        this.mEditLog.append(message);
//        this.mEditLog.invalidate(); //TODO trying to get Edit log to update after each line written
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //This broadcast receiver is necessary to get user permissions to access the attached USB device
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            debugMessage("USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
                            debugMessage("USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        } else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //This message handler is used to access local resources not
    //accessible by SGFingerPresentCallback() because it is called by
    //a separate thread.

    public Handler fingerDetectedHandler = new Handler() {
        // @Override
        public void handleMessage(Message msg) {
            //Handle the message
            if (bRegisterAutoOnMode) {
                bRegisterAutoOnMode = false;
                RegisterFingerPrint();
            } else if (bVerifyAutoOnMode) {
                bVerifyAutoOnMode = false;
//                VerifyFingerPrint();
            } else
//                CaptureFingerPrint();
                if (mAutoOnEnabled) {
//                EnableControls();
                }
        }
    };


    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
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
//                        btnPhotoRight.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "Enter onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_secu);
        ButterKnife.bind(this);
        initView();
        popu();

        mNumFakeThresholds = new int[1];
        mDefaultFakeThreshold = new int[1];
        mFakeEngineReady = new boolean[1];

        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES * JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i = 0; i < grayBuffer.length; ++i)
            grayBuffer[i] = Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
//        mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2) * (JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2)];
        for (int i = 0; i < sintbuffer.length; ++i)
            sintbuffer[i] = Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2);
//        mImageViewRegister.setImageBitmap(grayBitmap);
//        mImageViewVerify.setImageBitmap(grayBitmap);
        mMaxTemplateSize = new int[1];


        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        sgfplib = new JSGFPLib(this, (UsbManager) getSystemService(Context.USB_SERVICE));
//        this.mSwitchSmartCapture.setChecked(true);
//		DisableBrightnessControls();
//		this.mSwitchAutoOn.setChecked(false);
//        this.mSwitchNFIQ.setChecked(false);
        bSecuGenDeviceOpened = false;
        usbPermissionRequested = false;

        debugMessage("Starting Activity\n");
        debugMessage("JSGFPLib version: " + sgfplib.GetJSGFPLibVersion() + "\n");
        mAutoOnEnabled = false;
        autoOn = new SGAutoOnEventNotifier(sgfplib, this);
        nCaptureModeN = 0;
        Log.d(TAG, "Exit onCreate()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    @OnClick(R.id.btnSave)
    void enrol() {
        saveFingerprint();
    }


    @Override
    public void onPause() {
        Log.d(TAG, "Enter onPause()");
        debugMessage("Enter onPause()\n");
        if (bSecuGenDeviceOpened) {
            autoOn.stop();
//            EnableControls();
            sgfplib.CloseDevice();
            bSecuGenDeviceOpened = false;
        }
        unregisterReceiver(mUsbReceiver);
        mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
//        mImageViewFingerprint.setImageBitmap(grayBitmap);
//        mImageViewRegister.setImageBitmap(grayBitmap);
//        mImageViewVerify.setImageBitmap(grayBitmap);
        super.onPause();
        Log.d(TAG, "Exit onPause()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {
        Log.d(TAG, "Enter onResume()");
        debugMessage("Enter onResume()\n");
        super.onResume();
//        DisableControls();
        registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
            else
                dlgAlert.setMessage("Fingerprint device initialization failed!");
            dlgAlert.setTitle("SecuGen Fingerprint SDK");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                            return;
                        }
                    }
            );
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        } else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("SecuGen fingerprint sensor not found!");
                dlgAlert.setTitle("SecuGen Fingerprint SDK");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                                return;
                            }
                        }
                );
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } else {
                boolean hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                if (!hasPermission) {
                    if (!usbPermissionRequested) {
                        debugMessage("Requesting USB Permission\n");
                        //Log.d(TAG, "Call GetUsbManager().requestPermission()");
                        usbPermissionRequested = true;
                        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                    } else {
                        //wait up to 20 seconds for the system to grant USB permission
                        hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                        debugMessage("Waiting for USB Permission\n");
                        int i = 0;
                        while ((hasPermission == false) && (i <= 40)) {
                            ++i;
                            hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //Log.d(TAG, "Waited " + i*50 + " milliseconds for USB permission");
                        }
                    }
                }
                if (hasPermission) {
                    debugMessage("Opening SecuGen Device\n");
                    error = sgfplib.OpenDevice(0);
                    debugMessage("OpenDevice() ret: " + error + "\n");
                    if (error == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        bSecuGenDeviceOpened = true;
                        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
                        error = sgfplib.GetDeviceInfo(deviceInfo);
                        debugMessage("GetDeviceInfo() ret: " + error + "\n");
                        mImageWidth = deviceInfo.imageWidth;
                        mImageHeight = deviceInfo.imageHeight;
                        mImageDPI = deviceInfo.imageDPI;
                        debugMessage("Image width: " + mImageWidth + "\n");
                        debugMessage("Image height: " + mImageHeight + "\n");
                        debugMessage("Image resolution: " + mImageDPI + "\n");
                        debugMessage("Serial Number: " + new String(deviceInfo.deviceSN()) + "\n");

                        error = sgfplib.FakeDetectionCheckEngineStatus(mFakeEngineReady);
                        debugMessage("Ret[" + error + "] Fake Engine Ready: " + mFakeEngineReady[0] + "\n");
                        if (mFakeEngineReady[0]) {
                            error = sgfplib.FakeDetectionGetNumberOfThresholds(mNumFakeThresholds);
                            debugMessage("Ret[" + error + "] Fake Thresholds: " + mNumFakeThresholds[0] + "\n");
                            if (error != SGFDxErrorCode.SGFDX_ERROR_NONE)
                                mNumFakeThresholds[0] = 1; //0=Off, 1=TouchChip
//                            this.mSeekBarFDLevel.setMax(mNumFakeThresholds[0]);

                            error = sgfplib.FakeDetectionGetDefaultThreshold(mDefaultFakeThreshold);
                            debugMessage("Ret[" + error + "] Default Fake Threshold: " + mDefaultFakeThreshold[0] + "\n");
//                            this.mTextViewFDLevel.setText("Fake Threshold (" + mDefaultFakeThreshold[0] + "/" + mNumFakeThresholds[0] + ")");
                            mFakeDetectionLevel = mDefaultFakeThreshold[0];

                            //error = this.sgfplib.SetFakeDetectionLevel(mFakeDetectionLevel);
                            //debugMessage("Ret[" + error + "] Set Fake Threshold: " + mFakeDetectionLevel + "\n");

                            double[] thresholdValue = new double[1];
                            error = sgfplib.FakeDetectionGetThresholdValue(thresholdValue);
                            debugMessage("Ret[" + error + "] Fake Threshold Value: " + thresholdValue[0] + "\n");
                        } else {
                            mNumFakeThresholds[0] = 1;        //0=Off, 1=Touch Chip
                            mDefaultFakeThreshold[0] = 1;    //Touch Chip Enabled
//                            this.mTextViewFDLevel.setText("Fake Threshold (" + mDefaultFakeThreshold[0] + "/" + mNumFakeThresholds[0] + ")");
                        }

                        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
                        sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                        debugMessage("TEMPLATE_FORMAT_ISO19794 SIZE: " + mMaxTemplateSize[0] + "\n");
                        mRegisterTemplate = new byte[(int) mMaxTemplateSize[0]];
                        mVerifyTemplate = new byte[(int) mMaxTemplateSize[0]];
//                        EnableControls();
//                        boolean smartCaptureEnabled = this.mSwitchSmartCapture.isChecked();
                        boolean smartCaptureEnabled = false;
                        if (smartCaptureEnabled)
                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 1);
                        else
                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 0);
                        if (mAutoOnEnabled) {
//                            autoOn.start();
//                            DisableControls();
                        }
                    } else {
                        debugMessage("Waiting for USB Permission\n");
                    }
                }
                //Thread thread = new Thread(this);
                //thread.start();
            }
        }
        Log.d(TAG, "Exit onResume()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        Log.d(TAG, "Enter onDestroy()");
        sgfplib.CloseDevice();
        mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
        sgfplib.Close();
        super.onDestroy();
        Log.d(TAG, "Exit onDestroy()");
    }

//    //////////////////////////////////////////////////////////////////////////////////////////////
//    //Converts image to grayscale (NEW)
//    public Bitmap toGrayscale(byte[] mImageBuffer, int width, int height) {
//        byte[] Bits = new byte[mImageBuffer.length * 4];
//        for (int i = 0; i < mImageBuffer.length; i++) {
//            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
//            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
//        }
//
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
//        return bmpGrayscale;
//    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(byte[] mImageBuffer) {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////
//    //Converts image to grayscale (NEW)
//    public Bitmap toGrayscale(Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        for (int y = 0; y < height; ++y) {
//            for (int x = 0; x < width; ++x) {
//                int color = bmpOriginal.getPixel(x, y);
//                int r = (color >> 16) & 0xFF;
//                int g = (color >> 8) & 0xFF;
//                int b = color & 0xFF;
//                int gray = (r + g + b) / 3;
//                color = Color.rgb(gray, gray, gray);
//                //color = Color.rgb(r/3, g/3, b/3);
//                bmpGrayscale.setPixel(x, y, color);
//            }
//        }
//        return bmpGrayscale;
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////////////////////////////////////////////////
//    //Converts image to binary (OLD)
//    public Bitmap toBinary(Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        Canvas c = new Canvas(bmpGrayscale);
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//        paint.setColorFilter(f);
//        c.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void DumpFile(String fileName, byte[] buffer) {
        //Uncomment section below to dump images and templates to SD card
    	/*
        try {
            File myFile = new File("/sdcard/Download/" + fileName);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            fOut.write(buffer,0,buffer.length);
            fOut.close();
        } catch (Exception e) {
            debugMessage("Exception when writing file" + fileName);
        }
       */
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void SGFingerPresentCallback() {
        autoOn.stop();
        fingerDetectedHandler.sendMessage(new Message());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void RegisterFingerPrint() {
        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
        if (mRegisterImage != null)
            mRegisterImage = null;
        mRegisterImage = new byte[mImageWidth * mImageHeight];
        bFingerprintRegistered = false;
//        this.mCheckBoxMatched.setChecked(false);
        dwTimeStart = System.currentTimeMillis();
        long result = sgfplib.GetImageEx(mRegisterImage, IMAGE_CAPTURE_TIMEOUT_MS, IMAGE_CAPTURE_QUALITY);
        DumpFile("register.raw", mRegisterImage);
        mVerifyImage = mRegisterImage;
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("GetImageEx() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
        ivFpImage.setImageBitmap(this.toGrayscale(mRegisterImage));
        dwTimeStart = System.currentTimeMillis();
        result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("SetTemplateFormat(ISO19794) ret:" + result + " [" + dwTimeElapsed + "ms]\n");

        int quality[] = new int[1];
        result = sgfplib.GetImageQuality(mImageWidth, mImageHeight, mRegisterImage, quality);
        debugMessage("GetImageQuality() ret:" + result + "quality [" + quality[0] + "]\n");

        SGFingerInfo fpInfo = new SGFingerInfo();
        fpInfo.FingerNumber = 1;
        fpInfo.ImageQuality = quality[0];
        fpInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
        fpInfo.ViewNumber = 1;

        for (int i = 0; i < mRegisterTemplate.length; ++i)
            mRegisterTemplate[i] = 0;
        dwTimeStart = System.currentTimeMillis();
        result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);
        DumpFile("register.min", mRegisterTemplate);
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
        ivFpImage.setImageBitmap(this.toGrayscale(mRegisterImage));
        if (result == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            if (quality[0] >= 50) {

                int[] size = new int[1];
                result = sgfplib.GetTemplateSize(mRegisterTemplate, size);
//            debugMessage("GetTemplateSize() ret:" + result + " size [" + size[0] + "]\n");

//                boolean[] matched = new boolean[1];
//                int s = mRefEnrols.size();
                boolean duplicate = isDuplicate(mRefEnrols, mRegisterTemplate);
                if (!duplicate) {
                    mRefEnrols.add(mCountPosition - 1, mRegisterTemplate);
                    fingerprintTemplateString = Base64.encodeToString(mRegisterTemplate, Base64.DEFAULT);
//            Log.v("SGFP " + (mCountPosition - 1), fingerprintTemplateString.toString());
                    countPrint(mCountPosition);
                    tvFpStatu.setText("Captured");
                    bFingerprintRegistered = true;
                }

            } else {
                tvFpStatu.setText("");
                tvFpError.setText("Poor fingerprint image");

            }
        } else {
            tvFpStatu.setText("");
            tvFpError.setText("Fingerprint not registered");
        }

        setControlEnableState(true);

        mRegisterImage = null;
        fpInfo = null;

        mRegisterTemplate = new byte[(int) mMaxTemplateSize[0]];

    }

    private boolean isDuplicate(List<byte[]> refEnrols, byte[] registerTemplate) {
//    private boolean isDuplicate(List<byte[]> refEnrols, byte[] registerTemplate) {
        boolean[] matched = new boolean[1];
        boolean isMatched;
//      Log.e("TTTB",Base64.encodeToString(mRegisterTemplate, Base64.DEFAULT));
        for (byte[] ref : refEnrols) {
//            int score = MatchIsoTemplateByte(registerTemplate, ref);
//            Log.e("TTTB", Base64.encodeToString(ref, Base64.DEFAULT));
//                 if (sc>9)
//            boolean isMatched = VerifyFingerPrint(mRegisterTemplate, ref);
            long result = sgfplib.MatchTemplate(mRegisterTemplate, ref, SGFDxSecurityLevel.SL_NORMAL, matched);
            Log.v("TTB", "SCORE Result" + result);
            Log.v("TTB", "Ismatched Result" + result);

            isMatched = matched[0];
            if (isMatched) {
//            if (score >= 20) {
                tvFpStatu.setText("");
                tvFpError.setText("Finger already captured");
//                setControlEnableState(true);
                matched = null;
                return true;
            }
        }
        matched = null;
        tvFpError.setText("");
        return false;


    }

    public boolean VerifyFingerPrint(byte[] registerTemplate, byte[] verifyTemplate) {
//        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
////        this.mCheckBoxMatched.setChecked(false);
////        if (!bFingerprintRegistered) {
////            mTextViewResult.setText("Please Register a finger");
////            sgfplib.SetLedOn(false);
////            return;
////        }
//        if (mVerifyImage != null)
//            mVerifyImage = null;
//        mVerifyImage = new byte[mImageWidth*mImageHeight];
//        dwTimeStart = System.currentTimeMillis();
        long result = sgfplib.GetImageEx(mVerifyImage, IMAGE_CAPTURE_TIMEOUT_MS, IMAGE_CAPTURE_QUALITY);
//        DumpFile("verify.raw", mVerifyImage);
//        dwTimeEnd = System.currentTimeMillis();
//        dwTimeElapsed = dwTimeEnd-dwTimeStart;
//        debugMessage("GetImageEx() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
//        mImageViewFingerprint.setImageBitmap(this.toGrayscale(mVerifyImage));
//        mImageViewVerify.setImageBitmap(this.toGrayscale(mVerifyImage));
//        dwTimeStart = System.currentTimeMillis();
        result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
//        dwTimeEnd = System.currentTimeMillis();
//        dwTimeElapsed = dwTimeEnd-dwTimeStart;
//        debugMessage("SetTemplateFormat(ISO19794) ret:" +  result + " [" + dwTimeElapsed + "ms]\n");

        int quality[] = new int[1];
        result = sgfplib.GetImageQuality(mImageWidth, mImageHeight, mVerifyImage, quality);
        debugMessage("GetImageQuality() ret:" + result + "quality [" + quality[0] + "]\n");

        SGFingerInfo fpInfo = new SGFingerInfo();
        fpInfo.FingerNumber = 1;
        fpInfo.ImageQuality = quality[0];
        fpInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
        fpInfo.ViewNumber = 1;

        for (int i = 0; i < mVerifyTemplate.length; ++i)
            mVerifyTemplate[i] = 0;
//        dwTimeStart = System.currentTimeMillis();
//        result = sgfplib.CreateTemplate(fpInfo, verifyTemplate, mVerifyTemplate);
//        DumpFile("verify.min", mVerifyTemplate);
//        dwTimeEnd = System.currentTimeMillis();
//        dwTimeElapsed = dwTimeEnd-dwTimeStart;
//        debugMessage("CreateTemplate() ret:" + result+ " [" + dwTimeElapsed + "ms]\n");
        if (result == SGFDxErrorCode.SGFDX_ERROR_NONE) {

            int[] size = new int[1];
            result = sgfplib.GetTemplateSize(mVerifyTemplate, size);
//            debugMessage("GetTemplateSize() ret:" + result + " size [" + size[0] + "]\n");

            boolean[] matched = new boolean[1];
//            dwTimeStart = System.currentTimeMillis();
//
            result = sgfplib.MatchTemplate(registerTemplate, verifyTemplate, SGFDxSecurityLevel.SL_NORMAL, matched);
//            dwTimeEnd = System.currentTimeMillis();
//            dwTimeElapsed = dwTimeEnd - dwTimeStart;
//            debugMessage("MatchTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
            return matched[0];
//            if (matched[0]) {
////                mTextViewResult.setText("Fingerprint matched!\n");
////                this.mCheckBoxMatched.setChecked(true);
//                debugMessage("MATCHED!!\n");
//                return true
//            } else {
////                mTextViewResult.setText("Fingerprint not matched!");
//                debugMessage("NOT MATCHED!!\n");
//            }
//            matched = null;
        } else {
            return false;
        }
//            mTextViewResult.setText("Fingerprint template extraction failed.");
//        mVerifyImage = null;
//        fpInfo = null;
    }

    private void startEnrollment() {

        if (mCountPosition <= 5) {
            if (leftCount == capturedThreshold) {
                Util.showToast("Left fingers capture completed", true, SecuGenActivity.this, LayoutInflater.from(this));
                setControlEnableState(true);
                return;
            }
        } else {
            if (rightCount == capturedThreshold) {
                Util.showToast("Right fingers capture completed", true, SecuGenActivity.this, LayoutInflater.from(this));
                setControlEnableState(true);
                return;
            }
        }
        sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 1); //Enable Smart Capture

        // Auto Scan
//        debugMessage("Clicked REGISTER WITH AUTO ON\n");
//        bRegisterAutoOnMode = true;
//        mAutoOnEnabled = true;
//        autoOn.start(); //Enable Auto On

        // MANUAL SCAN
        setControlEnableState(true);
        RegisterFingerPrint();

    }


    @OnClick({R.id.pinkyLeft, R.id.ringLeft, R.id.middleLeft, R.id.indexLeft, R.id.thumbLeft, R.id.thumbRight, R.id.indexRight, R.id.middleRight, R.id.ringRight, R.id.pinkyRight,})
    public void onViewClicked(View view) {
        setControlEnableState(false);
        switch (view.getId()) {
            case R.id.pinkyLeft:
                if (biometric != null) {
                    if (biometric.getLeftFive() != null) {
                        if (leftCount > 0) leftCount -= 1;
                    }
                    biometric.setLeftFive(null);
                }
                flagClicked(pinkyLeft, 5);
                tvFingerSelec.setText("Left Pinky");
                break;
            case R.id.ringLeft:
                if (biometric != null) {
                    if (biometric.getLeftFour() != null) {
                        if (leftCount > 0) leftCount -= 1;
                    }
                    biometric.setLeftFour(null);
                }
                flagClicked(ringLeft, 4);
                tvFingerSelec.setText("Left Ring");

                break;
            case R.id.middleLeft:

                if (biometric != null) {
                    if (biometric.getLeftThree() != null) {
                        if (leftCount > 0) leftCount -= 1;
                    }
                    biometric.setLeftThree(null);
                }
                flagClicked(middleLeft, 3);
                tvFingerSelec.setText("Left Middle");

                break;
            case R.id.indexLeft:
                if (biometric != null) {
                    if (biometric.getLeftTwo() != null) {
                        if (leftCount > 0) leftCount -= 1;
                    }
                    biometric.setLeftTwo(null);
                }
                flagClicked(indexLeft, 2);
                tvFingerSelec.setText("Left Index");

                break;
            case R.id.thumbLeft:

                if (biometric != null) {
                    if (biometric.getLeftOne() != null) {
                        if (leftCount > 0) leftCount -= 1;
                    }
                    biometric.setLeftOne(null);
                }
                flagClicked(thumbLeft, 1);
                tvFingerSelec.setText("Left Thumb");

                break;
            case R.id.thumbRight:

                if (biometric != null) {
                    if (biometric.getRightOne() != null) {
                        if (rightCount > 0) rightCount -= 1;
                    }
                    biometric.setRightOne(null);
                }
                flagClicked(thumbRight, 6);
                tvFingerSelec.setText("Right Thumb");

                break;
            case R.id.indexRight:

                if (biometric != null) {
                    if (biometric.getRightTwo() != null) {
                        if (rightCount > 0) rightCount -= 1;
                    }
                    biometric.setRightTwo(null);

                }
                flagClicked(indexRight, 7);
                tvFingerSelec.setText("Right Index");

                break;
            case R.id.middleRight:

                if (biometric != null) {
                    if (biometric.getRightThree() != null) {
                        if (rightCount > 0) rightCount -= 1;
                    }
                    biometric.setRightThree(null);
                }

                flagClicked(middleRight, 8);
                tvFingerSelec.setText("Right Middle");

                break;
            case R.id.ringRight:
                if (biometric != null) {
                    if (biometric.getRightFour() != null) {
                        if (rightCount > 0) rightCount -= 1;
                    }
                    biometric.setRightFour(null);
                }
                flagClicked(ringRight, 9);
                tvFingerSelec.setText("Right Ring");

                break;
            case R.id.pinkyRight:

                if (biometric != null) {
                    if (biometric.getRightFive() != null) {
                        if (rightCount > 0) rightCount -= 1;
                    }
                    biometric.setRightFive(null);
                }
                flagClicked(pinkyRight, 10);
                tvFingerSelec.setText("Right Pinky");

                break;

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

    private boolean checkLeftMissing() {
//        if (biometric.getLeftFive() == null) return true;
//        if (biometric.getLeftFour() == null) return true;
//        if (biometric.getLeftThree() == null) return true;
//        if (biometric.getLeftTwo() == null) return true;
//        return biometric.getLeftOne() == null;
        return leftCount < capturedThreshold;
    }

    private boolean checkRightMissing() {

//        if (biometric.getRightFive() == null) return true;
//        if (biometric.getRightFour() == null) return true;
//        if (biometric.getRightThree() == null) return true;
//        if (biometric.getRightTwo() == null) return true;
//        return biometric.getRightOne() == null;
        return rightCount < capturedThreshold;

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
        switch (imageView.getId()) {
            case R.id.pinkyLeft:

                tvFingerSelec.setText("Left Pinky");
                break;
            case R.id.ringLeft:

                tvFingerSelec.setText("Left Ring");

                break;
            case R.id.middleLeft:

                tvFingerSelec.setText("Left Middle");

                break;
            case R.id.indexLeft:

                tvFingerSelec.setText("Left Index");

                break;
            case R.id.thumbLeft:

                tvFingerSelec.setText("Left Thumb");

                break;
            case R.id.thumbRight:

                tvFingerSelec.setText("Right Thumb");

                break;
            case R.id.indexRight:

                tvFingerSelec.setText("Right Index");

                break;
            case R.id.middleRight:

                tvFingerSelec.setText("Right Middle");

                break;
            case R.id.ringRight:

                tvFingerSelec.setText("Right Ring");

                break;
            case R.id.pinkyRight:

                tvFingerSelec.setText("Right Pinky");

                break;
            default:
                tvFingerSelec.setText("No finger selected");
        }
        startEnrollment();


    }

    private void countPrint(int count) {
        lastCount = 0;
//      mRefEnrols.add(count-1, refdataEnrol);
        switch (count) {
            case 1:

                flagCaptured(thumbLeft);
                biometric.setLeftOne(fingerprintTemplateString.toString());
                leftCount += 1;
//                startEnrollment();
                //checkBox.setEnabled(false);
//                flagClicked(indexLeft, count + 1);
//                nextLeftFinger();
                break;

            case 2:
                flagCaptured(indexLeft);
                leftCount += 1;

                biometric.setLeftTwo(fingerprintTemplateString.toString());
//                flagClicked(middleLeft, count + 1);
//                startEnrollment();
                break;
            case 3:
                flagCaptured(middleLeft);
                leftCount += 1;
                biometric.setLeftThree(fingerprintTemplateString.toString());
//                flagClicked(ringLeft, count + 1);
//                startEnrollment();
//                nextLeftFinger();
                break;

            case 4:
                flagCaptured(ringLeft);
                leftCount += 1;

                biometric.setLeftFour(fingerprintTemplateString.toString());
//                mRefEnrols.add(4, refdataEnrol);
//                flagClicked(pinkyLeft, count + 1);
//                startEnrollment();
//                nextLeftFinger();
                break;

            case 5:

                if (null == biometric) {
                    biometric = new Fingerprint();
                }
                flagCaptured(pinkyLeft);
                biometric.setLeftFive(fingerprintTemplateString.toString());
                leftCount += 1;

                if (checkLeftMissing() && !leftSkiped) {
//                    nextLeftFinger();
                } else if (checkRightMissing()) {
//                    nextRightFinger();
                } else {
                    tvFpStatu.setText(R.string.capture_completed);
//                    removePhotos();
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
                biometric.setRightOne(fingerprintTemplateString.toString());
                rightCount += 1;
//                startEnrollment();
                //checkBox.setEnabled(false);
//                flagClicked(indexRight, count + 1);
//                nextRightFinger();
                break;

            case 7:
                flagCaptured(indexRight);
                // ivFpRight.setImageResource(getIConID("righttwo"));
                biometric.setRightTwo(fingerprintTemplateString.toString());
                rightCount += 1;

//                startEnrollment();
//                flagClicked(middleRight, count + 1);
//                nextRightFinger();
                break;

            case 8:
                flagCaptured(middleRight);
                //ivFpRight.setImageResource(getIConID("rightthree"));
                biometric.setRightThree(fingerprintTemplateString.toString());
                rightCount += 1;

//                flagClicked(ringRight, count + 1);
//                nextRightFinger();
//                startEnrollment();
                break;

            case 9:
                flagCaptured(ringRight);
                // ivFpRight.setImageResource(getIConID("rightfour"));
                biometric.setRightFour(fingerprintTemplateString.toString());
                rightCount += 1;

//                startEnrollment();
//                flagClicked(pinkyRight, count + 1);
//                nextRightFinger();
                break;
            case 10:
                flagCaptured(pinkyRight);
                //ivFpRight.setImageResource(getIConID("rightfive"));
                biometric.setRightFive(fingerprintTemplateString.toString());
                rightCount += 1;

                if (checkRightMissing()) {
//                    nextRightFinger();
                } else if (checkLeftMissing()) {
//                    nextLeftFinger();
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

    private void clearTemplateString() {
        fingerprintTemplateString = null;
    }


    private void flagCaptured(ImageView imageView) {
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_captured));
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

    @OnClick(R.id.btnPhotoLeft)
    void photo() {
        startCamera();
        isRightFinger = false;
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

    @OnClick(R.id.btnReset)
    void reset() {
        tvFpError.setText("");
        tvFpStatu.setText("");
        leftCount = 0;
        rightCount = 0;
        lastCount = 0;

        mCountPosition = 0;
        biometric = null;
//        fingerCountLeft = 0;
//        fingerCountRight = 0;
        ivFpImage.setImageResource(0);
        photoLeft.setImageResource(0);
        checkBox.setEnabled(true);
        mRefEnrols.clear();
        popu();
//        refstring.setLength(0);

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

    private void setControlEnableState(boolean isEnable) {
        thumbLeft.setClickable(isEnable);
        indexLeft.setClickable(isEnable);
        middleLeft.setClickable(isEnable);
        ringLeft.setClickable(isEnable);
        pinkyLeft.setClickable(isEnable);

        thumbRight.setClickable(isEnable);
        indexRight.setClickable(isEnable);
        middleRight.setClickable(isEnable);
        ringRight.setClickable(isEnable);
        pinkyRight.setClickable(isEnable);
    }

    private void popu() {
        for (int i = 0; i < 10; i++)
            mRefEnrols.add(new byte[0]);
    }

    private void saveFingerprint() {

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


    //    public void run() {
//
//        while (true) {
//        }
//    }
//    public int MatchIsoTemplateStr(String strFeatureA, String strFeatureB) {
//        byte[] piFeatureA = Base64.decode(strFeatureA, Base64.DEFAULT);
//        byte[] piFeatureB = Base64.decode(strFeatureB, Base64.DEFAULT);
//
//        return MatchIsoTemplateByte(piFeatureA, piFeatureB);
//
//    }
//
//    public int MatchIsoTemplateByte(byte[] piFeatureA, byte[] piFeatureB) {
//        byte[] adat = new byte[256];
//        byte[] bdat = new byte[256];
//        int sc = 0;
////        switch (radioGroup.getCheckedRadioButtonId()) {
////            case R.id.radio1:
////                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ANSI_378_2004);
////                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ANSI_378_2004);
////                return FPMatch.getInstance().MatchTemplate(adat, bdat);
////            case R.id.radio2:
//        ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ANSI_378_2004);
//        ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ANSI_378_2004);
//        return FPMatch.getInstance().MatchTemplate(adat, bdat);
////            case R.id.radio3:
////                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ISO_19794_2009);
////                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ISO_19794_2009);
////                return FPMatch.getInstance().MatchTemplate(adat, bdat);
////            case R.id.radio4:
////                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureA, adat, ConversionsEx.ISO_19794_2011);
////                ConversionsEx.getInstance().AnsiIsoToStd(piFeatureB, bdat, ConversionsEx.ISO_19794_2011);
////                return FPMatch.getInstance().MatchTemplate(adat, bdat);
////        }
//    }

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

}