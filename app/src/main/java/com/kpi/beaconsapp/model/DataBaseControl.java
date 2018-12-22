package com.kpi.beaconsapp.model;


import android.os.Environment;
import android.util.Log;

import com.kpi.beaconsapp.MenuActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBaseControl implements DataBaseConnector {

    private Connection connection;


    private static final DataBaseConnector ourInstance = new DataBaseControl();
    private ArrayList<Note> notes = new ArrayList<>();
    private ArrayList<Rule> rules = new ArrayList<>();
    private ArrayList<Beacon> beacons = new ArrayList<>();
    private String mac;

    public static DataBaseConnector getInstance() {
        return ourInstance;
    }

    private DataBaseControl() {
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

        try {
//            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver((Driver) Class.forName("org.sqlite.JDBC").newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to register SQLDroidDriver");
        }
//        Log.d("IN_MANAGER", "OUTSIDE");

//        String jdbcUrl = "jdbc:sqldroid:" + "/data/data/" + PACKAGE_NAME + "/my-database.db";
//        String jdbcUrl = "jdbc:sqlite:traindb.db";
//        Log.d("package_name",MenuActivity.PACKAGE_NAME);


//        try {
//            _copydatabase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        File f = new File("/data/data/" + MenuActivity.PACKAGE_NAME);
//
//        File[] files = f.listFiles();
//
//        for (File inFile : files) {
//            Log.d("file", inFile.getName());
//
//        }
        Log.d("TTT", Environment.getDataDirectory().getAbsolutePath());
        String jdbcUrl = "jdbc:sqlite:" + "/data/data/" + MenuActivity.PACKAGE_NAME + "/traindb.db";
        Log.d("path", jdbcUrl);
        try {
            Log.d("IN_MANAGER", "INSIDE");

            this.connection = DriverManager.getConnection(jdbcUrl);
            Log.d("IN_MANAGER", "INSIDE");

        } catch (SQLException e) {
            Log.d("error",e.getMessage());
//            throw new RuntimeException(e);
        }


        this.mac = getMac();

    }

    public final String path = "/data/data/" + MenuActivity.PACKAGE_NAME+"/";
    public final String Name = "traindb.db";

    public void _copydatabase() throws IOException {

        OutputStream myOutput = new FileOutputStream(path + Name);
        byte[] buffer = new byte[1024];
        int length;
        InputStream myInput = MenuActivity.CONTEXT.getAssets().open("traindb.db");
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();

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
