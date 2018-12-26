package com.kpi.beaconsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.kpi.beaconsapp.model.DBRuleHandler;
//import com.kpi.beaconsapp.model.DataBaseEmulator;
import com.kpi.beaconsapp.model.Rule;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeaconScanner extends Service implements BeaconConsumer {
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private Region region;
    final Set<Beacon> rangedBeacons = new HashSet<>();
    public HashMap<Beacon, Long> visits = new HashMap<>();

    public BeaconScanner() {
        region = new Region("myMonitoringUniqueId", null, null, null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.setBackgroundScanPeriod(1000L);
        beaconManager.setForegroundScanPeriod(1000L);
        beaconManager.setBackgroundBetweenScanPeriod(1L);
        beaconManager.setForegroundBetweenScanPeriod(1L);
//        beaconManager.
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect     :  ");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.i(TAG, "collection size  " + collection.size());

//                Date now= Calendar.getInstance().getTime() ;

                Long now = System.currentTimeMillis();
                Log.d("NOWWWWWWW", Long.toString(now));

                double maxDist = 2.5;

                Log.d("COUNNNNNNNTTBBEEFFOOREE", "" + visits.size());


                for (org.altbeacon.beacon.Beacon oneBeacon : collection) {
                    Toast.makeText(getApplicationContext(), "" + oneBeacon.getId1().toUuid().toString() + " at " + oneBeacon.getDistance(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "!!!!!!!!!!FOUND " + oneBeacon.getId1().toUuid().toString() + " at " + oneBeacon.getDistance());
                    if (!rangedBeacons.contains(oneBeacon) && oneBeacon.getDistance() < maxDist) {

                        visits.put(oneBeacon, now);

                        Log.i(TAG, "oneBeacon.getId1().toUuid().toString():  " + oneBeacon.getId1().toUuid().toString());
                        showNotifications(oneBeacon.getId1().toUuid().toString());
                        rangedBeacons.add(oneBeacon);
                    } else if (oneBeacon.getDistance() > maxDist) {
                        rangedBeacons.remove(oneBeacon);

//                        rangedBeacons.remove(oneBeacon);
//                        Log.i(TAG, "rangedBeacons size  " + rangedBeacons.size());
                    }

//                    if(visits.containsKey(oneBeacon)){
//                        Long diff = now - visits.get(oneBeacon);
//                        Log.d("DDIIIIIIIIIIFFFFFFFF", Long.toString(now));
//
//                        if(diff > 10000) {
//                            visits.remove(oneBeacon);
//                            rangedBeacons.remove(oneBeacon);
//                        }
//                    }
                }

                ArrayList<Beacon> delete = new ArrayList<>();

                for (Map.Entry<Beacon, Long> visit :
                        visits.entrySet()) {
                    if (!collection.contains(visit.getKey())) {
                        Long diff = now - visits.get(visit.getKey());
                        Log.d("LEFTT", "" + Long.toString(visits.get(visit.getKey())));
                        Log.d("DDIIIIIIIIIIFFFFFFFF", Long.toString(diff));

                        if (diff > 20000) {
                            delete.add(visit.getKey());
//                            visits.remove(visit.getKey());
                            rangedBeacons.remove(visit.getKey());
                        }
                    }
                }

                for (Beacon beacon :
                        delete) {
                    visits.remove(beacon);
                }

                Log.d("COUNNNNNNNTT", "" + visits.size());
            }
        });

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "didEnterRegion     :  ");

            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "didExitRegion     :  ");

                rangedBeacons.clear();
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i(TAG, "didDetermineStateForRegion     :  ");


            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy     :  ");
        try {
            beaconManager.stopMonitoringBeaconsInRegion(region);
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.unbind(this);
    }

//    private void showToast(String code){
//        Toast.makeText(getApplicationContext(), "sfdfs", Toast.LENGTH_LONG).show();
//    }

    private void showNotifications(String beaconCode) {

        for (Rule rule : DBRuleHandler.getInstance().getRules()) {
            if (rule.getBeaconUUID().equals(beaconCode)) {
                Intent launch = getPackageManager().getLaunchIntentForPackage(rule.getAppPackage());
                startActivity(launch);
            }
        }
    }
}