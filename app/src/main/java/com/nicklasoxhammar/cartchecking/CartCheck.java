package com.nicklasoxhammar.cartchecking;

import java.util.ArrayList;

/**
 * Created by Nick on 2018-04-23.
 */

public class CartCheck {

    String date;
    String comment;

    Boolean correctlyRecycled;

    ArrayList<String> nonRecyclables;

    CartCheck(){}

    public CartCheck(String date, String comment, ArrayList<String> nonRecyclables, Boolean correctlyRecycled){

        this.date = date;
        this.comment = comment;
        this.nonRecyclables = nonRecyclables;
        this.correctlyRecycled = correctlyRecycled;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getNonRecyclables() {
        return nonRecyclables;
    }

    public void setNonRecyclables(ArrayList<String> nonRecyclables) {
        this.nonRecyclables = nonRecyclables;
    }

    public Boolean getCorrectlyRecycled() {
        return correctlyRecycled;
    }

    public void setCorrectlyRecycled(Boolean correctlyRecycled) {
        this.correctlyRecycled = correctlyRecycled;
    }
}
