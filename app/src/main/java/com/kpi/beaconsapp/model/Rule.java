package com.kpi.beaconsapp.model;

public class Rule {
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

    public String getApp() {
        return app;
    }

    private String app;

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public Rule(int id, String beaconUUID, String appName, String app){
        this.id = id;
        this.beaconUUID = beaconUUID;
        this.app = app;
        this.appName = appName;
        this.name = "Rule";
    }


}

//public class Note implements Serializable {
//    private int id;
//    private String name;
//    private String text;
//    private String color;
//    private ArrayList<Beacon> beacons;
//
//    Note() {
//        this.color = "#cac7d7";
//        this.beacons = new ArrayList<>();
//    }
//
//    Note(int id, String name, String text, ArrayList<Beacon> beacons, String color) {
//        this.id = id;
//        this.name = name;
//        this.text = text;
//        this.beacons = beacons;
//        this.color = color;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public ArrayList<Beacon> getBeacons() {
//        return beacons;
//    }
//
//    public String getBeaconsNames() {
//        StringBuilder s = new StringBuilder();
//        for(int i=0; i < beacons.size() - 1; i++){
//            s.append(beacons.get(i).getName()).append(", ");
//        }
//        if(beacons.size()-1 >= 0)  {
//            s.append(beacons.get(beacons.size()-1).getName());
//        }
//        return s.toString();
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public void setBeacons(ArrayList<Beacon> beacons) {
//        this.beacons = beacons;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//}