package com.udacity.stockhawk.sync;

import android.app.IntentService;
import android.content.Intent;

import timber.log.Timber;


public class QuoteIntentService extends IntentService {

    public QuoteIntentService() {
        super(QuoteIntentService.class.getSimpleName());
    }

    public static final String SYMBOL_ADDED_EXTRA = "SYMBOL_ADDED";
    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("Intent handled");
        String symbol = intent.getStringExtra(SYMBOL_ADDED_EXTRA);
        QuoteSyncJob.getQuotes(getApplicationContext(), symbol );
    }
}
