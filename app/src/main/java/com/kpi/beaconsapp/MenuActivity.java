package com.kpi.beaconsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kpi.beaconsapp.model.Beacon;
import com.kpi.beaconsapp.model.DBRuleHandler;
import com.kpi.beaconsapp.model.DataBaseConnector;
import com.kpi.beaconsapp.model.Rule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String PACKAGE_NAME;
    public static Context CONTEXT;


    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    static DataBaseConnector db;
    protected static final String TAG = "MonitoringActivity";
    ListView notesView;
    ListView beaconsView;
//    NoteItemAdapter noteItemAdapter;
//    BeaconItemAdapter beaconItemAdapter;
    RuleItemAdapter ruleItemAdapter;
    int lastNote;
    int lastRule;
    SharedPreferences preferences;

    private String path;
    private final String fileName = "rulesdb";

    private void _copydatabase() {

        try{
            File f = new File(path + fileName);
            if(f.exists() && !f.isDirectory()) {
                return;
            }


            OutputStream myOutput = new FileOutputStream(path + fileName);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = MenuActivity.CONTEXT.getAssets().open(fileName);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();
        }catch(Exception e){
            Log.d("ERROR", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        CONTEXT = getApplicationContext();
        path = "/data/data/" + getApplicationContext().getPackageName()+"/databases/";
        _copydatabase();

//-------------------------------------------------------------

//        DBRuleHandler db2 = new DBRuleHandler(this);
//
//        Log.d("rule handler created", "creatersd");
//
//        db2.addRule(new Rule(0,  "6d6b3e4a-9e0a-4ac4-9f42-bdd719850590", "Google", "com.google.android.googlequicksearchbox"));
//        List<Rule> rules = db2.getAllRules();
//
//        for (Rule rule : rules) {
//            String log = "beaconUUId: " + rule.getBeaconUUID() + " ,Name: " + rule.getName() + " ,App name: " + rule.getAppName() + " ,AppFullName: " + rule.getAppPackage();
//            // Writing shops  to log
//            Log.d("Rule: : ", log);
//        }

        db = DBRuleHandler.getInstance();

//        createNotificationChannel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        staerIntroActivitu();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseBeacon = new Intent(getApplicationContext(), AllBeaconsActivity.class);
                chooseBeacon.putExtra("com.kpi.beaconsapp.action", "addRule");
                startActivity(chooseBeacon);
//                lastNote = 0;
//                Intent showNoteDetail = new Intent(getApplicationContext(), NoteDetailsActivity.class);
//                showNoteDetail.putExtra("com.kpi.beaconsapp.NOTE_ID", db.getNewNoteId());
//                startActivity(showNoteDetail);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userNameView = navigationView.getHeaderView(0).findViewById(R.id.user_name_view);
        userNameView.setText(preferences.getString("user_name", "User"));

        //відображаємо список усіх нотаток, вішаємо на них екшен - відкрити актівіті нотатки
//        beaconsView = findViewById(R.id.notesView) ;
////        setContentView(R.layout.all_beacond_activity);
//        //відображаємо список усіх біконів
//        ListView allBeaconsView =  findViewById(R.id.all_beacons_list) ;
//        BeaconItemAdapter beaconItemAdapter = new BeaconItemAdapter(this, db.getBeacons());
//        allBeaconsView.setAdapter(beaconItemAdapter);
//
//        notesView = beaconsView;
//        notesView = findViewById(R.id.notesView) ;
        beaconsView = findViewById(R.id.notesView) ;
//        noteItemAdapter = new NoteItemAdapter(this, db.getNotes());
        ruleItemAdapter = new RuleItemAdapter(this, db.getRules());
//        notesView.setAdapter(noteItemAdapter);
        beaconsView.setAdapter(ruleItemAdapter);

        beaconsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent addRule = new Intent(getApplicationContext(), RuleActivity.class);
                lastRule = i;
                addRule.putExtra("com.kpi.beaconsapp.NOTE_ID", db.getRules().size() -1 - i);
                startActivity(addRule);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }


        if(preferences.getBoolean("beacon_scanning", false)){
            Thread thread = new Thread() {
                @Override
                public void run() {
                    startService(new Intent(MenuActivity.this,BeaconScanner.class));
                }
            };
            thread.start();
//            Intent startServiceIntent = new Intent(MenuActivity.this, BeaconScanner.class);
//            startService(startServiceIntent);
        }

        }




    private void staerIntroActivitu(){

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = preferences.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
//                    Intent i = new Intent(MenuActivity.this, IntroActivity.class);
//                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = preferences.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_note) {
            lastNote = 0;
            Intent showNoteDetail = new Intent(getApplicationContext(), NoteDetailsActivity.class);
            showNoteDetail.putExtra("com.kpi.beaconsapp.NOTE_ID", db.getNewNoteId());
            startActivity(showNoteDetail);
        } else if (id == R.id.all_beacons) {
            Intent showAllBeacons = new Intent(getApplicationContext(), AllBeaconsActivity.class);
            startActivity(showAllBeacons);
        }
        else if (id == R.id.all_apps) {

            Intent showAllApps = new Intent(getApplicationContext(), AllAppsActivity.class);
            startActivity(showAllApps);

        }else if (id == R.id.settings) {
            showToast("Rules: " + db.getRules().size());
//            showToast("Beacons: " + db.getBeacons().size());
            Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsActivity);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(String code){
//        final PackageManager pm = getPackageManager(); //get a list of installed apps.
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//        code = packages.get(1).packageName;
//        code = (String)pm.getApplicationLabel(packages.get(0));
        Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT).show();
//        Intent launch = getPackageManager().getLaunchIntentForPackage(code);
//        startActivity(launch);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TextView userNameView = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.user_name_view);
        userNameView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("user_name", "User"));

//        notesView = findViewById(R.id.notesView) ;
//        noteItemAdapter = new NoteItemAdapter(this, db.getNotes());
//        notesView.setAdapter(noteItemAdapter);
//        notesView.post(new Runnable() {
//            @Override
//            public void run() {
//                notesView.smoothScrollToPosition(lastNote+1);
//            }
//        });

//        createNotificationChannel();
    }

//    class NoteItemAdapter extends BaseAdapter {
//
//        private LayoutInflater layoutInflater;
//        private ArrayList<Note> notes;
//        private int size;
//
//        NoteItemAdapter(Context context, ArrayList<Note> notes) {
//            this.size = notes.size();
//            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//            this.notes = notes;
//        }
//
//        @Override
//        public int getCount() {
//            return notes.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return notes.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return notes.get(i).getId();
//        }
//
//        @SuppressLint({"ViewHolder", "InflateParams"})
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            Note note = notes.get(size - 1 - i);
//            view = layoutInflater.inflate(R.layout.note_item_layout, null);
//            view.setBackgroundColor(Color.parseColor(note.getColor()));
//
//            TextView noteName = view.findViewById(R.id.noteName);
//            TextView noteText = view.findViewById(R.id.noteText);
//            TextView noteBeacons = view.findViewById(R.id.noteBeacons);
//
//            noteName.setText(note.getName());
//            noteText.setText(note.getText());
//            noteBeacons.setText(note.getBeaconsNames());
//            return view;
//        }
//    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChannel";
            String description = "for notes and beacons";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG,"coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
    }

    class RuleItemAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Rule> rules;

        RuleItemAdapter(Context context, ArrayList<Rule> rules) {
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.rules = rules;
        }


        @Override
        public int getCount() {
            return rules.size();
        }

        @Override
        public Object getItem(int i) {
            return rules.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = layoutInflater.inflate(R.layout.rule_list_item_layout, null);

            TextView ruleName = view.findViewById(R.id.ruleNameView);
            TextView beaconName = view.findViewById(R.id.ruleBeaconName);
            TextView appName = view.findViewById(R.id.ruleAppName);

            Rule rule =  rules.get(i);
            ruleName.setText(rule.getName());
            beaconName.setText(rule.getBeaconUUID());
            appName.setText(rule.getAppName());
            return view;
        }
    }

}
