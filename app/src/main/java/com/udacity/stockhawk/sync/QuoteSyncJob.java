package com.udacity.stockhawk.sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.udacity.stockhawk.BuildConfig;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public final class QuoteSyncJob {

    private static final int ONE_OFF_ID = 2;
    private static final String ACTION_DATA_UPDATED = "com.udacity.stockhawk.ACTION_DATA_UPDATED";
    private static final int PERIOD = 300000;
    private static final int INITIAL_BACKOFF = 10000;
    private static final int PERIODIC_ID = 1;
    private static final int YEARS_OF_HISTORY = 2;

    private static final String QUANDL_ROOT = "https://www.quandl.com/api/v3/datasets/WIKI/";

    private static final OkHttpClient client = new OkHttpClient();
    private static final LocalDate startDate = LocalDate.now().minus(YEARS_OF_HISTORY, ChronoUnit.YEARS);
    private static final LocalDate endDate = LocalDate.now();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String LOG_TAG = QuoteSyncJob.class.getSimpleName();

    private QuoteSyncJob() {
    }

    static HttpUrl createQuery(String symbol) {

        HttpUrl.Builder httpUrl = HttpUrl.parse(QUANDL_ROOT + symbol + ".json").newBuilder();
        httpUrl.addQueryParameter("column_index", "4")  //closing price
                .addQueryParameter("start_date", formatter.format(startDate))
                .addQueryParameter("end_date", formatter.format(endDate));

        if(BuildConfig.QUANDL_KEY != null && !BuildConfig.QUANDL_KEY.isEmpty()){
            httpUrl.addQueryParameter("api_key", BuildConfig.QUANDL_KEY);
        }

        return httpUrl.build();
    }

    static ContentValues processStock(JSONObject jsonObject) throws JSONException {


        String stockSymbol = jsonObject.getString("dataset_code");

        JSONArray historicData = jsonObject.getJSONArray("data");

        double price = historicData.getJSONArray(0).getDouble(1);
        double change = price - historicData.getJSONArray(1).getDouble(1);
        double percentChange = 100 * ((price - historicData.getJSONArray(1).getDouble(1)) / historicData.getJSONArray(1).getDouble(1));

        StringBuilder historyBuilder = new StringBuilder();

        JSONArray array = null;
        try{
            for (int i = 0; i < historicData.length(); i++) {
                array = historicData.getJSONArray(i);
                historyBuilder.append(array.get(0) + ", " + array.getDouble(1) + "\n");
            }
        }catch (Exception e){
            Log.e(LOG_TAG, "It was not possible to add history for symbol " + stockSymbol + "for history " + array, e);
        }

        ContentValues quoteCV = new ContentValues();
        quoteCV.put(Contract.Quote.COLUMN_SYMBOL, stockSymbol);
        quoteCV.put(Contract.Quote.COLUMN_PRICE, price);
        quoteCV.put(Contract.Quote.COLUMN_PERCENTAGE_CHANGE, percentChange);
        quoteCV.put(Contract.Quote.COLUMN_ABSOLUTE_CHANGE, change);
        quoteCV.put(Contract.Quote.COLUMN_HISTORY, historyBuilder.toString());

        return quoteCV;
    }

    private static void sendDataChangedBroadcast(Context context) {
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        context.sendBroadcast(dataUpdatedIntent);
    }

    private static void getStockInfo(final Context context, final String stock) {
        Request request = new Request.Builder()
                .url(createQuery(stock)).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Timber.e("OKHTTP", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.code() != 404){
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        ContentValues quotes = processStock(jsonObject.getJSONObject("dataset"));

                        context.getContentResolver().insert(Contract.Quote.URI, quotes);
                        PrefUtils.addStock(context, stock);
                    }
                } catch (JSONException ex) {
                    Log.w(LOG_TAG,"Error parsing json response", ex);
                }
            }
        });
    }

    static void getQuotes(final Context context, final String stock) {

        Timber.d("Running sync job");

        try {
            if(stock != null){
                getStockInfo(context, stock);
            }else {
                for (String stockPref : PrefUtils.getStocks(context)) {
                    getStockInfo(context, stockPref);
                }
            }
            sendDataChangedBroadcast(context);
        } catch (Exception exception) {
            Timber.e(exception, "Error fetching stock quotes");
        }
    }

    private static void schedulePeriodic(Context context) {
        Timber.d("Scheduling a periodic task");


        JobInfo.Builder builder = new JobInfo.Builder(PERIODIC_ID, new ComponentName(context, QuoteJobService.class));


        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(PERIOD)
                .setBackoffCriteria(INITIAL_BACKOFF, JobInfo.BACKOFF_POLICY_EXPONENTIAL);


        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        scheduler.schedule(builder.build());
    }


    public static synchronized void initialize(final Context context) {
        schedulePeriodic(context);
        syncImmediately(context);
    }

    public static synchronized void syncImmediately(Context context) {
        syncImmediately(context, null);
    }

    public static synchronized void syncImmediately(Context context, String symbolAdded) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Intent nowIntent = new Intent(context, QuoteIntentService.class);
            if(symbolAdded != null){
                nowIntent.putExtra(QuoteIntentService.SYMBOL_ADDED_EXTRA, symbolAdded);
            }
            context.startService(nowIntent);
        } else {

            JobInfo.Builder builder = new JobInfo.Builder(ONE_OFF_ID, new ComponentName(context, QuoteJobService.class));


            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setBackoffCriteria(INITIAL_BACKOFF, JobInfo.BACKOFF_POLICY_EXPONENTIAL);


            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            scheduler.schedule(builder.build());


        }
    }


}
