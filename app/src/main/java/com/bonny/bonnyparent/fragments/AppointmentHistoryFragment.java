package com.bonny.bonnyparent.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.adapters.AppointmentsAdapter;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.listener.RecyclerViewListener;
import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.bonny.bonnyparent.models.AppointmentModel;
//import com.bonny.bonnyparent.models.FormDataHolder;
import com.bonny.bonnyparent.models.FormDataHolder;
import com.bonny.bonnyparent.models.ScheduleLists;
import com.bonny.bonnyparent.ui.AppointmentDetailsActivity;

//import com.bonny.bonnyparent.ui.AppointmentDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppointmentHistoryFragment extends Fragment {

    private View v;
    private ArrayList<AppointmentModel> appointmentModels;
    private RecyclerView recyclerView;
    private String TAG = getClass().getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;

    public AppointmentHistoryFragment() {
        appointmentModels = new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_appointment_history, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipeHistory);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        recyclerView = v.findViewById(R.id.lvAppointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipe();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getHistory();
            }
        });
        return v;
    }

    private void swipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    synchronized private void getHistory() {
        API api = new RetrofitConfig().config();
        Call<List<AppointmentModel>> call = api.getAppointments(new LoginSessionManager(getContext()).getUserDetails().get("key"), ScheduleLists.fullScheduleList.get(0).getBaby());
        call.enqueue(new Callback<List<AppointmentModel>>() {
            @Override
            public void onResponse(Call<List<AppointmentModel>> call, Response<List<AppointmentModel>> response) {
                appointmentModels.clear();
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getBaby() == ScheduleLists.fullScheduleList.get(i).getBaby()) {
                        AppointmentModel model = new AppointmentModel();
                        model.setId(response.body().get(i).getId());
                        model.setBaby(response.body().get(i).getBaby());
                        model.setAdministered_at(response.body().get(i).getAdministered_at());
                        model.setAdministered_on(response.body().get(i).getAdministered_on());
                        model.setStatus(response.body().get(i).getStatus());
                        appointmentModels.add(model);
                    }
                }

                AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(getActivity(), appointmentModels);
                recyclerView.setAdapter(appointmentsAdapter);

                recyclerView.addOnItemTouchListener(new RecyclerViewListener(getActivity(), recyclerView, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FormDataHolder.id = appointmentModels.get(position).getId();
                        Intent intent = new Intent(getActivity(), AppointmentDetailsActivity.class);
                        intent.putExtra("name", appointmentModels.get(position).getAdministered_at().getName());
                        intent.putExtra("address", appointmentModels.get(position).getAdministered_at().getAddress());
                        intent.putExtra("email", appointmentModels.get(position).getAdministered_at().getEmail());
                        intent.putExtra("contact", appointmentModels.get(position).getAdministered_at().getContact());
                        intent.putExtra("date", appointmentModels.get(position).getAdministered_on());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<AppointmentModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        });
    }
}