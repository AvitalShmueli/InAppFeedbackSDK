package com.avitalshmueli.inappfeedbacksdk.model;

import com.google.gson.annotations.SerializedName;

public class FeedbackForm {
    public enum FORM_TYPE{
        rating,
        free_text,
        rating_text
    }

    @SerializedName("_id")
    private String id;
    private String package_name;
    private String title;
    private FORM_TYPE type;

    public FeedbackForm() {
    }

    public String getId() {
        return id;
    }

    public FeedbackForm setId(String id) {
        this.id = id;
        return this;
    }

    public String getPackage_name() {
        return package_name;
    }

    public FeedbackForm setPackage_name(String package_name) {
        this.package_name = package_name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public FeedbackForm setTitle(String title) {
        this.title = title;
        return this;
    }

    public FORM_TYPE getType() {
        return type;
    }

    public FeedbackForm setType(FORM_TYPE type) {
        this.type = type;
        return this;
    }
}
