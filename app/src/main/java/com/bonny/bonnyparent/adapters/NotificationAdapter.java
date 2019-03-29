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
import com.bonny.bonnyparent.models.NotificationModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context context;
    private ArrayList<NotificationModel> notificationModels;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNotifTitle, tvNotifBody, tvNotifTime, tvNotifType;
        public ViewHolder(View itemView) {
            super(itemView);

            tvNotifType = itemView.findViewById(R.id.tvType);
            tvNotifTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvNotifTitle.setSelected(true);
            tvNotifBody = itemView.findViewById(R.id.tvNotifBody);
            tvNotifTime = itemView.findViewById(R.id.tvNotifTime);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        void bindView(int position){
            if(!notificationModels.get(position).isStatus()){
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }else{
                itemView.setBackgroundColor(context.getResources().getColor(R.color.grey));
            }

            if(notificationModels.get(position).getNotif_type().equalsIgnoreCase("success")){
                tvNotifType.setBackground(context.getResources().getDrawable(R.drawable.shape_notif_type_success));
                tvNotifType.setText(R.string.success);
            }

            if(notificationModels.get(position).getNotif_type().equalsIgnoreCase("info")){
                tvNotifType.setBackground(context.getResources().getDrawable(R.drawable.shape_notif_type_info));
                tvNotifType.setText(R.string.info);
            }

            if(notificationModels.get(position).getNotif_type().equalsIgnoreCase("error")){
                tvNotifType.setBackground(context.getResources().getDrawable(R.drawable.shape_notif_type_error));
                tvNotifType.setText(R.string.error);
            }

            tvNotifTitle.setText(notificationModels.get(position).getTitle());
            tvNotifBody.setText(notificationModels.get(position).getBody());
            tvNotifTime.setText(notificationModels.get(position).getNotif_time());
        }
    }
}
