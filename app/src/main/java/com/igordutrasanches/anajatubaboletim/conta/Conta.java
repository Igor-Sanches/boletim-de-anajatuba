package com.igordutrasanches.anajatubaboletim.conta;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.igordutrasanches.anajatubaboletim.data.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class Conta {
    private static final String ACCOUNT_ACTIVE = "account_active";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String FIREBASE_UID = "firebase_uid";
    private static final String PREF_USER_PROFILE = "user_profile";
    public static final String AVATAR = "avatar";
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static User getUserProfile(Context context) {
        return Data.getUser(context);
    }

    public static void setUserProfile(User user, Context context){
        Data.setUser(user, context);

    }

    public static void setValue(String key, String value, Context context){
        Data.setValue(key, value, context);
    }

    public static void saveUID(String uid, Context context) {

            Data.setUid(uid, context);

    }

    public static String getUID(Context context) {

            return Data.getUid(context);

    }

}
