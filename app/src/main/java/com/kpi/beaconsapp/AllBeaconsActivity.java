package com.kpi.beaconsapp;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kpi.beaconsapp.model.Beacon;
import com.kpi.beaconsapp.model.DBRuleHandler;
import com.kpi.beaconsapp.model.DataBaseConnector;
import com.kpi.beaconsapp.model.DataBaseEmulator;
import com.kpi.beaconsapp.model.Note;

import java.util.ArrayList;

public class AllBeaconsActivity extends AppCompatActivity {
    DataBaseConnector db = DBRuleHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_beacond_activity);
        //відображаємо список усіх біконів
        ListView allBeaconsView =  findViewById(R.id.all_beacons_list) ;
        BeaconItemAdapter beaconItemAdapter = new BeaconItemAdapter(this, db.getBeacons());
        allBeaconsView.setAdapter(beaconItemAdapter);
//        allBeaconsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                showNotesNotifications(db.getBeacons().get(i).getCode());
//            }
//        });
    }

//    public


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
            TextView beaconAddress = view.findViewById(R.id.beaconAddressView);
            TextView beaconCode = view.findViewById(R.id.beaconCodeView);

            Beacon beacon =  beacons.get(i);
            beaconName.setText(beacon.getName());
            beaconAddress.setText(beacon.getAddress());
            beaconCode.setText(beacon.getCode());
            return view;
        }
    }
}




