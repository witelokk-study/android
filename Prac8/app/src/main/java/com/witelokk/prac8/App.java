package com.witelokk.prac8;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class App extends Application {
    @Override
    public void onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this);
        super.onCreate();
    }
}
