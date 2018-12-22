package com.kpi.beaconsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AllAppsActivity extends AppCompatActivity {
    public ArrayList<String> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_apps_activity);

        ListView allAppsView =  findViewById(R.id.all_apps_list) ;
        final PackageManager pm = getPackageManager(); //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        apps = new ArrayList<>();
        int i = 20;
        for (ApplicationInfo pkg: packages) {
            if((pm.getLaunchIntentForPackage(pkg.packageName) != null) ){ // & (((pkg.flags & ApplicationInfo.FLAG_SYSTEM) == 0))) {
                apps.add((String) pm.getApplicationLabel(pkg) + " " + pkg.packageName);
            }
            i = i-1;
            if (i <= 0) break;
        }

        Log.i("AllAppsActivity","" +apps.size() + " apps added");
//        Intent launch = getPackageManager().getLaunchIntentForPackage(code);
//        startActivity(launch);
        AllAppsActivity.AppItemAdapter appItemAdapter = new AllAppsActivity.AppItemAdapter(this, apps);
        allAppsView.setAdapter(appItemAdapter);
//        allBeaconsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                showNotesNotifications(db.getBeacons().get(i).getCode());
//            }
//        });
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
//            Log.i("AllAppsActivity","" +appNameView.toString() + " appNameView");
//            Log.i("AllAppsActivity","apps contain " +apps.size() + " elements");
//            Log.i("AllAppsActivity","" +i + "th element requested");
//            Log.i("AllAppsActivity","" +apps.get(i) + " element requested");

            appNameView.setText(apps.get(i));
            return view;
        }
    }

}
