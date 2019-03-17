package com.bonny.bonnyparent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.models.RecordModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class HistoryRecordsAdapter extends RecyclerView.Adapter<HistoryRecordsAdapter.ViewHolder> {

    private final ArrayList<RecordModel> vaccineModels;
    private String TAG = getClass().getSimpleName();
    private Context context;
    private LayoutInflater layoutInflater;

    public HistoryRecordsAdapter(ArrayList<RecordModel> vaccineModels, Context context) {
        this.vaccineModels = vaccineModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.vaccineModel = vaccineModels.get(position);
        holder.tvVaccineName.setText(vaccineModels.get(position).getVaccine());
        holder.tvVaccineDate.setText(vaccineModels.get(position).getDose());
        holder.tvVaccineWeek.setText(String.valueOf(vaccineModels.get(position).getAmount()));
        if(vaccineModels.get(position).getStatus().equalsIgnoreCase("pending")){
            holder.tvVaccineStatus.setTextColor(Color.RED);
            holder.tvVaccineStatus.setText("Pending");
        }else if(vaccineModels.get(position).getStatus().equalsIgnoreCase("administered")){
            holder.tvVaccineStatus.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvVaccineStatus.setText("Administered");
        }else if(vaccineModels.get(position).getStatus().equalsIgnoreCase("scheduled")){
            holder.tvVaccineStatus.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.tvVaccineStatus.setText("Scheduled");
        }
    }

    @Override
    public int getItemCount() {
        return vaccineModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvVaccineName, tvVaccineDate, tvVaccineWeek, tvVaccineStatus;
        RecordModel vaccineModel;

        ViewHolder(View view) {
            super(view);
            tvVaccineName = view.findViewById(R.id.tvVaccineName);
            tvVaccineDate = view.findViewById(R.id.tvVaccineDate);
            tvVaccineWeek = view.findViewById(R.id.tvVaccineWeek);
            tvVaccineStatus = view.findViewById(R.id.tvVaccineStatus);
        }
    }
}
