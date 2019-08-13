package com.bonny.bonnyparent.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.config.ProgressDialogConfig;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.models.ParentModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private String email, username, pk, key;
    private EditText etUsername, etEmail, etFirstName, etLastName, etAddress, etAadhar, etContact;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setTitle(getString(R.string.update_profile));

        initui();
    }

    private void initui(){
        etUsername = findViewById(R.id.etUsername);
        etUsername.setClickable(false);
        etUsername.setFocusable(false);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setClickable(false);
        etEmail.setFocusable(false);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etAadhar = findViewById(R.id.etAadharId);
        etContact = findViewById(R.id.etContact);
        btnSave = findViewById(R.id.btnUpdateProfile);

        pk = getIntent().getExtras().getString("pk");
        email = getIntent().getExtras().getString("email");
        username = getIntent().getExtras().getString("username");
        key = getIntent().getExtras().getString("key");

        etEmail.setText(email);
        etUsername.setText(username);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private void updateProfile(){
        if(etFirstName.getText().toString().isEmpty()){
            etFirstName.setError(getString(R.string.cannot_be_empty));
        }else if(etLastName.getText().toString().isEmpty()){
            etLastName.setError(getString(R.string.cannot_be_empty));
        }else if(etAadhar.getText().toString().isEmpty()){
            etAadhar.setError(getString(R.string.cannot_be_empty));
        }else if(etAddress.getText().toString().isEmpty()){
            etAddress.setError(getString(R.string.cannot_be_empty));
        }else if(etContact.getText().toString().isEmpty()){
            etContact.setError(getString(R.string.cannot_be_empty));
        }else{
            final ProgressDialog progressDialog = ProgressDialogConfig.progressDialog(UpdateProfileActivity.this, getString(R.string.please_wait), false);
            API api = RetrofitConfig.config();
            Call<ParentModel> call = api.updateParent(key, Integer.parseInt(pk),
                    email, etFirstName.getText().toString(),
                    etLastName.getText().toString(),
                    etAddress.getText().toString(),
                    etContact.getText().toString(),
                    etAadhar.getText().toString());

            progressDialog.show();

            call.enqueue(new Callback<ParentModel>() {
                @Override
                public void onResponse(Call<ParentModel> call, Response<ParentModel> response) {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if(response.code() == 201){
                        Toast.makeText(UpdateProfileActivity.this, getString(R.string.registration_successfull), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateProfileActivity.this, AllBabiesActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(UpdateProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ParentModel> call, Throwable t) {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
