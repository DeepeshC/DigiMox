package com.digimox.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Deepesh on 21-Dec-15.
 */
public class DMLanguage implements Serializable {
    @SerializedName("language_id")
    @Expose
    private String languageId;
    @SerializedName("language_name")
    @Expose
    private String languageName;
    @SerializedName("language_short")
    @Expose
    private String languageShort;
    @SerializedName("language_status")
    @Expose
    private String languageStatus;
    @SerializedName("language_added_date")
    @Expose
    private String languageAddedDate;
    @SerializedName("language_updated_date")
    @Expose
    private String languageUpdatedDate;

    /**
     *
     * @return
     * The languageId
     */
    public String getLanguageId() {
        return languageId;
    }

    /**
     *
     * @param languageId
     * The language_id
     */
    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    /**
     *
     * @return
     * The languageName
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     *
     * @param languageName
     * The language_name
     */
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    /**
     *
     * @return
     * The languageShort
     */
    public String getLanguageShort() {
        return languageShort;
    }

    /**
     *
     * @param languageShort
     * The language_short
     */
    public void setLanguageShort(String languageShort) {
        this.languageShort = languageShort;
    }

    /**
     *
     * @return
     * The languageStatus
     */
    public String getLanguageStatus() {
        return languageStatus;
    }

    /**
     *
     * @param languageStatus
     * The language_status
     */
    public void setLanguageStatus(String languageStatus) {
        this.languageStatus = languageStatus;
    }

    /**
     *
     * @return
     * The languageAddedDate
     */
    public String getLanguageAddedDate() {
        return languageAddedDate;
    }

    /**
     *
     * @param languageAddedDate
     * The language_added_date
     */
    public void setLanguageAddedDate(String languageAddedDate) {
        this.languageAddedDate = languageAddedDate;
    }

    /**
     *
     * @return
     * The languageUpdatedDate
     */
    public String getLanguageUpdatedDate() {
        return languageUpdatedDate;
    }

    /**
     *
     * @param languageUpdatedDate
     * The language_updated_date
     */
    public void setLanguageUpdatedDate(String languageUpdatedDate) {
        this.languageUpdatedDate = languageUpdatedDate;
    }

}
