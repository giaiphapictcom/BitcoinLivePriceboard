//package com.giaiphapict.asynctask;
//
//import android.app.Activity;
//import android.util.Log;
//import android.widget.AbsListView;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.Toast;
//
//import com.giaiphapict.activity.R;
//import com.giaiphapict.commons.MoneyFormat;
//import com.giaiphapict.commons.api;
//import com.giaiphapict.commons.constant;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by QuanICT on 8/29/2017.
// */
//
//public class MarketAsyncTaskLoadPage extends GetAsyncTask {
//    ArrayList<HashMap<String, String>> markets = new ArrayList<>();
//    //private ListView MarkeListView;
//    private int pagenum = 0;
//    public MarketAsyncTaskLoadPage(Activity context, int page) {
//
//        super(context, "" );
//
//        if( page < 1 ){
//            page = 1;
//        }
//        pagenum = page;
//    }
//
//    protected String doInBackground(String... params){
//        String result = api.getContent(constant.BASE_URL + "/market/bitcoin/?convert="+constant.CURRENCY_CONVERT+"&page="+pagenum);
//        if(result!=null) {
//            try {
//                JSONArray jsonArray = new JSONArray(result);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                    HashMap<String, String> item = new HashMap<>();
//                    item.put("number", Integer.toString(i+1 + pageLimit*pagenum)  );
//                    item.put("source", jsonObject.getString("source") );
//                    item.put("pair", jsonObject.getString("pair"));
//
//                    Double volume_24 = Double.parseDouble(jsonObject.getString("volume_24_usd"));
//                    item.put("volume_24h", MoneyFormat.NoDecimal(volume_24,constant.usd_symbol));
//
//                    Double price_usd = Double.parseDouble(jsonObject.getString("price_usd"));
//                    item.put("price", MoneyFormat.NoDecimal(price_usd,constant.usd_symbol));
//
//                    item.put("volume_change", jsonObject.getString("volume_change"));
//                    item.put("update_status", jsonObject.getString("update_status"));
//
//                    markets.add(item);
//
//                }
//            } catch (final JSONException e) {
//                Log.e(TAG, "Json parsing error: " + e.getMessage());
//                String error = e.getMessage();
//                context.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }
//        } else {
//            Log.e(TAG, "Couldn't get json from server.");
//            context.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG)
//                            .show();
//                }
//            });
//
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        if (super.pDialog.isShowing())
//            super.pDialog.dismiss();
//
//        if(markets.size() > 0) {
//            int position = ListView.getLastVisiblePosition();
//
//            ListAdapter adapter = new SimpleAdapter(
//                    this.context, markets, R.layout.market_line,
//                    new String[]{"number","source","pair", "volume_24h","price","volume_change","update_status"},
//                    new int[]{R.id.Number,R.id.Source,R.id.Pair, R.id.Volume24, R.id.Price,  R.id.VolumeChange,  R.id.UpdateStatus});
//
//            ListView.setAdapter(adapter);
//
//            ListView.setSelectionFromTop(position, 0);
//        }
//    }
//}
