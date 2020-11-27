package com.example.plent;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.signed.Signature;
import com.cloudinary.android.signed.SignatureProvider;

import java.util.Map;

public class MainApplication extends Application {
    private static MainApplication _instance;

    public static MainApplication get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Mandatory - call a flavor of init. Config can be null if cloudinary_url is provided in the manifest.
        MediaManager.init(this);

        _instance = this;
    }
}
