package com.guanzhuli.zestate.controller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Guanzhu Li on 1/23/2017.
 */
public class SPManipulation {
    public static final String PREFS_NAME = "USER_INFO";
    public static final String PREFS_KEY_USERID = "USERID";
    private static SPManipulation mInstance = null;
    Context mContext;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public static SPManipulation getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SPManipulation(context);
        }
        return mInstance;
    }
    public boolean hasUserLoggedIn() {
        return settings.contains(PREFS_KEY_USERID);
    }
    private SPManipulation(Context context) {
        this.mContext = context;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setUserID(String text) {
        editor = settings.edit(); //2
        editor.putString(PREFS_KEY_USERID, text); //3
        editor.commit(); //4
    }

    public String getUserID() {
        return settings.getString(PREFS_KEY_USERID, null);
    }

    public void clearSharedPreference() {
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }

}
