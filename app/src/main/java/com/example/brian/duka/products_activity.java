package com.example.brian.duka;

import android.app.DatePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


public class products_activity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAddProduct,btnbarcode;
   private EditText editTextCode, editTextName, editTextQuantity, editTextDescription;
    TextView  textViewProduct,textview1,textview2;
    ListView listViewProducts;
    public String resultcode1;

    private ZXingScannerView scannerView;

    //qr code scanner object
    private IntentIntegrator qrScan;

    DatabaseReference databaseProducts;

    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Intent intent = getIntent();

        /*
        * this line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the products with unique ids
        * */
        databaseProducts = FirebaseDatabase.getInstance().getReference("products").child(intent.getStringExtra(main.ARTIST_ID));

        buttonAddProduct = (Button) findViewById(R.id.buttonAddProduct);
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        textViewProduct = (TextView) findViewById(R.id.textViewProduct);
        listViewProducts = (ListView) findViewById(R.id.listViewProducts);
        btnbarcode = (Button) findViewById(R.id.btnbarcode);
        textview1 = (TextView) findViewById(R.id.textview1);
        textview2 = (TextView) findViewById(R.id.textview2);

        products = new ArrayList<>();

        textViewProduct.setText(intent.getStringExtra(main.ARTIST_NAME));


        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });
        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = products.get(i);
                showUpdateDeleteDialog(product.getid(), product.getProductName());
                return true;
            }
        });


        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        btnbarcode.setOnClickListener(this);


    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textview1.setText(obj.getString("name"));
                    textview2.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    String rss1 = result.getContents();
                    Toast.makeText(this, rss1, Toast.LENGTH_LONG).show();
                    editTextCode.setText(rss1);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
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
                Product_list productListAdapter = new Product_list(products_activity.this, products);
                listViewProducts.setAdapter(productListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


        private void saveProduct() {
        String productCode = editTextCode.getText().toString().trim();
        String productName = editTextName.getText().toString().trim();
        String productQuantity = editTextQuantity.getText().toString().trim();
        String productDescription = editTextDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(productName)) {
            String id  = databaseProducts.push().getKey();
            Product product = new Product(id, productCode, productName, productQuantity, productDescription);
            databaseProducts.child(id).setValue(product);
            Toast.makeText(this, "Product saved", Toast.LENGTH_LONG).show();
            editTextCode.setText("");
            editTextName.setText("");
            editTextQuantity.setText("");
            editTextDescription.setText("");
        } else {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_LONG).show();
        }
    }


    private boolean updateProduct(String id, String productCode,  String productName, String productQuantity,  String description) {
        //getting the specified product reference
        DatabaseReference dR = databaseProducts.child(id);

        //updating product
        Product product = new Product(id, productCode, productName, productQuantity, description);
        dR.setValue(product);
        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
        return true;
    }


    private void showUpdateDeleteDialog (final String id, String productName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.updae_dialog2, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextbCode = (EditText) dialogView.findViewById(R.id.editTextbcode);
        final EditText editTextbName = (EditText) dialogView.findViewById(R.id.editTextbName);
        final EditText editTextbQuantity = (EditText) dialogView.findViewById(R.id.editTextbquantity);
        final EditText editTextbUOM = (EditText) dialogView.findViewById(R.id.editTextuom);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonbUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonbDeleteProduct);



        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productCode = editTextbCode.getText().toString().trim();
                String productName = editTextbName.getText().toString().trim();
                String productQuantity = editTextbQuantity.getText().toString().trim();
                String description = editTextbUOM.getText().toString().trim();
                if (!TextUtils.isEmpty(productCode)) {
                    updateProduct(id,  productCode,  productName, productQuantity, description);
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
                deleteStock(id);
                b.dismiss();
            }
        });
    }

    private boolean deleteStock(String id) {
        //getting the specified artist reference
        DatabaseReference dR = databaseProducts.child(id);

        //removing artist
        dR.removeValue();

        //getting the tracks reference for the specified artist
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("products").child(id);

        //removing all tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Stock Deleted", Toast.LENGTH_LONG).show();

        return true;
    }


}
