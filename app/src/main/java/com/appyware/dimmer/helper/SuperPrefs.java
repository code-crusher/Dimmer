package com.appyware.dimmer.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by
 * --Vatsal Bajpai on
 * --22/08/16 at
 * --2:01 PM
 */

public class SuperPrefs implements Constants {

    SharedPreferences sharedPreferences;

    /**
     * constructor
     *
     * @param context The context of the activity
     */
    public SuperPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(TAG_SHARED_PREF, Context.MODE_PRIVATE);
    }

    /**
     * static factory method
     *
     * @param context The context of the activity
     * @return The SuperPrefs object
     */
    public static SuperPrefs newInstance(Context context) {
        return new SuperPrefs(context);
    }

    /**
     * set string in sharedPreferences
     *
     * @param KEY   The unique key ID
     * @param value The string value to be stored
     */
    public void setString(String KEY, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, value);
        editor.apply();
    }

    /**
     * get string from sharedPreferences
     *
     * @param KEY The unique key ID
     * @return The stored string
     */
    public String getString(String KEY) {
        return sharedPreferences.getString(KEY, null);
    }

    /**
     * set int in sharedPreferences
     *
     * @param KEY   The unique key ID
     * @param value The int value to be stored
     */
    public void setInt(String KEY, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY, value);
        editor.apply();
    }

    /**
     * get int from sharedPreferences
     *
     * @param KEY The unique key ID
     * @return The stored int
     */
    public int getInt(String KEY, int defaultValue) {
        if (sharedPreferences.getInt(KEY, -1) == -1)
            return defaultValue;
        return sharedPreferences.getInt(KEY, -1);
    }

    /**
     * set boolean in sharedPreferences
     *
     * @param KEY   The unique key ID
     * @param value The boolean value to be stored
     */
    public void setBool(String KEY, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY, value);
        editor.apply();
    }

    /**
     * set reverse boolean in sharedPreferences
     *
     * @param KEY The unique key ID
     */
    public void setBoolNot(String KEY) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY, !sharedPreferences.getBoolean(KEY, false));
        editor.apply();
    }

    /**
     * get boolean from sharedPreferences
     *
     * @param KEY The unique key ID
     * @return The stored boolean
     */
    public Boolean getBool(String KEY) {
        return sharedPreferences.getBoolean(KEY, false);
    }
}
