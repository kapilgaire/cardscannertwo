package com.example.cardscannertwo.data.response;

import com.google.gson.annotations.SerializedName;

public class SiteName {

    @SerializedName("GUIDKey")
    private String GUIDKey;
    @SerializedName("Message")
    private String Message;
    @SerializedName("SiteName")
    private String SiteName;


    public SiteName(String siteName) {
        SiteName = siteName;
    }

    public String getGUIDKey() {
        return GUIDKey;
    }

    public void setGUIDKey(String GUIDKey) {
        this.GUIDKey = GUIDKey;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }
}
