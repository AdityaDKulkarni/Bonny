package com.bonny.bonnyparent.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.config.ProgressDialogConfig;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.bonny.bonnyparent.models.ParentModel;
import com.bonny.bonnyparent.models.TokenModel;
import com.bonny.bonnyparent.models.UserModel;
import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;
    private final String TAG = getClass().getSimpleName();


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginSessionManager sessionManager;
    private Button btnSignUp;
    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        System.exit(0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(getString(R.string.login));
        sessionManager = new LoginSessionManager(LoginActivity.this);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);

        btnSignUp = findViewById(R.id.sign_up_button);
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(mLoginFormView);
        }
    }



    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void login(final View view){
        final ProgressDialog progressDialog = ProgressDialogConfig.progressDialog(LoginActivity.this,"Verifying",false);
        progressDialog.show();
        API api = new RetrofitConfig().config();
        Call<TokenModel> call = api.getToken(mEmailView.getText().toString(),
                mPasswordView.getText().toString());

        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                switch (response.code()) {
                    case 200:
                        getUser("Token " + response.body().getKey(), progressDialog, view);
                        break;
                    case 400:
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Snackbar.make(view, getString(R.string.invalid_credentials), Snackbar.LENGTH_LONG).show();
                        break;
                    default:
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Snackbar.make(view, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Snackbar.make(view, getString(R.string.something_went_wrongg), Snackbar.LENGTH_LONG).show();
            }

        });
    }


    private void sendFCMToken(final View view)
    {

        API api = new RetrofitConfig().config();
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        Call<ResponseBody> call = api.registerFCM(userDetails.get("key"),
                userDetails.get("username"),
                userDetails.get("fcm_key"),
                userDetails.get("username"),
                true,
                "android");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                       Log.e("Token api", "Token sent to server.");
                       break;
                    case 400:

                        Snackbar.make(view, getString(R.string.registration_failed), Snackbar.LENGTH_LONG).show();
                        break;
                    default:
                        Snackbar.make(view, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(view, getString(R.string.something_went_wrongg), Snackbar.LENGTH_LONG).show();
            }

        });
    }

    private void getUser(final String key, final ProgressDialog progressDialog, final View view) {
        API api = new RetrofitConfig().config();
        Call<UserModel> call = api.getUser(key);
        call.enqueue(new Callback<UserModel>() {

        @Override
        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
            switch (response.code()){
                case 200:
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    int pk = response.body().getPk();
                    String username = response.body().getUsername();
                    String email = response.body().getEmail();
                    String first_name = response.body().getFirst_name();
                    String last_name = response.body().getLast_name();

                    Intent intent = new Intent(LoginActivity.this, AllBabiesActivity.class);
                    intent.putExtra("pk", String.valueOf(pk));
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);

                    new LoginSessionManager(LoginActivity.this).createLoginSession(username, key);
                    sendFCMToken(view);
                    startActivity(intent);
                    break;
                default:
                    Snackbar.make(view, getString(R.string.unable_to_fetch_profile), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<UserModel> call, Throwable t) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Snackbar.make(view, getString(R.string.failure), Snackbar.LENGTH_LONG).show();
            }


        });


    }
}

