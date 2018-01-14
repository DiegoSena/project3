package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.Utilities;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.DetailActivity;

public class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private final Context context;
    private Cursor cursor;
    private int appWidgetId;

    public StockRemoteViewsFactory(Context context, Intent intent){
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(cursor != null){
            cursor.close();
        }
        cursor = context.getContentResolver().query(Contract.Quote.URI, new String[]{}, null, new String[]{}, null);
    }

    @Override
    public void onDestroy() {
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.stock_widget_list_item);
        if(cursor.moveToPosition(position)){
            String stock = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
            double price = cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE));
            double change = cursor.getDouble(cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));
            rv.setTextViewText(R.id.symbol_widget, stock);
            rv.setTextViewText(R.id.price_widget, Utilities.formatDollars(price));
            rv.setTextViewText(R.id.change_widget, Utilities.formatDollarsWithPlus(change));

            if (change > 0) {
                rv.setInt(R.id.change_widget, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                rv.setInt(R.id.change_widget, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            final Intent fillInIntent = new Intent();
            final Bundle extras = new Bundle();
            extras.putString(DetailActivity.SYMBOL_EXTRA, stock);
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
