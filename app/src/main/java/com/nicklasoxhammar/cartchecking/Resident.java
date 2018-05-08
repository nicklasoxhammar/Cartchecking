package com.nicklasoxhammar.cartchecking;

import android.location.Address;
import android.provider.ContactsContract;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-04-23.
 */

public class Resident {

    String residentId;

    String firstName;
    String lastName;
    String email;
    String address;

    ArrayList<CartCheck> cartChecks;

    public Resident(){}

    public Resident(String residentId, String firstName, String lastName, String email, String address){

        this.residentId = residentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<CartCheck> getCartChecks() {
        return cartChecks;
    }

    public void setCartChecks(ArrayList<CartCheck> cartChecks) {
        this.cartChecks = cartChecks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
