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
    private static final String ORDERS_TO_PRINT = "ORDERSTOPRINT";
    private final SharedPreferences prefs;

    public KpkLibPrefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getLastUpdate() {
        return prefs.getInt(LAST_UPDATED, -1);
    }

    public void setLastUpdated(int timeStamp) {
        prefs.edit().putInt(LAST_UPDATED, timeStamp).apply();
    }
    public boolean isFirstTime() {
        return prefs.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean first) {
        prefs.edit().putBoolean(IS_FIRST_TIME, first).apply();
    }

    public boolean isRegistered() {
        return prefs.getBoolean(IS_REGISTERED, false);
    }

    public void setRegistered(boolean status) {
        prefs.edit().putBoolean(IS_REGISTERED, status).apply();
    }

    public String getFbToken() {
        return prefs.getString(SECRET, "");
    }

    public void setFbToken(String secret) {
        prefs.edit().putString(SECRET, secret).apply();
    }

    public void setRegistrationPushId(String regid) {
        prefs.edit().putString(REGISTRATION_ID, regid).apply();
    }

    public String getRegistrationPushId() {
        return prefs.getString(REGISTRATION_ID, "");
    }

    public int getRegisteredVersion() {
        return prefs.getInt(REGISTERED_VERSION, 0);
    }

    public void setRegisteredVersion(int version) {
        prefs.edit().putInt(REGISTERED_VERSION, version).apply();
    }

    public void setUserFbId(String userFbId) {
        prefs.edit().putString(USER_FB_ID, userFbId).apply();
    }

    public String getUserFbId() {
        return prefs.getString(USER_FB_ID, "");
    }

    public void setUserName(String userName) {
        prefs.edit().putString(USER_NAME, userName).apply();
    }

    public void setUserEmail(String email) {
        prefs.edit().putString(USER_EMAIL, email).apply();
    }

    public String getUserName() {
        return prefs.getString(USER_NAME, "");
    }

    public String getUserEmail() {
        return prefs.getString(USER_EMAIL, "");
    }

    public void setOrdersToPrint(String ordersToPrint) {
        prefs.edit().putString(ORDERS_TO_PRINT, ordersToPrint).apply();
    }

    public String getOrdersToPrint () {
        return prefs.getString(ORDERS_TO_PRINT, "");
    }
}
