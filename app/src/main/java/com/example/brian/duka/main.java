package com.example.brian.duka;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.text.TextUtils;
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

public class main extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //we will use these constants later to pass the artist name and id to another activity

    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";

    //view objects
    private EditText date;
    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;
    EditText stockname;
    Button btnaddstock;
    ListView listviewstock;

    //a list to store all the stock from firebase database
    List<Stock> stocks;

    //our database reference object
    DatabaseReference databaseStocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getting the reference of artists node
        databaseStocks = FirebaseDatabase.getInstance().getReference("stocks");

        //retrieve the views
        stockname = (EditText) findViewById(R.id.editTextName);
        listviewstock = (ListView) findViewById(R.id.listviewstock);
        btnaddstock = (Button) findViewById(R.id.buttonAddstock);
        date = (EditText) findViewById(R.id.editTextdate);
        date.setFocusable(false);

        //list to store artists
        stocks = new ArrayList<>();

        date.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(main.this, main.this, 2014, 5, 20);
            dialog.show();
        }
    });

        //adding an onclicklistener to button
        btnaddstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addStock()
                //the method is defined below
                //this method is actually performing the write operation
                addStock();
            }
        });

        //attaching listener to listview
        listviewstock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Stock stock = stocks.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), products_activity.class);

                //putting stock name and id to intent
                intent.putExtra(ARTIST_ID, stock.getStockId());
                intent.putExtra(ARTIST_NAME, stock.getStockName());

                //starting the activity with intent
                startActivity(intent);
            }
        });


        listviewstock.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stock stock = stocks.get(i);
                showUpdateDeleteDialog(stock.getStockId(), stock.getStockName());
                return true;
            }
        });

    }

    /*
   * This method is saving a new stock to the
   * Firebase Realtime Database
   * */
    private void addStock() {
        //getting the values to save
        String name = stockname.getText().toString().trim();
        String text_date = date.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Stock
            String id = databaseStocks.push().getKey();

            //creating a Stock Object
            Stock stock = new Stock(id, name, text_date);

            //Saving the Artist
            databaseStocks.child(id).setValue(stock);

            //setting edittext to blank again
            stockname.setText("");

            //displaying a success toast
            Toast.makeText(this, "Stock added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
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
                Stock_list stockAdapter = new Stock_list(main.this, stocks);
                //attaching adapter to the listview
                listviewstock.setAdapter(stockAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean updateStock(String id, String name, String date) {
        //getting the specified stock reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("stocks").child(id);

        //updating artist
        Stock stock = new Stock(id, name, date);
        dR.setValue(stock);
        Toast.makeText(getApplicationContext(), "Stock Updated", Toast.LENGTH_LONG).show();
        return true;
    }


    private void showUpdateDeleteDialog (final String stockId, String stockName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextDate = (EditText) dialogView.findViewById(R.id.editTextdate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateStock);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteStock);


        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(main.this, main.this, 2014, 5, 20);
                dialog.show();
            }
        });

        dialogBuilder.setTitle(stockName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateStock(stockId, name, date);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                * we will code this method to delete the artist
                * */
                        deleteStock(stockId);
                        b.dismiss();
                    }
                });
    }

    private boolean deleteStock(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("stocks").child(id);

        //removing artist
        dR.removeValue();

        //getting the tracks reference for the specified artist
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("products").child(id);

        //removing all tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Stock Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        date.setText( "" + dayofmonth + "-" + (month + 1) + "-" + year );
    }
}

