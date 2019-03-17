package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonny.bonnyphc.R;
import com.bonny.bonnyphc.models.VaccineModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */
public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<VaccineModel> vaccineModels;
    private String TAG = getClass().getSimpleName();
    private Context context;

    public ScheduleRecyclerViewAdapter(Context context, ArrayList<VaccineModel> items) {
        vaccineModels = items;
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
        holder.tvVaccineDate.setText(vaccineModels.get(position).getTentative_date());
        holder.tvVaccineWeek.setText(String.valueOf(vaccineModels.get(position).getWeek()));
        if(vaccineModels.get(position).getStatus().equalsIgnoreCase("pending")){
            holder.tvVaccineStatus.setTextColor(Color.RED);
            holder.tvVaccineStatus.setText(R.string.pending);
        }else if(vaccineModels.get(position).getStatus().equalsIgnoreCase("administered")){
            holder.tvVaccineStatus.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvVaccineStatus.setText(R.string.administered);
        }else if(vaccineModels.get(position).getStatus().equalsIgnoreCase("scheduled")){
            holder.tvVaccineStatus.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.tvVaccineStatus.setText(R.string.scheduled);
        }
    }

    @Override
    public int getItemCount() {
        return vaccineModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvVaccineName, tvVaccineDate, tvVaccineWeek, tvVaccineStatus;
        VaccineModel vaccineModel;

        ViewHolder(View view) {
            super(view);
            tvVaccineName = view.findViewById(R.id.tvVaccineName);
            tvVaccineDate = view.findViewById(R.id.tvVaccineDate);
            tvVaccineWeek = view.findViewById(R.id.tvVaccineWeek);
            tvVaccineStatus = view.findViewById(R.id.tvVaccineStatus);
        }
    }
}
