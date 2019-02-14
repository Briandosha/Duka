package com.example.brian.duka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Brian on 4/20/2017.
 */

public class Signup extends AppCompatActivity {

     EditText inputEmail, inputPassword;
     Button btnSignUp;
    private FirebaseAuth auth;
    private ProgressDialog mProgress;
    TextView link_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        link_login = (TextView) findViewById(R.id.link_login);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 5) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 5 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                       mProgress.setMessage("Signing up....");
                            mProgress.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Signup.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Signup.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    mProgress.dismiss();
                                    startActivity(new Intent(Signup.this, home.class));
                                    finish();
                                }
                            }
                        });

            }
        });


        link_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Sign up screen
                Intent i = new Intent(getApplicationContext(), login.class);
                startActivity(i);
            }
        });
    }
}