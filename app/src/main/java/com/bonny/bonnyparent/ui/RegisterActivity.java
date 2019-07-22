package com.bonny.bonnyparent.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.config.ProgressDialogConfig;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.bonny.bonnyparent.models.TokenModel;
import com.bonny.bonnyparent.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private EditText etUsername, etEmail, etPass1, etPass2;
    private Button btnRegister;
    private View llRegister;
    private ArrayList<UserModel> existingUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(getString(R.string.register));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initui();

        getExistingUsers();
    }

    private void getExistingUsers() {
        final ProgressDialog progressDialog = ProgressDialogConfig.progressDialog(RegisterActivity.this,
                getString(R.string.please_wait), false);
        progressDialog.show();
        API api = RetrofitConfig.config();
        Call<List<UserModel>> call = api.getExistingUsers();

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200) {
                    existingUsers.addAll(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Snackbar.make(llRegister, getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG);
            }
        });
    }

    private void initui() {
        llRegister = findViewById(R.id.llRegister);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPass1 = findViewById(R.id.etPassword1);
        etPass2 = findViewById(R.id.etPassword2);
        btnRegister = findViewById(R.id.btnRegister);
        existingUsers = new ArrayList<>();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError(getString(R.string.cannot_be_empty));
                } else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError(getString(R.string.cannot_be_empty));
                } else if (etPass1.getText().toString().isEmpty()) {
                    etPass1.setError(getString(R.string.cannot_be_empty));
                } else if (etPass2.getText().toString().isEmpty()) {
                    etPass2.setError(getString(R.string.cannot_be_empty));
                } else if (etPass1.getText().toString().length() < 8) {
                    etPass1.setError(getString(R.string.must_contain_8_characters));
                } else if (etPass2.getText().toString().length() < 8) {
                    etPass2.setError(getString(R.string.must_contain_8_characters));
                } else if (!etPass1.getText().toString().equals(etPass2.getText().toString())) {
                    etPass2.setError(getString(R.string.passwords_do_not_match));
                } else if (doesUserExist(etUsername.getText().toString())) {
                    etUsername.setError(getString(R.string.username_already_in_use));
                } else if (doesUserExist(etEmail.getText().toString())) {
                    etEmail.setError(getString(R.string.email_already_in_use));
                } else {
                    createUser();
                }


            }
        });
    }

    private void createUser() {
        final ProgressDialog progressDialog = ProgressDialogConfig.progressDialog(RegisterActivity.this,
                getString(R.string.please_wait), false);
        progressDialog.show();
        API api = RetrofitConfig.config();
        Call<TokenModel> call = api.register(etUsername.getText().toString(), etEmail.getText().toString(),
                etPass1.getText().toString(), etPass2.getText().toString());

        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 201) {
                    TokenModel tokenModel = new TokenModel();
                    tokenModel.setKey(response.body().getKey());
                    Snackbar.make(llRegister, getString(R.string.user_registered), Snackbar.LENGTH_LONG);
                    getUser("Token " + tokenModel.getKey(), progressDialog, llRegister);
                }

            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Snackbar.make(llRegister, t.getMessage(), Snackbar.LENGTH_LONG);
            }
        });
    }

    private boolean doesUserExist(String param) {
        Log.e(TAG, "doesUserExist: " + existingUsers.size() + "");
        for (int i = 0; i < existingUsers.size(); i++) {
            if (param.equals(existingUsers.get(i).getEmail()) || param.equals(existingUsers.get(i).getUsername())) {
                return true;
            }
        }

        return false;
    }

    private void getUser(final String key, final ProgressDialog progressDialog, final View view) {
        API api = new RetrofitConfig().config();
        Call<UserModel> call = api.getUser(key);
        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {

            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                switch (response.code()) {
                    case 200:
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        int pk = response.body().getPk();
                        String username = response.body().getUsername();
                        String email = response.body().getEmail();
                        String first_name = response.body().getFirst_name();
                        String last_name = response.body().getLast_name();

                        Intent intent = new Intent(RegisterActivity.this, UpdateProfileActivity.class);
                        intent.putExtra("pk", String.valueOf(pk));
                        intent.putExtra("username", username);
                        intent.putExtra("email", email);

                        new LoginSessionManager(RegisterActivity.this).createLoginSession(username, key);
                        startActivity(intent);
                        break;
                    default:
                        Snackbar.make(view, getString(R.string.unable_to_fetch_profile), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }
    }
}
