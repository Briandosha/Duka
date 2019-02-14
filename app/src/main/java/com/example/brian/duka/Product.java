package com.example.brian.duka;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Brian on 5/10/2017.
 */
@IgnoreExtraProperties
public class Product {
    private String id;
    private String productCode;
    private String productName;
    private String productQuantity;
    private String description;

    public Product() {

    }

    public Product(String id, String productCode, String productName, String productQuantity, String description) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.description = description;
        this.productCode = productCode;
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getDescription() {
        return description;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getid() {
        return id;
    }

}

