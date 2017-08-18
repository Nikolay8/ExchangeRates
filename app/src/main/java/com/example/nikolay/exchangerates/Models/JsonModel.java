package com.example.nikolay.exchangerates.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikolay on 18.07.2017.
 */

public class JsonModel {
    @SerializedName("ccy")
    @Expose
    private String ccy;
    @SerializedName("base_ccy")
    @Expose
    private String baseCcy;
    @SerializedName("buy")
    @Expose
    private String buy;
    @SerializedName("sale")
    @Expose
    private String sale;

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getBaseCcy() {
        return baseCcy;
    }

    public void setBaseCcy(String baseCcy) {
        this.baseCcy = baseCcy;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
