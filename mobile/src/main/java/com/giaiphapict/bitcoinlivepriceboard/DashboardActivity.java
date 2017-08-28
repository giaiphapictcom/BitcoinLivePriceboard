package com.giaiphapict.bitcoinlivepriceboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.Button;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.lang.String;
import com.giaiphapict.asynctask.TickerAsyncTask;

public class DashboardActivity extends AppCompatActivity {

    private TextSwitcher mSwitcher;
    private int mCounter = 0;
    ListView listView;
    private ProgressDialog pDialog;

    public String TAG = DashboardActivity.class.getSimpleName();

    // URL to get contacts JSON
    private static String api_url_main = "https://api.androidhive.info/contacts/";
    ArrayList<HashMap<String, String>> contactList;
    private ListView TicketListView;
    public String price_usd = "";
    public String percent_change_24h = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDashboard = (Button) this.findViewById(R.id.btnDashboard);
        btnDashboard.setVisibility(View.GONE);

        new TickerAsyncTask(DashboardActivity.this).execute();;

    }

    public void ViewHistory(View v){
        Intent myIntent = new Intent(DashboardActivity.this, HistoryActivity.class);
        DashboardActivity.this.startActivity(myIntent);
    }
    public void ViewChart(View v){
        Intent myIntent = new Intent(DashboardActivity.this, ChartActivity.class);
        DashboardActivity.this.startActivity(myIntent);
    }
    public void ViewMarket(View v){
        Intent myIntent = new Intent(DashboardActivity.this, MarketActivity.class);
        DashboardActivity.this.startActivity(myIntent);
    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

//    private static class EfficientAdapter extends BaseAdapter {
//        private LayoutInflater mInflater;
//
//        public EfficientAdapter(Context context) {
//            mInflater = LayoutInflater.from(context);
//        }
//
//        public int getCount() {
//            return CountriesList.abbreviations.length;
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.two_col_row, null);
//                holder = new ViewHolder();
//                holder.text1 = (TextView) convertView.findViewById(R.id.TextView01);
//                holder.text2 = (TextView) convertView.findViewById(R.id.TextView02);
//                holder.text3 = (TextView) convertView.findViewById(R.id.TextView03);
//                holder.text4 = (TextView) convertView.findViewById(R.id.TextView04);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            holder.text1.setText(CountriesList.abbreviations[position]);
//            holder.text2.setText(CountriesList.countries[position]);
//            holder.text3.setText(CountriesList.countries[position]);
//            holder.text4.setText(CountriesList.countries[position]);
//
//            return convertView;
//        }
//
//        static class ViewHolder {
//            TextView text1;
//            TextView text2;
//            TextView text3;
//            TextView text4;
//        }
//    }




    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashboardActivity.this);
            pDialog.setMessage("Loading data. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        private String CurrChar = "$ ";
        private String BitcoinChar = " BTC";
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String ticketsJson = sh.makeServiceCall(getString(R.string.api_url_main) + "/TickerAsyncTask/bitcoin/?convert="+getString(R.string.api_currency));
            ticketsJson = ticketsJson.replaceAll("\\n","");
            //Log.e(TAG, "Response from url: " + jsonStr);

            if (ticketsJson != "") {
                try {

                    DecimalFormat formattedPrice = new DecimalFormat("###,###");

                    JSONArray contacts = new JSONArray(ticketsJson);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        if( price_usd.length() < 1 ){
                            price_usd = c.getString("price_usd");
                        }
                        if( percent_change_24h.length() < 1 ){
                            percent_change_24h = c.getString("percent_change_24h");
                        }

                        HashMap<String, String> ticker = new HashMap<>();


                        ticker.put("market_cap",
                                CurrChar + formattedPrice.format( Double.parseDouble(c.getString("market_cap_usd"))).replace(".",",")
                        );
                        ticker.put("24h_volume",
                                CurrChar + formattedPrice.format( Double.parseDouble(c.getString("24h_volume_usd"))).replace(".", ",")
                        );
                        ticker.put("available_supply",
                                formattedPrice.format( Double.parseDouble(c.getString("available_supply"))).replace(".", ",") + BitcoinChar
                        );
                        ticker.put("max_supply",
                                c.getString("available_supply") + BitcoinChar
                        );

                        // adding contact to contact list
                        contactList.add(ticker);
                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    String error = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    DashboardActivity.this, contactList,
                    R.layout.ticker_line,
                    new String[]{"market_cap","available_supply", "24h_volume","available_supply"},
                    new int[]{R.id.MarketCap,R.id.MarketCapBTC, R.id.Volume24h, R.id.CirculatingSupply});

            TicketListView.setAdapter(adapter);


            if( price_usd.length() > 0 ){
                SpannableString PriceUSDSpan = new SpannableString("$"+price_usd);

                SpannableString PercentChangeSpan = new SpannableString("");
                if( percent_change_24h.length() > 0 ){
                    int percent_change_color = Color.GREEN;
                    if( percent_change_24h.substring(0, 1) == "-" ){
                        percent_change_color = Color.RED;
                        percent_change_24h = "-"+percent_change_24h;
                    }
                    percent_change_24h = "("+percent_change_24h+"%)";
                    PercentChangeSpan = new SpannableString(percent_change_24h);
                    PercentChangeSpan.setSpan(new ForegroundColorSpan(percent_change_color), 0, percent_change_24h.length(), 0);
                }
                TextView CurrencyTxt= (TextView) findViewById(R.id.CurrencyValue);
                CurrencyTxt.setText(TextUtils.concat(PriceUSDSpan," " ,PercentChangeSpan));
            }
        }

    }
}
