package com.example.brian.duka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class login extends AppCompatActivity {

    Button btnlogin;
    EditText inputemail,inputpassword;
    TextView link_signup;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(login.this, Signup.class));
//            finish();
//        }

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() == null){
//                   Intent loginintent = new Intent(login.this, Signup.class);
//                        loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                              startActivity(loginintent);
//                }
//            }
//        };
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        btnlogin = (Button)findViewById(R.id.btn_login);
        inputemail = (EditText)findViewById(R.id.input_email);
        inputpassword= (EditText)findViewById(R.id.input_password);
        link_signup = (TextView) findViewById(R.id.link_signup);


        mProgress = new ProgressDialog(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgress.setMessage("Loging in just a minute.....");
                mProgress.show();
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    mProgress.dismiss();
                                    if (password.length() < 6) {
                                        inputpassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    mProgress.dismiss();
                                    Intent intent = new Intent(login.this, home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });


        link_signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Sign up screen
                Intent i = new Intent(getApplicationContext(), Signup.class);
                startActivity(i);
            }
        });

    }
}