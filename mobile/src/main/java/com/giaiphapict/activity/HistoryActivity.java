package com.giaiphapict.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.giaiphapict.asynctask.HistoryAsyncTask;
import com.giaiphapict.commons.FontIcons;
import android.view.KeyEvent;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button btnHistory = (Button) this.findViewById(R.id.btnHistory);
        btnHistory.setVisibility(View.GONE);
        new FontIcons(HistoryActivity.this);

        new HistoryAsyncTask(HistoryActivity.this).execute();;
    }

    public void ViewDashboard(View v){
        Intent tickerIntent = new Intent(HistoryActivity.this, DashboardActivity.class);
        tickerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        HistoryActivity.this.startActivity(tickerIntent);
    }
    public void ViewChart(View v){
        Intent chartIntent = new Intent(HistoryActivity.this, ChartActivity.class);
        chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        HistoryActivity.this.startActivity(chartIntent);
    }
    public void ViewMarket(View v){
        Intent marketIntent = new Intent(HistoryActivity.this, MarketActivity.class);
        marketIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        HistoryActivity.this.startActivity(marketIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent tickerIntent = new Intent(HistoryActivity.this, DashboardActivity.class);
            tickerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            HistoryActivity.this.startActivity(tickerIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
