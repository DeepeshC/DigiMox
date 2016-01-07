package com.digimox.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Deepesh on 21-Dec-15.
 */
public class DMCurrency implements Serializable {
    @SerializedName("currency_id")
    @Expose
    private String currencyId;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("currency_country")
    @Expose
    private String currencyCountry;
    @SerializedName("currency_exchange_rate")
    @Expose
    private String currencyExchangeRate;
    @SerializedName("currency_status")
    @Expose
    private String currencyStatus;
    @SerializedName("currency_added_date")
    @Expose
    private String currencyAddedDate;
    @SerializedName("currency_updated_date")
    @Expose
    private String currencyUpdatedDate;

    /**
     * @return The currencyId
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId The currency_id
     */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return The currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode The currency_code
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return The currencyCountry
     */
    public String getCurrencyCountry() {
        return currencyCountry;
    }

    /**
     * @param currencyCountry The currency_country
     */
    public void setCurrencyCountry(String currencyCountry) {
        this.currencyCountry = currencyCountry;
    }

    /**
     * @return The currencyExchangeRate
     */
    public String getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    /**
     * @param currencyExchangeRate The currency_exchange_rate
     */
    public void setCurrencyExchangeRate(String currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }

    /**
     * @return The currencyStatus
     */
    public String getCurrencyStatus() {
        return currencyStatus;
    }

    /**
     * @param currencyStatus The currency_status
     */
    public void setCurrencyStatus(String currencyStatus) {
        this.currencyStatus = currencyStatus;
    }

    /**
     * @return The currencyAddedDate
     */
    public String getCurrencyAddedDate() {
        return currencyAddedDate;
    }

    /**
     * @param currencyAddedDate The currency_added_date
     */
    public void setCurrencyAddedDate(String currencyAddedDate) {
        this.currencyAddedDate = currencyAddedDate;
    }

    /**
     * @return The currencyUpdatedDate
     */
    public String getCurrencyUpdatedDate() {
        return currencyUpdatedDate;
    }

    /**
     * @param currencyUpdatedDate The currency_updated_date
     */
    public void setCurrencyUpdatedDate(String currencyUpdatedDate) {
        this.currencyUpdatedDate = currencyUpdatedDate;
    }
}
