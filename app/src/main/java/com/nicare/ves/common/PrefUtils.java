package com.nicare.ves.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nicare.ves.ui.preference.PrefFragment;


public class PrefUtils {

    private static final String SHARED_PREF_NAME = "NiCare";
    private static final String MIN_VERSION_CODE = "MINVERCODE";
    private static final String ACTIVATION = "Activated";
    private static final String PWD = "pass";
    private static final String USER = "user";

    private static final String WARD = "ward";
    private static final String LGA = "lga";
    private static final String TOKEN = "token";
    private static final String SYNCDATE = "syncdate";
    private static final String SYNCABLE = "syncable";
    private static final String ACCOUNT_DISABLE = "account_disable";
    private static final String DEVICE_DISABLE = "device_disable";
    private static final String ACCOUNT_SUSPEND = "account_suspend";
    private static final String DEVICE_SUSPEND = "device_suspend";
    private static final String EO_NAME = "eo_name";
    private static final String EO_PHONE = "eo_phone";
    private static final String LOGIN = "login";
    private static final String PIN = "pin";
    private static final String SQUESTION = "question";
    private static final String SANSWER = "answer";
    private static final String VERSION_NAME = "version_name";
    private static final String LATEST_APP_VERSION_CODE = "app_version_code";
    private static final String LATEST_APP_VERSION_DOWNLOADED = "app_version_dl";
    private static final String VERSION_DL_URL = "version_link";
    private static final String SUSPENDED_DEACTIVATED = "sus_deactivate";
    private static final String XCVBN = "YOUR-EncryptedKeysSharedPreferences";
    private static final String DEVICE_ID = "device_id";
    private static final String CAP = "cap";
    private static final String INSTANT_ID = "instantId";
    private static final String ENROLMENT_LIMIT = "limit";
    private static final String LGA_ENROLMENT_LIMIT = "lgalimit";

    private static Context mContext;
    private static PrefUtils mInstance;

    public PrefUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized PrefUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefUtils(context);
        }
        return mInstance;
    }

    public void setActivated(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ACTIVATION, activate);
        editor.apply();
    }

    public boolean isActivated() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(ACTIVATION, false);
    }

    public void setLogin(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGIN, activate);
        editor.apply();
    }

    public boolean isLogin() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(LOGIN, false);
    }


    public void setAccountDeactivated(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ACCOUNT_DISABLE, activate);
        editor.apply();
    }

    public boolean isAccountDeactivated() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(ACCOUNT_DISABLE, false);

    }

    public void setAccountSuspended(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ACCOUNT_SUSPEND, activate);
        editor.apply();
    }

    public boolean isAccountSuspended() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(ACCOUNT_SUSPEND, false);

    }

    public void setDeviceBlocked(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DEVICE_DISABLE, activate);
        editor.apply();
    }

    public boolean isDeviceBlocked() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(DEVICE_DISABLE, false);

    }

    public void setDeviceSuspend(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DEVICE_SUSPEND, activate);
        editor.apply();
    }

    public boolean isDeviceSuspended() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(DEVICE_SUSPEND, false);

    }

    public String getBiometric() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(PrefFragment.PREF_BIOMETRIC, null);
    }

    public void setBiometric(String biometric) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PrefFragment.PREF_BIOMETRIC, biometric);
        editor.apply();
    }
    public int getEnrolmentLimit() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getInt(ENROLMENT_LIMIT, 0);
    }

    public void setEnrolmentLimit(int limit) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ENROLMENT_LIMIT, limit);
        editor.apply();
    }
    public int getLGAEnrolmentLimit() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getInt(LGA_ENROLMENT_LIMIT, 0);
    }

    public void setLGAEnrolmentLimit(int limit) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LGA_ENROLMENT_LIMIT, limit);
        editor.apply();
    }

    public String getSyncType() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(PrefFragment.PREF_SYNC_TYPE, null);
    }

    public void setUser(String user) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER, user);
        editor.apply();
    }

    public void setPWD(String pass) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PWD, pass);
        editor.apply();
    }

    public String getPwd() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PWD, null);
    }

    public void setInstantId(int id) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(INSTANT_ID, id);
        editor.apply();
    }

    public int getInstantId() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(INSTANT_ID, 0);
    }

    public String getUser() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER, null);
    }


    public void setWard(String ward) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(WARD, ward);
        editor.apply();
    }

    public String getWard() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WARD, null);
    }
    public void setLGA(String lga) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LGA, lga);
        editor.apply();
    }


    public String getLGA() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LGA, null);
    }


    public void setToken(String token) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN, null);
    }

    public void setLastSync(String date) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SYNCDATE, date);
        editor.apply();
    }

    public String getLastSync() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SYNCDATE, null);
    }

    public String getEoName() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(EO_NAME, null);
    }

    public void setEoName(String name) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EO_NAME, name);
        editor.apply();
    }

    public String getEoPhone() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(EO_PHONE, null);
    }

    public void setEoPhone(String phone) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EO_PHONE, phone);
        editor.apply();
    }


    public String getPin() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PIN, null);
    }

    public void setPin(String pin) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PIN, pin);
        editor.apply();
    }

    public int getLatestAppVersionCode() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(LATEST_APP_VERSION_CODE, 0);
    }

    public void setLatestAppVersionCode(int pin) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LATEST_APP_VERSION_CODE, pin);
        editor.apply();
    }

    public int getDownloadedAppVersion() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(LATEST_APP_VERSION_DOWNLOADED, 0);
    }

    public void setLatestAppVersionDL(int downloadedVersion) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LATEST_APP_VERSION_DOWNLOADED, downloadedVersion);
        editor.apply();
    }

    public String getLatestVersionUrl() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(VERSION_DL_URL, null);
    }

    public void setLatestVersionUrl(String url) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSION_DL_URL, url);
        editor.apply();
    }

    public String getLatestVersionName() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(VERSION_NAME, null);
    }

    public void setLatestVersionName(String url) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSION_NAME, url);
        editor.apply();
    }

    public String getDeviceId() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(DEVICE_ID, null);
    }

    public void setDeviceId(String id) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_ID, id);
        editor.apply();
    }

    public String getXCVFRN() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(XCVBN, "");
    }

    public void setXCVFRN(String encrypted) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(XCVBN, encrypted);
        editor.apply();
    }


    public void setSynceable(boolean activate) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SYNCABLE, activate);
        editor.apply();
    }

    public boolean isSynceable() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(SYNCABLE, false);
    }

    public int getMinVersionCode() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(MIN_VERSION_CODE, 1);
    }

    public void setMinVersionCode(int versionCode) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MIN_VERSION_CODE, versionCode);
        editor.apply();
    }


    public String getAddress() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("ADDRESS", null);
    }

    public void setAddress(String address) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ADDRESS", address);
        editor.apply();
    }

    public String getSQuestion() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SQUESTION, null);
    }

    public void setSQuestion(String address) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SQUESTION, address);
        editor.apply();
    }

    public String getSAnswer() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SANSWER, null);
    }

    public void setSAnswer(String answer) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SANSWER, answer);
        editor.apply();
    }


    public void setCap(boolean address) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CAP, address);
        editor.apply();
    }

    public boolean getCap() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(CAP, false);
    }

}
