package com.example.brian.duka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class summary2 extends AppCompatActivity {

ListView listviewproduct2;
    TextView textviewproduct2;

    DatabaseReference databaseProducts;

    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary2);

        Intent intent = getIntent();
                /*
        * this line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the products with unique ids
        * */
        databaseProducts = FirebaseDatabase.getInstance().getReference("products").child(intent.getStringExtra(main.ARTIST_ID));

        textviewproduct2 = (TextView) findViewById(R.id.textViewproducts2);
        listviewproduct2 = (ListView) findViewById(R.id.listviewproduct2);

        products = new ArrayList<>();

        textviewproduct2.setText(intent.getStringExtra(main.ARTIST_NAME));
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    products.add(product);
                }
                Product_list productListAdapter = new Product_list(summary2.this, products);
                listviewproduct2.setAdapter(productListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
