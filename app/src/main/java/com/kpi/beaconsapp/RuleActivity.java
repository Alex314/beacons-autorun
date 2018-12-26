package com.kpi.beaconsapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kpi.beaconsapp.model.Beacon;
import com.kpi.beaconsapp.model.DBRuleHandler;
import com.kpi.beaconsapp.model.DataBaseConnector;
//import com.kpi.beaconsapp.model.DataBaseEmulator;
//import com.kpi.beaconsapp.model.Note;
import com.kpi.beaconsapp.model.Rule;
//import com.kpi.beaconsapp.select.beacons.ListViewWithCheckboxActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class RuleActivity extends AppCompatActivity {
    DataBaseConnector db = DBRuleHandler.getInstance();
    int rule_id;
    Rule rule;
    Beacon beacon;
    View root;
    TextView ruleNameView;
    TextView ruleDescView;
    TextView appTextView;
    public ArrayList<String> apps;
    public ArrayList<String> apps2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_apps_activity);
        setTitle("Choose beacon");

        rule_id = getIntent().getIntExtra("com.example.bogdanaiurchienko.myapplication.NOTE_ID", 0);
        beacon = db.getBeacons().get(rule_id);

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
        AppItemAdapter appItemAdapter = new AppItemAdapter(this, apps);
        allAppsView.setAdapter(appItemAdapter);

        allAppsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final PackageManager pm = getPackageManager(); //get a list of installed apps.
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                apps2 = new ArrayList<>();
                for (ApplicationInfo pkg: packages) {
                    if((pm.getLaunchIntentForPackage(pkg.packageName) != null) ){ // & (((pkg.flags & ApplicationInfo.FLAG_SYSTEM) == 0))) {
                        apps.add((String) pkg.packageName);
                    }
                }

                Rule r = new Rule(i, beacon.getCode(), apps.get(i), apps.get(i));
                db.addRule(r);
                Log.i("click", " " + beacon.getCode());

//                Intent addRule = new Intent(getApplicationContext(), RuleActivity.class);
//                lastRule = i;
//                addRule.putExtra("com.kpi.beaconsapp.NOTE_ID", db.getRules().size() -1 - i);
//                startActivity(addRule);
            }
        });
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


//        findViewById(R.id.set_color).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setColour(view);
//            }
//        });

}
//
//public class NoteDetailsActivity extends AppCompatActivity {
//
//    DataBaseConnector db = DataBaseEmulator.getInstance();
//    int noteId;
//    Note note;
//    View root;
//    TextView noteNameView;
//    TextView noteTextView;
//    TextView noteBeaconsView;
//    String color;
//
//    LinkedHashMap<String, String> colors = new LinkedHashMap<>();
//    {
//        colors.put("Honey Suckle", "#fcc875");
//        colors.put("#fcc875", "Honey Suckle");
//        colors.put("Warm Gray", "#baa896");
//        colors.put("#baa896", "Warm Gray");
//        colors.put("Putty", "#e6ccb5");
//        colors.put("#e6ccb5", "Putty");
//        colors.put("Faded Rose", "#e38b75");
//        colors.put("#e38b75", "Faded Rose");
//        colors.put("Linen", "#eae2d6");
//        colors.put("#eae2d6", "Linen");
//        colors.put("Oyster", "#d5c3aa");
//        colors.put("#d5c3aa", "Oyster");
//        colors.put("Biscotti", "#ebb582");
//        colors.put("#ebb582", "Biscotti");
//        colors.put("Hazelnut", "#d6c6b9");
//        colors.put("#d6c6b9", "Hazelnut");
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note_details);
//
//        //ставимо лісенери на елементи боттом меню
//        findViewById(R.id.set_color).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setColour(view);
//            }
//        });
//
//        findViewById(R.id.set_beacons).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setBeacons(view);
//            }
//        });
//
//        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteNote(view);
//            }
//        });
//
//        //отримуємо поля
//        noteNameView = findViewById(R.id.noteName);
//        noteTextView = findViewById(R.id.noteText);
//        noteBeaconsView = findViewById(R.id.noteBeacons);
//
//
//        //отримуємо переданий в це актівіті айді нотатки
//        noteId = getIntent().getIntExtra("com.example.bogdanaiurchienko.myapplication.NOTE_ID", 0);
//        //просимо у бази даних цю нотатку
//        note = db.getNote(noteId);
//        color = note.getColor();
//
//        //отримуємо батьківський контейнер (щоб поставити його колір, тобто колір бекграунда)
//        root = noteNameView.getRootView();
//        setTheBGColor();
//
//        //виводимо текст нотатки в поля
//        noteNameView.setText(note.getName());
//        noteTextView.setText(note.getText());
//        noteBeaconsView.setText(note.getBeaconsNames());
//    }
//
//    //зберігаємо усі зміни в бд, коли користувач нажимає кнопку "назад"
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        System.out.println("onBackPressed    "+noteNameView.getText().toString() + "   " + noteTextView.getText().toString());
//        note.setName(noteNameView.getText().toString());
//        note.setText(noteTextView.getText().toString());
//    }
//
//    //тут викликаємо діалог вибору кольору
//    public void setColour(View v) {
//
//        final Dialog dialog = new Dialog(NoteDetailsActivity.this);
//        dialog.setContentView(R.layout.set_color);
//
//        dialog.setTitle("Choose the colour");
//        dialog.setCancelable(true);
//        Window window = dialog.getWindow();
//        if(window != null) {
//            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        }
//
//        View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RadioButton rb = (RadioButton)v;
//                switch (rb.getId()) {
//                    case R.id.color1: color = "#fcc875";
//                        break;
//                    case R.id.color2: color = "#baa896";
//                        break;
//                    case R.id.color3: color = "#e6ccb5";
//                        break;
//                    case R.id.color4: color = "#e38b75";
//                        break;
//                    case R.id.color5: color = "#eae2d6";
//                        break;
//                    case R.id.color6: color = "#d5c3aa";
//                        break;
//                    case R.id.color7: color = "#ebb582";
//                        break;
//                    case R.id.color8: color = "#d6c6b9";
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//
//        ArrayList<RadioButton> buttons = new ArrayList<>();
//
//        buttons.add((RadioButton) dialog.findViewById(R.id.color1));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color2));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color3));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color4));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color5));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color6));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color7));
//        buttons.add((RadioButton) dialog.findViewById(R.id.color8));
//
//        for(RadioButton btn: buttons){
//            btn.setOnClickListener(radioButtonClickListener);
//            if(Objects.equals(colors.get(btn.getText().toString()), note.getColor())) {
//                btn.setChecked(true);
//            }
//        }
//
//        dialog.findViewById(R.id.OkButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                note.setColor(color);
//                setTheBGColor();
//                dialog.dismiss();
//            }
//        });
//
//
//        dialog.findViewById(R.id.CancelButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }
//
//    //тут викликаємо актівіті вибору біконів
//    public void setBeacons(View v) {
//        Intent intent = new Intent(this, ListViewWithCheckboxActivity.class);
//        intent.putExtra("com.kpi.beaconsapp.NOTE_ID", noteId);
//        intent.putExtra("com.kpi.beaconsapp.BG_COLOR", note.getColor());
//        startActivity(intent);
//    }
//
//    //тут видаляємо нотатку і повертаємось на новий екран
//    public void deleteNote(View v) {
//        db.deleteNote(noteId);
//        finish();
//    }
//
//    private void setTheBGColor() {
//        //ставимо колір бекграунда той, що записаний у нотатці
//        root.setBackgroundColor(Color.parseColor(color));
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        noteBeaconsView.setText(note.getBeaconsNames());
//    }
//}
//
