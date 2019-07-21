package com.example.cardscannertwo.data.response;

import com.google.gson.annotations.SerializedName;

public class TransactionDetails {

    @SerializedName("CrDate")
    private String CrDate;
    @SerializedName("ErrorMessage")
    private String ErrorMessage;
    @SerializedName("Quantity")
    private String Quantity;
    @SerializedName("RegistrationNo")
    private String RegistrationNo;

    public String getCrDate() {
        return CrDate;
    }

    public void setCrDate(String crDate) {
        CrDate = crDate;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }
}
