package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import de.haw_landshut.hawmobile.Fakultaet;

@Entity
public class FaecherData{

    @NonNull
    private Fakultaet studiengang;

    @ColumnInfo()
    private String fach;

    @PrimaryKey (autoGenerate = true)
    private long key;

    public FaecherData(Fakultaet studiengang, String fach){
        this.fach=fach;
        this.studiengang=studiengang;
    }


    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public void setStudiengang(@NonNull Fakultaet studiengang) {
        this.studiengang = studiengang;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }


    public Fakultaet getStudiengang(){
        return this.studiengang;
    }
    public String getFach(){
        return this.fach;
    }
}