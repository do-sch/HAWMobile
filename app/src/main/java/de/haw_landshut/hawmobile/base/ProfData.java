package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
@Entity
public class ProfData{

    public ProfData(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }
    @ColumnInfo()
    private String firstName;

    @PrimaryKey
    private String lastName;

    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
}