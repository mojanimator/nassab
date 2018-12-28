package com.mmr.nassab.Model;

import java.util.HashMap;

/**
 * Created by Mojtaba Rajabi on 26/12/2018.
 */

public class Util {
    private HashMap<String, String> devices; //id name
    private HashMap<String, String> clusters;
    private HashMap<String, String> projects;

    private HashMap<String, String> devicesReverse; //name id
    private HashMap<String, String> clustersReverse;
    private HashMap<String, String> projectsReverse;

    public Util() {
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
}
