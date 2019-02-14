package com.example.brian.duka;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Brian on 4/26/2017.
 */
@IgnoreExtraProperties
public class Stock  {

    private String stockId;
    private String stockName;
    private String stockdate;

    public Stock(){
        //this constructor is required
    }

    public Stock(String stockId, String stockName, String stockdate) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.stockdate = stockdate;
    }

    public String getStockId() {
        return stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public String getStockdate(){
        return stockdate;
    }

}
