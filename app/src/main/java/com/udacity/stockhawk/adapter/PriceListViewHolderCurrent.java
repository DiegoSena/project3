package com.udacity.stockhawk.adapter;

import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;

/**
 * Created by diego on 09/01/18.
 */

public class PriceListViewHolderCurrent extends PriceListViewHolder {

    private TextView currentPrice;
    private TextView currentVariation;

    public PriceListViewHolderCurrent(View itemView) {
        super(itemView);
        this.currentPrice = (TextView) itemView.findViewById(R.id.current_price);
        this.currentVariation = (TextView) itemView.findViewById(R.id.current_variation_textview);
    }

    public void setCurrentPrice(String currentPrice){
        this.currentPrice.setText(currentPrice);
        this.currentPrice.setContentDescription(currentPrice);
    }

    public void setCurrentVariation(String currentVariation){
        this.currentVariation.setText(currentVariation);
        this.currentVariation.setContentDescription(currentVariation);
    }


    public void setCurrentVariationBackground(int currentVariationBackground) {
        this.currentVariation.setBackgroundResource(currentVariationBackground);
    }
}
