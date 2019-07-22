package com.bonny.bonnyparent.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.bonny.bonnyparent.R;

public class UpdateProfileActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private String email, username, pk;
    private EditText etUsername, etEmail, etFirstName, etLastName, etAddress, etAadhar, etContact;

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

        pk = getIntent().getExtras().getString("pk");
        email = getIntent().getExtras().getString("email");
        username = getIntent().getExtras().getString("username");

        etEmail.setText(email);
        etUsername.setText(username);
    }
}
