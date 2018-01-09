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

    public void setCurrentPrice(double currentPrice){
        this.currentPrice.setText(String.valueOf(currentPrice));
        this.currentPrice.setContentDescription(String.valueOf(currentPrice));
    }

    public void setCurrentVariation(double currentVariation){
        this.currentVariation.setText(String.valueOf(currentVariation));
        this.currentVariation.setContentDescription(String.valueOf(currentVariation));

        if(currentVariation > 0){
            this.currentVariation.setBackgroundResource(R.drawable.percent_change_pill_green);
        }else{
            this.currentVariation.setBackgroundResource(R.drawable.percent_change_pill_red);
        }
    }


}
