package com.udacity.stockhawk.adapter;

import java.util.Date;

/**
 * Created by diego on 09/01/18.
 */

public class Price {

    private double price;
    private Date date;
    private double variation;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getVariation() {
        return variation;
    }

    public void setVariation(double variation) {
        this.variation = variation;
    }
}
