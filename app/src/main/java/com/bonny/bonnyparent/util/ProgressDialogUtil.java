package com.bonny.bonnyparent.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.bonny.bonnyparent.models.BabyModel;

import java.util.List;

import retrofit2.Callback;

/**
 * @author Aditya Kulkarni
 */

public class ProgressDialogUtil {

    public static ProgressDialog progressDialog(Context context, String message, boolean isCancellable){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancellable);
        progressDialog.setCanceledOnTouchOutside(isCancellable);
        return progressDialog;
    }
}
