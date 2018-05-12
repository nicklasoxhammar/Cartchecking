package com.nicklasoxhammar.cartchecking;

import com.nicklasoxhammar.cartchecking.Resident;

/**
 * Created by Nick on 2018-05-12.
 */

public class ResidentAddress {

    String streetNumber;
    String streetName;
    String apartmentNumber;

    public ResidentAddress(){}

    public ResidentAddress(String streetNumber, String streetName, String apartmentNumber){

        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.apartmentNumber = apartmentNumber;
    }

    public ResidentAddress(String streetNumber, String streetName){

        this.streetNumber = streetNumber;
        this.streetName = streetName;
    }


    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

}
