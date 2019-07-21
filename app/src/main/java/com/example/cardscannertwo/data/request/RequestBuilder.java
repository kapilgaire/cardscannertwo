package com.example.cardscannertwo.data.request;

import com.google.gson.annotations.SerializedName;

public class RequestBuilder {

    @SerializedName("CardNo")
    private String CardNo;

    @SerializedName("FuelType")
    private String FuelType;

    @SerializedName("IfOther")
    private String IfOther;

    @SerializedName("Key")
    private String Key;

    @SerializedName("MeterReading")
    private String MeterReading;

    @SerializedName("Quantity")
    private String Quantity;

    @SerializedName("RegistrationNo")
    private String RegistrationNo;

    @SerializedName("SiteName")
    private String SiteName;

    public RequestBuilder(RequestParameterBuilder requestParameterBuilder) {

        this.CardNo=requestParameterBuilder.CardNo;
        this.FuelType=requestParameterBuilder.FuelType;
        this.IfOther=requestParameterBuilder.IfOther;
        this.Key=requestParameterBuilder.Key;
        this.MeterReading=requestParameterBuilder.MeterReading;
        this.Quantity=requestParameterBuilder.Quantity;
        this.RegistrationNo=requestParameterBuilder.RegistrationNo;
        this.SiteName=requestParameterBuilder.SiteName;
    }

    public static class RequestParameterBuilder {

        private String CardNo;
        private String FuelType;
        private String IfOther;
        private String Key;
        private String MeterReading;
        private String Quantity;
        private String RegistrationNo;
        private String SiteName;



        public RequestParameterBuilder() {

        }


        public RequestParameterBuilder setCardNo(String cardNo) {
            CardNo = cardNo;
            return this;
        }

        public RequestParameterBuilder setFuelType(String fuelType) {
            FuelType = fuelType;
            return this;
        }

        public RequestParameterBuilder setIfOther(String ifOther) {
            IfOther = ifOther;
            return this;
        }

        public RequestParameterBuilder setKey(String key) {
            Key = key;
            return this;
        }

        public RequestParameterBuilder setMeterReading(String meterReading) {
            MeterReading = meterReading;
            return this;
        }

        public RequestParameterBuilder setQuantity(String quantity) {
            Quantity = quantity;
            return this;
        }

        public RequestParameterBuilder setRegistrationNo(String registrationNo) {
            RegistrationNo = registrationNo;
            return this;
        }

        public RequestParameterBuilder setSiteName(String siteName) {
            SiteName = siteName;
            return this;
        }


        public RequestBuilder build() {
            return new RequestBuilder(this);
        }
    }

}
