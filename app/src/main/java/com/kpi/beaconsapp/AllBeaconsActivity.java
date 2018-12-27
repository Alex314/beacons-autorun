package com.kpi.beaconsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kpi.beaconsapp.model.Beacon;
import com.kpi.beaconsapp.model.DBRuleHandler;
import com.kpi.beaconsapp.model.DataBaseConnector;

import java.util.ArrayList;

public class AllBeaconsActivity extends AppCompatActivity {
    DataBaseConnector db = DBRuleHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_beacons_activity);
        ListView allBeaconsView =  findViewById(R.id.all_beacons_list) ;
        BeaconItemAdapter beaconItemAdapter = new BeaconItemAdapter(this, db.getBeacons());
        allBeaconsView.setAdapter(beaconItemAdapter);

        String action = getIntent().getStringExtra("com.kpi.beaconsapp.action");

        if (action.equals("addRule")) {
            setTitle("Choose beacon for new rule");

            allBeaconsView.setOnItemClickListener((adapterView, view, i, l) -> {
                Toast.makeText(getApplicationContext(), "beacon " + i, Toast.LENGTH_SHORT).show();
                Intent chooseApp = new Intent(getApplicationContext(), AllAppsActivity.class);
                chooseApp.putExtra("com.kpi.beaconsapp.action", "addRule");
                chooseApp.putExtra("com.kpi.beaconsapp.chosenBeacon", db.getBeacons().get(i).getCode());
                startActivity(chooseApp);
                finish();
            });

        }
    }

    class BeaconItemAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private ArrayList<Beacon> beacons;

        BeaconItemAdapter(Context context, ArrayList<Beacon> beacons) {
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.beacons = beacons;
        }

        @Override
        public int getCount() {
            return beacons.size();
        }

        @Override
        public Object getItem(int i) {
            return beacons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return beacons.get(i).getId();
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = layoutInflater.inflate(R.layout.beacon_list_item_layout, null);

            TextView beaconName = view.findViewById(R.id.beaconNameView);
            TextView beaconDesc = view.findViewById(R.id.beaconAddressView);
            TextView beaconCode = view.findViewById(R.id.beaconCodeView);

            Beacon beacon =  beacons.get(i);
            beaconName.setText(beacon.getName());
            beaconDesc.setText(beacon.getDescription());
            beaconCode.setText(beacon.getCode());
            return view;
        }
    }
}
