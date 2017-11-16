package de.haw_landshut.hawmobile;

import android.util.Log;

public enum Fakultaet {

    IF, BW, EW, MA, SA;

    public static Fakultaet get(String s){
        return Fakultaet.values()[Integer.valueOf(s)];
    }
}
