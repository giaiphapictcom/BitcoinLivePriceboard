package com.giaiphapict.asynctask;

import com.giaiphapict.bitcoinlivepriceboard.R;
import com.giaiphapict.commons.MoneyFormat;
import com.giaiphapict.commons.api;
import com.giaiphapict.commons.constant;

import android.app.Activity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class HistoryAsyncTask extends GetAsyncTask {
    ArrayList<HashMap<String, String>> histories = new ArrayList<>();

    public HistoryAsyncTask(Activity context) {
        super(context, constant.BASE_URL + "/history/bitcoin/");
    }

    @Override
    protected String doInBackground(String... params){
        String result = api.getContent(constant.BASE_URL + "/history/bitcoin/");
        if(result!=null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    HashMap<String, String> item = new HashMap<>();
                    item.put("Date", jsonObject.getString("date")  );


                    Double open = Double.parseDouble(jsonObject.getString("open"));
                    item.put("Open", MoneyFormat.NoDecimal(open,""));

                    Double high = Double.parseDouble(jsonObject.getString("high"));
                    item.put("High", MoneyFormat.NoDecimal(high,""));
                    Double low = Double.parseDouble(jsonObject.getString("low"));
                    item.put("Low", MoneyFormat.NoDecimal(high,""));

                    Double close = Double.parseDouble(jsonObject.getString("close"));
                    item.put("Close", MoneyFormat.NoDecimal(close,""));

                    Double volume = Double.parseDouble(jsonObject.getString("volume"));
                    item.put("Volume", MoneyFormat.NoDecimal(volume,""));

                    Double market_cap = Double.parseDouble(jsonObject.getString("market_cap"));
                    item.put("MarketCap", MoneyFormat.NoDecimal(market_cap,""));
                    histories.add(item);

                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                String error = e.getMessage();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG)
                            .show();
                }
            });

        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (super.pDialog.isShowing())
            super.pDialog.dismiss();

        if(histories.size() > 0) {
            ListView HistoryListView = (ListView) this.context.findViewById(R.id.Histories);

            ListAdapter adapter = new SimpleAdapter(
                    this.context, histories, R.layout.history_line,
                    new String[]{"Date","Open","High", "Low","Close","Volume","MarketCap"},
                    new int[]{R.id.Date,R.id.Open,R.id.High, R.id.Low, R.id.Close,  R.id.Volume,  R.id.MarketCap});

            HistoryListView.setAdapter(adapter);
        }
    }
}
