//package com.nicare.ves.ui.enrol.premium;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.google.android.material.textfield.TextInputEditText;
//import com.nicare.ees.R;
//import com.nicare.ees.common.Util;
//import com.nicare.ees.persistence.local.databasemodels.utilmodels.Premium;
//import com.nicare.ees.vision.barcodedetection.LiveBarcodeScanningActivity;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import io.reactivex.MaybeObserver;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.observers.DisposableCompletableObserver;
//import io.reactivex.schedulers.Schedulers;
//
////import com.nicare.ees.ui.ml.LiveBarcodeScanningActivity;
//
//public class PremiumSalesActivity extends AppCompatActivity {
//
//    @BindView(R.id.btnVerify)
//    Button btnVerify;
//    @BindView(R.id.edtID)
//    TextInputEditText edtID;
////    @BindView(R.id.imgQR)
////    ImageView imgQR;
//    private PINActivityViewModel mViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_premium_sales);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ButterKnife.bind(this);
//imgQR.setEnabled(false);
//        mViewModel = ViewModelProviders.of(this).get(PINActivityViewModel.class);
//    }
//
//    @OnClick(R.id.imgQR)
//    void scanQR() {
//        Intent intent = new Intent(this, LiveBarcodeScanningActivity.class);
//        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btnVerify)
//    void verify() {
//
//        mViewModel.getPINByCode(edtID.getText().toString())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MaybeObserver<Premium>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Premium premium) {
//                        if (premium.isSales_status()) {
//                            Util.showDialogueMessae(PremiumSalesActivity.this, "Premium has been sold already, and can't be resale", "Failed");
//
//                        } else {
//                            premium.setSales_status(true);
//                            premium.setSoldAt(Util.dateTimeString());
//                            mViewModel.updatePIN(premium).subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribeWith(new DisposableCompletableObserver() {
//                                        @Override
//                                        public void onComplete() {
//                                            edtID.getText().clear();
//                                            Util.showDialogueMessae(PremiumSalesActivity.this, "Premium marked as sold", "Success");
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//
//                                        }
//                                    });
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Util.showDialogueMessae(PremiumSalesActivity.this, "No premium found with this Sale code", "Wrong Sales Code");
//                    }
//                });
//
//    }
//
//}
