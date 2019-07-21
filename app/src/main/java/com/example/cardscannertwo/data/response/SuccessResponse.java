package com.example.cardscannertwo.data.response;

import com.google.gson.annotations.SerializedName;

public class SuccessResponse {

    @SerializedName("Message")
    private String Message;

    public String getMessage() {
        return Message;
    }


}
