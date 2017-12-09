package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FaecherData{
    @PrimaryKey
    private String studiengang;

    @ColumnInfo(name="fach")
    private String fach;

    public String getStudiengang(){
        return this.studiengang;
    }
    public String getFach(){
        return this.fach;
    }
}