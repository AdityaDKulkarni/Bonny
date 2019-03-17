package com.bonny.bonnyparent.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bonny.bonnyparent.R;
import com.bonny.bonnyparent.adapters.ScheduleRecyclerViewAdapter;
//import com.bonny.bonnyparent.models.FormDataHolder;
import com.bonny.bonnyparent.models.ScheduleLists;

/**
 * @author Aditya Kulkarni
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private String TAG = getClass().getSimpleName();
    private LinearLayout llBottomSheet;
    private FloatingActionButton fabVaccine;

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(int someInt) {
        ScheduleFragment myFragment = new ScheduleFragment();

        Bundle args = new Bundle();
        args.putInt("babyId", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container,savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        recyclerView = view.findViewById(R.id.rvSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      //  fabVaccine = getActivity().findViewById(R.id.fabVaccine);
       // final BottomDialogFragment dialogFragment = BottomDialogFragment.getInstance();
        /*fabVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormDataHolder.id = getArguments().getInt("babyId");
                dialogFragment.show(getFragmentManager(), "Bottom sheet");
            }
        });*/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*if(dy > 0 && fabVaccine.getVisibility() == View.VISIBLE){
                    fabVaccine.hide();
                }else if(dy < 0 && fabVaccine.getVisibility() != View.VISIBLE){
                    fabVaccine.show();
                }*/
            }
        });

        ScheduleRecyclerViewAdapter scheduleRecyclerViewAdapter = new ScheduleRecyclerViewAdapter(getContext(),
                ScheduleLists.fullScheduleList);

        recyclerView.setAdapter(scheduleRecyclerViewAdapter);
        return view;
    }


}
