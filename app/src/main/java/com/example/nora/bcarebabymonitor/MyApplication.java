package com.example.nora.bcarebabymonitor;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by nora on 11/09/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
