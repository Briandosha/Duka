package com.example.brian.duka;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import android.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;


import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    Button btnaddstock,btnanalytics,btnsell;
    String meso;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            meso = extras.getString("messo");
        }


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent loginintent = new Intent(home.this, login.class);
                    loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginintent);
                }
            }
        };

        btnaddstock = (Button) findViewById(R.id.btn_addstock);
        btnsell = (Button) findViewById(R.id.btn_sell);
        btnanalytics = (Button) findViewById(R.id.btn_analytics);

        btnaddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),main.class);
                startActivity(intent);
            }
        });

        btnsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),search_product.class);
                startActivity(intent);
            }
        });


        btnanalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),summary.class);
                startActivity(intent);
            }
        });

        sendNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout)
            logout();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

    }
    public void sendNotification(){

        //get an instance of new notification manager
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("Stock running low");
        mBuilder.setContentText("Stock for"+meso+" and others is running low");


        Intent resultIntent = new Intent(this, summary.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(summary.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(001, mBuilder.build());


    }

    private void logout() {

        auth.signOut();
    }
}