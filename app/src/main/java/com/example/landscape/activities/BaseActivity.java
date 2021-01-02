package com.example.landscape.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.landscape.utils.constants.*;


public class BaseActivity extends AppCompatActivity {
    final String TAG = "BaseActivity";


    String uid;
    public static SharedPreferences sharedPref;
    private  static  SharedPreferences.Editor editor;

   // protected static  DatabaseReference mRootRef, mUsersRef, mUserInfoRef;

   // FirebaseAuth mAuth;
  //  DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      //  mAuth  = FirebaseAuth.getInstance();


     sharedPref = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    editor = sharedPref.edit();



     //   mRootRef = firebaseDatabase.getReference();

//        putString(UID,mAuth.getCurrentUser().getUid());






    }

    public static void putPString(String key,String value)
    {
        editor.putString(key,value);
        editor.apply();
    }

    public static String getPString(String key,String defvalue)
    {
        return  sharedPref.getString(key,defvalue);
    }

    public  static  void putPInt(String key,int value)
    {
        editor.putInt(key,value);
        editor.apply();
    }





    public static int getPInt(String key,int defvalue)
    {
        return  sharedPref.getInt(key,defvalue);
    }




}