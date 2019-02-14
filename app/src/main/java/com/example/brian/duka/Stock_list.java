package com.example.brian.duka;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Brian on 5/16/2017.
 */

public class Stock_list extends ArrayAdapter<Stock> {
    private Activity context;
    List<Stock> stocks;

    public Stock_list(Activity context, List<Stock> stocks) {
        super(context, R.layout.stock_list, stocks);
        this.context = context;
        this.stocks = stocks;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.stock_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);

        Stock stock = stocks.get(position);
        textViewName.setText(stock.getStockName());
        textViewDate.setText(stock.getStockdate());

        return listViewItem;
    }
}