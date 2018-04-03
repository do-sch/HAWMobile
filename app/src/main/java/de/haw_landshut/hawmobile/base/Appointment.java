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
    public int start;
    public int end;
    public String appointment;

    public Appointment(int start, int end, String appointment){
        this.start = start;
        this.end = end;
        this.appointment = appointment;
    }

}
