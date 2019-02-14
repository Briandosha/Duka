package com.example.brian.duka;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

//implementing onclicklistener
public class search_product extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan,btnsearch;
    private TextView textViewName, textViewAddress,txt3,txt4,txt5,txt6;
    private EditText ed1;
    private String bcode,bcode2,bcode3,bcode4,pcode,pname,pdesc,pquantity,pquantity2,iz;
    private RecyclerView searched_list;
    private String edString;
    private String barcode;
    private LinearLayout sell_layout;
    private int x,y,z;



    //qr code scanner object
    private IntentIntegrator qrScan;

    DatabaseReference databaseProducts;
    DatabaseReference dbr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        //View objects

        searched_list = (RecyclerView) findViewById(R.id.searched_list);
        searched_list.setHasFixedSize(true);
        searched_list.setLayoutManager(new LinearLayoutManager(this));

         pcode = "Product Code:";
        pdesc = "Product Unit Of Measurements:";
        pname = "Product Name:";
        pquantity = "Product Quantity:";
        pquantity2 = "New Product Quantity:";
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt5 = (TextView) findViewById(R.id.txt5);
        txt6 = (TextView) findViewById(R.id.txt6);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        ed1 = (EditText) findViewById(R.id.ed1);
        btnsearch = (Button) findViewById(R.id.btnsearch);
        sell_layout = (LinearLayout) findViewById(R.id.sell_layout);



        sell_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(search_product.this, "Long Click working", Toast.LENGTH_SHORT).show();
                sell_dialog();
                return true;
            }
        });




        Intent intent = getIntent();

        /*
        * this line is important
        * this time we are not getting the reference of a direct node
        * but inside the node track we are creating a new child with the artist id
        * and inside that node we will store all the products with unique ids
        * */
        databaseProducts = FirebaseDatabase.getInstance().getReference().child("products");


           btnsearch.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   barcode = ed1.getText().toString();

                   final Query qRef = databaseProducts.orderByKey();

                   qRef.addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                           for (DataSnapshot shudren: dataSnapshot.getChildren()){
                             //  Log.v("Query","qr"+shudren.child("productCode").getValue());

                        bcode = shudren.child("productCode").getValue().toString();

//                               shudren.getRef().child("productName").setValue("ugali");
                               Log.v("Yobro",barcode);
                               if (!bcode.isEmpty()&& bcode.equals(barcode)) {
                           // Log.v("Yobro",bcode);
//                            txt3.setText(bcode);
                                   txt3.setText(shudren.child("productCode").getValue().toString());
                                   txt4.setText(shudren.child("productName").getValue().toString());
                                   txt5.setText(shudren.child("productQuantity").getValue().toString());
                                   txt6.setText(shudren.child("description").getValue().toString());
                                   bcode2 = shudren.child("productName").getValue().toString();
                                   bcode3 = shudren.child("description").getValue().toString();
                                   bcode4 = shudren.child("productQuantity").getValue().toString();
                                   dbr2 =shudren.getRef().child("productQuantity");





                        }

                        else{
                                   Toast.makeText(search_product.this, "null pointer", Toast.LENGTH_SHORT).show();
                               }
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



               }
           });






        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
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
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                     edString = result.getContents();
                    Toast.makeText(this, edString, Toast.LENGTH_LONG).show();
                    ed1.setText(edString);

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


    private void sell_dialog () {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.sell_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView TextViewCode = (TextView) dialogView.findViewById(R.id.TextView1);
        final TextView TextViewName = (TextView) dialogView.findViewById(R.id.TextView2);
        final TextView TextViewdescription = (TextView) dialogView.findViewById(R.id.TextView3);
        final TextView TextViewquantity = (TextView) dialogView.findViewById(R.id.TextView4);
        final EditText editTextquantity2 = (EditText) dialogView.findViewById(R.id.editText5);
        final TextView TextViewquantity3 = (TextView) dialogView.findViewById(R.id.TextView6);
        final Button buttonsell = (Button) dialogView.findViewById(R.id.buttonsell);


            dialogBuilder.setTitle(bcode2);
            final AlertDialog b = dialogBuilder.create();
            b.show();

            TextViewCode.setText(pcode + barcode);
            TextViewName.setText(pname + bcode2);
            TextViewdescription.setText(pdesc + bcode3);
            TextViewquantity.setText(pquantity + bcode4);


        TextWatcher inputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                 x = Integer.parseInt(editable.toString());
                 y = Integer.parseInt(bcode4);
                 z = y-x;
                iz = String.valueOf(z);
                TextViewquantity3.setText(pquantity2 + iz);
            }
        };
        editTextquantity2.addTextChangedListener(inputTextWatcher);
//


        buttonsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbr2.setValue(iz);
                b.dismiss();
            }
        });
//
//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = editTextName.getText().toString().trim();
//                String date = editTextDate.getText().toString().trim();
//                if (!TextUtils.isEmpty(name)) {
//                    updateStock(stockId, name, date);
//                    b.dismiss();
//                }
//            }
//        });


//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                /*
//                * we will code this method to delete the artist
//                * */
//                deleteStock(stockId);
//                b.dismiss();
//            }
//        });
    }

//    TextWatcher inputTextWatcher = new TextWatcher() {
//        public void afterTextChanged(Editable s) {
//            textview.setText(s.toString());
//        }
//        public void beforeTextChanged(CharSequence s, int start, int count, int after){
//        }
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//        }
//    };
//
//editText.addTextChangedListener(inputTextWatcher);

    }

