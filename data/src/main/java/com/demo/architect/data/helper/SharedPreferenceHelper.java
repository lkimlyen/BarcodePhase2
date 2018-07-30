package com.demo.architect.data.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by uyminhduc on 4/5/17.
 */

public class SharedPreferenceHelper {
    private static final String PREFERENCE_MAIN = "com.demo.uyminhduc.MAIN";
    private static final String MY_PREFERENCE = "com.demo.uyminhduc.MAIN.MY_PREFERENCE";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER = "USER";
    private static final String CONTACT = "CONTACT";
    private static final String SAVE_LOGIN = "SAVE_LOGIN";
    private static final String WAS_STARTED = "WAS_STARTED";
    private SharedPreferences sharedPreferences;

    private static SharedPreferenceHelper _instance;

    public static SharedPreferenceHelper getInstance(Context context) {
        if (_instance == null) {
            _instance = new SharedPreferenceHelper(context);
        }
        return _instance;
    }

    public SharedPreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }

    public void pushString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public void pushBoolean(String key, boolean bool) {
        sharedPreferences.edit().putBoolean(key, bool).apply();
    }

    public boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    public void pushWasStartedBoolean(boolean bool) {
        sharedPreferences.edit().putBoolean(WAS_STARTED, bool).apply();
    }

    public boolean wasStartedBoolean(boolean def) {
        return sharedPreferences.getBoolean(WAS_STARTED, def);
    }

    public boolean existKey(String key) {
        return sharedPreferences.contains(key);
    }

//    public void pushUserObject(UserEntity object) {
//        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//        String json = "";
//        if (object != null) {
//            Gson gson = new Gson();
//            json = gson.toJson(object);
//        }
//        prefsEditor.putString(USER, json);
//        prefsEditor.commit();
//    }
//
//    public void pushListContactObject(LinkContactEntity object) {
//        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//        String json = "";
//        if (object != null) {
//            Gson gson = new Gson();
//            json = gson.toJson(object);
//        }
//        prefsEditor.putString(CONTACT, json);
//        prefsEditor.commit();
//    }
//
//    public UserEntity getUserObject() {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(USER, "");
//        UserEntity obj = null;
//        if (!TextUtils.isEmpty(json)) {
//            obj = gson.fromJson(json, UserEntity.class);
//        }
//        return obj;
//    }
//    public LinkContactEntity getListContactObject() {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(CONTACT, "");
//        LinkContactEntity obj = null;
//        if (!TextUtils.isEmpty(json)) {
//            obj = gson.fromJson(json, LinkContactEntity.class);
//        }
//        return obj;
//    }
//
//    public UserLogin getUserLoginObject() {
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(SAVE_LOGIN, "");
//        UserLogin obj = null;
//        if (!TextUtils.isEmpty(json)) {
//            obj = gson.fromJson(json, UserLogin.class);
//        }
//        return obj;
//    }
//
//    public void pushUserLoginObject(UserLogin object) {
//        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//        String json = "";
//        if (object != null) {
//            Gson gson = new Gson();
//            json = gson.toJson(object);
//        }
//        prefsEditor.putString(SAVE_LOGIN, json);
//        prefsEditor.commit();
//    }
}
