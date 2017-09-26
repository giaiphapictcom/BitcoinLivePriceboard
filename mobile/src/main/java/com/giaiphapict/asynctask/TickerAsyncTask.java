package com.giaiphapict.asynctask;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.String;

import com.giaiphapict.commons.api;
import com.giaiphapict.commons.constant;
import com.giaiphapict.commons.MoneyFormat;
import com.giaiphapict.model.Ticker;
import com.giaiphapict.activity.R;


public class TickerAsyncTask extends GetAsyncTask {
    ArrayList<HashMap<String, String>> tickers = new ArrayList<>();
    String percent_change_24h = "";
    String price_usd = "";;

    public TickerAsyncTask(Activity context) {
        super(context, constant.BASE_URL + "/ticker_line/bitcoin/?convert="+constant.CURRENCY_CONVERT );
    }

    @Override
    protected String doInBackground(String... params){
        String result = api.getContent(constant.BASE_URL + "/ticker/bitcoin/?convert="+constant.CURRENCY_CONVERT);
        if(result!=null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Ticker ticker = new Ticker(
                            jsonObject.getString("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("symbol"),
                            "",
                            jsonObject.getString("price_btc"),
                            jsonObject.getString("24h_volume_usd"),
                            jsonObject.getString("market_cap_usd"),
                            jsonObject.getString("available_supply"),
                            jsonObject.getString("max_supply"),
                            jsonObject.getString("percent_change_1h"),
                            jsonObject.getString("percent_change_24h"),
                            jsonObject.getString("percent_change_7d"),
                            jsonObject.getString("last_updated")
                    );
                    //HashMap<String, String> ticker_line = new HashMap<>();

                    HashMap<String, String> item = new HashMap<>();

                    item.put("market_cap", MoneyFormat.Decimal(ticker.market_cap_usd,constant.usd_symbol) );
                    item.put("24h_volume", MoneyFormat.NoDecimal(ticker.volume_24h_usd,constant.usd_symbol));
                    item.put("available_supply", MoneyFormat.NoDecimal(ticker.available_supply,constant.bitcoin_symbol));
                    item.put("max_supply", MoneyFormat.NoDecimal(ticker.total_supply,constant.bitcoin_symbol));

                    tickers.add(item);

                    if( price_usd.length() < 1 ){
                        price_usd = jsonObject.getString("price_usd");
                    }
                    if( percent_change_24h.length() < 1 ){
                        percent_change_24h = jsonObject.getString("percent_change_24h");
                    }
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

        if(tickers.size() > 0) {
            ListView TicketListView = (ListView) this.context.findViewById(R.id.Tickets);

            ListAdapter adapter = new SimpleAdapter(
                    this.context, tickers, R.layout.ticker_line,
                    new String[]{"market_cap","available_supply", "24h_volume","available_supply","max_supply"},
                    new int[]{R.id.MarketCap,R.id.MarketCapBTC, R.id.Volume24h, R.id.CirculatingSupply, R.id.MaxSupply});

            TicketListView.setAdapter(adapter);
            UpdateMain();
        }
    }

    private void UpdateMain(){
        SpannableString PriceUSDSpan = new SpannableString( MoneyFormat.Decimal(Double.parseDouble(price_usd),constant.usd_symbol) );
        TextView CurrencyTxt= this.context.findViewById(R.id.CurrencyValue);


        SpannableString PercentChangeSpan = new SpannableString("");
        int percent_change_color = Color.GREEN;
        if( percent_change_24h.length() > 0 ){
            if( percent_change_24h.substring(0, 1).equals("-")){
                //percent_change_color = Color.parseColor("#d14836");
                percent_change_color = Color.RED;
            }
            percent_change_24h = "("+percent_change_24h+"%)";
            PercentChangeSpan = new SpannableString(percent_change_24h);
        }
        PercentChangeSpan.setSpan(new ForegroundColorSpan(percent_change_color), 0, percent_change_24h.length(), 0);
        CurrencyTxt.setText(TextUtils.concat(PriceUSDSpan," " ,PercentChangeSpan));


    }
}
