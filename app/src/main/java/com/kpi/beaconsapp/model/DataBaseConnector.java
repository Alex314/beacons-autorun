package com.kpi.beaconsapp.model;

import java.util.ArrayList;

public interface DataBaseConnector {

//    ArrayList<Note> getNotes();
    ArrayList<Rule> getRules();

//    Note getNote(int i);

    void addRule(Rule r);

    //створює нову пусту нотатку і повертає її айді
//    int getNewNoteId();
//
//    void editNote(int id, String name, String text, String color, String[] beaconsCodes);
//
//    void deleteNote(int id);

    ArrayList<Beacon> getBeacons();

    void setBeacons(ArrayList<Beacon> beacons);

}
