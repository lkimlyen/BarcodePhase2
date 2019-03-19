package com.demo.architect.data.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.PositionScan;
import com.demo.architect.data.model.UserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by uyminhduc on 4/5/17.
 */

public class SharedPreferenceHelper {
    private static final String PREFERENCE_MAIN = "com.demo.uyminhduc.MAIN";
    private static final String MY_PREFERENCE = "com.demo.uyminhduc.MAIN.MY_PREFERENCE";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER = "USER";
    private static final String POSITION_SCAN_UPDATE = "POSITION_SCAN_UPDATE";
    private static final String DEPARTMENT = "DEPARTMENT";
    private static final String WAS_STARTED = "WAS_STARTED";
    private static final String MACHINE = "MACHINE";
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

    public void pushUserObject(UserEntity object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(USER, json);
        prefsEditor.commit();
    }


    public UserEntity getUserObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER, "");
        UserEntity obj = null;
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson(json, UserEntity.class);
        }
        return obj;
    }

    public void pushListDepartmentObject(List<DepartmentEntity> object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(DEPARTMENT, json);
        prefsEditor.commit();
    }



    public void pushListMachineObject(List<MachineEntity> object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(MACHINE, json);
        prefsEditor.commit();
    }


    public List<DepartmentEntity> getListDepartmentObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(DEPARTMENT, "");
        List<DepartmentEntity> obj = null;
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson(json, new TypeToken<List<DepartmentEntity>>(){}.getType());
        }
        return obj;
    }


    public List<MachineEntity> getListMachineObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(MACHINE, "");
        List<MachineEntity> obj = null;
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson(json, new TypeToken<List<MachineEntity>>(){}.getType());
        }
        return obj;
    }

    public void pushListPositionScanObject(LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>> object) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = "";
        if (object != null) {
            Gson gson = new Gson();
            json = gson.toJson(object);
        }
        prefsEditor.putString(POSITION_SCAN_UPDATE, json);
        prefsEditor.commit();
    }


    public LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>> getListPositionScanObject() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(POSITION_SCAN_UPDATE, "");
        LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>> obj = new LinkedHashMap<>();
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson(json, new TypeToken<LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>>>(){}.getType());
        }
        return obj;
    }

}
