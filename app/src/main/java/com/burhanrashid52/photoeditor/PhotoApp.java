package com.burhanrashid52.photoeditor;

import android.app.Application;
import android.content.Context;

/**
 * Created by Burhanuddin Rashid on 1/23/2018.
 */

public class PhotoApp extends Application {
    private static PhotoApp sPhotoApp;
    private static final String TAG = PhotoApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        sPhotoApp = this;
    }

    public static PhotoApp getPhotoApp() {
        return sPhotoApp;
    }

    public Context getContext() {
        return sPhotoApp.getContext();
    }
}
