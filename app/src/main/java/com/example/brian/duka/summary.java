package com.example.brian.duka;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.brian.duka.main.ARTIST_ID;
import static com.example.brian.duka.main.ARTIST_NAME;

public class summary extends AppCompatActivity {

    ListView listviewstock2;
    Button btnfinished,btnmail;

    //a list to store all the stock from firebase database
    List<Stock> stocks;

    //our database reference object
    DatabaseReference databaseStocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        listviewstock2 = (ListView) findViewById(R.id.listviewstock2);
        btnfinished = (Button) findViewById(R.id.btn_finished);
        btnmail = (Button) findViewById(R.id.btn_mail);

        //getting the reference of stock node
        databaseStocks = FirebaseDatabase.getInstance().getReference("stocks");

        //list to store stocks
        stocks = new ArrayList<>();

        //adding onclicklistener to button
        btnmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ndungubrian412@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(summary.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //attaching listener to listview
        listviewstock2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Stock stock = stocks.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), summary2.class);

                //putting stock name and id to intent
                intent.putExtra(ARTIST_ID, stock.getStockId());
                intent.putExtra(ARTIST_NAME, stock.getStockName());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        btnfinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),summary3.class);
                startActivity(intent);
            }
        });

        sendNotification();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseStocks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous stock list
                stocks.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting stock
                    Stock stock = postSnapshot.getValue(Stock.class);
                    //adding stock to the list
                    stocks.add(stock);
                }

                //creating adapter
                Stock_list stockAdapter = new Stock_list(summary.this, stocks);
                //attaching adapter to the listview
                listviewstock2.setAdapter(stockAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendNotification(){

        //get an instance of new notification manager
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("Stock running low");
        mBuilder.setContentText("Stock for and others is running low");


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
}
