package kpklib.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by goofyahead on 10/1/15.
 */
public class KpkLibPrefs {
    private static final String IS_REGISTERED = "IS_REGISTERED";
    private static final String REGISTRATION_ID = "REGISTRATIOND_ID";
    private static final String SECRET = "SECRET";
    private static final String REGISTERED_VERSION = "REGISTERED_VERSION";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String LAST_UPDATED = "LAST_UPDATE";
    private static final String USER_FB_ID = "USER_FB_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private final SharedPreferences prefs;

    public KpkLibPrefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getLastUpdate() {
        return prefs.getInt(LAST_UPDATED, 0);
    }

    public void setLastUpdated(int timeStamp) {
        prefs.edit().putInt(LAST_UPDATED, timeStamp).commit();
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

    public String getFbToken() {
        return prefs.getString(SECRET, "");
    }

    public void setFbToken(String secret) {
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

    public void setUserFbId(String userFbId) {
        prefs.edit().putString(USER_FB_ID, userFbId).commit();
    }

    public String getUserFbId() {
        return prefs.getString(USER_FB_ID, "");
    }

    public void setUserName(String userName) {
        prefs.edit().putString(USER_NAME, userName).commit();
    }

    public void setUserEmail(String email) {
        prefs.edit().putString(USER_EMAIL, email).commit();
    }

    public String getUserName() {
        return prefs.getString(USER_NAME, "");
    }

    public String getUserEmail() {
        return prefs.getString(USER_EMAIL, "");
    }
}
