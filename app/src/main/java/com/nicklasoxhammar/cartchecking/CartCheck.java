package com.nicklasoxhammar.cartchecking;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-04-23.
 */

public class CartCheck {

    String date;
    String notes;
    // 0 = not setout, 1 = setout, 3 = only trash cart is set out
    String setOut;

    //0 = clean 1 = not;
    String correctlyRecycled;

    ArrayList<String> nonRecyclables;

    CartCheck(){}

    public CartCheck(String date, String notes, String correctlyRecycled, String setOut){

        this.date = date;
        this.setOut = setOut;
        this.notes = notes;
        this.nonRecyclables = nonRecyclables;
        this.correctlyRecycled = correctlyRecycled;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getNonRecyclables() {
        return nonRecyclables;
    }

    public void setNonRecyclables(ArrayList<String> nonRecyclables) {
        this.nonRecyclables = nonRecyclables;
    }

    public String getCorrectlyRecycled() {
        return correctlyRecycled;
    }

    public void setCorrectlyRecycled(String correctlyRecycled) {
        this.correctlyRecycled = correctlyRecycled;
    }
}
