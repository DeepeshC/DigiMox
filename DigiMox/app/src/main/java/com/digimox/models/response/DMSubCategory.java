package com.digimox.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMSubCategory implements Serializable {
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_desc")
    @Expose
    private String itemDesc;
    @SerializedName("language_id")
    @Expose
    private String languageId;
    @SerializedName("item_image")
    @Expose
    private String itemImage;
    @SerializedName("item_group_id")
    @Expose
    private String itemGroupId;
    @SerializedName("item_price_unit")
    @Expose
    private String itemPriceUnit;
    @SerializedName("item_price_usd")
    @Expose
    private String itemPriceUsd;
    private String id;

    /**
     * @return The itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId The item_id
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return The itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName The item_name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return The itemDesc
     */
    public String getItemDesc() {
        return itemDesc;
    }

    /**
     * @param itemDesc The item_desc
     */
    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    /**
     * @return The languageId
     */
    public String getLanguageId() {
        return languageId;
    }

    /**
     * @param languageId The language_id
     */
    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    /**
     * @return The itemImage
     */
    public String getItemImage() {
        return itemImage;
    }

    /**
     * @param itemImage The item_image
     */
    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    /**
     * @return The itemGroupId
     */
    public String getItemGroupId() {
        return itemGroupId;
    }

    /**
     * @param itemGroupId The item_group_id
     */
    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    /**
     * @return The itemPriceUnit
     */
    public String getItemPriceUnit() {
        return itemPriceUnit;
    }

    /**
     * @param itemPriceUnit The item_price_unit
     */
    public void setItemPriceUnit(String itemPriceUnit) {
        this.itemPriceUnit = itemPriceUnit;
    }

    /**
     * @return The itemPriceUsd
     */
    public String getItemPriceUsd() {
        return itemPriceUsd;
    }

    /**
     * @param itemPriceUsd The item_price_usd
     */
    public void setItemPriceUsd(String itemPriceUsd) {
        this.itemPriceUsd = itemPriceUsd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
