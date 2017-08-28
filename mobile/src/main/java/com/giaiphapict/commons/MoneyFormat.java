package com.giaiphapict.commons;

import java.text.DecimalFormat;

public class MoneyFormat {
    public static String NoDecimal(Double number, String currency_code){
        DecimalFormat formattedPrice = new DecimalFormat("###,###");

        String money = formattedPrice.format(number);
        money = money.replace(".", ",");
        switch (currency_code){
            case "$" :
                money = "$ " + money;
                break;
            case "BTC" :
                money += " BTC";
                break;
            default:
                break;
        }
        return money;
    }
}
