//package com.nicare.ves.ui.enrol.premium;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//
//import com.nicare.ves.R;
//import com.nicare.ves.common.Constant;
//import com.nicare.ves.common.Util;
//
//import butterknife.ButterKnife;
//
//public class DependentPinVerification extends PinVerificationActivity {
//
//    private String mDepenentType;
//    private String en;
//    private long pid;
//    private boolean isActive;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_enrolled_dependent_pin_v);
//        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        // setSupportActionBar(toolbar);
//
//        ButterKnife.bind(this);
//        readIntentStateValues();
//
//    }
//
//    private void readIntentStateValues() {
//        Intent intent = getIntent();
//        mDepenentType = intent.getStringExtra(DEPENDENT_TYPE);
//        pid = intent.getLongExtra(PRINCIPAL_ID, 0);
//        en = intent.getStringExtra(PRINCIPAL_ENROLMENT_NUMBER);
//        isActive = intent.getBooleanExtra(IS_ACTIVE, false);
//    }
//
//    @Override
//    protected void onVerified(String pinType, String pin) {
//
//        if (pinType.equalsIgnoreCase(Constant.PIN_TYPE_HOUSEHOLD)) {
//            Util.showDialogueMessae(this, "Ohh sorry! An Individual Premium is required to add this dependent", getString(R.string.error_msg));
//            return;
//        }
//
//        if (pinType.equalsIgnoreCase(Constant.PIN_TYPE_VULNERABLE)) {
//            Util.showDialogueMessae(this, "Ohh sorry! You cant enroll dependent with an Vulnerable  Pin, please use an individual pin.", getString(R.string.error_msg));
//            return;
//        }
//
//        Intent intent = new Intent(this, EnrollDependentActivity.class);
//
//        if (mDepenentType.equalsIgnoreCase(Constant.DEPENDENT_TYPE_SPOUSE)) {
//            intent.putExtra(EnrollDependentActivity.DEPENDENT_TYPE, Constant.DEPENDENT_TYPE_SPOUSE);
//        } else {
//            intent.putExtra(EnrollDependentActivity.DEPENDENT_TYPE, Constant.DEPENDENT_TYPE_CHILD);
//        }
//
//        intent.putExtra(EnrollDependentActivity.PIN, edtPIN.getText().toString());
//        intent.putExtra(EnrollDependentActivity.PRINCIPAL_ID, pid);
//        intent.putExtra(EnrollDependentActivity.PRINCIPAL_ENROLMENT_NUMBER, en);
//        intent.putExtra(EnrollDependentActivity.IS_ACTIVE, isActive);
//        startActivity(intent);
//        finish();
//
//    }
//
//}
