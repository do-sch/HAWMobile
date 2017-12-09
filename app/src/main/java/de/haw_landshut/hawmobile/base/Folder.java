package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Folder {

    @PrimaryKey
    private String name;

    private long uidvalidaty;



}
