package com.kpi.beaconsapp;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.kpi.beaconsapp.model.DBRuleHandler;
import com.kpi.beaconsapp.model.Rule;


public class RuleEditActivity extends AppCompatActivity {
    DBRuleHandler db = DBRuleHandler.getInstance();
    int ruleId;
    Rule rule;
    TextView ruleNameView;
    TextView beaconIDView;
    TextView appView;
    TextView distView;
    BottomNavigationItemView save;
    BottomNavigationItemView remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_details);

        ruleNameView = findViewById(R.id.noteName);
        beaconIDView = findViewById(R.id.noteText);
        appView = findViewById(R.id.noteBeacons);
        distView = findViewById(R.id.distance);
        save = findViewById(R.id.save_rule);
        remove = findViewById(R.id.delete_rule);

        ruleId = getIntent().getIntExtra("com.kpi.beaconsapp.ruleid", 0);
        rule = db.getRules().get(ruleId);

        ruleNameView.setText(rule.getName());
        beaconIDView.setText(rule.getBeaconUUID());
        appView.setText(rule.getAppName());
        distView.setText(String.valueOf(rule.getDistance()));
        Toast.makeText(getApplicationContext(), String.valueOf(rule.getDistance()), Toast.LENGTH_SHORT).show();

        save.setOnClickListener(v -> saveRule());
        remove.setOnClickListener(v -> removeRule());
    }

    public void saveRule(){
        String ruleName = ruleNameView.getText().toString();
        String ds = distView.getText().toString();
        try{
            if (ds.length() < 1){
                Toast.makeText(getApplicationContext(), "Invalid distance", Toast.LENGTH_SHORT).show();
            }
            double dist = Double.parseDouble(ds);
            rule.setDistance(dist);
            rule.setName(ruleName);
            db.updateRule(rule);
            finish();
        }
        catch (NullPointerException | NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Invalid field", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeRule(){
        db.deleteRule(rule);
        finish();
    }
}
