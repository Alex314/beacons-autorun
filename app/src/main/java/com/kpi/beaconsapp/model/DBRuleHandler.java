package com.kpi.beaconsapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kpi.beaconsapp.MenuActivity;

import java.util.ArrayList;

public class DBRuleHandler extends SQLiteOpenHelper implements DataBaseConnector {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "rulesdb";

    // Contacts table name
    private static final String TABLE_RULES = "rules";
    private static final String TABLE_BEACONS = "beacons";

    // Rules
    private static final String KEY_ID = "id";
    private static final String KEY_BEACON_UUID = "beacon_uuid";
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_APP_PACKAGE_NAME = "app_package_name";
    private static final String KEY_DISTANCE_LIMIT = "distance_limit";
    private static final String KEY_RULE_NAME = "name";

    // Beacons
    private static final String KEY_UUID = "uuid";
    private static final String KEY_BEACON_NAME = "name";
    private static final String KEY_BEACON_DESC = "desc";

    public DBRuleHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DBRuleHandler instance;

    public static DBRuleHandler getInstance(){
        if(instance == null)
            instance = new DBRuleHandler(MenuActivity.CONTEXT);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS);
        // Creating tables again
        onCreate(db);
    }

    @Override
    public ArrayList<Rule> getRules() {
        return getAllRules();
    }

    public void addRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BEACON_UUID, rule.getBeaconUUID()); // Rule beacon uuid
        values.put(KEY_APP_NAME, rule.getAppName()); // Rule app name
        values.put(KEY_APP_PACKAGE_NAME, rule.getAppPackage()); // Rule app full name
        values.put(KEY_DISTANCE_LIMIT, rule.getDistance()); // Rule app full name
        values.put(KEY_RULE_NAME, rule.getName());

        db.insert(TABLE_RULES, null, values);
        db.close(); // Closing database connection
    }

    public void addBeacon(Beacon beacon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UUID, beacon.getCode()); // beacon uuid
        values.put(KEY_BEACON_NAME, beacon.getName()); // beacon name
        values.put(KEY_BEACON_DESC, "Desc"); // Beacon description

        // Inserting Row
        db.insert(TABLE_BEACONS, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public ArrayList<Beacon> getBeacons() {
        ArrayList<Beacon> beacons = new ArrayList<Beacon>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BEACONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Beacon b = new Beacon(
                        0,
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(0)
                );
                beacons.add(b);
            } while (cursor.moveToNext());
        }
        return beacons;
    }

    public Rule getRule(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RULES, new String[]{KEY_ID,
                        KEY_BEACON_UUID, KEY_APP_NAME, KEY_APP_PACKAGE_NAME, KEY_DISTANCE_LIMIT, KEY_RULE_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Rule rule = new Rule(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(5),
                cursor.getString(1),
                cursor.getString(3),
                cursor.getString(2),
                Double.parseDouble(cursor.getString(4))
        );
        return rule;
    }

    public ArrayList<Rule> getAllRules() {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RULES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Rule rule = new Rule(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(5),
                        cursor.getString(1),
                        cursor.getString(3),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(4))
                );
                rules.add(rule);
            } while (cursor.moveToNext());
        }
        return rules;
    }

    public int getRulesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RULES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BEACON_UUID, rule.getBeaconUUID());
        values.put(KEY_APP_NAME, rule.getAppName());
        values.put(KEY_APP_PACKAGE_NAME, rule.getAppPackage());
        values.put(KEY_DISTANCE_LIMIT, String.valueOf(rule.getDistance()));
        values.put(KEY_RULE_NAME, rule.getName());

        // updating row
        return db.update(TABLE_RULES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(rule.getID())});
    }

    public void deleteRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RULES, KEY_ID + " = ?",
                new String[] { String.valueOf(rule.getID()) });
        db.close();
    }
}
