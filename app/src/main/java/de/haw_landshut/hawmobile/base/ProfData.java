package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
@Entity
public class ProfData{

    public ProfData(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }

    private String firstName;

    @PrimaryKey(autoGenerate = true)
    private long profkey;


    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getProfkey() {
        return profkey;
    }

    public void setProfkey(long profkey) {
        this.profkey = profkey;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}