package com.giaiphapict.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.giaiphapict.commons.api;
import android.widget.ListView;


abstract class GetAsyncTask extends AsyncTask<String, Void, String> {
    private String url;
    public String TAG;
    public Activity context;
    public ProgressDialog pDialog;
    public int pageLimit = 30;
    public ListView ListView;


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
