package com.example.cardscannertwo.ui;

import android.app.Application;

import com.balsikandar.crashreporter.CrashReporter;

public class CardScanner extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReporter.initialize(this);
    }
}
