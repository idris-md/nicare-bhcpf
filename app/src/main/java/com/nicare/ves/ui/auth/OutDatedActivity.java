package com.nicare.ves.ui.auth;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nicare.ves.R;
import com.nicare.ves.common.PrefUtils;
import com.nicare.ves.common.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OutDatedActivity extends AppCompatActivity {
    @BindView(R.id.button6)
    Button btnInstall;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMsg)
    TextView tvMsg;

    private long downloadID;
//    private PrefUtils mInstance;

//    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            //Fetching the download id received with the broadcast
//            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            if (downloadID == id) {
//
//                Toast.makeText(OutDatedActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
//                mInstance.setLatestAppVersionDL(mInstance.getLatestAppVersionCode());
//                invalidateOptionsMenu();
//            }
//
//        }
//
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_dated);
        ButterKnife.bind(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(onDownloadComplete);
    }
    void installUpdate() {
        String dir = "NiCare/ees " + PrefUtils.getInstance(this).getLatestVersionName() + ".apk";
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

    @OnClick(R.id.button6)
    void beginDownload() {

        if (btnInstall.getText().equals(getString(R.string.install_now))) {
            installUpdate();
        } else {
//            File file = new File(getExternalFilesDir(null), "update.apk");
            String dir = "NiCare/ees " + PrefUtils.getInstance(this).getLatestVersionName() + ".apk";
            File file = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + dir);
            String url=PrefUtils.getInstance(this).getLatestVersionUrl();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
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
                    boolean downloading = true;
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
                                progressBar.setProgress((int) currentProgress);
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    btnInstall.setText(R.string.install_now);
                                    installUpdate();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                        cursor.close();

                    }
                }
            }).start();
        }

    }

    private String statusMessage(int status) {
        String msg = "???";
        switch (status) {
            case DownloadManager.STATUS_RUNNING:
                msg = "Downloading";
                break;
            case DownloadManager.STATUS_PAUSED:
                msg = "Downloading paused";
                break;
            case DownloadManager.STATUS_FAILED:
                msg = "Downloading failed retry";
                btnInstall.setEnabled(true);
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
}