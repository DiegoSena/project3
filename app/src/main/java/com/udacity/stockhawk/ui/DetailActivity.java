package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapter.Price;
import com.udacity.stockhawk.adapter.PriceListAdapter;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);
        String symbol = getIntent().getStringExtra("SYMBOL");
        symbolTextView.setText(symbol);
        List<Price> prices = getPrices(symbol);


        priceListAdapter = new PriceListAdapter(this, prices);
        pricesRecyclerView.setAdapter(priceListAdapter);
        pricesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @NonNull
    private List<Price> getPrices(String symbol) {
        List<Price> prices = new ArrayList<>();

        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(symbol), new String[]{}, null, new String[]{}, null);

        if(cursor.moveToFirst()){
            double price = cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE));
            double absoluteChange = cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));
            double percentageChange = cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));

            Price priceEntry = new Price();
            priceEntry.setPrice(price);
            priceEntry.setDate(new Date());
            priceEntry.setAbsoluteChange(absoluteChange);
            priceEntry.setPercentageChange(percentageChange);

            prices.add(priceEntry);

            String history = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));

            prices.addAll(parseHistory(history));
        }

        return prices;
    }

    private List<Price> parseHistory(String history) {
        List<Price> prices = new ArrayList<>();

        if(history != null){
            String[] days = history.split("\n");

            for(int i = 0; i < days.length; i++){
                Price price = new Price();
                String[] dateAndPrice = days[i].split(",");
                try {
                    price.setDate(sdf.parse(dateAndPrice[0].trim()));

                    double priceOfTheDay = Double.valueOf(dateAndPrice[1].trim());
                    price.setPrice(priceOfTheDay);

                    if(i != days.length - 1){
                        double yesterdayPrice = Double.valueOf(days[i + 1].split(",")[1]);
                        price.setPercentageChange(100 * ((priceOfTheDay - yesterdayPrice) / yesterdayPrice));
                        price.setAbsoluteChange(priceOfTheDay - yesterdayPrice);
                    }

                    prices.add(price);

                } catch (Exception e) {
                    Log.e(LOG_TAG, "It was not possible to parse history: " + days[i]);
                }
            }
        }

        return prices;
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
