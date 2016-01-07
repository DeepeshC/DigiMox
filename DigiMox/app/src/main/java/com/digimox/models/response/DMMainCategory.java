package com.digimox.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Deepesh on 08-Dec-15.
 */
public class DMMainCategory implements Serializable {

    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_image")
    @Expose
    private String groupImage;
    @SerializedName("has_child")
    @Expose
    private String hasChild;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("language_id")
    @Expose
    private String languageId;

    /**
     * @return The groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The group_id
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return The groupImage
     */
    public String getGroupImage() {
        return groupImage;
    }

    /**
     * @param groupImage The group_image
     */
    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    /**
     * @return The hasChild
     */
    public String getHasChild() {
        return hasChild;
    }

    /**
     * @param hasChild The has_child
     */
    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    /**
     * @return The groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName The group_name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
