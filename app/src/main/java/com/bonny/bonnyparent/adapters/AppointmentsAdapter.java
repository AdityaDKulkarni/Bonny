package com.bonny.bonnyparent.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.models.AppointmentModel;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AppointmentModel> appointmentModels;

    public AppointmentsAdapter(Context context, ArrayList<AppointmentModel> appointmentModels) {
        this.context = context;
        this.appointmentModels = appointmentModels;
    }

    @Override
    public AppointmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_row_layout, parent, false);

        return new AppointmentsAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(AppointmentsAdapter.ViewHolder holder, int position) {
        try {
            holder.tvPlace.setText(appointmentModels.get(position).getAdministered_at().getName());
            holder.tvTime.setText(appointmentModels.get(position).getAdministered_on());
            holder.tvStaus.setText(appointmentModels.get(position).getStatus());
            switch (appointmentModels.get(position).getStatus().toLowerCase()) {
                case "completed":
                    holder.tvStaus.setBackground(context.getResources().getDrawable(R.drawable.eclipse_green));
                    break;
                case "scheduled":
                    holder.tvStaus.setBackground(context.getResources().getDrawable(R.drawable.eclipse_orange));
                    break;
                case "partial":
                    holder.tvStaus.setBackground(context.getResources().getDrawable(R.drawable.eclipse_orange));
                    break;
                case "cancelled":
                    holder.tvStaus.setBackground(context.getResources().getDrawable(R.drawable.eclipse_red));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appointmentModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlace, tvStaus, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvPlace = itemView.findViewById(R.id.tv_place);
            tvTime = itemView.findViewById(R.id.tv_date);
            tvStaus = itemView.findViewById(R.id.tv_status);
        }
    }
}
