package com.example.landscape.utils;

import android.app.Application;
import android.content.Intent;

import com.example.landscape.activities.MainActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class KeepUserLogin extends Application {


    @Override
    public void onCreate() {


        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

       if(firebaseUser != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }
    }


}
