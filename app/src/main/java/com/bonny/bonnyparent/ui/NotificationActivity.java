package com.bonny.bonnyparent.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.adapters.NotificationAdapter;
import com.bonny.bonnyparent.models.NotificationModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rvNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle(getString(R.string.notifications));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvNotif = findViewById(R.id.rvNotif);
        rvNotif.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));

        if(getIntent().hasExtra("notifs")){
            ArrayList<NotificationModel> notificationModels = (ArrayList<NotificationModel>) getIntent().getExtras().get("notifs");
            Log.e("@@@", notificationModels.size() +"");
            rvNotif.setAdapter(new NotificationAdapter(NotificationActivity.this, notificationModels));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
