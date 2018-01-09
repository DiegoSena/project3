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

    public void setPrice(double price){
        this.price.setText(String.valueOf(price));
        this.price.setContentDescription(String.valueOf(price));
    }

    public void setVariation(double variation){
        this.variation.setText(String.valueOf(variation));
        this.variation.setContentDescription(String.valueOf(variation));

        if(variation > 0){
            this.variation.setBackgroundResource(R.drawable.percent_change_pill_green);
        }else{
            this.variation.setBackgroundResource(R.drawable.percent_change_pill_red);
        }
    }
}
