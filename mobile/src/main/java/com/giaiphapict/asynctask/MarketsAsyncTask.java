package com.giaiphapict.asynctask;

import android.app.Activity;

import com.giaiphapict.commons.constant;

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
import java.lang.String;

import com.giaiphapict.commons.api;
import com.giaiphapict.commons.MoneyFormat;
import com.giaiphapict.activity.R;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
//import com.giaiphapict.asynctask.MarketAsyncTaskLoadPage;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

public class MarketsAsyncTask extends GetAsyncTask {
    ArrayList<HashMap<String, String>> markets = new ArrayList<>();
    public ListView MarkeListView;

    ListAdapter adapter;
    Activity contextTaget;
    int pageCurrent = 1;
    Boolean endOfList = false;

    public MarketsAsyncTask(Activity context,int page) {

        super(context, "" );
        contextTaget = context;
        MarkeListView = this.context.findViewById(R.id.Markets);
    }

    @Override
    protected String doInBackground(String... params){
        String result = api.getContent(constant.BASE_URL + "/market/bitcoin?limit="+pageLimit);
        if(result!=null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    HashMap<String, String> item = new HashMap<>();
                    item.put("number", Integer.toString(markets.size() + 1)  );
                    item.put("source", jsonObject.getString("source") );
                    item.put("pair", jsonObject.getString("pair"));

                    Double volume_24 = Double.parseDouble(jsonObject.getString("volume_24_usd"));
                    item.put("volume_24h", MoneyFormat.NoDecimal(volume_24,constant.usd_symbol));

                    Double price_usd = Double.parseDouble(jsonObject.getString("price_usd"));
                    item.put("price", MoneyFormat.Decimal(price_usd,constant.usd_symbol));

                    item.put("volume_change", jsonObject.getString("volume_change"));
                    item.put("update_status", jsonObject.getString("update_status"));

                    markets.add(item);

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

    private int firstVisibleItem, visibleItemCount,totalItemCount;

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (super.pDialog.isShowing())
            super.pDialog.dismiss();

        if(markets.size() > 0) {
            adapter = new SimpleAdapter(
                    this.context, markets, R.layout.market_line,
                    new String[]{"number","source","pair", "volume_24h","price","volume_change","update_status"},
                    new int[]{R.id.Number,R.id.Source,R.id.Pair, R.id.Volume24, R.id.Price,  R.id.VolumeChange,  R.id.UpdateStatus});

            MarkeListView.setAdapter(adapter);

//            MarkeListView.setOnTouchListener(new OnTouchListener() {
//
//                private static final float OVERSCROLL_THRESHOLD_IN_PIXELS = 100;
//                private float downY;
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    int firstVisibleItem = MarkeListView.getFirstVisiblePosition();
//                    int totalItemCount = MarkeListView.getCount();
//                    int visibleItemCount = MarkeListView.getChildCount();
//                    boolean onTop = firstVisibleItem == 0 && MarkeListView.getChildAt(0) != null && MarkeListView.getChildAt(0).getTop() == 0;
//                    boolean onBottom = firstVisibleItem + visibleItemCount == totalItemCount && MarkeListView.getChildAt(visibleItemCount-1).getBottom() == MarkeListView.getHeight();
//
//                    if(onTop || onBottom) {
//                        switch(event.getAction()) {
//                            case MotionEvent.ACTION_DOWN:
//                                downY = event.getY();
//                                break;
//                            case MotionEvent.ACTION_MOVE:
//                                float deltaY = event.getY() - downY;
//                                if(onTop && deltaY > OVERSCROLL_THRESHOLD_IN_PIXELS)
//                                {
//    //                                    pageCurrent = 1;
//    //                                    MarkeListView.setEmptyView( contextTaget.findViewById(R.id.Markets) );
//    //                                    markets.clear();
//    //
//    //                                    new LoadMoreDataTask(contextTaget,pageCurrent).execute();
//
//                                }
//                                if(onBottom && -deltaY > OVERSCROLL_THRESHOLD_IN_PIXELS) {
//                                    pageCurrent++;
//                                    new LoadMoreDataTask(contextTaget,pageCurrent).execute();
//                                }
//                                break;
//                        }
//                    }
//
//                    return false;
//                }
//            });

            MarkeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItemm, int visibleItemCountt, int totalItemCountt) {
                    firstVisibleItem = firstVisibleItemm;
                    visibleItemCount = visibleItemCountt;
                    totalItemCount = totalItemCountt;
                }
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
                        pageCurrent++;
                        new LoadMoreDataTask(contextTaget,pageCurrent).execute();
                    }
                }
            });
        }
    }

    public class LoadMoreDataTask extends GetAsyncTask {
        public LoadMoreDataTask(Activity context,int page) {
            super(context, "" );
        }

        @Override
        protected String doInBackground(String... params){
            String result = api.getContent(constant.BASE_URL + "/market/bitcoin?limit="+pageLimit+"&page="+Integer.toString(pageCurrent));
            if(result!=null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length() ; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        HashMap<String, String> item = new HashMap<>();
                        item.put("number", Integer.toString(markets.size() + 1)  );
                        item.put("source", jsonObject.getString("source") );
                        item.put("pair", jsonObject.getString("pair"));

                        Double volume_24 = Double.parseDouble(jsonObject.getString("volume_24_usd"));
                        item.put("volume_24h", MoneyFormat.NoDecimal(volume_24,constant.usd_symbol));

                        item.put("price", MoneyFormat.Symbol(jsonObject.getString("price"),constant.usd_symbol));

                        item.put("volume_change", jsonObject.getString("volume_change"));
                        item.put("update_status", jsonObject.getString("update_status"));

                        markets.add(item);

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
                endOfList = true;
                return "empty";
            }

            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (super.pDialog.isShowing())
                super.pDialog.dismiss();

            if( result.equals("done") ){
//                int position = MarkeListView.getLastVisiblePosition();
                int position = MarkeListView.getFirstVisiblePosition();
                adapter = new SimpleAdapter(
                        this.context, markets, R.layout.market_line,
                        new String[]{"number","source","pair", "volume_24h","price","volume_change","update_status"},
                        new int[]{R.id.Number,R.id.Source,R.id.Pair, R.id.Volume24, R.id.Price,  R.id.VolumeChange,  R.id.UpdateStatus});

                MarkeListView.setAdapter(adapter);
                if( pageCurrent > 1 ){
                    MarkeListView.setSelectionFromTop(position, 0);
                    MarkeListView.clearFocus();
                }


            }
        }
    }
}
