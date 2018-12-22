package com.kpi.beaconsapp.model;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBaseEmulator implements DataBaseConnector {
    private static final DataBaseConnector ourInstance = new DataBaseEmulator();
    private ArrayList<Note> notes = new ArrayList<>();
    private ArrayList<Rule> rules = new ArrayList<>();
    private ArrayList<Beacon> beacons = new ArrayList<>();
    private String mac;

    public static DataBaseConnector getInstance() {
        return ourInstance;
    }

    private DataBaseEmulator() {
        beacons.add(new Beacon(0, "me", "portable", "6d6b3e4a-9e0a-4ac4-9f42-bdd719850590"));
        beacons.add(new Beacon(1, "Alex", "portable", "3af70a12-9856-47eb-9050-2b3691ac157b"));
        beacons.add(new Beacon(2, "Comp", "portable", "e20a39f4-73f5-4bc4-a12f-17d1ad07a961"));
        beacons.add(new Beacon(3, "door", "main door", "1d6dea35-aa00-4c9b-bcc9-114d6ce98eea"));
        beacons.add(new Beacon(4, "Olya", "portable", "8dfe9950-7efe-46b2-ac80-6e739bbc9c01"));

        Rule r1 = new Rule(0, beacons.get(4).getCode(), "Google", "com.google.android.googlequicksearchbox");
        Rule r2 = new Rule(1, beacons.get(3).getCode(), "Telegram", "org.telegram.messenger");
        Rule r3 = new Rule(2, beacons.get(1).getCode(), "Google", "com.google.android.googlequicksearchbox");
        Rule r4 = new Rule(3, beacons.get(4).getCode(), "Telegram", "org.telegram.messenger");
//        rules.add(r1);
        rules.add(r2);
        rules.add(r3);
        rules.add(r4);

        this.mac = getMac();

    }

    public void addRule(Rule r){
        rules.add(r);
    }


    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public ArrayList<Rule> getRules() {
        return rules;
    }

    public Note getNote(int i){
        if(i < notes.size()) return notes.get(i);
        else return null;
    }

    public int getNewNoteId() {
        notes.add(new Note());
        return notes.size() - 1;
    }

    public void editNote(int id, String name, String text, String color, String[] beaconsCodes) {
        Note note = notes.get(id);
        note.setId(id);
        note.setName(name);
        note.setText(text);
        ArrayList<Beacon> b = new ArrayList<>();
        for(String code: beaconsCodes){
            b.add(beacons.get(beacons.indexOf(new Beacon(code))));
        }
        note.setBeacons(b);
        note.setColor(color);
    }

    public void deleteNote(int id){
        notes.remove(id);
    }


    public ArrayList<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    static private String getMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif: all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b: macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
}
