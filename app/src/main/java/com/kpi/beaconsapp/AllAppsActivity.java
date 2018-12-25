package com.kpi.beaconsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AllAppsActivity extends AppCompatActivity {
    public ArrayList<String> apps;
    public HashMap<String, String> app_code;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apps = new ArrayList<>();
        app_code = new HashMap<>();
        setContentView(R.layout.all_apps_activity);

        ListView allAppsView =  findViewById(R.id.all_apps_list) ;
        final PackageManager pm = getPackageManager(); //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo pkg: packages) {
            if((pm.getLaunchIntentForPackage(pkg.packageName) != null) ){ // & (((pkg.flags & ApplicationInfo.FLAG_SYSTEM) == 0))) {
                apps.add((String) pm.getApplicationLabel(pkg));
                app_code.put((String) pm.getApplicationLabel(pkg), pkg.packageName);
            }
        }
        apps.sort(String::compareTo);


        Log.i("AllAppsActivity","" +apps.size() + " apps added");
        AllAppsActivity.AppItemAdapter appItemAdapter = new AllAppsActivity.AppItemAdapter(this, apps);
        allAppsView.setAdapter(appItemAdapter);

        String action = getIntent().getStringExtra("com.kpi.beaconsapp.action");

        if (action.equals("addRule")) {
            setTitle("Choose application for new rule");

            allAppsView.setOnItemClickListener((adapterView, view, i, l) -> {
                Toast.makeText(getApplicationContext(), "app " + i, Toast.LENGTH_SHORT).show();
                String packageName = app_code.get(apps.get(i));
                int beaconId = getIntent().getIntExtra("com.kpi.beaconsapp.chosenBeacon", 0);
                finish();
            });

        }
        else{
            allAppsView.setOnItemClickListener((adapterView, view, i, l) -> {
                String packageName = app_code.get(apps.get(i));
                Intent launch = getPackageManager().getLaunchIntentForPackage(packageName);
                startActivity(launch);
            });
        }


    }

    class AppItemAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<String> apps;

        AppItemAdapter(Context context, ArrayList<String> apps) {
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.apps = apps;
        }


        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int i) {
            return apps.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 1;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = layoutInflater.inflate(R.layout.apps_list_item_layout, null);

            TextView appNameView = view.findViewById(R.id.appNameView);
            appNameView.setText(apps.get(i));
            return view;
        }
    }

}
