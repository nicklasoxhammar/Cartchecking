package com.nicklasoxhammar.cartchecking;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Nick on 2018-04-23.
 */

public class Resident {

    private String ID;

    //String firstName;
    String lastName;
    //String email;
    //ResidentAddress address;
    String streetName;
    String streetNumber;
    String apartmentNumber;
    String cartOnDifferentStreet;

    public boolean alreadyChecked = false;

    //HashMap<String, CartCheck> cartChecks;

    public Resident() {
    }

    /*public Resident(String ID, String lastName, String streetName, String streetNumber, String apartmentNumber, String cartOnDifferentStreet, HashMap<String, CartCheck> cartChecks) {

        this.ID = ID;
        //this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.apartmentNumber = apartmentNumber;
        this.cartOnDifferentStreet = cartOnDifferentStreet;
        //this.email = email;
        //this.address = address;
        this.cartChecks = cartChecks;
    }*/

    public Resident(String ID, String lastName, String streetName, String streetNumber, String apartmentNumber, String cartOnDifferentStreet) {

        this.ID = ID;
        this.lastName = lastName;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.apartmentNumber = apartmentNumber;
        this.cartOnDifferentStreet = cartOnDifferentStreet;

    }

     /*public Boolean alreadyChecked() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM", Locale.US);
        String strDate = dateFormat.format((Calendar.getInstance().getTime()));

       if (cartChecks != null) {
            for (CartCheck c : cartChecks) {

                if (c != null && c.getDate().equals(strDate)) {
                    return true;
                }
            }
        }



        return false;
    }*/

    /*public Resident(String firstName, String lastName, ResidentAddress address, ArrayList<CartCheck> cartChecks){

        //this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        //this.email = email;
        this.address = address;
        this.cartChecks = cartChecks;
    }*/

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    /*public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }*/

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public int getStreetNumberInt() {

        String streetNumberOnlyDigits = streetNumber.replaceAll("[^\\d]", "");

        int streetNumberInt = Integer.parseInt(streetNumberOnlyDigits);

        return streetNumberInt;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getCartOnDifferentStreet() {
        return cartOnDifferentStreet;
    }

    public void setCartOnDifferentStreet(String cartOnDifferentStreet) {
        this.cartOnDifferentStreet = cartOnDifferentStreet;
    }


    /* public ResidentAddress getAddress() {

        return address;
    }

    public void setAddress(ResidentAddress address) {
        this.address = address;
    }*/


    /*public HashMap<String, CartCheck> getCartChecks() {
        return cartChecks;
    }

    public void setCartChecks(HashMap<String, CartCheck> cartChecks) {
        this.cartChecks = cartChecks;
    }

    /*public void addCartCheck(CartCheck cartCheck){
        cartChecks.add(cartCheck);
    }*/

    /*public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

}
