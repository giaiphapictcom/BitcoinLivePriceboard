package com.giaiphapict.model;

/**
 * Created by QuanICT on 8/26/2017.
 */

public class Ticker {
    private String id;
    private String name;
    private String symbol;
    private String rank;
    private String price_btc;
    public Double volume_24h_usd;
    public Double market_cap_usd;
    public Double available_supply;
    public Double total_supply;
    private String percent_change_1h;
    private String percent_change_24h;
    private String percent_change_7d;
    private String last_updated;

    public Ticker(String id, String name, String symbol, String rank,
                  String price_btc,String volume_24h_usd,String market_cap_usd,
                  String available_supply,String total_supply,
                  String percent_change_1h,String percent_change_24h,String percent_change_7d,
                  String last_updated) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.price_btc = price_btc;
        this.volume_24h_usd = Double.parseDouble(volume_24h_usd);
        this.market_cap_usd = Double.parseDouble(market_cap_usd);
        this.available_supply = Double.parseDouble(available_supply);
        this.total_supply = Double.parseDouble(total_supply);
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        this.percent_change_7d = percent_change_7d;
        this.last_updated = last_updated;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
