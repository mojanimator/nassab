package com.mmr.nassab.Model;

/**
 * Created by Mojtaba Rajabi on 10/12/2018.
 */

public class Report {

    private int gps_id;
    private int person_id;
    private String hardness;
    private String date;

    public int getId() {
        return gps_id;
    }

    public void setId(int gps_id) {
        this.gps_id = gps_id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getHardness() {
        return hardness;
    }

    public void setHardness(String hardness) {
        this.hardness = hardness;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
