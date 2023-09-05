package com.nicare.ves.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EOStatusReceiver extends BroadcastReceiver {


    public static final String ACTION_EVENT_EO_STATUS = "com.nicare.ees.EO_STATUS";
    public static final String EVENT_EO_STATUS_CODE_MESSAGE = "com.nicare.ees.EO_STATUS_CODE_MESSAGE";

    private StatusRecieverCallback mCallback;

    public void setCallback(StatusRecieverCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_EVENT_EO_STATUS.equals(intent.getAction())) {
            if (mCallback != null)
                mCallback.onStatusRecieved(intent.getIntExtra(EVENT_EO_STATUS_CODE_MESSAGE, 0));
        }
    }

}
