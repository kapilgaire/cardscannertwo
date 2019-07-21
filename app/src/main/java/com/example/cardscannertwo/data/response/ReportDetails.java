package com.example.cardscannertwo.data.response;

import com.google.gson.annotations.SerializedName;

public class ReportDetails {

    @SerializedName("ErrorMessage")
    private String ErrorMessage;
    @SerializedName("FuelDate")
    private String FuelDate;
    @SerializedName("FuelType")
    private String FuelType;
    @SerializedName("Quantity")
    private String Quantity;
    @SerializedName("RegistrationNo")
    private String RegistrationNo;
    @SerializedName("Totalfuel")
    private String Totalfuel;
    @SerializedName("UserName")
    private String UserName;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getFuelDate() {
        return FuelDate;
    }

    public void setFuelDate(String fuelDate) {
        FuelDate = fuelDate;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
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

    public String getTotalfuel() {
        return Totalfuel;
    }

    public void setTotalfuel(String totalfuel) {
        Totalfuel = totalfuel;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        return "ReportDetails{" +
                "ErrorMessage='" + ErrorMessage + '\'' +
                ", FuelDate='" + FuelDate + '\'' +
                ", FuelType='" + FuelType + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", RegistrationNo='" + RegistrationNo + '\'' +
                ", Totalfuel='" + Totalfuel + '\'' +
                ", UserName='" + UserName + '\'' +
                '}';
    }
}
