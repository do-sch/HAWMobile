package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {EMail.class, EMailFolder.class, ProfData.class,FaecherData.class,CustomTimetable.class}, version = 1, exportSchema = false)
@TypeConverters({EMailConverters.class})
public abstract class HAWDatabase extends RoomDatabase {

    public abstract EMailDao eMailDao();
    public abstract ScheduleDao scheduleDao();
}
