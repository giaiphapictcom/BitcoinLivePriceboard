package com.giaiphapict.commons;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableString;
import com.giaiphapict.activity.R;

import android.text.TextUtils;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

public class FontIcons {
    public Activity context;
    Typeface fontawesome;

    public FontIcons(Activity ctx){
        this.context = ctx;
        fontawesome = Typeface.createFromAsset( this.context.getAssets(), "fontawesome-webfont.ttf" );

        this.ButtonIcons();
    }

    public void ButtonIcons(){

        SpannableString icon;
        SpannableString text;

        Button btnTicker = (Button)this.context.findViewById( R.id.btnDashboard );
        btnTicker.setTypeface(fontawesome);
        icon = new SpannableString( this.context.getString(R.string.fa_icon_btc) );
        text = new SpannableString( btnTicker.getText() );
        btnTicker.setText(TextUtils.concat(icon," " ,text));

        Button btnChart = (Button)this.context.findViewById( R.id.btnChart );
        btnChart.setTypeface(fontawesome);
        icon = new SpannableString( this.context.getString(R.string.fa_icon_bar_chart) );
        text = new SpannableString( btnChart.getText() );
        btnChart.setText(TextUtils.concat(icon," " ,text));
        btnChart.setVisibility(View.GONE);


        Button btnMarket = (Button)this.context.findViewById( R.id.btnMarket );
        btnMarket.setTypeface(fontawesome);
        icon = new SpannableString( this.context.getString(R.string.fa_icon_pie_chart) );
        text = new SpannableString( btnMarket.getText() );
        btnMarket.setText(TextUtils.concat(icon," " ,text));

        Button btnHistory = (Button)this.context.findViewById( R.id.btnHistory );
        btnHistory.setTypeface(fontawesome);
        icon = new SpannableString( this.context.getString(R.string.fa_icon_database) );
        text = new SpannableString( btnHistory.getText() );
        btnHistory.setText(TextUtils.concat(icon," " ,text));
    }
}
