package com.giaiphapict.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.giaiphapict.asynctask.MarketsAsyncTask;
import com.giaiphapict.commons.FontIcons;



public class MarketActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        Button btnMarket = (Button) this.findViewById(R.id.btnMarket);
        btnMarket.setVisibility(View.GONE);
        new FontIcons(MarketActivity.this);
        new MarketsAsyncTask(MarketActivity.this,1).execute();
        SwipeRefresh();

    }

    public void ViewDashboard(View v){
        Intent tickerIntent = new Intent(MarketActivity.this, DashboardActivity.class);
        tickerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MarketActivity.this.startActivity(tickerIntent);
    }
    public void ViewChart(View v){
        Intent chartIntent = new Intent(MarketActivity.this, ChartActivity.class);
        chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MarketActivity.this.startActivity(chartIntent);
    }
    public void ViewHistory(View v){
        Intent historyIntent = new Intent(MarketActivity.this, HistoryActivity.class);
        historyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MarketActivity.this.startActivity(historyIntent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent tickerIntent = new Intent(MarketActivity.this, DashboardActivity.class);
            tickerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MarketActivity.this.startActivity(tickerIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void SwipeRefresh(){
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        new MarketsAsyncTask(MarketActivity.this,1).execute();
                    }
                },1);
            }
        });
    }
}
