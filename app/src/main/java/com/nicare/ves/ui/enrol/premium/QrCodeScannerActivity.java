package com.nicare.ves.ui.enrol.premium;//package com.nicare.ees.ui.premium;
//
//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.Result;
//import com.nicare.ees.common.Util;
//
//import java.util.Collections;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.observers.DisposableCompletableObserver;
//import io.reactivex.schedulers.Schedulers;
//import me.dm7.barcodescanner.zxing.ZXingScannerView;
//
//import static android.Manifest.permission.CAMERA;
//
//public class QrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
//
//    private static final int REQUEST_CAMERA = 1;
//    private ZXingScannerView mScannerView;
//    private PINActivityViewModel mViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.e("onCreate", "onCreate");
//
//        mScannerView = new ZXingScannerView(this);
//        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
//        setContentView(mScannerView);
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
//            if (checkPermission()) {
//                Toast.makeText(getApplicationContext(), "Scanner initialized", Toast.LENGTH_LONG).show();
//            } else {
//                requestPermission();
//            }
//        }
//
//        mViewModel = ViewModelProviders.of(this).get(PINActivityViewModel.class);
//
//    }
//
//    private boolean checkPermission() {
//        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                if (grantResults.length > 0) {
//
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted) {
//                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (shouldShowRequestPermissionRationale(CAMERA)) {
//                                showMessageOKCancel("You need to allow access to both the permissions",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermissions(new String[]{CAMERA},
//                                                            REQUEST_CAMERA);
//                                                }
//                                            }
//                                        });
//                                return;
//                            }
//                        }
//                    }
//                }
//                break;
//        }
//    }
//
//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//
//        new AlertDialog.Builder(QrCodeScannerActivity.this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//        //mScannerView.setFlash(true);
//    }
//
//    private void startScanner() {
//        int currentapiVersion = Build.VERSION.SDK_INT;
//        if (currentapiVersion >= Build.VERSION_CODES.M) {
//            if (checkPermission()) {
//                if (mScannerView == null) {
//                    mScannerView = new ZXingScannerView(this);
//                    setContentView(mScannerView);
//                }
//                mScannerView.setResultHandler(this);
//                mScannerView.startCamera();
//            } else {
//                requestPermission();
//            }
//        } else {
//
//            mScannerView = new ZXingScannerView(this);
//            mScannerView.startCamera();
//            setContentView(mScannerView);
//
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();
//    }
//
//    @Override
//    public void handleResult(Result rawResult) {
//
//        final String result = rawResult.getText();
//        Log.d("QRCodeScanner", rawResult.getText());
//        Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setPositiveButton("Mark sold", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //  mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
//                processResult(result);
//
//            }
//        });
//
//        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
//
//            }
//        });
//        builder.setMessage(rawResult.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();
//    }
//
//    @SuppressLint("CheckResult")
//    private void processResult(String result) {
//        mViewModel.getPINByCode(result)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(premium -> {
//                    if (premium != null) {
//
//                        if (premium.isSales_status()) {
//                            //Toast.makeText(this, "Premium has been sold already", Toast.LENGTH_LONG).show();
//                            Util.showDialogueMessae(QrCodeScannerActivity.this, "Premium already sold", "Premium sold");
//                            mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
//
//                        } else {
//                            // premium.setSales_status(true);
//                            premium.setSales_status(true);
//                            premium.setSoldAt(Util.dateTimeString());
//                            mViewModel.updatePIN(premium)
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribeWith(new DisposableCompletableObserver() {
//                                        @Override
//                                        public void onComplete() {
//                                            //Toast.makeText(this, "Premium marked as sold", Toast.LENGTH_LONG).show();
//                                            Util.showDialogueMessae(QrCodeScannerActivity.this, "Premium marked as sold", "Premium sold");
//                                            mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
//
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//                                            //Toast.makeText(this, "Premium marked as sold", Toast.LENGTH_LONG).show();
//                                            Util.showDialogueMessae(QrCodeScannerActivity.this, "Premium marked as sold", "Premium sold");
//                                            mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
//
//                                        }
//                                    });
//                        }
//
//                    } else {
//                        Util.showDialogueMessae(QrCodeScannerActivity.this, "No pin found with this Sale code", "Wrong Sales Code");
//                        mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
//                    }
//                });
//
//
//    }
//}
