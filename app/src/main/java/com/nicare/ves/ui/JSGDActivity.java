/*
 * Copyright (C) 2016 SecuGen Corporation
 *
 */

package com.nicare.ves.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.nicare.ves.R;

import java.nio.ByteBuffer;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxConstant;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGFingerPresentEvent;
import SecuGen.FDxSDKPro.SGImpressionType;

public class JSGDActivity extends Activity
        implements View.OnClickListener, Runnable, SGFingerPresentEvent {

    private static final String TAG = "SecuGen USB";
    private static final int IMAGE_CAPTURE_TIMEOUT_MS = 10000;
    private static final int IMAGE_CAPTURE_QUALITY = 50;

    private Button mButtonCapture;
    private Button mButtonCaptureAutoOn;
    private Button mButtonRegister;
//    private Button mButtonRegisterAutoOn;
//    private Button mButtonMatch;
//    private Button mButtonMatchAutoOn;
//    private Button mSDKTest;
    private EditText mEditLog;
    private TextView mTextViewResult;
    private android.widget.CheckBox mCheckBoxMatched;
    //    private android.widget.ToggleButton mToggleButtonCaptureModeN;
    private PendingIntent mPermissionIntent;
//    private ImageView mImageViewFingerprint;
    private ImageView mImageViewRegister;
//    private ImageView mImageViewVerify;
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
    private Button mButtonReadSN;
    private boolean bSecuGenDeviceOpened;
    private JSGFPLib sgfplib;
    private boolean usbPermissionRequested;
    //    private Switch mSwitchAutoOn;
    private Switch mSwitchNFIQ;
//    private Switch mSwitchSmartCapture;

//    private Switch mSwitchModeN;
//    private Switch mSwitchLED;
//    private SeekBar mSeekBarFDLevel;
//    private TextView mTextViewFDLevel;
    private int[] mNumFakeThresholds;
    private int[] mDefaultFakeThreshold;
    private boolean[] mFakeEngineReady;
    private boolean bRegisterAutoOnMode;
    private boolean bVerifyAutoOnMode;
    private boolean bFingerprintRegistered;
    private int mFakeDetectionLevel = 1;


    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    private void debugMessage(String message) {
//        this.mEditLog.append(message);
//        this.mEditLog.invalidate(); //TODO trying to get Edit log to update after each line written
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
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
    public Handler fingerDetectedHandler = new Handler() {
        // @Override
        @SuppressLint("SuspiciousIndentation")
        public void handleMessage(Message msg) {
            //Handle the message
//			if (bRegisterAutoOnMode) {
//				bRegisterAutoOnMode = false;
//				RegisterFingerPrint();
//			}
//			else if (bVerifyAutoOnMode) {
//				bVerifyAutoOnMode = false;
////				VerifyFingerPrint();
//			}
//			else
//				CaptureFingerPrint();
//	    	if (mAutoOnEnabled) {
//		    	EnableControls();
//	    	}
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void EnableControls() {

        this.mButtonRegister.setClickable(true);
        this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.black));

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void DisableControls() {

        this.mButtonRegister.setClickable(false);
        this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.white));

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Enter onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        mButtonRegister = (Button) findViewById(R.id.buttonRegister);
        mButtonRegister.setOnClickListener(this);
        mImageViewRegister = (ImageView) findViewById(R.id.imageViewRegister);
//        mSwitchSmartCapture = (Switch) findViewById(R.id.switchSmartCapture);
//        mSwitchSmartCapture.setOnClickListener(this);
        mNumFakeThresholds = new int[1];
        mDefaultFakeThreshold = new int[1];
        mFakeEngineReady = new boolean[1];

        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES * JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i = 0; i < grayBuffer.length; ++i)
            grayBuffer[i] = Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2) * (JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2)];
        for (int i = 0; i < sintbuffer.length; ++i)
            sintbuffer[i] = Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2);
        mImageViewRegister.setImageBitmap(grayBitmap);
        mMaxTemplateSize = new int[1];


        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        sgfplib = new JSGFPLib(this, (UsbManager) getSystemService(Context.USB_SERVICE));
//        this.mSwitchSmartCapture.setChecked(true);

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
    @Override
    public void onPause() {
        Log.d(TAG, "Enter onPause()");
        debugMessage("Enter onPause()\n");
        if (bSecuGenDeviceOpened) {
            autoOn.stop();
            EnableControls();
            sgfplib.CloseDevice();
            bSecuGenDeviceOpened = false;
        }
        unregisterReceiver(mUsbReceiver);
        mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
        mImageViewRegister.setImageBitmap(grayBitmap);
        super.onPause();
        Log.d(TAG, "Exit onPause()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {
        Log.d(TAG, "Enter onResume()");
        debugMessage("Enter onResume()\n");
        super.onResume();
        DisableControls();
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

                        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                        sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                        debugMessage("TEMPLATE_FORMAT_ISO19794 SIZE: " + mMaxTemplateSize[0] + "\n");
                        mRegisterTemplate = new byte[(int) mMaxTemplateSize[0]];
                        mVerifyTemplate = new byte[(int) mMaxTemplateSize[0]];
                        EnableControls();
                        boolean smartCaptureEnabled = true;
                        if (smartCaptureEnabled)
                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 1);
                        else
                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 0);
                        if (mAutoOnEnabled) {
                            autoOn.start();
                            DisableControls();
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

    public Bitmap toGrayscale(byte[] mImageBuffer, int width, int height) {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }

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


    public void DumpFile(String fileName, byte[] buffer) {
        //Uncomment section below to dump images and templates to SD card
    }

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
        dwTimeStart = System.currentTimeMillis();
        long result = sgfplib.GetImageEx(mRegisterImage, IMAGE_CAPTURE_TIMEOUT_MS, IMAGE_CAPTURE_QUALITY);
        DumpFile("register.raw", mRegisterImage);
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("GetImageEx() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
        dwTimeStart = System.currentTimeMillis();
        result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("SetTemplateFormat(ISO19794) ret:" + result + " [" + dwTimeElapsed + "ms]\n");

        int quality1[] = new int[1];
        result = sgfplib.GetImageQuality(mImageWidth, mImageHeight, mRegisterImage, quality1);
        debugMessage("GetImageQuality() ret:" + result + "quality [" + quality1[0] + "]\n");

        SGFingerInfo fpInfo = new SGFingerInfo();
        fpInfo.FingerNumber = 1;
        fpInfo.ImageQuality = quality1[0];
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
        mImageViewRegister.setImageBitmap(this.toGrayscale(mRegisterImage));
        if (result == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            bFingerprintRegistered = true;

            int[] size = new int[1];
            result = sgfplib.GetTemplateSize(mRegisterTemplate, size);
            debugMessage("GetTemplateSize() ret:" + result + " size [" + size[0] + "]\n");

//            mTextViewResult.setText("Fingerprint registered");
        } else {
//            mTextViewResult.setText("Fingerprint not registered");
        }

        mRegisterImage = null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void onClick(View v) {
        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;

//        if (v == mSwitchSmartCapture) {
//            if (mSwitchSmartCapture.isChecked()) {
                sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 1); //Enable Smart Capture
//            } else {
//                sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 0); //Disable Smart Capture
//            }
//        }
        if (v == this.mButtonRegister) {
            debugMessage("Clicked REGISTER\n");
            RegisterFingerPrint();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void run() {

        while (true) {

        }
    }
}