package com.example.landscape.login_and_register;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landscape.activities.MainActivity;

import com.example.landscape.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText  mEmail,mPassword;
    Button mLogButton;

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);





    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mLogButton  = view.findViewById(R.id.btn_login);
        mEmail  = view.findViewById(R.id.et_email);
        mPassword= view.findViewById(R.id.et_password);

        mAuth  = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progress_bar);


        mLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mPassword.setError("password is Required");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                }

                progressBar.setVisibility(View.VISIBLE);


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "login successful", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                            startActivity(new Intent(getContext(), MainActivity.class));

                        } else {
                            Toast.makeText(getContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });


            }

        });
    }
}
