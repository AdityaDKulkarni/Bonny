package com.bonny.bonnyparent.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.adapters.BabyRecyclerAdapter;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.comparators.WeekComparator;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.listener.RecyclerViewListener;
import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.bonny.bonnyparent.models.BabyModel;
import com.bonny.bonnyparent.models.FormDataHolder;
import com.bonny.bonnyparent.models.NotificationModel;
import com.bonny.bonnyparent.models.ScheduleLists;
import com.bonny.bonnyparent.models.UserModel;
import com.bonny.bonnyparent.models.VaccineModel;
import com.bonny.bonnyparent.util.ProgressDialogUtil;
import com.bonny.bonnyparent.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBabiesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private ArrayList<NotificationModel> notificationModels;
    private ImageButton imgBtnNotif;
    private Toolbar toolbar;
    private View navHeaderView;
    private TextView tvName, tvEmail;
    private RecyclerView rvBabies;
    private HashMap<String, String> userDetails;
    private String username, email, key, TAG = getClass().getSimpleName();
    private LoginSessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<BabyModel> babyModels;
    private API api;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_babies);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.all_babies));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        api = new RetrofitConfig().config();
        progressDialog = new ProgressDialogUtil().progressDialog(AllBabiesActivity.this,
                "Getting schedule...", false);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        intitUi();
        getAllBabies();
        swipe();

    }

    private void intitUi() {
        imgBtnNotif = toolbar.findViewById(R.id.imgBtnNotif);
        navHeaderView = navigationView.getHeaderView(0);
        tvName = navHeaderView.findViewById(R.id.tvHeaderName);
        tvEmail = navHeaderView.findViewById(R.id.tvHeaderEmail);
        rvBabies = findViewById(R.id.babyDetails);
        rvBabies.setLayoutManager(new LinearLayoutManager(this));
        sessionManager = new LoginSessionManager(AllBabiesActivity.this);
        userDetails = sessionManager.getUserDetails();
        username = userDetails.get("username");
        key = userDetails.get("key");
        tvName.setText(username);


        imgBtnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNotifs();
            }
        });


    }

    private void swipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllBabies();
            }
        });
    }

    private synchronized void getAllBabies() {
        swipeRefreshLayout.setRefreshing(true);

        Call<List<BabyModel>> call = api.getAllBabies(key);
        call.enqueue(new Callback<List<BabyModel>>() {
            @Override
            public void onResponse(Call<List<BabyModel>> call, Response<List<BabyModel>> response) {
                switch (response.code()) {
                    case 200:
                        babyModels = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            BabyModel babyModel = new BabyModel();
                            babyModel.setId(response.body().get(i).getId());
                            babyModel.setWeek(response.body().get(i).getWeek());
                            babyModel.setFirst_name(response.body().get(i).getFirst_name());
                            babyModel.setPlace_of_birth(response.body().get(i).getPlace_of_birth());
                            babyModel.setBlood_group(Utils.getFormattedBloodGroup(response.body().get(i).getBlood_group()));
                            babyModel.setGender(response.body().get(i).getGender());
                            babyModel.setBirth_date(response.body().get(i).getBirth_date());
                            babyModel.setWeight(response.body().get(i).getWeight());
                            babyModels.add(babyModel);
                        }
                        rvBabies.setAdapter(new BabyRecyclerAdapter(AllBabiesActivity.this, babyModels));

                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        rvBabies.addOnItemTouchListener(new RecyclerViewListener(AllBabiesActivity.this,
                                rvBabies, new RecyclerViewListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                FormDataHolder.selectedBabyModel = babyModels.get(position);
                                FormDataHolder.selectedBabyId = babyModels.get(position).getId();
                                getSchedule(babyModels.get(position).getId(), position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
                        rvBabies.setAdapter(new BabyRecyclerAdapter(AllBabiesActivity.this, babyModels));

                        break;
                    case 500:
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(AllBabiesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BabyModel>> call, Throwable t) {

            }

            private void getSchedule(int pk, final int position) {
                final ArrayList<VaccineModel> vaccineModels = new ArrayList<>();
                progressDialog.show();
                Call<List<VaccineModel>> call = api.getSchedule(new LoginSessionManager(AllBabiesActivity.this).getUserDetails().get("key"), pk);
                call.enqueue(new Callback<List<VaccineModel>>() {
                    @Override
                    public void onResponse(Call<List<VaccineModel>> call, Response<List<VaccineModel>> response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        switch (response.code()) {
                            case 200:
                                ScheduleLists.currentWeekVaccineList = new ArrayList<>();
                                ScheduleLists.fullScheduleList = new ArrayList<>();
                                for (int i = 0; i < response.body().size(); i++) {
                                    VaccineModel vaccineModel = new VaccineModel();
                                    vaccineModel.setBaby(response.body().get(i).getBaby());
                                    vaccineModel.setVaccine(response.body().get(i).getVaccine());
                                    vaccineModel.setWeek(response.body().get(i).getWeek());
                                    vaccineModel.setTentative_date(response.body().get(i).getTentative_date());
                                    vaccineModel.setStatus(response.body().get(i).getStatus());
                                    vaccineModels.add(vaccineModel);

                                    if (vaccineModel.getStatus().equalsIgnoreCase("pending")
                                            && vaccineModel.getWeek() == babyModels.get(position).getWeek()) {
                                        ScheduleLists.currentWeekVaccineList.add(vaccineModel);
                                    }
                                }
                                Collections.sort(vaccineModels, new WeekComparator());
                                ScheduleLists.fullScheduleList = vaccineModels;
                                Intent intent = new Intent(AllBabiesActivity.this, BabyDetailsActivity.class);
                                intent.putExtra("babyModel", babyModels.get(position));
                                startActivity(intent);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<VaccineModel>> call, Throwable t) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }


        })

        ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllBabies();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();

        }
    }

    private void getNotifs() {

        Call<List<NotificationModel>> call = api.getNotifs(sessionManager.getUserDetails().get("key"));
        progressDialog.show();
        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.code() == 200) {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    notificationModels = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        if (!response.body().get(i).isStatus()) {
                            imgBtnNotif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_received));
                            break;
                        }
                    }

                    for (int i = 0; i < response.body().size(); i++) {
                        notificationModels.add(response.body().get(i));
                    }

                    Intent intent = new Intent(AllBabiesActivity.this, NotificationActivity.class);
                    intent.putExtra("notifs", notificationModels);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(AllBabiesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Call<UserModel> call = api.logout();
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.body().getDetail().equalsIgnoreCase("Successfully logged out.")) {
                        new LoginSessionManager(AllBabiesActivity.this).logOutUser();
                    } else {
                        Toast.makeText(AllBabiesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(AllBabiesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            });
        }

        if(id == R.id.nav_notification){
            getNotifs();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}