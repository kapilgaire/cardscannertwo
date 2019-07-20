package com.example.cardscannertwo.data.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CardDetails implements Parcelable {

    @SerializedName("CardsNO")
    private String CardsNO;
    @SerializedName("ErrorMessage")
    private String ErrorMessage;
    @SerializedName("FuelType")
    private String FuelType;
    @SerializedName("FullName")
    private String FullName;
    @SerializedName("RegistrationNo")
    private String RegistrationNo;




    public String getCardsNO() {
        return CardsNO;
    }

    public void setCardsNO(String cardsNO) {
        CardsNO = cardsNO;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CardsNO);
        dest.writeString(this.ErrorMessage);
        dest.writeString(this.FuelType);
        dest.writeString(this.FullName);
        dest.writeString(this.RegistrationNo);
    }

    public CardDetails() {
    }

    protected CardDetails(Parcel in) {
        this.CardsNO = in.readString();
        this.ErrorMessage = in.readString();
        this.FuelType = in.readString();
        this.FullName = in.readString();
        this.RegistrationNo = in.readString();
    }

    public static final Parcelable.Creator<CardDetails> CREATOR = new Parcelable.Creator<CardDetails>() {
        @Override
        public CardDetails createFromParcel(Parcel source) {
            return new CardDetails(source);
        }

        @Override
        public CardDetails[] newArray(int size) {
            return new CardDetails[size];
        }
    };

    @Override
    public String toString() {
        return "CardDetails{" +
                "CardsNO='" + CardsNO + '\'' +
                ", ErrorMessage='" + ErrorMessage + '\'' +
                ", FuelType='" + FuelType + '\'' +
                ", FullName='" + FullName + '\'' +
                ", RegistrationNo='" + RegistrationNo + '\'' +
                '}';
    }
}
