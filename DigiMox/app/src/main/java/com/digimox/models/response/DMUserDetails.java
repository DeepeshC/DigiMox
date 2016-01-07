package com.digimox.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Deepesh on 08-Dec-15.
 */
public class DMUserDetails implements Serializable {
    @SerializedName("user_details_id")
    @Expose
    private String userDetailsId;
    @SerializedName("user_fname")
    @Expose
    private String userFname;
    @SerializedName("user_lname")
    @Expose
    private String userLname;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_details_restaurant_name")
    @Expose
    private String userDetailsRestaurantName;
    @SerializedName("user_details_timing")
    @Expose
    private String userDetailsTiming;
    @SerializedName("user_details_address")
    @Expose
    private String userDetailsAddress;
    @SerializedName("user_details_city")
    @Expose
    private String userDetailsCity;
    @SerializedName("user_details_state")
    @Expose
    private String userDetailsState;
    @SerializedName("user_details_website")
    @Expose
    private String userDetailsWebsite;
    @SerializedName("user_details_logo")
    @Expose
    private String userDetailsLogo;
    @SerializedName("restaurant_about")
    @Expose
    private String restaurantAbout;
    @SerializedName("user_details_currency_id")
    @Expose
    private String user_details_currency_id;
    @SerializedName("user_default_currency")
    @Expose
    private String user_default_currency;
    @SerializedName("user_default_language")
    @Expose
    private String user_default_language;
    @SerializedName("user_details_language_id")
    @Expose
    private String user_details_language_id;

    /**
     * @return The userDetailsId
     */
    public String getUserDetailsId() {
        return userDetailsId;
    }

    /**
     * @param userDetailsId The user_details_id
     */
    public void setUserDetailsId(String userDetailsId) {
        this.userDetailsId = userDetailsId;
    }

    /**
     * @return The userFname
     */
    public String getUserFname() {
        return userFname;
    }

    /**
     * @param userFname The user_fname
     */
    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    /**
     * @return The userLname
     */
    public String getUserLname() {
        return userLname;
    }

    /**
     * @param userLname The user_lname
     */
    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    /**
     * @return The userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail The user_email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The userDetailsRestaurantName
     */
    public String getUserDetailsRestaurantName() {
        return userDetailsRestaurantName;
    }

    /**
     * @param userDetailsRestaurantName The user_details_restaurant_name
     */
    public void setUserDetailsRestaurantName(String userDetailsRestaurantName) {
        this.userDetailsRestaurantName = userDetailsRestaurantName;
    }

    /**
     * @return The userDetailsTiming
     */
    public String getUserDetailsTiming() {
        return userDetailsTiming;
    }

    /**
     * @param userDetailsTiming The user_details_timing
     */
    public void setUserDetailsTiming(String userDetailsTiming) {
        this.userDetailsTiming = userDetailsTiming;
    }

    /**
     * @return The userDetailsAddress
     */
    public String getUserDetailsAddress() {
        return userDetailsAddress;
    }

    /**
     * @param userDetailsAddress The user_details_address
     */
    public void setUserDetailsAddress(String userDetailsAddress) {
        this.userDetailsAddress = userDetailsAddress;
    }

    /**
     * @return The userDetailsCity
     */
    public String getUserDetailsCity() {
        return userDetailsCity;
    }

    /**
     * @param userDetailsCity The user_details_city
     */
    public void setUserDetailsCity(String userDetailsCity) {
        this.userDetailsCity = userDetailsCity;
    }

    /**
     * @return The userDetailsState
     */
    public String getUserDetailsState() {
        return userDetailsState;
    }

    /**
     * @param userDetailsState The user_details_state
     */
    public void setUserDetailsState(String userDetailsState) {
        this.userDetailsState = userDetailsState;
    }

    /**
     * @return The userDetailsWebsite
     */
    public String getUserDetailsWebsite() {
        return userDetailsWebsite;
    }

    /**
     * @param userDetailsWebsite The user_details_website
     */
    public void setUserDetailsWebsite(String userDetailsWebsite) {
        this.userDetailsWebsite = userDetailsWebsite;
    }

    /**
     * @return The userDetailsLogo
     */
    public String getUserDetailsLogo() {
        return userDetailsLogo;
    }

    /**
     * @param userDetailsLogo The user_details_logo
     */
    public void setUserDetailsLogo(String userDetailsLogo) {
        this.userDetailsLogo = userDetailsLogo;
    }

    /**
     * @return The restaurantAbout
     */
    public String getRestaurantAbout() {
        return restaurantAbout;
    }

    /**
     * @param restaurantAbout The restaurant_about
     */
    public void setRestaurantAbout(String restaurantAbout) {
        this.restaurantAbout = restaurantAbout;
    }

    public String getUser_details_currency_id() {
        return user_details_currency_id;
    }

    public void setUser_details_currency_id(String user_details_currency_id) {
        this.user_details_currency_id = user_details_currency_id;
    }

    public String getUser_default_currency() {
        return user_default_currency;
    }

    public void setUser_default_currency(String user_default_currency) {
        this.user_default_currency = user_default_currency;
    }

    public String getUser_default_language() {
        return user_default_language;
    }

    public void setUser_default_language(String user_default_language) {
        this.user_default_language = user_default_language;
    }

    public String getUser_details_language_id() {
        return user_details_language_id;
    }

    public void setUser_details_language_id(String user_details_language_id) {
        this.user_details_language_id = user_details_language_id;
    }
}
