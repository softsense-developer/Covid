package com.smartsense.covid;

import android.content.Context;
import android.content.SharedPreferences;

import com.smartsense.covid.api.model.User;
import com.smartsense.covid.api.model.UserModel;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // Shared preferences file name
    private static final String PACKAGE_NAME = "com.smartsense.covidapp";

    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String USERNAME = "username";
    private static final String USER_PHONE = "userPhone";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_BIRTHDAY = "userBirthday";
    private static final String USER_IDENTITY = "userIdentity";
    private static final String USER_GENDER = "userGender";
    private static final String USER_TRACKING_TYPE = "userTrackingType";
    private static final String USER_DIAGNOSIS= "userDiagnosis";
    private static final String USER_BLOOD_GROUP= "userBloodGroup";
    private static final String USER_ID = "userID";
    private static final String USER_ROLE = "userRole";
    private static final String DOCTOR_ID = "doctorID";
    private static final String DOCTOR_NAME_SURNAME = "doctorNameSurname";
    private static final String HOSPITAL_NAME = "hospitalName";
    private static final String AUTH_TOKEN = "authToken";
    private static final String USER_PASS = "userPass";
    private static final String IS_LOCATION_CAN_CHANGE = "isLocationCanChange";
    private static final String HOME_LOCATION_LAT = "homeLocationLat";
    private static final String HOME_LOCATION_LONG = "homeLocationLong";
    private static final String EMAIL_VERIFIED = "emailVerified";

    private static final String BAND_MAC = "bandMAC";
    private static final String BAND_NAME = "bandName";
    private static final String TEMP_TIME = "tempTime";
    private static final String HEART_TIME = "heartTime";
    private static final String SPO2_TIME = "spo2Time";
    private static final String TEMP_SENT_LAST = "tempLastTime";
    private static final String HEART_SENT_LAST = "heartLastTime";
    private static final String SPO2_SENT_LAST = "spo2LastTime";
    private static final String QUARANTINE_STATUS = "quarantineStatus";
    private static final String WARNING_DATA = "warningData";
    private static final String WARNING_DATE = "warningDate";
    private static final String WARNING_TYPE = "warningType";

    private static final String SENT_DATA_SERVER = "sentDataServer";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setName(String val) {
        editor.putString(PACKAGE_NAME + "." + NAME, val);
        editor.apply();
    }

    public String getName() {
        return pref.getString(PACKAGE_NAME + "." + NAME, null);
    }

    public void setSurname(String val) {
        editor.putString(PACKAGE_NAME + "." + SURNAME, val);
        editor.apply();
    }

    public String getSurname() {
        return pref.getString(PACKAGE_NAME + "." + SURNAME, null);
    }

    public void setUsername(String val) {
        editor.putString(PACKAGE_NAME + "." + USERNAME, val);
        editor.apply();
    }

    public String getUsername() {
        return pref.getString(PACKAGE_NAME + "." + USERNAME, null);
    }

    public void setUserPhone(String val) {
        editor.putString(PACKAGE_NAME + "." + USER_PHONE, val);
        editor.apply();
    }

    public String getUserPhone() {
        return pref.getString(PACKAGE_NAME + "." + USER_PHONE, null);
    }

    public void setUserEmail(String val) {
        editor.putString(PACKAGE_NAME + "." + USER_EMAIL, val);
        editor.apply();
    }

    public String getUserEmail() {
        return pref.getString(PACKAGE_NAME + "." + USER_EMAIL, null);
    }

    public void setUserPass(String val) {
        editor.putString(PACKAGE_NAME + "." + USER_PASS, val);
        editor.apply();
    }

    public String getUserPass() {
        return pref.getString(PACKAGE_NAME + "." + USER_PASS, null);
    }

    public void setDoctorID(long val) {
        editor.putLong(PACKAGE_NAME + "." + DOCTOR_ID, val);
        editor.apply();
    }

    public long getDoctorID() {
        return pref.getLong(PACKAGE_NAME + "." + DOCTOR_ID, 0);
    }

    public void setDoctorNameSurname(String val) {
        editor.putString(PACKAGE_NAME + "." + DOCTOR_NAME_SURNAME, val);
        editor.apply();
    }

    public String getDoctorNameSurname() {
        return pref.getString(PACKAGE_NAME + "." + DOCTOR_NAME_SURNAME, null);
    }

    public void setHospitalName(String val) {
        editor.putString(PACKAGE_NAME + "." + HOSPITAL_NAME, val);
        editor.apply();
    }

    public String getHospitalName() {
        return pref.getString(PACKAGE_NAME + "." + HOSPITAL_NAME, null);
    }

    public void setUserData(User user) {
        editor.putInt(PACKAGE_NAME + "." + USER_ID, 1);
        setName(user.getName());
        setSurname(user.getSurname());
        setUserPass(user.getPassword());
        setUserEmail(user.getEmail());
        setUserPhone(user.getPhone());
        editor.apply();
    }

    public void setUserNA(UserModel user) {
        editor.putLong(PACKAGE_NAME + "." + USER_ID, 1);
        setName(user.getName());
        setSurname(user.getSurname());
        setUserPass(user.getPassword());
        setUserEmail(user.getEmail());
        editor.apply();
    }

    public void setAuthToken(String val) {
        editor.putString(PACKAGE_NAME + "." + AUTH_TOKEN, val);
        editor.apply();
    }

    public String getAuthToken() {
        return pref.getString(PACKAGE_NAME + "." + AUTH_TOKEN, null);
    }

    public User getUserData() {
        return new User(getUserEmail(), getUserPass(), getName(), getSurname(), getUserPhone(), getDoctorID());
    }

    public void setUserID(long id) {
        editor.putLong(PACKAGE_NAME + "." + USER_ID, id);
        editor.apply();
    }

    public long getID() {
        return pref.getLong(PACKAGE_NAME + "." + USER_ID, -1L);
    }

    public boolean isLoggedIn() {
        return getID() != -1L;
    }

    public void setBandMac(String val) {
        editor.putString(PACKAGE_NAME + "." + BAND_MAC, val);
        editor.apply();
    }

    public String getBandMac() {
        return pref.getString(PACKAGE_NAME + "." + BAND_MAC, null);
    }

    public void setBandName(String val) {
        editor.putString(PACKAGE_NAME + "." + BAND_NAME, val);
        editor.apply();
    }

    public String getBandName() {
        return pref.getString(PACKAGE_NAME + "." + BAND_NAME, null);
    }


    public void setTimeTemp(int val) {
        editor.putInt(PACKAGE_NAME + "." + TEMP_TIME, val);
        editor.apply();
    }

    public int getTimeTemp() { // 15 min = 60*15=900 sec -> 900*1000=900000 millis
        return pref.getInt(PACKAGE_NAME + "." + TEMP_TIME, 5 * 60 * 1000);
    }

    public void setTimeHeart(int val) {
        editor.putInt(PACKAGE_NAME + "." + HEART_TIME, val);
        editor.apply();
    }

    public int getTimeHeart() {
        return pref.getInt(PACKAGE_NAME + "." + HEART_TIME, 5 * 60 * 1000);
    }

    public void setTimeSpO2(int val) {
        editor.putInt(PACKAGE_NAME + "." + SPO2_TIME, val);
        editor.apply();
    }

    public int getTimeSpO2() {
        return pref.getInt(PACKAGE_NAME + "." + SPO2_TIME, 5 * 60 * 1000);
    }

    public void setTempSentLast(long val) {
        editor.putLong(PACKAGE_NAME + "." + TEMP_SENT_LAST, val);
        editor.apply();
    }

    public long getTempSentLast() {
        return pref.getLong(PACKAGE_NAME + "." + TEMP_SENT_LAST, 0);
    }

    public void setSentDataServer(boolean val) {
        editor.putBoolean(PACKAGE_NAME + "." + SENT_DATA_SERVER, val);
        editor.apply();
    }

    public boolean getSentDataServer() {
        return pref.getBoolean(PACKAGE_NAME + "." + SENT_DATA_SERVER, false);
    }

    public void setIsLocationCanChange(boolean val) {
        editor.putBoolean(PACKAGE_NAME + "." + IS_LOCATION_CAN_CHANGE, val);
        editor.apply();
    }

    public boolean getIsLocationCanChange() {
        return pref.getBoolean(PACKAGE_NAME + "." + IS_LOCATION_CAN_CHANGE, true);
    }


    public void setHomeLocationLat(float val) {
        editor.putFloat(PACKAGE_NAME + "." + HOME_LOCATION_LAT, val);
        editor.apply();
    }

    public float getHomeLocationLat() {
        return pref.getFloat(PACKAGE_NAME + "." + HOME_LOCATION_LAT, 0.0f);
    }

    public void setHomeLocationLong(float val) {
        editor.putFloat(PACKAGE_NAME + "." + HOME_LOCATION_LONG, val);
        editor.apply();
    }

    public float getHomeLocationLong() {
        return pref.getFloat(PACKAGE_NAME + "." + HOME_LOCATION_LONG, 0.0f);
    }

    public void setQuarantineStatus(int val) {
        editor.putInt(PACKAGE_NAME + "." + QUARANTINE_STATUS, val);
        editor.apply();
    }

    public int getQuarantineStatus() {
        return pref.getInt(PACKAGE_NAME + "." + QUARANTINE_STATUS, MyConstant.QUARANTINE_HOME);
    }

    public void setWarningData(float val) {
        editor.putFloat(PACKAGE_NAME + "." + WARNING_DATA, val);
        editor.apply();
    }

    public float getWarningData() {
        return pref.getFloat(PACKAGE_NAME + "." + WARNING_DATA, -1.0f);
    }

    public void setWarningDate(long val) {
        editor.putLong(PACKAGE_NAME + "." + WARNING_DATE, val);
        editor.apply();
    }

    public long getWarningDate() {
        return pref.getLong(PACKAGE_NAME + "." + WARNING_DATE, 0);
    }

    public void setWarningType(int val) {
        editor.putInt(PACKAGE_NAME + "." + WARNING_TYPE, val);
        editor.apply();
    }

    public int getWarningType() {
        return pref.getInt(PACKAGE_NAME + "." + WARNING_TYPE, 0);
    }

    public void setEmailVerified(boolean val) {
        editor.putBoolean(PACKAGE_NAME + "." + EMAIL_VERIFIED, val);
        editor.apply();
    }

    public boolean getEmailVerified() {
        return pref.getBoolean(PACKAGE_NAME + "." + EMAIL_VERIFIED, false);
    }

    public void setUserBirthday(long val) {
        editor.putLong(PACKAGE_NAME + "." + USER_BIRTHDAY, val);
        editor.apply();
    }

    public long getUserBirthday() {
        return pref.getLong(PACKAGE_NAME + "." + USER_BIRTHDAY, -1);
    }

    public void setUserIdentity(String val) {
        editor.putString(PACKAGE_NAME + "." + USER_IDENTITY, val);
        editor.apply();
    }

    public String getUserIdentity() {
        return pref.getString(PACKAGE_NAME + "." + USER_IDENTITY, null);
    }

    public void setUserTrackingType(int val) {
        editor.putInt(PACKAGE_NAME + "." + USER_TRACKING_TYPE, val);
        editor.apply();
    }

    public int getUserTrackingType() {
        return pref.getInt(PACKAGE_NAME + "." + USER_TRACKING_TYPE, MyConstant.TRACKING_OTHER);
    }

    public void setUserGender(int val) {
        editor.putInt(PACKAGE_NAME + "." + USER_GENDER, val);
        editor.apply();
    }

    public int getUserGender() {
        return pref.getInt(PACKAGE_NAME + "." + USER_GENDER, 0);
    }

    public void setUserDiagnosis(String val) {
        editor.putString(PACKAGE_NAME + "." + USER_DIAGNOSIS, val);
        editor.apply();
    }

    public String getUserDiagnosis() {
        return pref.getString(PACKAGE_NAME + "." + USER_DIAGNOSIS, null);
    }

    public void setUserRole(int val) {
        editor.putInt(PACKAGE_NAME + "." + USER_ROLE, val);
        editor.apply();
    }

    public int getUserRole() {
        return pref.getInt(PACKAGE_NAME + "." + USER_ROLE, MyConstant.PATIENT_ROLE);
    }


    public void setUserBloodGroup(String val) {
        editor.putString(PACKAGE_NAME + "." + USER_BLOOD_GROUP, val);
        editor.apply();
    }

    public String getUserBloodGroup() {
        return pref.getString(PACKAGE_NAME + "." + USER_BLOOD_GROUP, null);
    }




    public void clear() {
        editor.clear();
        editor.apply();
    }


}
