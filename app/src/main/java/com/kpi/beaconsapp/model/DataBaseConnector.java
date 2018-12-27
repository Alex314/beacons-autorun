package com.kpi.beaconsapp.model;

import java.util.ArrayList;

public interface DataBaseConnector {

    ArrayList<Rule> getRules();

    void addRule(Rule r);

    ArrayList<Beacon> getBeacons();
}
