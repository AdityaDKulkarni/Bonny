package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonny.bonnyparent.models.BabyModel;
import com.bonny.bonnyphc.R;
import com.bonny.bonnyphc.models.BabyModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class BabyRecyclerAdapter extends RecyclerView.Adapter<BabyRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BabyModel> babyModels;

    public BabyRecyclerAdapter(Context context, ArrayList<BabyModel> babyModels) {
        this.context = context;
        this.babyModels = babyModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.baby_row_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.tvBabyFirstName.setText(babyModels.get(position).getFirst_name());
            holder.tvBabyPlaceOfBirth.setText(babyModels.get(position).getPlace_of_birth());
            holder.tvBabyBloodGroup.setText(babyModels.get(position).getBlood_group());
            holder.tvWeight.setText(String.valueOf(babyModels.get(position).getWeight()) + " Kg");
            holder.tvBabyBirthDate.setText(babyModels.get(position).getBirth_date());
            if(babyModels.get(position).getGender().equalsIgnoreCase("male")){
                holder.tvGender.setText("M");
            }else{
                holder.tvGender.setText("F");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return babyModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBabyFirstName, tvBabyPlaceOfBirth, tvBabyBloodGroup, tvGender, tvBabyBirthDate,
            tvWeight;
        public ViewHolder(View itemView) {
            super(itemView);

            tvBabyFirstName = itemView.findViewById(R.id.tvBabyFirstName);
            tvBabyPlaceOfBirth= itemView.findViewById(R.id.tvBabyPlaceOfBirth);
            tvBabyPlaceOfBirth.setSelected(true);
            tvBabyBloodGroup = itemView.findViewById(R.id.tvBabyBloodGroup);
            tvGender = itemView.findViewById(R.id.tvBabyGender);
            tvBabyBirthDate = itemView.findViewById(R.id.tvBabyBirthDate);
            tvWeight = itemView.findViewById(R.id.tvBabyWeight);
        }
    }
}
