package com.nicare.ves.ui.main;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.nicare.ves.BuildConfig;
import com.nicare.ves.R;
import com.nicare.ves.broadcasts.EOStatusReceiver;
import com.nicare.ves.broadcasts.StatusRecieverCallback;
import com.nicare.ves.broadcasts.SyncStatusReceiver;
import com.nicare.ves.broadcasts.SyncStatusRecieverCallback;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;
import com.nicare.ves.persistence.local.databasemodels.utilmodels.Ward;
import com.nicare.ves.persistence.remote.apimodels.Transaction;
import com.nicare.ves.ui.adapter.WardSpinnerAdapter;
import com.nicare.ves.ui.auth.OutDatedActivity;
import com.nicare.ves.ui.auth.device.DeviceBlockedActivity;
import com.nicare.ves.ui.auth.device.SuspendActivity;
import com.nicare.ves.ui.enrol.premium.PinVerificationActivity;
import com.nicare.ves.ui.enrol.recapture.UnApprovedActivity;
import com.nicare.ves.ui.enrol.vulnerable.EnrolVulnActivity;
import com.nicare.ves.ui.enrol.vulnerable.VulnerableListActivity;
import com.nicare.ves.ui.preference.SettingActivity;
import com.nicare.ves.ui.profile.ProfileActivity;
import com.nicare.ves.ui.splash.SplashActivity;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends DaggerAppCompatActivity implements StatusRecieverCallback, SyncStatusRecieverCallback {

    public String TOKEN;
    @BindView(R.id.enroll)
    MaterialCardView btnEnroll;
    @BindView(R.id.re_capture)
    MaterialCardView btnRecapture;
    @BindView(R.id.enrolled)
    MaterialCardView btnEnrolled;
    @BindView(R.id.profile)
    MaterialCardView btnProfile;
    @BindView(R.id.btnSyncPin)
    MaterialButton btnSync;
//    @BindView(R.id.tvPinLeft)
//    TextView tvPinLeft;
    @BindView(R.id.tvPinUsed)
    TextView tvPinUsed;
    @BindView(R.id.tvUploads)
    TextView tvUpload;
    @BindView(R.id.tvEnrolled)
    TextView tvEnrolled;
    @BindView(R.id.tvSync)
    TextView tvLastSync;
    @BindView(R.id.tvNotSynced)
    TextView tvNotSynced;
    @BindView(R.id.tvWard)
    MaterialSpinner spWard;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvUpdate)
    TextView tvUpdate;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.btnInstall)
    Button btnInstall;
    @BindView(R.id.layout_update)
    CardView layoutUpdate;
    @Inject
    ViewModelProviderFactory mProviderFactory;
    MainActivityViewModel viewModel;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private long downloadID;
    private int latestVersionCode;
    private PrefUtils mInstance;
    private boolean isLatestVersionDownloaded;

    private WorkManager mWorkManager;
    private SyncStatusReceiver mStatusReceiver;
    private EOStatusReceiver mEoStatusReceiver;
    WardSpinnerAdapter mWardSpinnerAdapter;
    private Ward selectedWard;
    private int allCount;
    boolean downloading;
    private int registration;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(onDownloadComplete);
        unregisterReceiver(mStatusReceiver);
        unregisterReceiver(mEoStatusReceiver);

    }

    @OnClick(R.id.btnInstall)
    void installUpdate() {
        String dir = "NiCare/Huwe/huwe ees " + PrefUtils.getInstance(this).getLatestVersionName() + ".apk";
        File file = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + dir);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

            startActivity(intent);
        } else {
            Util.showDialogueMessae(this, "Couldn't find downloaded update file", "File Not Found");
        }

    }

    private void versionCheck() {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        PrefUtils instance = PrefUtils.getInstance(getBaseContext());
        latestVersionCode = instance.getLatestAppVersionCode();
        int dlVersionCode = instance.getDownloadedAppVersion();

        if (latestVersionCode > currentVersionCode && latestVersionCode > dlVersionCode) {
            //if (instance.isLatestAppVersionDL()) {
            isLatestVersionDownloaded = true;
            invalidateOptionsMenu();
            //}
            //invalidateOptionsMenu();
        } else if (currentVersionCode < dlVersionCode) {
            layoutUpdate.setVisibility(View.VISIBLE);
        }
    }

    void beginDownload() {
        layoutUpdate.setVisibility(View.VISIBLE);
//        if (btnInstall.getText().equals(getString(R.string.install_now))) {
//            installUpdate();
//        } else {
//        File file = new File(getExternalFilesDir(null), "update.apk");
        String dir = "NiCare/Huwe/huwe ees " + PrefUtils.getInstance(this).getLatestVersionName() + ".apk";
        File file = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + dir);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(PrefUtils.getInstance(this).getLatestVersionUrl()))
                .setTitle(getString(R.string.app_name))// Title of the Download Notification
                .setDescription("Downloading update")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

        new Thread(new Runnable() {
            @Override
            public void run() {
                downloading = true;
                while (downloading) {

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);

                    Cursor cursor = downloadManager.query(query);
                    cursor.moveToFirst();

                    int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int currentProgress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                    final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnInstall.setText(statusMessage(status));

                            progressBar.setProgress(currentProgress);
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                btnInstall.setText("Click to install");
                                btnInstall.setEnabled(true);
//                                    btnInstall.setBackgroundColor(getColor(R.color.primaryDarkColor));
                                progressBar.setVisibility(View.INVISIBLE);
//                                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                                    if (downloadID == id) {
                                tvUpdate.setVisibility(View.VISIBLE);
                                mInstance.setLatestAppVersionDL(mInstance.getLatestAppVersionCode());
                                invalidateOptionsMenu();
//                                    }

                            }
                        }
                    });

                    cursor.close();

                }
            }
        }).start();


    }

//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        bindViewModel();
        // registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        mInstance = PrefUtils.getInstance(getBaseContext());
        TOKEN = mInstance.getToken();
        mWardSpinnerAdapter = new WardSpinnerAdapter(this);
        spWard.setAdapter(mWardSpinnerAdapter);
        mWorkManager = WorkManager.getInstance(this);
        setupBroadcastReciever();

    }

    void setupBroadcastReciever() {

        mStatusReceiver = new SyncStatusReceiver();
        mStatusReceiver.setCallback(this::onSyncStatus);
        IntentFilter statusIntentFilter = new IntentFilter(SyncStatusReceiver.ACTION_EVENT_SYNC_STATUS);

        mEoStatusReceiver = new EOStatusReceiver();
        mEoStatusReceiver.setCallback(this::onStatusRecieved);
        IntentFilter eoIntentFilter = new IntentFilter(EOStatusReceiver.ACTION_EVENT_EO_STATUS);

        registerReceiver(mEoStatusReceiver, eoIntentFilter);
        registerReceiver(mStatusReceiver, statusIntentFilter);

    }

    @SuppressLint("CheckResult")
    private void bindViewModel() {

        viewModel = ViewModelProviders.of(this, mProviderFactory).get(MainActivityViewModel.class);
        tvLastSync.setText(PrefUtils.getInstance(this).getDeviceId());


        viewModel.getPinsUnused().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                allCount = integer;
                tvNotSynced.setText(Util.formatStat(integer));
            }
        });

        spWard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                selectedWard = (Ward) materialSpinner.getAdapter().getItem(i);
                PrefUtils.getInstance(MainActivity.this).setWard(selectedWard.getWard_id());
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

//            viewModel.getLGAById()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(address -> {
//                        PrefUtils.getInstance(this).setAddress(Util.toTitleCase(address.getWard() + " Ward, " + Util.toTitleCase(address.getLga()) + ", Niger State"));
//                        tvWard.setText(PrefUtils.getInstance(this).getAddress());
//                    });

        viewModel.getWardsByLG(PrefUtils.getInstance(this).getLGA()).observe(this, new Observer<List<Ward>>() {
            @Override
            public void onChanged(List<Ward> wards) {
                mWardSpinnerAdapter.notifyEntryChange(wards);
                String ward = PrefUtils.getInstance(MainActivity.this).getAddress();
                if (ward != null) {
                    spWard.setSelection(wards.indexOf(new Ward(ward)));
                }
            }
        });


//        viewModel.getPinsused().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                tvPinUsed.setText(viewModel.formatStat(integer));
//            }
//        });

//        viewModel.getPinsUnused().observe(this, integer -> tvPinLeft.setText(viewModel.formatStat(integer)));
        viewModel.getTransaction().observe(this, transaction -> {
            if (transaction != null) {
                tvUpload.setText(Util.formatStat(transaction.getSync()));
                registration = transaction.getRegistration();
                tvEnrolled.setText(Util.formatStat(registration));
            } else {
                transaction = new Transaction(Util.todayDate());
                viewModel.insertOrUpdateTransaction(transaction)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });


        viewModel.observerAuthWork().observe(this, workInfo -> {
            if (workInfo.getState() != null) {
                switch (workInfo.getState()) {

                    case FAILED:
                        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCEEDED:
//                        Toast.makeText(this, "Connection complete", Toast.LENGTH_SHORT).show();
                        break;

                    case RUNNING:
                        Toast.makeText(this, "Initialising Connection", Toast.LENGTH_SHORT).show();
                        break;

                    case ENQUEUED:
//                        Toast.makeText(this, "Connection enqueued", Toast.LENGTH_SHORT).show();
                        break;

                    case CANCELLED:
//                        Toast.makeText(this, "Connection Cancelled", Toast.LENGTH_SHORT).show();
                        break;

                    case BLOCKED:
//                        Toast.makeText(this, "Connection Blocked", Toast.LENGTH_SHORT).show();
//                        Log.e("Worker", "Connection Blocked");
                        break;

                }
            }
        });

        viewModel.observerSyncWork().observe(this, workInfo -> {
            switch (workInfo.getState()) {

                case FAILED:
                    Toast.makeText(this, "Synchronization failed", Toast.LENGTH_SHORT).show();
//                        viewModel.cancelAllWork();
                    break;

                case SUCCEEDED:
                    Toast.makeText(this, "Synchronization complete", Toast.LENGTH_SHORT).show();

                    if (allCount > 0) {
                        viewModel.initPeriodicWorker();
                    }
                    break;

                case RUNNING:
                    Toast.makeText(this, "Synchronizing", Toast.LENGTH_SHORT).show();
                    break;

                case ENQUEUED:
//                        Toast.makeText(this, "Synchronization enqueued", Toast.LENGTH_SHORT).show();
                    break;

                case CANCELLED:
                    Toast.makeText(this, "Synchronization Cancelled", Toast.LENGTH_SHORT).show();
                    break;

                case BLOCKED:
//                        Toast.makeText(this, "Synchronization Blocked", Toast.LENGTH_SHORT).show();
//                        viewModel.cancelAllWork();
                    Log.e("Worker", "Connection Blocked");
                    break;

            }
        });


    }


    @OnClick(R.id.btnSyncPin)
    void syncNow() {

//        if (allCount == 0) {
//            Toast.makeText(MainActivity.this, "No Record to Synchronize", Toast.LENGTH_LONG).show();
//         Down   Toast.makeText(MainActivity.this, "No Record to Synchronize", Toast.LENGTH_LONG).show();
//            return;
//        }

        PrefUtils.getInstance(this).setSynceable(true);
        viewModel.initPeriodicWorker();
        Toast.makeText(MainActivity.this, "Synchronize Initialized, please wait..", Toast.LENGTH_LONG).show();

        new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long l) {
                btnSync.setText("Retry in " + new SimpleDateFormat("mm:ss").format(l));
                btnSync.setBackgroundColor(getResources().getColor(R.color.gray));
                btnSync.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btnSync.setText(getString(R.string.sync_now));
                btnSync.setEnabled(true);
                btnSync.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

        }.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_install);
        if (isLatestVersionDownloaded) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        }

//        if (id == R.id.action_profile) {
//            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
//            startActivity(intent);
//            return true;
//        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_install) {
            if (!downloading)
                beginDownload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrefUtils.getInstance(this).setSynceable(true);
        tvAddress.setText(PrefUtils.getInstance(this).getAddress() + " WARD");

        versionCheck();
    }

    @Override
    public void onStatusRecieved(int code) {

        Intent deactivatedIntent = new Intent(this, DeviceBlockedActivity.class);
        deactivatedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent suspendedIntent = new Intent(this, SuspendActivity.class);
        suspendedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        switch (code) {
            case 123:
                // Deactivated DEVICE
            case 212:
                // Deactivated EO
            case 112:
                //Suspend EO
            case 111:
                //Suspend DEVICE
            case 211:
                restartApp();
                break;

            case 113:
                startActivity(new Intent(this, OutDatedActivity.class));
                finish();
                break;
            //startActivity(suspendedIntent);

        }
    }

    private void restartApp() {
        Intent mStartActivity = new Intent(this, SplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    @Override
    public void onSyncStatus(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private String statusMessage(int status) {
        String msg = "???";
        switch (status) {

            case DownloadManager.STATUS_RUNNING:
                msg = "Downloading update";
                break;
            case DownloadManager.STATUS_PAUSED:
                msg = "Downloading paused";
                break;
            case DownloadManager.STATUS_FAILED:
                msg = "Downloading failed retry";
                invalidateOptionsMenu();
                break;
            case DownloadManager.STATUS_PENDING:
                msg = "Download pending";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;
            default:
                msg = "No file found";
                break;

        }
        return msg;
    }

    @OnClick(R.id.enroll)
    void enrol() {
        int limit = PrefUtils.getInstance(getBaseContext()).getEnrolmentLimit();
        int lgaLimit = PrefUtils.getInstance(getBaseContext()).getLGAEnrolmentLimit();
        if (registration < limit) {
            int ballanceLeft = lgaLimit - allCount;
            if (ballanceLeft > 0) {
                startActivity(new Intent(this, EnrolVulnActivity.class));
            } else {
                Util.showToast("You've reached your LGA enrolment limit", false, this, LayoutInflater.from(this));
            }
        } else {
            Util.showToast("You've reached your enrolment limit for today", false, this, LayoutInflater.from(this));
        }
    }

//    @OnClick(R.id.under_five)
//    void underFiver() {
//        startActivity(new Intent(this, PinVerificationActivity.class));
//    }

    @OnClick(R.id.re_capture)
    void reCapture() {
        startActivity(new Intent(this, UnApprovedActivity.class));
    }

    @OnClick(R.id.enrolled)
    void lists() {
        startActivity(new Intent(this, VulnerableListActivity.class));
    }

    @OnClick(R.id.profile)
    void profile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }


    private void load() {

    }

}
