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

    // Beacons
    private static final String KEY_UUID = "uuid";
    private static final String KEY_BEACON_NAME = "name";
    private static final String KEY_BEACON_DESC = "desc";

    public DBRuleHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//
//
//        beacons.add(new Beacon(0, "me", "portable", "6d6b3e4a-9e0a-4ac4-9f42-bdd719850590"));
//        beacons.add(new Beacon(1, "Alex", "portable", "3af70a12-9856-47eb-9050-2b3691ac157b"));
//        beacons.add(new Beacon(2, "Comp", "portable", "e20a39f4-73f5-4bc4-a12f-17d1ad07a961"));
//        beacons.add(new Beacon(3, "door", "main door", "1d6dea35-aa00-4c9b-bcc9-114d6ce98eea"));
//        beacons.add(new Beacon(4, "Olya", "portable", "8dfe9950-7efe-46b2-ac80-6e739bbc9c01"));
//
//        addRule(new Rule(0, beacons.get(4).getCode(), "Google", "com.google.android.googlequicksearchbox"));
//        addRule(new Rule(1, beacons.get(3).getCode(), "Telegram", "org.telegram.messenger"));
//        addRule(new Rule(2, beacons.get(1).getCode(), "Google", "com.google.android.googlequicksearchbox"));
//        addRule(new Rule(3, beacons.get(4).getCode(), "Telegram", "org.telegram.messenger"));

    }



    private ArrayList<Note> notes = new ArrayList<>();
    private ArrayList<Beacon> beacons = new ArrayList<>();

    private static DBRuleHandler instance;

    public static DBRuleHandler getInstance(){
        if(instance == null)
            instance = new DBRuleHandler(MenuActivity.CONTEXT);

        return instance;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RULES + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_BEACON_UUID + " TEXT,"
//                + KEY_APP_NAME + " TEXT," + KEY_APP_PACKAGE_NAME + " TEXT" + ");";
//
//        String CREATE_BEACONS_TABLE = "CREATE TABLE " + TABLE_BEACONS + "("
//                + KEY_UUID + "TEXT PRIMARY KEY,"
//                + KEY_BEACON_NAME + "TEXT, " + KEY_BEACON_ADDRESS + " TEXT" + ");";
//        String CREATE_BEACONS_TABLE = "CREATE TABLE beacons (\n" +
//                "  uuid  TEXT NOT NULL PRIMARY KEY,\n" +
//                "  name  TEXT,\n" +
//                "  desc  TEXT\n" +
//                ");";
//
//        String CREATE_RULES_TABLE = "CREATE TABLE rules (\n" +
//                "id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                "beacon_uuid  TEXT NOT NULL,\n" +
//                "app_package_name  TEXT NOT NULL,\n" +
//                "app_name  TEXT NOT NULL,\n" +
//                "distance_limit  REAL NOT NULL,\n" +
//                "FOREIGN KEY(beacon_uuid) REFERENCES beacons(uuid) on update cascade on delete cascade\n" +
//                ");";
//
//        db.execSQL(CREATE_BEACONS_TABLE + CREATE_RULES_TABLE);
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
    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public ArrayList<Rule> getRules() {
        return getAllRules();
    }

    @Override
    public Note getNote(int i) {
        if(i < notes.size()) return notes.get(i);
        else return null;
    }

    public void addRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BEACON_UUID, rule.getBeaconUUID()); // Rule beacon uuid
        values.put(KEY_APP_NAME, rule.getAppName()); // Rule app name
        values.put(KEY_APP_PACKAGE_NAME, rule.getAppPackage()); // Rule app full name
        values.put(KEY_DISTANCE_LIMIT, 10); // Rule app full name

        // Inserting Row
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
    public int getNewNoteId() {
        notes.add(new Note());
        return notes.size() - 1;
    }

    @Override
    public void editNote(int id, String name, String text, String color, String[] beaconsCodes) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteNote(int id) {
        notes.remove(id);
    }

    @Override
    public ArrayList<Beacon> getBeacons() {
        return beacons;
    }

    @Override
    public void setBeacons(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    public Rule getRule(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RULES, new String[]{KEY_ID,
                        KEY_BEACON_UUID, KEY_APP_NAME, KEY_APP_PACKAGE_NAME, KEY_DISTANCE_LIMIT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Rule rule = new Rule(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );
        return rule;
    }

    // Getting All Shops
    public ArrayList<Rule> getAllRules() {
        ArrayList<Rule> shopList = new ArrayList<Rule>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RULES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Rule rule = new Rule(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
                );
                // Adding contact to list
                shopList.add(rule);
            } while (cursor.moveToNext());
        }

        // return contact list
        return shopList;
    }

    // Getting shops Count
    public int getRulesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RULES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
//
//    // Updating a shop
//    public int updateRule(Shop shop) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, shop.getName());
//        values.put(KEY_SH_ADDR, shop.getAddress());
//
//        // updating row
//        return db.update(TABLE_RULES, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(shop.getId())});
//    }

    // Deleting a shop
    public void deleteRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RULES, KEY_BEACON_UUID + " = ?",
                new String[] { String.valueOf(rule.getBeaconUUID()) });
        db.close();
    }
}

