package com.example.nikolay.exchangerates.HistoryRate;

/**
 * Created by Nikolay on 21.07.2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ExchangeRate {
    @SerializedName("baseCurrency")
    @Expose
    private String baseCurrency;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("saleRateNB")
    @Expose
    private Float saleRateNB;
    @SerializedName("purchaseRateNB")
    @Expose
    private Float purchaseRateNB;
    @SerializedName("saleRate")
    @Expose
    private Float saleRate;
    @SerializedName("purchaseRate")
    @Expose
    private Float purchaseRate;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getSaleRateNB() {
        return saleRateNB;
    }

    public void setSaleRateNB(Float saleRateNB) {
        this.saleRateNB = saleRateNB;
    }

    public Float getPurchaseRateNB() {
        return purchaseRateNB;
    }

    public void setPurchaseRateNB(Float purchaseRateNB) {
        this.purchaseRateNB = purchaseRateNB;
    }

    public Float getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(Float saleRate) {
        this.saleRate = saleRate;
    }

    public Float getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Float purchaseRate) {
        this.purchaseRate = purchaseRate;
    }
}
