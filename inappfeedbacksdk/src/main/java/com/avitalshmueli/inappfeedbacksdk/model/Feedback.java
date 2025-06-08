package com.avitalshmueli.inappfeedbacksdk.model;

import java.util.Date;

public class Feedback {
    private String package_name;
    private String message;
    private Integer rating;
    private String app_version;
    private String device_info;
    private String form_id;
    private String user_id;
    private Date created_at;

    public Feedback(String packageName, String formId, String userId, String message, Integer rating,
                    String appVersion, String deviceInfo, Date createdAt) {
        this.package_name = packageName;
        this.form_id = formId;
        this.user_id = userId;
        this.message = message;
        this.rating = rating;
        this.app_version = appVersion;
        this.device_info = deviceInfo;
        this.created_at = createdAt;
    }

    public String getForm_id() {
        return form_id;
    }

    public Feedback setForm_id(String form_id) {
        this.form_id = form_id;
        return this;
    }

    public String getUser_id() {
        return user_id;
    }

    public Feedback setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Feedback setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getRating() {
        return rating;
    }

    public Feedback setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public String getApp_version() {
        return app_version;
    }

    public Feedback setApp_version(String app_version) {
        this.app_version = app_version;
        return this;
    }

    public String getDevice_info() {
        return device_info;
    }

    public Feedback setDevice_info(String device_info) {
        this.device_info = device_info;
        return this;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Feedback setCreated_at(Date created_at) {
        this.created_at = created_at;
        return this;
    }
}
