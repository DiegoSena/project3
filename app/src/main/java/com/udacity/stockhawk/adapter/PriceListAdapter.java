package com.udacity.stockhawk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 09/01/18.
 */

public class PriceListAdapter extends RecyclerView.Adapter<PriceListViewHolder> {
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private List<Price> prices = new ArrayList<>();

    public PriceListAdapter(List<Price> prices){
        this.prices = prices;
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
            PriceListViewHolderCurrent current = (PriceListViewHolderCurrent) holder;

            current.setCurrentPrice(price.getPrice());
            current.setCurrentVariation(price.getVariation());

        }else{
            PriceListViewHolderHistory history = (PriceListViewHolderHistory) holder;
            history.setDate(format.format(price.getDate()));
            history.setPrice(price.getPrice());
            history.setVariation(price.getVariation());
        }
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
