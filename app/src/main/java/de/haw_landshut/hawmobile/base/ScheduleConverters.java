package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.TypeConverter;
import de.haw_landshut.hawmobile.Fakultaet;

public class ScheduleConverters {

    @TypeConverter
    public Fakultaet longToFakultaet(final int index){
         return Fakultaet.values()[index];
    }

    @TypeConverter
    public int fakultaetToLong(final Fakultaet fak){
        return fak.ordinal();
    }
}
