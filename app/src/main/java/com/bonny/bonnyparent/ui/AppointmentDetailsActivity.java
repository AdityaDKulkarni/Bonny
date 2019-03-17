package com.bonny.bonnyparent.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.adapters.HistoryRecordsAdapter;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.models.FormDataHolder;
import com.bonny.bonnyparent.models.RecordModel;
import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.bonny.bonnyparent.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetailsActivity extends AppCompatActivity {

    private TextView tvPHCName, tvPHCAddress, tvPHCContact, tvPHCEmail, tvPHCDate;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(getString(R.string.appointment_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initui();
    }

    private void initui(){
        tvPHCName = findViewById(R.id.tvPHCName);
        tvPHCAddress = findViewById(R.id.tvPHCAddress);
        tvPHCContact = findViewById(R.id.tvPHCContact);
        tvPHCEmail = findViewById(R.id.tvPHCEmail);
        tvPHCDate = findViewById(R.id.tvPHCDate);

        if(getIntent().hasExtra("name")){
            tvPHCName.setText(getIntent().getStringExtra("name"));
        }
        if(getIntent().hasExtra("address")){
            tvPHCAddress.setText(getIntent().getStringExtra("address"));
        }
        if(getIntent().hasExtra("email")){
            tvPHCEmail.setText(getIntent().getStringExtra("email"));
        }
        if(getIntent().hasExtra("contact")){
            tvPHCContact.setText(getIntent().getStringExtra("contact"));
        }
        if(getIntent().hasExtra("date")){
            tvPHCDate.setText(getIntent().getStringExtra("date"));
        }

        recyclerView = findViewById(R.id.rvRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getHistory();
    }

    private void getHistory(){
        final ProgressDialog progressDialog = ProgressDialogUtil.progressDialog(AppointmentDetailsActivity.this,
                getString(R.string.please_wait), false);
        progressDialog.show();
        API api = new RetrofitConfig().config();
        Call<List<RecordModel>> call = api.getRecords(new LoginSessionManager(AppointmentDetailsActivity.this).getUserDetails().get("key"), FormDataHolder.id);
        final ArrayList<RecordModel> recordModels = new ArrayList();
        call.enqueue(new Callback<List<RecordModel>>() {
            @Override
            public void onResponse(Call<List<RecordModel>> call, Response<List<RecordModel>> response) {
                for(int i = 0; i < response.body().size(); i++){
                    recordModels.add(response.body().get(i));
                }

                recyclerView.setAdapter(new HistoryRecordsAdapter(recordModels, AppointmentDetailsActivity.this));
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<RecordModel>> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
