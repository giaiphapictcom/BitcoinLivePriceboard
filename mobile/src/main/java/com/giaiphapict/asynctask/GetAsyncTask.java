package com.giaiphapict.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.giaiphapict.bitcoinlivepriceboard.DashboardActivity;
import com.giaiphapict.commons.api;
/**
 * Created by QuanICT on 8/26/2017.
 */

abstract class GetAsyncTask extends AsyncTask<String, Void, String> {
    private String url;
    public String TAG;
    public Activity context;
    public ProgressDialog pDialog;



    public GetAsyncTask(Activity ctx, String url){
        this.context = ctx;
        this.url = url;
        this.TAG = this.context.getClass().getSimpleName();
    }
    @Override
    protected String doInBackground(String... params) {
        return api.getContent(this.url);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(this.context);
        pDialog.setMessage("Loading data. Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }
}
