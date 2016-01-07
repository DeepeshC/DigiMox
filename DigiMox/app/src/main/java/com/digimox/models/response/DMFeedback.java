package com.digimox.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Deepesh on 21-Dec-15.
 */
public class DMFeedback implements Serializable {
    @SerializedName("feedback_id")
    @Expose
    private String feedbackId;
    @SerializedName("user_details_id")
    @Expose
    private String userDetailsId;
    @SerializedName("feedback_status")
    @Expose
    private String feedbackStatus;
    @SerializedName("feedback_added_date")
    @Expose
    private String feedbackAddedDate;
    @SerializedName("feedback_updated_date")
    @Expose
    private String feedbackUpdatedDate;
    @SerializedName("feedback_lang_id")
    @Expose
    private String feedbackLangId;
    @SerializedName("feedback_lang_question")
    @Expose
    private String feedbackLangQuestion;
    @SerializedName("language_id")
    @Expose
    private String languageId;

    /**
     * @return The feedbackId
     */
    public String getFeedbackId() {
        return feedbackId;
    }

    /**
     * @param feedbackId The feedback_id
     */
    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

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
     * @return The feedbackStatus
     */
    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    /**
     * @param feedbackStatus The feedback_status
     */
    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    /**
     * @return The feedbackAddedDate
     */
    public String getFeedbackAddedDate() {
        return feedbackAddedDate;
    }

    /**
     * @param feedbackAddedDate The feedback_added_date
     */
    public void setFeedbackAddedDate(String feedbackAddedDate) {
        this.feedbackAddedDate = feedbackAddedDate;
    }

    /**
     * @return The feedbackUpdatedDate
     */
    public String getFeedbackUpdatedDate() {
        return feedbackUpdatedDate;
    }

    /**
     * @param feedbackUpdatedDate The feedback_updated_date
     */
    public void setFeedbackUpdatedDate(String feedbackUpdatedDate) {
        this.feedbackUpdatedDate = feedbackUpdatedDate;
    }

    /**
     * @return The feedbackLangId
     */
    public String getFeedbackLangId() {
        return feedbackLangId;
    }

    /**
     * @param feedbackLangId The feedback_lang_id
     */
    public void setFeedbackLangId(String feedbackLangId) {
        this.feedbackLangId = feedbackLangId;
    }

    /**
     * @return The feedbackLangQuestion
     */
    public String getFeedbackLangQuestion() {
        return feedbackLangQuestion;
    }

    /**
     * @param feedbackLangQuestion The feedback_lang_question
     */
    public void setFeedbackLangQuestion(String feedbackLangQuestion) {
        this.feedbackLangQuestion = feedbackLangQuestion;
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
}
