package com.bonny.bonnyparent.config;

import android.util.Log;

import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FCMInstaceService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token", token);
        LoginSessionManager sessionManager=new LoginSessionManager(this);
        sessionManager.setFCM_KEY(token);
    }
}
