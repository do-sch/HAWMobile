package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FaecherData{

    public FaecherData(String studiengang,String fach){
        this.fach=fach;
        this.studiengang=studiengang;
    }

    @PrimaryKey
    private String studiengang;

    @ColumnInfo()
    private String fach;

    public String getStudiengang(){
        return this.studiengang;
    }
    public String getFach(){
        return this.fach;
    }
}