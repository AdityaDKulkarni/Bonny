package com.bonny.bonnyparent.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bonny.bonnyparent.models.BabyModel;
import com.bonny.bonnyparent.ui.LoginActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

public class LoginSessionManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    String PREFERENCE_NAME = "Login Preference";
    String IS_LOGGED_IN = "IsLoggedIn";
    String IS_FIRST_TIME_LAUNCHED = "isFirstTimeLaunched";
    public String FCM_KEY = "fcm_key";

    public LoginSessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        editor = preferences.edit();
    }


    public void setFCM_KEY(String key) {
        editor.putString(FCM_KEY, key);
        editor.commit();
    }

    public void createLoginSession(String username, String key) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString("username", username);
        editor.putString("key", key);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put("username", preferences.getString("username", null));
        user.put("key", preferences.getString("key", null));
        user.put("fcm_key", preferences.getString("fcm_key", null));
        return user;
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){
        editor.putBoolean(IS_FIRST_TIME_LAUNCHED, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCHED, true);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void logOutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
