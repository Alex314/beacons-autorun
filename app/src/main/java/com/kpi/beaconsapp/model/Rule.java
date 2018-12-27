package com.kpi.beaconsapp.model;

public class Rule {

    public int getID() {
        return id;
    }

    private int id;

    private String beaconUUID;

    public String getName() {
        return name;
    }

    private String name;

    public String getAppName() {
        return appName;
    }

    private String appName;

    public String getAppPackage() {
        return appPackage;
    }

    private String appPackage;

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;

    public double getDistance() {
        return distance;
    }

    public Rule(int id, String beaconUUID, String appName, String appPackage){
        this.id = id;
        this.beaconUUID = beaconUUID;
        this.appPackage = appPackage;
        this.appName = appName;
        this.name = "Rule";
        this.distance = 15;
    }

    public Rule(int id, String name, String beaconUUID, String appName, String appPackage, double distance){
        this.id = id;
        this.name = name;
        this.beaconUUID = beaconUUID;
        this.appPackage = appPackage;
        this.appName = appName;
        this.distance = distance;
    }
}
