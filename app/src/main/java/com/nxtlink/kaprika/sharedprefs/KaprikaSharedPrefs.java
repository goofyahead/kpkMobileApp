package com.nxtlink.kaprika.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by goofyahead on 9/10/14.
 */
public class KaprikaSharedPrefs {

    private static final String IS_REGISTERED = "IS_REGISTERED";
    private static final String REGISTRATION_ID = "REGISTRATIOND_ID";
    private static final String SECRET = "SECRET";
    private static final String REGISTERED_VERSION = "REGISTERED_VERSION";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private final SharedPreferences prefs;

    public KaprikaSharedPrefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isFirstTime() {
        return prefs.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean first) {
        prefs.edit().putBoolean(IS_FIRST_TIME, first).commit();
    }

    public boolean isRegistered() {
        return prefs.getBoolean(IS_REGISTERED, false);
    }

    public void setRegistered(boolean status) {
        prefs.edit().putBoolean(IS_REGISTERED, status).commit();
    }

    public String getSecret() {
        return prefs.getString(SECRET, "");
    }

    public void setSecret(String secret) {
        prefs.edit().putString(SECRET, secret).commit();
    }

    public void setRegistrationPushId(String regid) {
        prefs.edit().putString(REGISTRATION_ID, regid).commit();
    }

    public String getRegistrationPushId() {
        return prefs.getString(REGISTRATION_ID, "");
    }

    public int getRegisteredVersion() {
        return prefs.getInt(REGISTERED_VERSION, 0);
    }

    public void setRegisteredVersion(int version) {
        prefs.edit().putInt(REGISTERED_VERSION, version).commit();
    }
}
