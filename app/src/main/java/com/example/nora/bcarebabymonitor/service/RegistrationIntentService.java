package com.example.nora.bcarebabymonitor.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by nora on 17/09/16.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RIS";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, ":"+ token);
    }
}
