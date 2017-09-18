package com.giaiphapict.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.Button;
import android.support.v4.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Activity;
import android.os.Handler;

import com.giaiphapict.asynctask.TickerAsyncTask;
import com.giaiphapict.commons.FontIcons;

public class DashboardActivity extends AppCompatActivity {

    private TextSwitcher mSwitcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDashboard = (Button) this.findViewById(R.id.btnDashboard);
        btnDashboard.setVisibility(View.GONE);

        new FontIcons(DashboardActivity.this);
        new TickerAsyncTask(DashboardActivity.this).execute();
        SwipeRefresh();

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
                        new TickerAsyncTask(DashboardActivity.this).execute();
                    }
                },1);
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


}
