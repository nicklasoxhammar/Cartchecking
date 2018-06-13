package com.nicklasoxhammar.cartchecking;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-04-23.
 */

public class Resident {

    String residentId;

    String firstName;
    String lastName;
    //String email;
    ResidentAddress address;

    //ArrayList<CartCheck> cartChecks;

    public Resident(){}

    public Resident(String firstName, String lastName, ResidentAddress address){

        //this.residentId = residentId;
        this.firstName = firstName;
        this.lastName = lastName;
        //this.email = email;
        this.address = address;
        //this.cartChecks = cartChecks;
    }

    /*public Resident(String firstName, String lastName, ResidentAddress address, ArrayList<CartCheck> cartChecks){

        //this.residentId = residentId;
        this.firstName = firstName;
        this.lastName = lastName;
        //this.email = email;
        this.address = address;
        this.cartChecks = cartChecks;
    }*/

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

    public ResidentAddress getAddress() {

        return address;
    }

    public void setAddress(ResidentAddress address) {
        this.address = address;
    }


   /*public ArrayList<CartCheck> getCartChecks() {
        return cartChecks;
    }

    public void setCartChecks(ArrayList<CartCheck> cartChecks) {
        this.cartChecks = cartChecks;
    }

    public void addCartCheck(CartCheck cartCheck){
        cartChecks.add(cartCheck);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

}
