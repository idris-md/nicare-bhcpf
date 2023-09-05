package com.nicare.ves.ui.enrol.fingerprint;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.dpfpddusbhost.DPFPDDUsbException;
import com.digitalpersona.uareu.dpfpddusbhost.DPFPDDUsbHost;
import com.nicare.ves.R;
import com.nicare.ves.common.Globals;
import com.nicare.ves.persistence.local.databasemodels.enrolmentmodels.Fingerprint;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UareUMainActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.digitalpersona.uareu.dpfpddusbhost.USB_PERMISSION";
    private final int GENERAL_ACTIVITY_RESULT = 1;
    private final int ENROLMENT_ACTIVITY_RESULT = 2;
    Reader m_reader;
    @BindView(R.id.btnGetReader)
    Button buttonReader;
    @BindView(R.id.btnEnroll)
    Button buttonEnroll;
    @BindView(R.id.tvDevice)
    TextView tvDevice;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            CheckDevice();
                        }
                    } else {
                        setButtonsEnabled(false);
                    }
                }
            }
        }
    };
    private String m_deviceName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //enable tracing
        System.setProperty("DPTRACE_ON", "1");
        //System.setProperty("DPTRACE_VERBOSITY", "10");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uare_umain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setButtonsEnabled(false);

        // register handler for UI elements
        buttonReader.setOnClickListener(v -> launchGetReader());
        buttonEnroll.setOnClickListener(v -> launchEnrollment());


    }

    protected void launchGetReader() {
        Intent i = new Intent(UareUMainActivity.this, UareUSelectReaderActivity.class);
        i.putExtra("device_name", m_deviceName);
        startActivityForResult(i, 1);
    }

    protected void launchEnrollment() {
//        Intent i = new Intent(UareUMainActivity.this, UareUFingerCaptureActivity.class);
//        i.putExtra("device_name", m_deviceName);
//        startActivityForResult(i, ENROLMENT_ACTIVITY_RESULT);
    }

    protected void setButtonsEnabled(Boolean enabled) {
        buttonEnroll.setEnabled(enabled);
    }

    protected void setButtonsEnabled_Capture(Boolean enabled) {
        buttonEnroll.setEnabled(enabled);
    }

    protected void CheckDevice() {
        try {
            m_reader.Open(Reader.Priority.EXCLUSIVE);
            Reader.Capabilities cap = m_reader.GetCapabilities();
            setButtonsEnabled(true);
            setButtonsEnabled_Capture(cap.can_capture);
            //setButtonsEnabled_Stream(cap.can_stream);
            m_reader.Close();
        } catch (UareUException e1) {
            displayReaderNotFound();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            displayReaderNotFound();
            return;
        }

        Globals.ClearLastBitmap();
        m_deviceName = (String) data.getExtras().get("device_name");

        switch (requestCode) {
            case GENERAL_ACTIVITY_RESULT:

                if ((m_deviceName != null) && !m_deviceName.isEmpty()) {
                    tvDevice.setText("Device: " + m_deviceName);

                    try {
                        Context applContext = getApplicationContext();
                        m_reader = Globals.getInstance().getReader(m_deviceName, applContext);

                        {
                            PendingIntent mPermissionIntent;
                            mPermissionIntent = PendingIntent.getBroadcast(applContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                            applContext.registerReceiver(mUsbReceiver, filter);

                            if (DPFPDDUsbHost.DPFPDDUsbCheckAndRequestPermissions(applContext, mPermissionIntent, m_deviceName)) {
                                CheckDevice();
                            }


                        }
                    } catch (UareUException e1) {
                        displayReaderNotFound();
                    } catch (DPFPDDUsbException e) {
                        displayReaderNotFound();
                    }

                } else {
                    displayReaderNotFound();
                }

                break;
            case ENROLMENT_ACTIVITY_RESULT:

                Fingerprint biometric = data.getParcelableExtra("biometric");

                Intent intent = new Intent();
                intent.putExtra("biometric", (Parcelable) biometric);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }

    private void displayReaderNotFound() {
        tvDevice.setText("Device: (No Reader Selected)");
        setButtonsEnabled(false);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Reader Not Found");
        alertDialogBuilder.setMessage("Plug in a reader and try again.").setCancelable(false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
