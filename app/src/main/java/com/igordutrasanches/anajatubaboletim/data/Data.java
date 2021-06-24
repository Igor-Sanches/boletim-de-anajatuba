package com.igordutrasanches.anajatubaboletim.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.igordutrasanches.anajatubaboletim.MainActivity;
import com.igordutrasanches.anajatubaboletim.activity.DonateActivity;
import com.igordutrasanches.anajatubaboletim.activity.TimeLineActivity;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.models.Boletim;
import com.igordutrasanches.anajatubaboletim.models.Support;
;
public class Data {
    public static String PREFERENCE_NAME = "Igor_Sanches";
    private static final String PREFERENCE_FILE_NAME = "Picadas_db";
    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences getmSharedPreferencesEditor(Context context){
        if(mSharedPreferences ==null){
            mSharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        }
        return mSharedPreferences;
    }

    public static User getUser(Context context){
        User user = new User();
        user.setUid(getUid(context));
        user.setEmail(getEmail(context));
        user.setName(getName(context));
        user.setDonate(isDonate(context));
        if(getPhoto(context)!= null){

            user.setAvatar(getPhoto(context));
        }
        return user;
    }

    public static void setUser(User user, Context context){
        setEmail(user.getEmail(), context);
        setName(user.getName(), context);
        if(user.getAvatar() != null){
            setPhoto(user.getAvatar(), context);
        }
        setDonate(user.isDonate(), context);
    }

    public static String getUid(Context context){
        return getmSharedPreferencesEditor(context).getString("Uid", null);
    }

    public static void setUid(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("Uid", value);
        editor.commit();
    }

    public static String getPhoto(Context context){
        return getmSharedPreferencesEditor(context).getString(Conta.AVATAR, null);
    }

    public static void setPhoto(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString(Conta.AVATAR, value);
        editor.commit();
    }

    public static String getEmail(Context context){
        return getmSharedPreferencesEditor(context).getString(Conta.EMAIL, null);
    }

    public static void setEmail(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString(Conta.EMAIL, value);
        editor.commit();
    }

    public static String getName(Context context){
        return getmSharedPreferencesEditor(context).getString(Conta.NAME, null);
    }

    public static void setName(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString(Conta.NAME, value);
        editor.commit();
    }

    public static boolean isCheckedDialogDonate(Context context){
        return getmSharedPreferencesEditor(context).getBoolean("CheckedDialogDonate", false);
    }

    public static void setCheckedDialogDonate(boolean value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putBoolean("CheckedDialogDonate", value);
        editor.commit();
    }

    public static void setValue(String key, String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDataUpate(Context context) {
        return getmSharedPreferencesEditor(context).getString("update", "");
    }

    public static void setDataUpate(String value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("update", value);
        editor.commit();
    }

    public static boolean isDonate(Context context) {
        return getmSharedPreferencesEditor(context).getBoolean("ValueDonate", false);
    }

    public static void setDonate(boolean value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putBoolean("ValueDonate", value);
        editor.commit();
    }

    public static void setSortingMessageApoio(int value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putInt("SortingMessageApoio", value);
        editor.commit();
    }

    public static int getSortingMessageApoio(Context context) {
        return getmSharedPreferencesEditor(context).getInt("SortingMessageApoio", 0);
    }

    public static void setSortingLocalidade(int value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putInt("SortingLocalidade", value);
        editor.commit();
    }

    public static int getSortingLocalidade(Context context) {
        return getmSharedPreferencesEditor(context).getInt("SortingLocalidade", 0);
    }

    public static void setSupport(Support value, Context context) {
        setEmailSupport(value.getEmail(), context);
        setFacebookSupport(value.getFacebook(), context);
        setPhoneCallSupport(value.getPhoneCall(), context);
        setTwitterSupport(value.getTwitter(), context);
        setWhatsappSupport(value.getWhatsapp(), context);
    }

    private static void setWhatsappSupport(String whatsapp, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("whatsapp", whatsapp);
        editor.commit();
    }

    public static String getWhatsappSupport(Context context) {
        return getmSharedPreferencesEditor(context).getString("whatsapp", "https://api.whatsapp.com/send?phone=5598985766514&text=Ol%C3%A1");
    }

    private static void setTwitterSupport(String twitter, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("twitter", twitter);
        editor.commit();
    }

    public static String getTwitterSupport(Context context) {
        return getmSharedPreferencesEditor(context).getString("twitter", "http://www.twitter.com/igordutra2014");
    }

    private static void setPhoneCallSupport(String phoneCall, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("phoneCall", phoneCall);
        editor.commit();
    }

    public static String getPhoneCallSupport(Context context) {
        return getmSharedPreferencesEditor(context).getString("phoneCall", "tel:98985766514");
    }

    private static void setFacebookSupport(String facebook, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("facebook", facebook);
        editor.commit();
    }

    public static String getFacebookSupport(Context context) {
        return getmSharedPreferencesEditor(context).getString("facebook", "http://www.facebook.com/igor.dutra.3557");
    }

    private static void setEmailSupport(String email, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putString("email", email);
        editor.commit();
    }

    public static String getEmailSupport(Context context) {
        return getmSharedPreferencesEditor(context).getString("email", "igordsanches2014@gmail.com");
    }

    public static void removeValue(Context context){
        remove("SortingLocalidade", context);
        remove("SortingMessageApoio", context);
        remove("ValueDonate", context);
        remove("update", context);
        remove("CheckedDialogDonate", context);
        remove(Conta.NAME, context);
        remove(Conta.EMAIL, context);
        remove(Conta.AVATAR, context);
        remove("Uid", context);
    }

    private static void remove(String key, Context context){
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean isShowDialogDonate(Context context) {
        return getmSharedPreferencesEditor(context).getBoolean("ShowDialogDonate", false);
    }

    public static void setShowDialogDonate(boolean value, Context context) {
        SharedPreferences.Editor editor = getmSharedPreferencesEditor(context).edit();
        editor.putBoolean("ShowDialogDonate", value);
        editor.commit();
    }

}
