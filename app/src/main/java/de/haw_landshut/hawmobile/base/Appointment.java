package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
