package com.udacity.stockhawk.adapter;

import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;

/**
 * Created by diego on 09/01/18.
 */

public class PriceListViewHolderHistory extends PriceListViewHolder {

    private TextView date;
    private TextView price;
    private TextView variation;

    public PriceListViewHolderHistory(View itemView) {
        super(itemView);
        date = (TextView) itemView.findViewById(R.id.price_list_date_textview);
        price = (TextView) itemView.findViewById(R.id.price_list_price_textview);
        variation = (TextView) itemView.findViewById(R.id.price_list_variation_textview);
    }

    public void setDate(String date) {
        this.date.setText(date);
        this.date.setContentDescription(date);
    }

    public void setPrice(String price){
        this.price.setText(price);
        this.price.setContentDescription(price);
    }

    public void setVariation(String variation){
        this.variation.setText(variation);
        this.variation.setContentDescription(String.valueOf(variation));
    }

    public void setVariationBackground(int variationBackground) {
        this.variation.setBackgroundResource(variationBackground);
    }
}
