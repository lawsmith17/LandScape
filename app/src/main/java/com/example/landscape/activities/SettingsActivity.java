package com.example.landscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static  com.example.landscape.utils.constants.*;
import static  com.example.landscape.activities.BaseActivity.*;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.landscape.classes.MetricSystem;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.example.landscape.R;

public class SettingsActivity extends BaseActivity {
SwitchMaterial Metric;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    String userID;
    DatabaseReference reference;
    DatabaseReference metricrefernce;

    RelativeLayout profileConationer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Metric  = findViewById(R.id.metric_switch);
       Toolbar toolbar =  findViewById(R.id.toolbar_gallery);
        mAuth  = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        reference = database.getReference("Users");
        metricrefernce = database.getReference("Users/"+userID);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    putPString(CURRENT_DISTANCE_METRIC, "KM");

                }
                else
                {

                    putPString(CURRENT_DISTANCE_METRIC, "Meters");

                }


                saveMetric();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        metricrefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                loadSavedMetric(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void saveMetric()
    {
  MetricSystem system = new MetricSystem(getPString(CURRENT_DISTANCE_METRIC,"Meters"));

     Map<String,Object> upd = new HashMap<>();


            upd.put("CurrentDistanceType",system.getCurrentDistanceType());

            reference.child(userID).child("Metric").updateChildren(upd);

    }



    public  void loadSavedMetric(DataSnapshot snapshot)
    {

        try {


            MetricSystem shot = snapshot.child("Metric").getValue(MetricSystem.class);

            if(shot != null) {
                putPString(CURRENT_DISTANCE_METRIC,shot.getCurrentDistanceType());



                String Distance = getPString(CURRENT_DISTANCE_METRIC,"Meters");


                if(Distance.equals("Meters"))
                {

                  Metric.setChecked(false);
                }else
                if(Distance.equals("KM"))
                {
                  Metric.setChecked(true);
                }

            }





        }catch (Exception er)
        {
           er.printStackTrace();
        }

    }


    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}