package com.giaiphapict.bitcoinlivepriceboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.giaiphapict.asynctask.HistoryAsyncTask;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button btnHistory = (Button) this.findViewById(R.id.btnHistory);
        btnHistory.setVisibility(View.GONE);

        new HistoryAsyncTask(HistoryActivity.this).execute();;
    }
}
