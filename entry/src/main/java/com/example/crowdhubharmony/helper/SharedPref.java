package com.example.crowdhubharmony.helper;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import org.jetbrains.annotations.NotNull;

public class SharedPref {

    private static final String PREFS_NAME = "MyPrefs";
    private Preferences preferences;
    private static SharedPref sharedPref;

    private SharedPref(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context.getApplicationContext());
        preferences = databaseHelper.getPreferences(PREFS_NAME);
    }

    public static SharedPref getInstance(Context context) {
        if (sharedPref == null) {
            sharedPref = new SharedPref(context);
        }
        return sharedPref;
    }

    public final void setIsLoggedIn(boolean boool) {
        preferences.putBoolean("is_login", boool);
        preferences.flushSync();
    }

    public final boolean getIsLoggedIn() {
        return preferences.getBoolean("is_login", false);
    }

    public final void setDeviceId(@NotNull String deviceId) {

        preferences.putString("device_id", deviceId);
        preferences.flushSync();
    }
    public final void setDeviceServerId(int id){
        preferences.putInt("deviceServerId", id);
        preferences.flushSync();
    }

    public final int getDeviceServerId(){
        return preferences.getInt("deviceServerId", -1);
    }
    public final void setCheckActionTime(String str){
        preferences.putString("CheckActionTime", str);
        preferences.flushSync();
    }

    public final String getCheckActionTime(){
        return preferences.getString("CheckActionTime", "");
    }

    @NotNull
    public final String getDeviceId() {

        return preferences.getString("device_id", "");
    }

    public final void setLastCheckOutDate(long date) {

        preferences.putLong("lastCheckOutDate", date);
        preferences.flushSync();
    }

    public final long getLastCheckOutDate() {
        return preferences.getLong("lastCheckOutDate", System.currentTimeMillis());
    }

    public final void setIsCheckedIn(boolean boool) {

        preferences.putBoolean("is_checked", boool);

        preferences.flushSync();
    }

    public final boolean getIsCheckedIn() {
        return preferences.getBoolean("is_checked", false);
    }

    public final void setIsCheckOutHit(boolean boool) {

        preferences.putBoolean("is_checkout_hit", boool);

        preferences.flushSync();
    }

    public final boolean getIsCheckOutHit() {
        return preferences.getBoolean("is_checkout_hit", false);
    }

    public final String getFcmToken() {
        return preferences.getString("get_FCM_TOKEN", "");
    }

}


