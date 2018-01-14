package com.udacity.stockhawk.adapter;

import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriceListViewHolderCurrent extends PriceListViewHolder {

    @BindView(R.id.current_price)
    TextView currentPrice;
    @BindView(R.id.current_variation_textview)
    TextView currentVariation;

    public PriceListViewHolderCurrent(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
