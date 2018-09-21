package com.nicklasoxhammar.cartchecking;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nick on 2018-09-21.
 */

public class Street {


    HashMap<String, Resident> residents;

    Street(){}

    Street(HashMap<String, Resident> residents){
        this.residents = residents;
    }

    public HashMap<String, Resident> getResidents() {
        return residents;
    }

    public void setResidents(HashMap<String, Resident> residents) {
        this.residents = residents;
    }
}
