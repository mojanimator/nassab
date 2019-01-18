package com.mmr.nassab.Model;

/**
 * Created by Mojtaba Rajabi on 10/12/2018.
 */

public class Car {

    private String id;
    private String name;
    private String numberplate;
    private String gps_imei;
    private String gps_simcard;
    private String driver_name;
    private String driver_simcard;
    private String cluster;
    private short status;
    private String nassab_id;
    private String device_id;
    private String project_id;

    public String getGps_pos() {
        return gps_pos;
    }

    public void setGps_pos(String gps_pos) {
        this.gps_pos = gps_pos;
    }

    private String gps_pos;

    public String getGps_simcard() {
        return gps_simcard;
    }

    public void setGps_simcard(String gps_simcard) {
        this.gps_simcard = gps_simcard;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_simcard() {
        return driver_simcard;
    }

    public void setDriver_simcard(String driver_simcard) {
        this.driver_simcard = driver_simcard;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public String getGps_imei() {
        return gps_imei;
    }

    public void setGps_imei(String gps_imei) {
        this.gps_imei = gps_imei;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }


    public String getNassab_id() {
        return nassab_id;
    }

    public void setNassab_id(String nassab_id) {
        this.nassab_id = nassab_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
