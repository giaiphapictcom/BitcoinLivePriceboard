package com.giaiphapict.bitcoinlivepriceboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.giaiphapict.asynctask.MarketsAsyncTask;

public class MarketActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        Button btnMarket = (Button) this.findViewById(R.id.btnMarket);
        btnMarket.setVisibility(View.GONE);

        new MarketsAsyncTask(MarketActivity.this).execute();;
    }
}
