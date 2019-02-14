package com.example.brian.duka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class summary3 extends AppCompatActivity {

    public String ncode,tcode;
//    private TextView txt4a;
    private int nc;
    private ListView listviewsummary3;
    private List<Product> summary3List;

    DatabaseReference databaseProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary3);
//
//        Intent msg = new Intent(summary3.this, home.class);
//        msg.putExtra(ncode,messo);
//        startActivity(msg);


//        txt4a = (TextView) findViewById(R.id.txt4a);
        listviewsummary3 = (ListView) findViewById(R.id.listviewsummary3);

        summary3List = new ArrayList<>();


        Intent intent = getIntent();

        /*
        * this line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the products with unique ids
        * */
        databaseProducts = FirebaseDatabase.getInstance().getReference().child("products");



    }



    @Override
    protected void onStart() {
        super.onStart();


                final Query query = databaseProducts.orderByChild("productQuantity").startAt("1").endAt("10");

                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       summary3List.clear();

                        for (DataSnapshot outstock: dataSnapshot.getChildren()){
//                      Log.v("Query","qr"+outstock.child("productQuantity").getValue());
//
//                            Product finished = outstock.getValue(Product.class);
//                            summary3List.add(finished);
//                            Log.v("Query","qr"+outstock.child("productQuantity").getValue());
//
//                            Product_list summaryListAdapter = new Product_list(summary3.this, summary3List);
//                            listviewsummary3.setAdapter(summaryListAdapter);


                            ncode = outstock.child("productQuantity").getValue().toString();
                            nc = Integer.parseInt(ncode);
                            if (nc<=10){
                                Product finished = outstock.getValue(Product.class);
                                summary3List.add(finished);
                                Log.v("Query","qr"+outstock.child("productQuantity").getValue());
                            tcode = outstock.child("productName").getValue().toString();

                                Product_list summaryListAdapter = new Product_list(summary3.this, summary3List);
                                listviewsummary3.setAdapter(summaryListAdapter);

                                Intent i = new Intent(getApplicationContext(), home.class);
                                i.putExtra("messo",tcode);
                                startActivity(i);
                            }
                            else{
//                                Toast.makeText(summary3.this, "null pointer", Toast.LENGTH_SHORT).show();
                            }

//



                        }
                    }



                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {




                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                summary3List.clear();
//                for (DataSnapshot outstock : dataSnapshot.getChildren()) {
//                    ncode = outstock.child("productQuantity").getValue().toString();
//                    System.out.println(ncode);
//                    nc = Integer.parseInt(ncode);
//                    if (nc<=10){
//                        Product finished = outstock.getValue(Product.class);
//                        summary3List.add(finished);
//                        txt4a.setText("found it");
//                        Log.v("Query","qr"+outstock.child("productQuantity").getValue());
//
//                        Product_list summaryListAdapter = new Product_list(summary3.this, summary3List);
//                        listviewsummary3.setAdapter(summaryListAdapter);
//                    }
//                    else{
//                        Toast.makeText(summary3.this, "null ", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        String value = "value";


    }


    }


