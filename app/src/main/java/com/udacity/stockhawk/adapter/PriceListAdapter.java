package com.udacity.stockhawk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by diego on 09/01/18.
 */

public class PriceListAdapter extends RecyclerView.Adapter<PriceListViewHolder> {
    private Context context;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private List<Price> prices = new ArrayList<>();
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public PriceListAdapter(Context context, List<Price> prices){
        this.context = context;
        this.prices = prices;
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public PriceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_price_current, parent, false);
            return new PriceListViewHolderCurrent(layoutView);
        }
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_price, parent, false);
        return new PriceListViewHolderHistory(layoutView);

    }

    @Override
    public void onBindViewHolder(PriceListViewHolder holder, int position) {
        final Price price = prices.get(position);
        if(holder instanceof PriceListViewHolderCurrent){
            setCurrentPriceLayout((PriceListViewHolderCurrent) holder, price);
        }else{
            setHistoryPriceLayout((PriceListViewHolderHistory) holder, price);
        }
    }

    private void setHistoryPriceLayout(PriceListViewHolderHistory holder, Price price) {
        PriceListViewHolderHistory history = holder;
        history.setDate(format.format(price.getDate()));
        history.setPrice(dollarFormat.format(price.getPrice()));

        if(PrefUtils.getDisplayMode(context).equals(R.string.pref_display_mode_absolute_key)){
            history.setVariation(dollarFormatWithPlus.format(price.getAbsoluteChange()));

        }else{
            history.setVariation(percentageFormat.format(price.getPercentageChange() / 100));
        }

        history.setVariationBackground(getVariation(price.getAbsoluteChange()));
    }

    private void setCurrentPriceLayout(PriceListViewHolderCurrent holder, Price price) {
        PriceListViewHolderCurrent current = holder;

        current.setCurrentPrice(dollarFormat.format(price.getPrice()));
        if(PrefUtils.getDisplayMode(context).equals(R.string.pref_display_mode_absolute_key)){
            current.setCurrentVariation(dollarFormatWithPlus.format(price.getAbsoluteChange()));
        }else{
            current.setCurrentVariation(percentageFormat.format(price.getPercentageChange() / 100));
        }
        current.setCurrentVariationBackground(getVariation(price.getAbsoluteChange()));
    }

    private int getVariation(double absoluteChange) {
        return absoluteChange > 0 ? R.drawable.percent_change_pill_green : R.drawable.percent_change_pill_red;
    }

    @Override
    public int getItemCount() {
        return prices.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 2;
    }
}
