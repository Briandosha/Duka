package com.example.brian.duka;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Brian on 5/16/2017.
 */

public class Product_list extends ArrayAdapter<Product> {
private Activity context;
        List<Product> products;

public Product_list(Activity context, List<Product> products) {
        super(context, R.layout.list_product, products);
        this.context = context;
        this.products = products;
        }


@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_product, null, true);

        TextView textViewCode =  (TextView) listViewItem.findViewById(R.id.textViewCode);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewQuantity = (TextView) listViewItem.findViewById(R.id.textViewQuantity);
        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.textViewDescription);

        Product product = products.get(position);
        textViewCode.setText(product.getProductCode());
        textViewName.setText(product.getProductName());
        textViewQuantity.setText(product.getProductQuantity());
        textViewDescription.setText(product.getDescription());

        return listViewItem;
        }
        }