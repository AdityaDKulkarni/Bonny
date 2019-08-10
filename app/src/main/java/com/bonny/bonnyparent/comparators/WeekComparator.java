package com.bonny.bonnyparent.comparators;


import com.bonny.bonnyparent.models.VaccineModel;

import java.util.Comparator;

public class WeekComparator implements Comparator<VaccineModel> {

    @Override
    public int compare(VaccineModel babyModel, VaccineModel t1) {
        if(babyModel.getWeek() < t1.getWeek()){
            return -1;
        }

        if(babyModel.getWeek() > t1.getWeek()){
            return 1;
        }

        return 0;
    }
}
