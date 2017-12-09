package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
@Entity
public class ProfData{
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