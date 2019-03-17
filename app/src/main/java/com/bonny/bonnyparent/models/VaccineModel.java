package com.bonny.bonnyparent.models;

import android.support.annotation.NonNull;

import java.io.Serializable;


/**
 * @author Aditya Kulkarni
 */

public class VaccineModel implements  Comparable<VaccineModel>,Serializable{
    private int baby, week;
    private String vaccine, status;
    private String tentative_date;

    public int getBaby() {
        return baby;
    }

    public void setBaby(int baby) {
        this.baby = baby;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTentative_date() {
        return tentative_date;
    }

    public void setTentative_date(String tentative_date) {
        this.tentative_date = tentative_date;
    }

    @Override
    public int compareTo(VaccineModel comparesto) {

        int compareWeek=comparesto.getWeek();
        return this.week-compareWeek;
    }
}
