package com.udacity.stockhawk.adapter;

import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriceListViewHolderHistory extends PriceListViewHolder {

    @BindView(R.id.price_list_date_textview)
    TextView date;
    @BindView(R.id.price_list_price_textview)
    TextView price;
    @BindView(R.id.price_list_variation_textview)
    TextView variation;

    public PriceListViewHolderHistory(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
