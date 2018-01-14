package com.udacity.stockhawk.adapter;

import java.util.Date;

/**
 * Created by diego on 09/01/18.
 */

public class Price {

    private double price;
    private Date date;
    private double percentageChange;

    private double absoluteChange;

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

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public double getAbsoluteChange() {
        return absoluteChange;
    }

    public void setAbsoluteChange(double absoluteChange) {
        this.absoluteChange = absoluteChange;
    }

    @Override
    public String toString() {
        return date.toString() + " - " + String.valueOf(price);
    }
}
