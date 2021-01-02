package com.example.landscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.landscape.adapters.SavedLocationAdapter;
import com.example.landscape.classes.FavouritePlace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;
import com.example.landscape.R;

public class SavedLocationsActivity extends AppCompatActivity {
RecyclerView save_recycler;
    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    String userID;
    DatabaseReference savedrefernce ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);
        Toolbar toolbar =  findViewById(R.id.toolbar_gallery);
        save_recycler = findViewById(R.id.save_recycler);
        mAuth  = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        reference = database.getReference("Users");
        savedrefernce = database.getReference("Users/"+userID+"/SavedLocations");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        savedrefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                LoadPlaces(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadPlaces(DataSnapshot snapshot)
    {
        try {
            List<FavouritePlace> fav = new ArrayList<>();
            for (DataSnapshot shot : snapshot.getChildren()) {

                FavouritePlace dInfo = new FavouritePlace();




                dInfo.setPlaceName(shot.getValue(FavouritePlace.class).getPlaceName());
                dInfo.setPlaceAddresses(shot.getValue(FavouritePlace.class).getPlaceAddresses());
                dInfo.setPlaceLongitude(shot.getValue(FavouritePlace.class).getPlaceLongitude());
                dInfo.setPlaceLatitude(shot.getValue(FavouritePlace.class).getPlaceLatitude());
                fav.add(dInfo);
            }

         SavedLocationAdapter adapter = new SavedLocationAdapter(getApplicationContext(),fav );
           save_recycler.setAdapter(adapter);
            save_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        } catch(NullPointerException e)
        {
           FavouritePlace dInfo = new FavouritePlace("nothing","nothing",47.5287132,-121.8253906);
            List<FavouritePlace> info = new ArrayList<>();
            info.add(dInfo);
           SavedLocationAdapter adapter = new SavedLocationAdapter(getApplicationContext(),info );
           save_recycler.setAdapter(adapter);
            save_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}