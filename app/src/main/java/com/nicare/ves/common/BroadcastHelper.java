package com.nicare.ves.common;

import android.content.Context;
import android.content.Intent;

import static com.nicare.ves.broadcasts.EOStatusReceiver.ACTION_EVENT_EO_STATUS;
import static com.nicare.ves.broadcasts.EOStatusReceiver.EVENT_EO_STATUS_CODE_MESSAGE;
import static com.nicare.ves.broadcasts.SyncStatusReceiver.ACTION_EVENT_SYNC_STATUS;
import static com.nicare.ves.broadcasts.SyncStatusReceiver.EVENT_SYNC_EXTRA_MESSAGE;

public class BroadcastHelper {


    public static void sendSyncStatusBroadcast(Context context, String message) {

        Intent intent = new Intent(ACTION_EVENT_SYNC_STATUS);
        intent.putExtra(EVENT_SYNC_EXTRA_MESSAGE, message);

        context.sendBroadcast(intent);

    }


    public static void sendUserStatusBroadcast(Context context, int code) {

        Intent intent = new Intent(ACTION_EVENT_EO_STATUS);
        intent.putExtra(EVENT_EO_STATUS_CODE_MESSAGE, code);

        context.sendBroadcast(intent);

    }




}
