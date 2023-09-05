package com.nicare.ves.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SyncStatusReceiver extends BroadcastReceiver {

    public static final String ACTION_EVENT_SYNC_STATUS = "com.nicare.ees.SYNC_STATUS";
    public static final String EVENT_SYNC_EXTRA_MESSAGE = "com.nicare.ees.SYNC_STATUS_MESSAGE";

    private SyncStatusRecieverCallback mCallback;

    public void setCallback(SyncStatusRecieverCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_EVENT_SYNC_STATUS.equals(intent.getAction())) {
            if (mCallback != null)
                mCallback.onSyncStatus(intent.getStringExtra(EVENT_SYNC_EXTRA_MESSAGE));
        }
    }

}
