package com.bonny.bonnyparent.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.api.API;
import com.bonny.bonnyparent.config.RetrofitConfig;
import com.bonny.bonnyparent.managers.LoginSessionManager;
import com.bonny.bonnyparent.models.AppointmentModel;
import com.bonny.bonnyparent.models.BabyModel;
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
    private ListView listView;
    private List<String> strings;
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
        listView = v.findViewById(R.id.lvAppointment);
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
        strings = new ArrayList();
        API api = new RetrofitConfig().config();
        Call<List<AppointmentModel>> call = api.getAppointments(new LoginSessionManager(getContext()).getUserDetails().get("key"), ScheduleLists.fullScheduleList.get(0).getBaby());
        call.enqueue(new Callback<List<AppointmentModel>>() {
            @Override
            public void onResponse(Call<List<AppointmentModel>> call, Response<List<AppointmentModel>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getBaby() == ScheduleLists.fullScheduleList.get(i).getBaby()) {
                        AppointmentModel model = new AppointmentModel();
                        model.setId(response.body().get(i).getId());
                        model.setBaby(response.body().get(i).getBaby());
                        model.setAdministered_at(response.body().get(i).getAdministered_at());
                        model.setAdministered_on(response.body().get(i).getAdministered_on());
                        appointmentModels.add(model);
                        strings.add(model.getAdministered_at().getName() + " - " + model.getAdministered_on());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, strings);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        FormDataHolder.id = appointmentModels.get(i).getId();
                        Intent intent = new Intent(getActivity(),AppointmentDetailsActivity.class);
                        intent.putExtra("name",appointmentModels.get(i).getAdministered_at().getName());
                        intent.putExtra("address",appointmentModels.get(i).getAdministered_at().getAddress());
                        intent.putExtra("email",appointmentModels.get(i).getAdministered_at().getEmail());
                        intent.putExtra("contact",appointmentModels.get(i).getAdministered_at().getContact());
                        intent.putExtra("date",appointmentModels.get(i).getAdministered_on());
                        startActivity(intent);
                    }
                });

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