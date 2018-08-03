package com.android.gpstest;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Application extends android.app.Application {

    private static Application mApp;

    private SharedPreferences mPrefs;

    public static Application get() {
        return mApp;
    }

    public static SharedPreferences getPrefs() {
        return get().mPrefs;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set theme
        if (Application.getPrefs().getBoolean(getString(R.string.pref_key_dark_theme), false)) {
            setTheme(R.style.AppTheme_Dark);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mApp = null;
    }
}
