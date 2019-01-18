package com.mmr.nassab.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mojtaba Rajabi on 26/12/2018.
 */

public class Util {
    public ArrayList<String> getDeviceNames() {
        return deviceNames;
    }

    public void setDeviceNames(ArrayList<String> deviceNames) {
        this.deviceNames = deviceNames;
    }

    public ArrayList<String> getClusterNames() {
        return clusterNames;
    }

    public void setClusterNames(ArrayList<String> clusterNames) {
        this.clusterNames = clusterNames;
    }

    public ArrayList<String> getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(ArrayList<String> projectNames) {
        this.projectNames = projectNames;
    }

    private ArrayList<String> deviceNames;
    private ArrayList<String> clusterNames;
    private ArrayList<String> projectNames;

    private HashMap<String, String> devices; //id name
    private HashMap<String, String> clusters;
    private HashMap<String, String> projects;

    private HashMap<String, String> devicesReverse; //name id
    private HashMap<String, String> clustersReverse;
    private HashMap<String, String> projectsReverse;

    public Util() {
        deviceNames = new ArrayList<>();
        clusterNames = new ArrayList<>();
        projectNames = new ArrayList<>();

        devices = new HashMap<>();
        clusters = new HashMap<>();
        projects = new HashMap<>();
        devicesReverse = new HashMap<>();
        clustersReverse = new HashMap<>();
        projectsReverse = new HashMap<>();
    }


    public HashMap<String, String> getDevices() {
        return devices;
    }

    public void addDevices(String key, String value) {
        this.devices.put(key, value);
    }

    public HashMap<String, String> getClusters() {
        return clusters;
    }

    public void addClusters(String key, String value) {
        this.clusters.put(key, value);
    }

    public HashMap<String, String> getProjects() {
        return projects;
    }

    public void addProjects(String key, String value) {
        this.projects.put(key, value);
    }

    //
    public HashMap<String, String> getDevicesReverse() {
        return devicesReverse;
    }

    public void addDevicesReverse(String key, String value) {
        this.devicesReverse.put(key, value);
    }

    public HashMap<String, String> getClustersReverse() {
        return clustersReverse;
    }

    public void addClustersReverse(String key, String value) {
        this.clustersReverse.put(key, value);
    }

    public HashMap<String, String> getProjectsReverse() {
        return projectsReverse;
    }

    public void addProjectsReverse(String key, String value) {
        this.projectsReverse.put(key, value);
    }

    public void clearAll() {
        projectNames.clear();
        deviceNames.clear();
        clusterNames.clear();

        projects.clear();
        devices.clear();
        clusters.clear();

        projectsReverse.clear();
        devicesReverse.clear();
        clustersReverse.clear();
    }
}
