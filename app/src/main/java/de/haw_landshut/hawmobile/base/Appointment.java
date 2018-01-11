package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by X on 11.01.2018.
 */

@Entity
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public String appointment;

    public Appointment(String date, String appointment){
        this.date = date;
        this.appointment = appointment;
    }

}
