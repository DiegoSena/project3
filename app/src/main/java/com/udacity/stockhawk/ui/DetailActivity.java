package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapter.Price;
import com.udacity.stockhawk.adapter.PriceListAdapter;
import com.udacity.stockhawk.data.PrefUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.symbol_textview)
    TextView symbolTextView;

    @BindView(R.id.prices_recyclerview)
    RecyclerView pricesRecyclerView;

    PriceListAdapter priceListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);
        String symbol = getIntent().getStringExtra("SYMBOL");
        symbolTextView.setText(symbol);
        List<Price> prices = new ArrayList<>();
        Price price1 = new Price();
        price1.setDate(new Date());
        price1.setPrice(10.0);
        price1.setVariation(+40.0);
        prices.add(price1);

        Price price2 = new Price();
        price2.setDate(new Date());
        price2.setPrice(10.0);
        price2.setVariation(+40.0);
        prices.add(price2);

        Price price3 = new Price();
        price3.setDate(new Date());
        price3.setPrice(10.0);
        price3.setVariation(-40.0);
        prices.add(price3);


        priceListAdapter = new PriceListAdapter(prices);
        pricesRecyclerView.setAdapter(priceListAdapter);
        pricesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_settings, menu);
        MenuItem item = menu.findItem(R.id.action_change_units);
        setDisplayModeMenuItemIcon(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_units) {
            PrefUtils.toggleDisplayMode(this);
            setDisplayModeMenuItemIcon(item);
            priceListAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDisplayModeMenuItemIcon(MenuItem item) {
        if (PrefUtils.getDisplayMode(this)
                .equals(getString(R.string.pref_display_mode_absolute_key))) {
            item.setIcon(R.drawable.ic_percentage);
        } else {
            item.setIcon(R.drawable.ic_dollar);
        }
    }
}
