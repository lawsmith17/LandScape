package com.example.landscape.login_and_register;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.*;

import com.example.landscape.activities.MainActivity;
import com.example.landscape.R;
import com.example.landscape.login_and_register.LoginRegisterActivity;
import com.example.landscape.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText mName, mEmail,mPassword,mRetype_password;
    Button mRegButton;

    FirebaseAuth mAuth;
    ProgressBar progressBar;
FirebaseDatabase database;

DatabaseReference reference;
 String UserID;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_register, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //the data collected from user
      mName = view.findViewById(R.id.et_name);
     mRegButton  = view.findViewById(R.id.btn_register);
  mEmail  = view.findViewById(R.id.et_email);
     mPassword= view.findViewById(R.id.et_password);
     mRetype_password = view.findViewById(R.id.et_repassword);


     mAuth  = FirebaseAuth.getInstance();
     database = FirebaseDatabase.getInstance();
     reference = database.getReference("Users");
        //progress bar to let the user know processing hass began
      progressBar = view.findViewById(R.id.progress_bar);

      // when the user clicks the register button
      mRegButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(final View view) {
              final String email = mEmail.getText().toString().trim();
              final String password = mPassword.getText().toString().trim();
              final String fulname = mName.getText().toString().trim();
              if(TextUtils.isEmpty(email))
              {
                  //check to see if email is empty
                  mEmail.setError("Email is Required");
                  return;
              }

              if(TextUtils.isEmpty(email))
              {
                  //check to see if email is empty
                  mPassword.setError("password is Required");
                  return;
              }

              if(password.length() < 6)
              {
                  mPassword.setError("Password Must be >= 6 Characters");
              }

              progressBar.setVisibility(View.VISIBLE);
// in firebase dtatbase

              mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()) {
                          Toast.makeText(getContext(), "User Created ", Toast.LENGTH_LONG).show();
                          UserID = mAuth.getCurrentUser().getUid();
                          savetodp(UserID,fulname,email);
                          LoginRegisterActivity.viewPager.setCurrentItem(0,true);
progressBar.setVisibility(View.INVISIBLE);
                      }else
                      {
                          progressBar.setVisibility(View.INVISIBLE);
                          Toast.makeText(getContext(), "Error : "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();

                      }
                  }


              });


          }
      });
    }

    public void  savetodp( String id,String name,String email)
    {
        User user = new User(name,email);
        reference.child(id).child("User").setValue(user);
    }



}
